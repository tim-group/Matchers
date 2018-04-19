package com.youdevise.testutils.matchers;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public abstract class CollectionMatcher<T> extends TypeSafeDiagnosingMatcher<Iterable<T>> {

    private final Matcher<Iterable<T>> contains;
    private final List<Matcher<? super T>> expected;


    public CollectionMatcher(List<Matcher<? super T>> expected, Matcher<Iterable<T>> contains) {
        this.expected = expected;
        this.contains = contains;
    }

    @Override
    public void describeTo(Description description) {
        contains.describeTo(description);
    }

    @Override
    protected boolean matchesSafely(Iterable<T> actual, Description mismatchDescription) {
        final List<T> actualList = listOf(actual);
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

    protected abstract void diagnoseFailures(Iterable<T> actual, Description mismatchDescription, List<Matcher<? super T>> expectedMatcher);
}
