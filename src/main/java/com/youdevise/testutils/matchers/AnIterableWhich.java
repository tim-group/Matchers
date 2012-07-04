package com.youdevise.testutils.matchers;

import java.util.Collection;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.google.common.collect.Lists;

public class AnIterableWhich<T> extends TypeSafeDiagnosingMatcher<Iterable<? super T>> {
    
    private AnIterable<T> anIterableMatcher;
    private Collection<Matcher<Iterable<T>>> matchers = Lists.newLinkedList();
    
    AnIterableWhich(AnIterable<T> anIterableMatcher, Matcher<Iterable<T>> firstMatcher) {
        this.anIterableMatcher = anIterableMatcher;
        matchers.add(firstMatcher);
    }
    
    public AnIterableWhich<T> and(Matcher<Iterable<T>> matcher) {
        matchers.add(matcher);
        return this;
    }

    @Override
    public void describeTo(Description description) {
        description.appendDescriptionOf(anIterableMatcher).appendText(" matching ").appendDescriptionOf(allMatchers());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Matcher<Iterable<? super T>> allMatchers() {
        return Matchers.allOf((Iterable) matchers);
    }

    @Override
    protected boolean matchesSafely(Iterable<? super T> item, Description mismatchDescription) {
        if (anIterableMatcher.matches(item)) {
            Matcher<Iterable<? super T>> allMatchers = allMatchers();
            if (allMatchers.matches(item)) {
                return true;
            }
            allMatchers.describeMismatch(item, mismatchDescription);
            return false;
        }
        anIterableMatcher.describeMismatch(item, mismatchDescription);
        return false;
    }
}