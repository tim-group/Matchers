package com.youdevise.testutils.matchers;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.google.common.collect.Iterables;

public class ContainsTheItems<T> extends TypeSafeDiagnosingMatcher<Iterable<T>> {

    private final Matcher<Iterable<T>> contains;
    private final List<Matcher<? super T>> expected;

    public ContainsTheItems(List<Matcher<? super T>> expected) {
        this.expected = expected;
        @SuppressWarnings("unchecked")
        Matcher<? super T>[] expectedArray = Iterables.toArray(expected, Matcher.class);
        contains = Matchers.<T>hasItems(expectedArray);
    }

    @Override
    public void describeTo(Description description) {
        contains.describeTo(description);
    }

    @Override
    protected boolean matchesSafely(Iterable<T> actual, Description mismatchDescription) {
        List<T> actualList = new ArrayList<T>();
        for (T t : actual) {
            actualList.add(t);
        }
        diagnoseFailures(actual, mismatchDescription);
        mismatchDescription.appendText("\n\tComplete actual iterable: ").appendValue(actualList);
        return contains.matches(actual);
    }

    private void diagnoseFailures(Iterable<T> actual, Description mismatchDescription) {
        List<T> actualList = new ArrayList<T>();
        for (T t : actual) {
            actualList.add(t);
        }
        if (actualList.isEmpty()) {
            mismatchDescription.appendText("the actual collection was empty ");
            return;
        }
        boolean first = true;
        for (int i = 0; i < expected.size(); i++) {
            if (!Matchers.hasItem(expected.get(i)).matches(actualList)) {
                if (first) {
                    mismatchDescription.appendText("\n\tItems that were expected, but not present: ");
                    first = false;
                }
                mismatchDescription.appendText("\n\t  ").appendValue(i + 1).appendText(" ").appendValue(expected.get(i));
            }
        }
    }
}
