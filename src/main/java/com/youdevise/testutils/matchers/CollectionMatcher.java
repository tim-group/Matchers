package com.youdevise.testutils.matchers;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public abstract class CollectionMatcher<T> extends TypeSafeDiagnosingMatcher<Iterable<? extends T>> {

    private final Matcher<? super Iterable<? extends T>> contains;
    private final List<? extends Matcher<? super T>> expected;


    public CollectionMatcher(List<? extends Matcher<? super T>> expected, Matcher<? super Iterable<? extends T>> contains) {
        this.expected = expected;
        this.contains = contains;
    }

    @Override
    public void describeTo(Description description) {
        contains.describeTo(description);
    }

    @Override
    protected boolean matchesSafely(Iterable<? extends T> actual, Description mismatchDescription) {
        final List<T> actualList = Lists.newArrayList(actual);
        diagnoseFailures(actual, mismatchDescription, expected);
        mismatchDescription.appendText("\n\n\tComplete actual iterable: ").appendValue(actualList);
        return contains.matches(actual);
    }

    protected final List<T> listOf(Iterable<T> actual) {
        final List<T> actualList = new ArrayList<T>();
        for (final T t : actual) {
            actualList.add(t);
        }
        return actualList;
    }

    protected final boolean actualCollectionIsEmpty(Description mismatchDescription, List<T> actualList) {
        if (actualList.isEmpty()) {
            mismatchDescription.appendText("the actual collection was empty ");
            return true;
        }
        return false;
    }

    protected abstract void diagnoseFailures(Iterable<? extends T> actual, Description mismatchDescription, List<? extends Matcher<? super T>> expectedMatcher);
}
