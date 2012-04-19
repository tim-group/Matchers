package com.youdevise.testutils.matchers.beans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import static java.util.Arrays.asList;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.beans.PropertyUtil.NO_ARGUMENTS;
import static org.hamcrest.beans.PropertyUtil.propertyDescriptorsFor;

import static com.google.common.collect.Iterables.filter;
import static com.youdevise.testutils.matchers.beans.PropertyMatcher.an_object_whose;

public class SimilarPropertyValuesMatcher<T> extends TypeSafeDiagnosingMatcher<T> {

    private final Set<String> ignoredProperties = Sets.newHashSet();
    private final T expected;
    
    private SimilarPropertyValuesMatcher(T expected) {
        this.expected = expected;
    }

    @Factory
    public static <T> SimilarPropertyValuesMatcher<T> similar_properties_as(T expected) {
        return new SimilarPropertyValuesMatcher<T>(expected);
    }
    
    public SimilarPropertyValuesMatcher<T> ignoring(String... propertyNames) {
        this.ignoredProperties.addAll(Arrays.asList(propertyNames));
        return this;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("an object ");
        
        for (PropertyMatcher matcher : propertyMatchersFor(expected)) {
            description.appendDescriptionOf(matcher);
        }
        
        description.appendText("ignoring properties ").appendValueList("[", ", ", "]", ignoredProperties);
    }

    @Override
    protected boolean matchesSafely(T actual, Description mismatchDescription) {
        final List<Description> mismatchDescriptions = new ArrayList<Description>();
        
        boolean matched = true;
        for (PropertyMatcher matcher : propertyMatchersFor(actual)) {
            if (matcher.matches(actual)) {
                continue;
            }
            
            matched = false;
            Description thisMismatch = new StringDescription();
            mismatchDescriptions.add(thisMismatch);
            matcher.describeMismatch(actual, thisMismatch);
        }
        
        mismatchDescription.appendValueList("", ", ", "", mismatchDescriptions);
        
        return matched;
    }

    private List<PropertyMatcher> propertyMatchersFor(T actual) {
        List<PropertyMatcher> matchers = new ArrayList<PropertyMatcher>();
        
        for (PropertyDescriptor property : readablePropertyDescriptorsFor(actual)) {
            if (!ignoredProperties.contains(property.getDisplayName())) {
                Matcher<?> matcher = matcherFor(property);
                        
                matchers.add(an_object_whose(property.getDisplayName(), matcher));
            }
        }

        return matchers;
    }

    private Matcher<?> matcherFor(PropertyDescriptor property) {
        return equalTo(readProperty(property.getReadMethod(), expected));
    }

    private Iterable<PropertyDescriptor> readablePropertyDescriptorsFor(T actual) {
        return filter(asList(propertyDescriptorsFor(actual, null)), HAS_GETTER);
    }

    private static final Predicate<PropertyDescriptor> HAS_GETTER = new Predicate<PropertyDescriptor>() {
        @Override public boolean apply(PropertyDescriptor input) { return input.getReadMethod() != null; }
    };

    private static Object readProperty(Method method, Object target) {
        try {
          return method.invoke(target, NO_ARGUMENTS);
        } catch (Exception e) {
          throw new IllegalArgumentException("Could not invoke " + method + " on " + target, e);
        }
      }

}
