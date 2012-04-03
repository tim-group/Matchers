package com.youdevise.testutils.matchers.beans;

public interface JavaBeanPropertyMatcherMaker<T> {
    void makeProperty(JavaBeanMatcher<T> matcher, Object[] args);
}
