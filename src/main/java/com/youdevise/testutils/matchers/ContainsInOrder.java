package com.youdevise.testutils.matchers;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public class ContainsInOrder<T> extends CollectionMatcher<T> {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ContainsInOrder(List<? extends Matcher<? super T>> expected) {
        super(expected, (expected == null || expected.size() == 0) ? Matchers.emptyIterable() : Matchers.contains((List<Matcher<? super T>>) expected));
    }

    @Override
    protected void diagnoseFailures(Iterable<? extends T> actual, Description mismatchDescription, List<? extends Matcher<? super T>> expected) {
        List<T> actualList = Lists.newArrayList(actual);

        if (actualList.isEmpty()) {
            mismatchDescription.appendText("the actual collection was empty ");
            return;
        }
        if (Matchers.containsInAnyOrder(expected).matches(actual)) {
            mismatchDescription.appendText("actual list had the right items but in the wrong order! ");
        }
        if (actualList.size() < expected.size())  {
            mismatchDescription.appendText(String.format("expected size %d, actual size %d; ", expected.size(), actualList.size()));
            mismatchDescription.appendText("\n\tItems that were expected, but not present: ");
            for (int i = actualList.size(); i < expected.size(); i++) {
                mismatchDescription.appendText("\n\t  ").appendValue(i + 1).appendText(" ").appendValue(expected.get(i));
            }
        }
        if (actualList.size() > expected.size())  {
            mismatchDescription.appendText(String.format("expected size %d, actual size %d; ", expected.size(), actualList.size()));
            mismatchDescription.appendText("\n\tUnexpected items: ");
            for (int i = expected.size(); i < actualList.size(); i++) {
                mismatchDescription.appendText("\n\t  ").appendValue(i + 1).appendText(" ").appendValue(actualList.get(i));
            }
        }
        describeNonCorrespondances(mismatchDescription, actualList, expected);
    }

    private void describeNonCorrespondances(Description mismatchDescription, List<T> actualList, List<? extends Matcher<? super T>> expected) {
        boolean first = true;
        for (int i = 0; i < Math.min(expected.size(), actualList.size()); i++) {
            if (itemsDontCorrespond(actualList.get(i), expected.get(i))) {
                if (first) {
                    mismatchDescription.appendText("\n\tItems that did not match their corresponding expectations: ");
                    first = false;
                }
                mismatchDescription.appendText("\n\t  ").appendValue(i + 1)
                    .appendText(" Expected (").appendDescriptionOf(expected.get(i)).appendText(")")
                    .appendText("\n but ");
                expected.get(i).describeMismatch(actualList.get(i), mismatchDescription);
            }
        }
    }

    private boolean itemsDontCorrespond(T actual, Matcher<? super T> matcher) {
        return !matcher.matches(actual);
    }
}
