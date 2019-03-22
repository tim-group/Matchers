package com.youdevise.testutils.matchers;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class WithoutDuplicatesMatcher<T> extends TypeSafeDiagnosingMatcher<Iterable<? extends T>> {
    
    public static <T> WithoutDuplicatesMatcher<T> withoutDuplicates() {
        return new WithoutDuplicatesMatcher<T>();
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText("a collection containing no duplicates");
    }

    @Override
    protected boolean matchesSafely(Iterable<? extends T> item, Description mismatchDescription) {
        if (Iterables.size(item) == Sets.newHashSet(item).size()) {
            return true;
        }
        mismatchDescription.appendText("contained duplicates");
        return false;
    }
    
}