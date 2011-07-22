package com.youdevise.testutils.matchers;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class ContainsTheItem<T> extends TypeSafeDiagnosingMatcher<Iterable<T>> {
    
    private final Matcher<Iterable<? super T>> contains;
    private final Matcher<T> expected;
    
    public ContainsTheItem(Matcher<T> expected) {
        this.expected = expected;
        contains = Matchers.<T>hasItem(expected);
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
        int i = 0;
        for (T actualItem : actualList) {
            mismatchDescription.appendText("\n\t  ").appendValue(++i).appendText(" ");
            expected.describeMismatch(actualItem, mismatchDescription);
        }
    }
}