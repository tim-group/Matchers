package com.youdevise.testutils.matchers;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;

import com.google.common.base.Function;

public abstract class MismatchDescriber<T> implements Function<T, SelfDescribing> {
    protected abstract void describeMismatch(T item, Description mismatchDescription);
    
    @Override public SelfDescribing apply(final T item) {
        return new SelfDescribing() {
            @Override
            public void describeTo(Description mismatchDescription) {
                describeMismatch(item, mismatchDescription);
            }
        };
    }
}