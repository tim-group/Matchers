package com.youdevise.testutils.matchers;

import com.google.common.collect.Iterables;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class IsEmptyMatcher<T> extends TypeSafeDiagnosingMatcher<Iterable<? extends T>> {
    
    public static <T> IsEmptyMatcher<T> isEmpty() {
        return new IsEmptyMatcher<T>();
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText("an empty iterable");
    }

    @Override
    protected boolean matchesSafely(Iterable<? extends T> item, Description mismatchDescription) {
        if (Iterables.isEmpty(item)) {
            return true;
        }
        mismatchDescription.appendText("was not empty");
        return false;
    }
}