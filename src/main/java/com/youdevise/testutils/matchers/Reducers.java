package com.youdevise.testutils.matchers;

import java.util.Iterator;



public final class Reducers {
    private Reducers() {}
    
    public static <A, B> B reduce(Iterable<A> iterable, Reducer<A, B> reducer) {
        B result = reducer.identity();
        Iterator<A> iterator = iterable.iterator();
        while(iterator.hasNext()) {
            result = reducer.product(iterator.next(), result);
        }
        return result;
    }
    
    public static Reducer<Integer, Integer> sumIntegers() {
        return new Reducer<Integer, Integer>() {
            @Override public Integer identity() {
                return 0;
            }

            @Override public Integer product(Integer a, Integer b) {
                return a + b;
            }
        };
    }
    
    public static Reducer<String, String> joinStrings(final String separator) {
        return new Reducer<String, String>() {
            @Override public String identity() {
                return "";
            }

            @Override public String product(String a, String b) {
                return a + separator + b;
            }
        };
    }
}