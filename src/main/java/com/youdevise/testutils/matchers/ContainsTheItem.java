package com.youdevise.testutils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.List;

public class ContainsTheItem<T> extends TypeSafeDiagnosingMatcher<Iterable<? extends T>> {
    private final Matcher<Iterable<? super T>> contains;
    private final Matcher<? super T> expected;

    public ContainsTheItem(Matcher<? super T> expected) {
        this.expected = expected;
        contains = Matchers.<T>hasItem(expected);
    }

    @Override
    public void describeTo(Description description) {
        contains.describeTo(description);
    }

    @Override
    protected boolean matchesSafely(Iterable<? extends T> actual, Description mismatchDescription) {
        List<? extends T> actualList = CollectionMatcher.listOf(actual);
        diagnoseFailures(actual, mismatchDescription);
        mismatchDescription.appendText("\n\tComplete actual iterable: ").appendValue(actualList);
        return contains.matches(actual);
    }

    private void diagnoseFailures(Iterable<? extends T> actual, Description mismatchDescription) {
        List<? extends T> actualList = CollectionMatcher.listOf(actual);
        if (actualList.isEmpty()) {
            mismatchDescription.appendText("the actual collection was empty ");
            return;
        }
        int i = 0;
        for (T actualItem : actualList) {
            mismatchDescription.appendText("\n\t  ").appendValue(++i).appendText(" ");
            expected.describeMismatch(actualItem, mismatchDescription);
        }
    }
}
