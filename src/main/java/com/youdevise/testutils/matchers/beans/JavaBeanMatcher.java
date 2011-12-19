package com.youdevise.testutils.matchers.beans;

import org.hamcrest.Matcher;

public interface JavaBeanMatcher<T> extends Matcher<T> {
    void withProperty(String name, Matcher<?> matcher);
}
