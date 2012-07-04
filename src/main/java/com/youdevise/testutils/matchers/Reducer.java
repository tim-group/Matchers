package com.youdevise.testutils.matchers;

public interface Reducer<A, B> {
    B identity();
    B product(A a, B b);
}