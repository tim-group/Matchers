package com.youdevise.testutils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.ArrayList;
import java.util.List;

public abstract class CollectionMatcher<T> extends TypeSafeDiagnosingMatcher<Iterable<? extends T>> {

    private final Matcher<Iterable<? extends T>> contains;
    private final List<Matcher<? super T>> expected;


    public CollectionMatcher(List<Matcher<? super T>> expected, Matcher<Iterable<? extends T>> contains) {
        this.expected = expected;
        this.contains = contains;
    }

    @Override
    public void describeTo(Description description) {
        contains.describeTo(description);
    }

    @Override
    protected boolean matchesSafely(Iterable<? extends T> actual, Description mismatchDescription) {
        final List<? extends T> actualList = listOf(actual);
        diagnoseFailures(actual, mismatchDescription, expected);
        mismatchDescription.appendText("\n\n\tComplete actual iterable: ").appendValue(actualList);
        return contains.matches(actual);
    }

    public static <A> List<A> listOf(Iterable<A> actual) {
        final List<A> actualList = new ArrayList<>();
        for (final A t : actual) {
            actualList.add(t);
        }
        return actualList;
    }

    protected final boolean actualCollectionIsEmpty(Description mismatchDescription, List<? extends T> actualList) {
        if (actualList.isEmpty()) {
            mismatchDescription.appendText("the actual collection was empty ");
            return true;
        }
        return false;
    }

    protected abstract void diagnoseFailures(Iterable<? extends T> actual, Description mismatchDescription, List<Matcher<? super T>> expectedMatcher);
}
