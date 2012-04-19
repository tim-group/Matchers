package com.youdevise.testutils.matchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import com.google.common.collect.Lists;

public class Contains {
    private Contains() { }

    public static <T> Matcher<Iterable<T>> inOrder(final T... expected) {
        final List<Matcher<? super T>> expectedMatchers = new ArrayList<Matcher<? super T>>();
        for (final T t : expected) {
            expectedMatchers.add(Matchers.equalTo(t));
        }
        return inOrder(expectedMatchers);
    }

    public static <T> Matcher<Iterable<T>> inOrder(final Matcher<? super T>... expected) {
        return inOrder(Arrays.asList(expected));
    }

    public static <T> Matcher<Iterable<T>> inOrder(final Iterable<Matcher<? super T>> expected) {
        return inOrder(Lists.newArrayList(expected));
    }

    public static <T> Matcher<Iterable<T>> inOrder(final List<Matcher<? super T>> expected) {
        return new ContainsInOrder<T>(expected);
    }

    @SuppressWarnings("unchecked")
    public static <T> Matcher<Iterable<T>> only(final T expected) {
        return inOrder(expected);
    }

    @SuppressWarnings("unchecked")
    public static <T> Matcher<Iterable<T>> only(final Matcher<? super T> expected) {
        return inOrder(expected);
    }

    public static <T> Matcher<Iterable<T>> inAnyOrder(final T... expected) {
        final List<Matcher<? super T>> expectedMatchers = new ArrayList<Matcher<? super T>>();
        for (final T t : expected) {
            expectedMatchers.add(Matchers.equalTo(t));
        }
        return inAnyOrder(expectedMatchers);
    }

    public static <T> Matcher<Iterable<T>> inAnyOrder(final Matcher<? super T>... expected) {
        return inAnyOrder(Arrays.asList(expected));
    }

    public static <T> Matcher<Iterable<T>> inAnyOrder(final Iterable<Matcher<? super T>> expected) {
        return inAnyOrder(Lists.newArrayList(expected));
    }

    public static <T> Matcher<Iterable<T>> inAnyOrder(final List<Matcher<? super T>> expected) {
        return new ContainsInAnyOrder<T>(expected);
    }

    public static <T> Matcher<Iterable<T>> theItems(final T... expected) {
        final List<Matcher<? super T>> expectedMatchers = new ArrayList<Matcher<? super T>>();
        for (final T t : expected) {
            expectedMatchers.add(Matchers.equalTo(t));
        }
        return theItems(expectedMatchers);
    }

    public static <T> Matcher<Iterable<T>> theItems(final Matcher<? super T>... expected) {
        return theItems(Arrays.asList(expected));
    }

    public static <T> Matcher<Iterable<T>> theItems(final Iterable<Matcher<? super T>> expected) {
        return theItems(Lists.newArrayList(expected));
    }

    public static <T> Matcher<Iterable<T>> theItems(final List<Matcher<? super T>> expected) {
        return new ContainsTheItems<T>(expected);
    }

    public static <T> Matcher<Iterable<T>> theItem(final Matcher<? super T> expected) {
        return new ContainsTheItem<T>(expected);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> Matcher<Iterable<T>> theItem(final T expected) {
        return theItem((Matcher)Matchers.is(expected));
    }
}
