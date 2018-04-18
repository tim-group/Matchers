package com.youdevise.testutils.matchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public final class Contains {
    private Contains() { }

    @SafeVarargs
    public static <T> Matcher<? super Iterable<? extends T>> inOrder(final T... expected) {
        final List<Matcher<? super T>> expectedMatchers = new ArrayList<>();
        for (final T t : expected) {
            expectedMatchers.add(Matchers.equalTo(t));
        }
        return inOrder(expectedMatchers);
    }

    @SafeVarargs
    public static <T> Matcher<? super Iterable<? extends T>> inOrder(final Matcher<T>... expected) {
        return inOrder(ImmutableList.copyOf(expected));
    }

    public static <T> Matcher<? super Iterable<? extends T>> inOrder(final Iterable<? extends Matcher<? super T>> expected) {
        return inOrder(ImmutableList.copyOf(expected));
    }

    public static <T> Matcher<? super Iterable<? extends T>> inOrder(final List<? extends Matcher<? super T>> expected) {
        return new ContainsInOrder<>(expected);
    }

    @SuppressWarnings("unchecked")
    public static <T> Matcher<? super Iterable<? extends T>> only(final T expected) {
        return inOrder(expected);
    }

    @SuppressWarnings("unchecked")
    public static <T> Matcher<? super Iterable<? extends T>> only(final Matcher<T> expected) {
        return inOrder(expected);
    }

    @SafeVarargs
    public static <T> Matcher<? super Iterable<? extends T>> inAnyOrder(final T... expected) {
        final List<Matcher<? super T>> expectedMatchers = new ArrayList<>();
        for (final T t : expected) {
            expectedMatchers.add(Matchers.equalTo(t));
        }
        return inAnyOrder(expectedMatchers);
    }

    @SafeVarargs
    public static <T> Matcher<? super Iterable<? extends T>> inAnyOrder(final Matcher<T>... expected) {
        return inAnyOrder(ImmutableList.copyOf(expected));
    }

    public static <T> Matcher<? super Iterable<? extends T>> inAnyOrder(final Iterable<? extends Matcher<? super T>> expected) {
        return inAnyOrder(ImmutableList.copyOf(expected));
    }

    public static <T> Matcher<? super Iterable<? extends T>> inAnyOrder(final List<? extends Matcher<? super T>> expected) {
        return new ContainsInAnyOrder<>(expected);
    }

    @SafeVarargs
    public static <T> Matcher<? super Iterable<? extends T>> theItems(final T... expected) {
        final List<Matcher<? super T>> expectedMatchers = new ArrayList<>();
        for (final T t : expected) {
            expectedMatchers.add(Matchers.equalTo(t));
        }
        return theItems(expectedMatchers);
    }

    @SafeVarargs
    public static <T> Matcher<? super Iterable<? extends T>> theItems(final Matcher<T>... expected) {
        return theItems(ImmutableList.copyOf(expected));
    }

    public static <T> Matcher<? super Iterable<? extends T>> theItems(final Iterable<? extends Matcher<? super T>> expected) {
        return theItems(ImmutableList.copyOf(expected));
    }

    public static <T> Matcher<? super Iterable<? extends T>> theItems(final List<? extends Matcher<? super T>> expected) {
        return new ContainsTheItems<>(expected);
    }

    public static <T> Matcher<? super Iterable<? extends T>> theItem(final Matcher<? super T> expected) {
        return new ContainsTheItem<>(expected);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> Matcher<? super Iterable<? extends T>> theItem(final T expected) {
        return theItem((Matcher) Matchers.equalTo(expected));
    }
}
