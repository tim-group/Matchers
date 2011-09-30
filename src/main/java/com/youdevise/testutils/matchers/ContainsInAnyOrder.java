package com.youdevise.testutils.matchers;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public class ContainsInAnyOrder<T> extends CollectionMatcher<T> {

    public ContainsInAnyOrder(Matcher<T>[] expected) {
        super(expected, (expected == null || expected.length == 0) ? Matchers.<T>emptyIterable() : Matchers.<T>containsInAnyOrder(expected));
    }

    @Override
    protected void diagnoseFailures(Iterable<T> actual, Description mismatchDescription, Matcher<T>[] expected) {
        List<T> actualList = listOf(actual);

        if (actualCollectionIsEmpty(mismatchDescription, actualList)) {
            return;
        }

        if (actualList.size() != expected.length)  {
            mismatchDescription.appendText(String.format("expected size %d, actual size %d; ", expected.length, actualList.size()));
        }

        List<Integer> unsatisfiedIndices = unsatisfiedIndices(actualList, expected);
        describeMismatchList(mismatchDescription, "Items that were expected, but not present", expected, unsatisfiedIndices);
        List<Integer> unexpectedIndices = unexpectedIndices(actualList, expected);
        describeMismatchList(mismatchDescription, "Unexpected items", actualList.toArray(), unexpectedIndices);
    }

    private List<Integer> unsatisfiedIndices(List<T> actualList, Matcher<T>[] expected) {
        List<Integer> unsatisfied = new ArrayList<Integer>();
        for (int i = 0; i < expected.length; i++) {
            Matcher<T> expectedItem = expected[i];
            if ( !Matchers.hasItem(expectedItem).matches(actualList) ) {
                unsatisfied.add(i);
            }
        }
        return unsatisfied;
    }
    
    private List<Integer> unexpectedIndices(List<T> actualList, Matcher<T>[] expected) {
        List<Integer> unexpected = new ArrayList<Integer>();
        for (int i = 0; i < actualList.size(); i++) {
            if (!Matchers.anyOf(expected).matches(actualList.get(i))) {
                unexpected.add(i);
            }
        }
        return unexpected;
    }

    private void describeMismatchList(Description mismatchDescription, String title, Object[] items, List<Integer> mismatchIndices) {
        boolean first = true;
        for (int i = 0; i < mismatchIndices.size(); i++) {
            if (first) {
                mismatchDescription.appendText("\n\t" + title + ": ");
                first = false;
            }
            Integer mismatchIndex = mismatchIndices.get(i);
            Object mismatchItem = items[mismatchIndex];
            mismatchDescription.appendText("\n\t  ").appendValue(mismatchIndex + 1).appendText(" ").appendValue(mismatchItem);
        }
    }

}