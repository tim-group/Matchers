package com.youdevise.testutils.matchers.beans;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class DelegatingJavaBeanMatcher<T> extends BaseMatcher<T> implements JavaBeanMatcher<T> {

    private final BaseJavaBeanMatcher<T> innerMatcher;
    public DelegatingJavaBeanMatcher(BaseJavaBeanMatcher<T> innerMatcher) {
        this.innerMatcher = innerMatcher;
    }

    @Override
    public boolean matches(Object item) {
        return innerMatcher.matches(item);
    }

    @Override
    public void describeTo(Description description) {
        innerMatcher.describeTo(description);
    }

    @Override
    public void withProperty(String name, Matcher<?> matcher) {
        innerMatcher.withProperty(name, matcher);
    }

    @Override
    public void describeMismatch(Object item, Description mismatchDescription) {
        innerMatcher.describeMismatch(item, mismatchDescription);
    }

}
