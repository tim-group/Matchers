package com.youdevise.testutils.matchers.beans;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static com.youdevise.testutils.matchers.beans.PropertyMatcher.an_object_with_property;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.allOf;

public abstract class BaseJavaBeanMatcher<T> extends BaseMatcher<T> {

    private final List<Matcher<?>> matchers = new ArrayList<Matcher<?>>();
    private final String objectTypeName;
    private final Class<T> expectedType;
    
    @SuppressWarnings("unchecked")
    public BaseJavaBeanMatcher() {
        this.expectedType = (Class<T>) getSuperclassType(getClass());
        this.objectTypeName = expectedType.getSimpleName();
    }
    
    @SuppressWarnings("unchecked")
    public BaseJavaBeanMatcher(String objectTypeDescription) {
        this.expectedType = (Class<T>) getSuperclassType(getClass());
        this.objectTypeName = objectTypeDescription;
    }

    public BaseJavaBeanMatcher(Class<T> objectType, String objectTypeDescription) {
        this.expectedType = objectType;
        this.objectTypeName = objectTypeDescription;
    }

    public BaseJavaBeanMatcher(Class<T> objectType, String objectTypeDescription, Matcher<?>... matcher) {
        this(objectType, objectTypeDescription);
        matchingWhen(matcher);
    }

    protected final void matchingWhen(Matcher<?>... matcher) {
        matchers.addAll(asList(matcher));
    }
    
    protected final void withProperty(String propertyName, Matcher<?> matcher) {
        matchingWhen(an_object_with_property(propertyName, matcher));
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public final void describeTo(Description description) {
        description.appendText("a ").appendText(objectTypeName);
        if (matchers.isEmpty()) {
            return;
        }
        description.appendText(" ").appendDescriptionOf(allOf((List)matchers));
    }

    @Override
    public final boolean matches(Object item) {
        return matchesSafely(item, new Description.NullDescription());
    }

    @Override
    public final void describeMismatch(Object item, Description mismatchDescription) {
        matchesSafely(item, mismatchDescription);
    }
    
    private boolean matchesSafely(Object item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("was a <null> ").appendText(objectTypeName);
            return false;
        }
        
        if (!expectedType.isInstance(item)) {
            mismatchDescription.appendText("was not a ").appendText(objectTypeName);
            return false;
        }
        
        for (Matcher<?> matcher : matchers) {
            if (!matcher.matches(item)) {
                mismatchDescription.appendText("was a ").appendText(objectTypeName).appendText(" ");
                matcher.describeMismatch(item, mismatchDescription);
                return false;
            }
        }
        return true;
    }

    private static Type getSuperclassType(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        return ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }
}
