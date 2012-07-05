package com.youdevise.testutils.matchers;

public abstract class EndoReducer<T> implements Reducer<T, T> {
    @Override public T merge(T left, T right) {
        return product(left, right);
    }
}