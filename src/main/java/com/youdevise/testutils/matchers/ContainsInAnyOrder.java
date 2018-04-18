package com.youdevise.testutils.matchers;

import com.google.common.collect.Lists;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.AnyOf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ContainsInAnyOrder<T> extends CollectionMatcher<T> {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ContainsInAnyOrder(List<? extends Matcher<? super T>> expected) {
        super(expected, (expected == null || expected.size() == 0)
                            ? Matchers.emptyIterable()
                            : Matchers.containsInAnyOrder((Collection<Matcher<? super T>>) expected));
    }

    @Override
    protected void diagnoseFailures(Iterable<? extends T> actual, Description mismatchDescription, List<? extends Matcher<? super T>> expected) {
        final List<T> actualList = Lists.newArrayList(actual);

        if (actualCollectionIsEmpty(mismatchDescription, actualList)) {
            return;
        }

        if (actualList.size() != expected.size()) {
            mismatchDescription.appendText(String.format("expected size %d, actual size %d; ", expected.size(), actualList.size()));
        }

        final List<Integer> unsatisfiedIndices = unsatisfiedIndices(actualList, expected);
        final List<Integer> unexpectedIndices = unexpectedIndices(actualList, expected);

        describeSingleMismatch(mismatchDescription, expected, unsatisfiedIndices, actualList, unexpectedIndices);
        describeMismatchList(mismatchDescription, "Items that were expected, but not present", expected, unsatisfiedIndices);
        describeMismatchList(mismatchDescription, "Unexpected items", actualList, unexpectedIndices);
    }

    private List<Integer> unsatisfiedIndices(List<? extends T> actualList, List<? extends Matcher<? super T>> expected) {
        final List<Integer> unsatisfied = new ArrayList<>();
        for (int i = 0; i < expected.size(); i++) {
            final Matcher<? super T> expectedItem = expected.get(i);
            if (!Matchers.hasItem(expectedItem).matches(actualList)) {
                unsatisfied.add(i);
            }
        }
        return unsatisfied;
    }

    private List<Integer> unexpectedIndices(List<? extends T> actualList, List<? extends Matcher<? super T>> expected) {
        final List<Integer> unexpected = new ArrayList<>();
        for (int i = 0; i < actualList.size(); i++) {
            if (!anyOf(expected).matches(actualList.get(i))) {
                unexpected.add(i);
            }
        }
        return unexpected;
    }

    @SuppressWarnings("unchecked")
    private AnyOf<T> anyOf(List<? extends Matcher<? super T>> expected) {
        return Matchers.anyOf((List<Matcher<? super T>>) expected);
    }

    private void describeSingleMismatch(Description mismatchDescription,
                                        List<? extends Matcher<? super T>> expected,
                                        List<? extends Integer> unsatisfiedIndices,
                                        List<? extends T> actualList,
                                        List<? extends Integer> unexpectedIndices) {
        if ((unsatisfiedIndices.size() == 1) && (unexpectedIndices.size() == 1)) {
            final int unsatisfiedIndex = unsatisfiedIndices.get(0);
            final Matcher<? super T> unsatisfied = expected.get(unsatisfiedIndex);
            final int unexpectedIndex = unexpectedIndices.get(0);
            final T unexpected = actualList.get(unexpectedIndex);
            mismatchDescription.appendText("\n\t Possible mismatch between: ");
            mismatchDescription.appendText("unsatisfied expectation ").appendValue(unsatisfiedIndex + 1)
                               .appendText(" and unexpected ").appendValue(unexpectedIndex + 1)
                               .appendText(" ");
            unsatisfied.describeMismatch(unexpected, mismatchDescription);
        }
    }

    private void describeMismatchList(Description mismatchDescription, String title, List<?> items, List<? extends Integer> mismatchIndices) {
        boolean first = true;
        for (int i = 0; i < mismatchIndices.size(); i++) {
            if (first) {
                mismatchDescription.appendText("\n\t" + title + ": ");
                first = false;
            }
            final Integer mismatchIndex = mismatchIndices.get(i);
            final Object mismatchItem = items.get(mismatchIndex);
            mismatchDescription.appendText("\n\t  ").appendValue(mismatchIndex + 1).appendText(" ").appendValue(mismatchItem);
        }
    }
}
