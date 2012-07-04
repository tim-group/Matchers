package com.youdevise.testutils.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public class WithoutDuplicatesMatcher<T> extends TypeSafeDiagnosingMatcher<Iterable<T>> {
    
    public static <T> WithoutDuplicatesMatcher<T> withoutDuplicates() {
        return new WithoutDuplicatesMatcher<T>();
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText("a collection containing no duplicates");
    }

    @Override
    protected boolean matchesSafely(Iterable<T> item, Description mismatchDescription) {
        if (Iterables.size(item)==Sets.newHashSet(item).size()) {
            return true;
        }
        mismatchDescription.appendText("contained duplicates");
        return false;
    }
    
}