package com.youdevise.testutils.matchers;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class ContainsTheItems<T> extends TypeSafeDiagnosingMatcher<Iterable<T>> {
    
    private final Matcher<Iterable<T>> contains;
    private final Matcher<T>[] expected;
    
    public ContainsTheItems(Matcher<T>[] expected) {
        this.expected = expected;
        contains = Matchers.<T>hasItems(expected);
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
        for (int i = 0; i < expected.length; i++) {
            if (!Matchers.hasItem(expected[i]).matches(actualList)) {
                if (first) {
                    mismatchDescription.appendText("\n\tItems that were expected, but not present: ");
                    first = false;
                }
                mismatchDescription.appendText("\n\t  ").appendValue(i + 1).appendText(" ").appendValue(expected[i]);
            }
        }
    }
}