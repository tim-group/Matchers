package com.youdevise.testutils.matchers.beans;

import org.hamcrest.Matcher;

public interface JavaBeanMatcher<T> extends Matcher<T> {
    public void withProperty(String name, Matcher<?> matcher);
}
