package com.youdevise.testutils.matchers;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public final class Reducers {
    private Reducers() {}

    public static <A, B> B reduce(Iterable<A> iterable, Reducer<A, B> reducer) {
        B result = reducer.identity();
        Iterator<A> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            result = reducer.product(iterator.next(), result);
        }
        return result;
    }

    public static <A> Reducer<A, A> endo(A identity, BinaryOperator<A> op) {
        return new EndoReducer<A>() {
            @Override
            public A identity() {
                return identity;
            }

            @Override
            public A product(A a, A b) {
                return op.apply(a, b);
            }
        };
    }

    public static <A, B> Reducer<A, B> general(B identity, BinaryOperator<B> combiner,
            BiFunction<A, B, B> accumulator) {
        return new Reducer<A, B>() {
            @Override
            public B identity() {
                return identity;
            }

            @Override
            public B merge(B first, B second) {
                return combiner.apply(first, second);
            }

            @Override
            public B product(A a, B b) {
                return accumulator.apply(a, b);
            }
        };
    }

    public static Reducer<Integer, Integer> sumIntegers() {
        return endo(0, Integer::sum);
    }

    public static Reducer<String, String> joinStrings(final String separator) {
        return endo("", (a, b) -> a + separator + b);
    }
}