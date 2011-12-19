package com.youdevise.testutils.matchers;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public class ContainsInAnyOrder<T> extends CollectionMatcher<T> {

    public ContainsInAnyOrder(Matcher<T>[] expected) {
        super(expected, (expected == null || expected.length == 0)
                            ? Matchers.<T>emptyIterable()
                            : Matchers.<T>containsInAnyOrder(expected));
    }

    @Override
    protected void diagnoseFailures(Iterable<T> actual, Description mismatchDescription, Matcher<T>[] expected) {
        final List<T> actualList = listOf(actual);

        if (actualCollectionIsEmpty(mismatchDescription, actualList)) {
            return;
        }

        if (actualList.size() != expected.length) {
            mismatchDescription.appendText(String.format("expected size %d, actual size %d; ", expected.length, actualList.size()));
        }

        final List<Integer> unsatisfiedIndices = unsatisfiedIndices(actualList, expected);
        final List<Integer> unexpectedIndices = unexpectedIndices(actualList, expected);

        describeSingleMismatch(mismatchDescription, expected, unsatisfiedIndices, actualList, unexpectedIndices);
        describeMismatchList(mismatchDescription, "Items that were expected, but not present", expected, unsatisfiedIndices);
        describeMismatchList(mismatchDescription, "Unexpected items", actualList.toArray(), unexpectedIndices);
    }

    private List<Integer> unsatisfiedIndices(List<T> actualList, Matcher<T>[] expected) {
        final List<Integer> unsatisfied = new ArrayList<Integer>();
        for (int i = 0; i < expected.length; i++) {
            final Matcher<T> expectedItem = expected[i];
            if (!Matchers.hasItem(expectedItem).matches(actualList)) {
                unsatisfied.add(i);
            }
        }
        return unsatisfied;
    }

    private List<Integer> unexpectedIndices(List<T> actualList, Matcher<T>[] expected) {
        final List<Integer> unexpected = new ArrayList<Integer>();
        for (int i = 0; i < actualList.size(); i++) {
            if (!Matchers.anyOf(expected).matches(actualList.get(i))) {
                unexpected.add(i);
            }
        }
        return unexpected;
    }

    private void describeSingleMismatch(Description mismatchDescription,
                                        Matcher<T>[] expected,
                                        List<Integer> unsatisfiedIndices,
                                        List<T> actualList,
                                        List<Integer> unexpectedIndices) {
        if ((unsatisfiedIndices.size() == 1) && (unexpectedIndices.size() == 1)) {
            final int unsatisfiedIndex = unsatisfiedIndices.get(0);
            final Matcher<T> unsatisfied = expected[unsatisfiedIndex];
            final int unexpectedIndex = unexpectedIndices.get(0);
            final T unexpected = actualList.get(unexpectedIndex);
            mismatchDescription.appendText("\n\t Possible mismatch between: ");
            mismatchDescription.appendText("unsatisfied expectation ").appendValue(unsatisfiedIndex + 1)
                               .appendText(" and unexpected ").appendValue(unexpectedIndex + 1)
                               .appendText(" ");
            unsatisfied.describeMismatch(unexpected, mismatchDescription);
        }
    }

    private void describeMismatchList(Description mismatchDescription, String title, Object[] items, List<Integer> mismatchIndices) {
        boolean first = true;
        for (int i = 0; i < mismatchIndices.size(); i++) {
            if (first) {
                mismatchDescription.appendText("\n\t" + title + ": ");
                first = false;
            }
            final Integer mismatchIndex = mismatchIndices.get(i);
            final Object mismatchItem = items[mismatchIndex];
            mismatchDescription.appendText("\n\t  ").appendValue(mismatchIndex + 1).appendText(" ").appendValue(mismatchItem);
        }
    }

}