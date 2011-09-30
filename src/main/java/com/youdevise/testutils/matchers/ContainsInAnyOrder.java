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

        List<Matcher<T>> unsatisfiedMatchers = unsatisfiedMatchers(actualList, expected);
        describeMismatchList(mismatchDescription, "Items that were expected, but not present", unsatisfiedMatchers);
        List<T> unexpectedItems = unexpectedItems(actualList, expected);
        describeMismatchList(mismatchDescription, "Unexpected items", unexpectedItems);
    }

    private List<Matcher<T>> unsatisfiedMatchers(List<T> actualList, Matcher<T>[] expected) {
        List<Matcher<T>> unsatisfied = new ArrayList<Matcher<T>>();
        for (int i = 0; i < expected.length; i++) {
            Matcher<T> expectedItem = expected[i];
            if ( !Matchers.hasItem(expectedItem).matches(actualList) ) {
                unsatisfied.add(expectedItem);
            }
        }
        return unsatisfied;
    }
    
    private List<T> unexpectedItems(List<T> actualList, Matcher<T>[] expected) {
        List<T> unexpected = new ArrayList<T>();
        for (T actual : actualList) {
            if (!Matchers.anyOf(expected).matches(actual)) {
                unexpected.add(actual);
            }
        }
        return unexpected;
    }

    private void describeMismatchList(Description mismatchDescription, String title, List<?> unexpectedItems) {
        boolean first = true;
        for (int i = 0; i < unexpectedItems.size(); i++) {
            if (first) {
                mismatchDescription.appendText("\n\t" + title + ": ");
                first = false;
            }
            mismatchDescription.appendText("\n\t  ").appendValue(i + 1).appendText(" ").appendValue(unexpectedItems.get(i));
        }
    }

}