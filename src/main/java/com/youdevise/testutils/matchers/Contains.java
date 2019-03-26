package com.youdevise.testutils.matchers;

import com.google.common.collect.Lists;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

/* IntelliJ will flag several type parameters as redundant, but in fact javac 11 and 12 require them. */
@SuppressWarnings("RedundantTypeArguments")
public final class Contains {
    private Contains() { }

    public static Matcher<Iterable<?>> nothing() {
        return Matchers.emptyIterable();
    }

    @SafeVarargs
    public static <T> Matcher<Iterable<? extends T>> inOrder(final T... expected) {
        final List<Matcher<? super T>> expectedMatchers = new ArrayList<>();
        for (final T t : expected) {
            expectedMatchers.add(equalTo(t));
        }
        return inOrder(expectedMatchers);
    }

    @SafeVarargs
    public static <T> Matcher<Iterable<? extends T>> inOrder(final Matcher<? super T>... expected) {
        return Contains.<T> inOrder(Arrays.asList(expected));
    }

    public static <T> Matcher<Iterable<? extends T>> inOrder(final Iterable<Matcher<? super T>> expected) {
        return Contains.<T> inOrder(Lists.newArrayList(expected));
    }

    public static <T> Matcher<Iterable<? extends T>> inOrder(final List<Matcher<? super T>> expected) {
        return new ContainsInOrder<>(expected);
    }

    @SuppressWarnings("unchecked")
    public static <T> Matcher<Iterable<? extends T>> only(final T expected) {
        return inOrder(expected);
    }

    @SuppressWarnings("unchecked")
    public static <T> Matcher<Iterable<? extends T>> only(final Matcher<? super T> expected) {
        return inOrder(expected);
    }

    @SafeVarargs
    public static <T> Matcher<Iterable<? extends T>> inAnyOrder(final T... expected) {
        final List<Matcher<? super T>> expectedMatchers = new ArrayList<>();
        for (final T t : expected) {
            expectedMatchers.add(equalTo(t));
        }
        return inAnyOrder(expectedMatchers);
    }

    @SafeVarargs
    public static <T> Matcher<Iterable<? extends T>> inAnyOrder(final Matcher<? super T>... expected) {
        return Contains.<T> inAnyOrder(Arrays.asList(expected));
    }

    public static <T> Matcher<Iterable<? extends T>> inAnyOrder(final Iterable<Matcher<? super T>> expected) {
        return Contains.<T> inAnyOrder(Lists.newArrayList(expected));
    }

    public static <T> Matcher<Iterable<? extends T>> inAnyOrder(final List<Matcher<? super T>> expected) {
        return new ContainsInAnyOrder<>(expected);
    }

    @SafeVarargs
    public static <T> Matcher<Iterable<? extends T>> theItems(final T... expected) {
        final List<Matcher<? super T>> expectedMatchers = new ArrayList<>();
        for (final T t : expected) {
            expectedMatchers.add(equalTo(t));
        }
        return theItems(expectedMatchers);
    }

    @SafeVarargs
    public static <T> Matcher<Iterable<? extends T>> theItems(final Matcher<T>... expected) {
        return Contains.<T> theItems(Arrays.asList((Matcher<? super T>[]) expected));
    }

    public static <T> Matcher<Iterable<? extends T>> theItems(final Iterable<Matcher<? super T>> expected) {
        return Contains.<T> theItems(Lists.newArrayList(expected));
    }

    public static <T> Matcher<Iterable<? extends T>> theItems(final List<Matcher<? super T>> expected) {
        return new ContainsTheItems<>(expected);
    }

    public static <T> Matcher<Iterable<? extends T>> theItem(final Matcher<? super T> expected) {
        return new ContainsTheItem<>(expected);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> Matcher<Iterable<? extends T>> theItem(final T expected) {
        return Contains.theItem(equalTo(expected));
    }
}
