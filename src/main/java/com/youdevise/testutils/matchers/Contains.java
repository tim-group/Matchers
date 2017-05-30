package com.youdevise.testutils.matchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public final class Contains {
    private Contains() { }

    @SafeVarargs
    public static <T> Matcher<Iterable<T>> inOrder(final T... expected) {
        final List<Matcher<? super T>> expectedMatchers = new ArrayList<>();
        for (final T t : expected) {
            expectedMatchers.add(Matchers.equalTo(t));
        }
        return inOrder(expectedMatchers);
    }

    @SafeVarargs
    public static <T> Matcher<Iterable<T>> inOrder(final Matcher<T>... expected) {
        return inOrder(Arrays.asList((Matcher<? super T>[]) expected));
    }

    public static <T> Matcher<Iterable<T>> inOrder(final Iterable<Matcher<? super T>> expected) {
        return inOrder(Lists.newArrayList(expected));
    }

    public static <T> Matcher<Iterable<T>> inOrder(final List<Matcher<? super T>> expected) {
        return new ContainsInOrder<>(expected);
    }

    @SuppressWarnings("unchecked")
    public static <T> Matcher<Iterable<T>> only(final T expected) {
        return inOrder(expected);
    }

    @SuppressWarnings("unchecked")
    public static <T> Matcher<Iterable<T>> only(final Matcher<T> expected) {
        return inOrder(expected);
    }

    @SafeVarargs
    public static <T> Matcher<Iterable<T>> inAnyOrder(final T... expected) {
        final List<Matcher<? super T>> expectedMatchers = new ArrayList<>();
        for (final T t : expected) {
            expectedMatchers.add(Matchers.equalTo(t));
        }
        return inAnyOrder(expectedMatchers);
    }

    @SafeVarargs
    public static <T> Matcher<Iterable<T>> inAnyOrder(final Matcher<T>... expected) {
        return inAnyOrder(Arrays.asList((Matcher<? super T>[]) expected));
    }

    public static <T> Matcher<Iterable<T>> inAnyOrder(final Iterable<Matcher<? super T>> expected) {
        return inAnyOrder(Lists.newArrayList(expected));
    }

    public static <T> Matcher<Iterable<T>> inAnyOrder(final List<Matcher<? super T>> expected) {
        return new ContainsInAnyOrder<>(expected);
    }

    @SafeVarargs
    public static <T> Matcher<Iterable<T>> theItems(final T... expected) {
        final List<Matcher<? super T>> expectedMatchers = new ArrayList<>();
        for (final T t : expected) {
            expectedMatchers.add(Matchers.equalTo(t));
        }
        return theItems(expectedMatchers);
    }

    @SafeVarargs
    public static <T> Matcher<Iterable<T>> theItems(final Matcher<T>... expected) {
        return theItems(Arrays.asList((Matcher<? super T>[]) expected));
    }

    public static <T> Matcher<Iterable<T>> theItems(final Iterable<Matcher<? super T>> expected) {
        return theItems(Lists.newArrayList(expected));
    }

    public static <T> Matcher<Iterable<T>> theItems(final List<Matcher<? super T>> expected) {
        return new ContainsTheItems<>(expected);
    }

    public static <T> Matcher<Iterable<T>> theItem(final Matcher<T> expected) {
        return new ContainsTheItem<>(expected);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> Matcher<Iterable<T>> theItem(final T expected) {
        return theItem((Matcher) Matchers.equalTo(expected));
    }
}
