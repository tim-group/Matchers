package com.youdevise.testutils.matchers;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public class ContainsInOrder<T> extends CollectionMatcher<T> {
    
    public ContainsInOrder(Matcher<T>[] expected) {
        super(expected, (expected == null || expected.length == 0) ? Matchers.<T>emptyIterable() : Matchers.<T>contains(expected));
    }

    protected void diagnoseFailures(Iterable<T> actual, Description mismatchDescription, Matcher<T>[] expected) {
        List<T> actualList = listOf(actual);
        
        if (actualList.isEmpty()) {
            mismatchDescription.appendText("the actual collection was empty ");
            return;
        } 
        if (Matchers.containsInAnyOrder(expected).matches(actual)) {
            mismatchDescription.appendText("actual list had the right items but in the wrong order! ");
        }
        if (actualList.size() < expected.length)  {
            mismatchDescription.appendText(String.format("expected size %d, actual size %d; ", expected.length, actualList.size()));
            mismatchDescription.appendText("\n\tItems that were expected, but not present: ");
            for (int i = actualList.size(); i < expected.length; i++) {
                mismatchDescription.appendText("\n\t  ").appendValue(i + 1).appendText(" ").appendValue(expected[i]);
            }
        } 
        if (actualList.size() > expected.length)  {
            mismatchDescription.appendText(String.format("expected size %d, actual size %d; ", expected.length, actualList.size()));
            mismatchDescription.appendText("\n\tUnexpected items: ");
            for (int i = expected.length; i < actualList.size(); i++) {
                mismatchDescription.appendText("\n\t  ").appendValue(i + 1).appendText(" ").appendValue(actualList.get(i));
            }
        } 
        describeNonCorrespondances(mismatchDescription, actualList, expected);
    }

    private void describeNonCorrespondances(Description mismatchDescription, List<T> actualList, Matcher<T>[] expected) {
        boolean first = true;
        for (int i = 0; i < Math.min(expected.length, actualList.size()); i++) {
            if (itemsDontCorrespond(actualList.get(i), expected[i])) {
                if (first) {
                    mismatchDescription.appendText("\n\tItems that did not match their corresponding expectations: ");
                    first = false;
                }
                mismatchDescription.appendText("\n\t  ").appendValue(i + 1)
                    .appendText(" Expected ").appendValue(expected[i])
                    .appendText(" but was ").appendValue(actualList.get(i));
            }
        }
    }

    private boolean itemsDontCorrespond(T actual, Matcher<T> matcher) {
        return !matcher.matches(actual);
    }
}