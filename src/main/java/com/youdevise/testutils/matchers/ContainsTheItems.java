package com.youdevise.testutils.matchers;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.google.common.collect.Iterables;

public class ContainsTheItems<T> extends TypeSafeDiagnosingMatcher<Iterable<? extends T>> {

    private final Matcher<Iterable<T>> contains;
    private final List<? extends Matcher<? super T>> expected;

    public ContainsTheItems(List<? extends Matcher<? super T>> expected) {
        this.expected = expected;
        @SuppressWarnings("unchecked")
        Matcher<? super T>[] expectedArray = Iterables.toArray(expected, Matcher.class);
        contains = Matchers.hasItems(expectedArray);
    }

    @Override
    public void describeTo(Description description) {
        contains.describeTo(description);
    }

    @Override
    protected boolean matchesSafely(Iterable<? extends T> actual, Description mismatchDescription) {
        List<T> actualList = ImmutableList.copyOf(actual);
        diagnoseFailures(actualList, mismatchDescription);
        mismatchDescription.appendText("\n\tComplete actual iterable: ").appendValue(actualList);
        return contains.matches(actual);
    }

    private void diagnoseFailures(List<? extends T> actual, Description mismatchDescription) {
        if (actual.isEmpty()) {
            mismatchDescription.appendText("the actual collection was empty ");
            return;
        }
        boolean first = true;
        for (int i = 0; i < expected.size(); i++) {
            if (!Matchers.hasItem(expected.get(i)).matches(actual)) {
                if (first) {
                    mismatchDescription.appendText("\n\tItems that were expected, but not present: ");
                    first = false;
                }
                mismatchDescription.appendText("\n\t  ").appendValue(i + 1).appendText(" ").appendValue(expected.get(i));
            }
        }
    }
}
