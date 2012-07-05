package com.youdevise.testutils.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.google.common.base.Predicate;

public class MatchableWith<T> extends TypeSafeDiagnosingMatcher<T> {
    
    public static <T> MatchableWith<T> thePredicate(Predicate<? super T> predicate) {
        return new MatchableWith<T>(predicate);
    }
    
    private final Predicate<? super T> predicate;
    
    public MatchableWith(Predicate<? super T> predicate) {
        this.predicate = predicate;
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText("an item matching the supplied predicate");
    }

    @Override
    protected boolean matchesSafely(T item, Description mismatchDescription) {
        if (predicate.apply(item)) {
            return true;
        }
        mismatchDescription.appendValue(item);
        mismatchDescription.appendText(" did not match the supplied predicate");
        return false;
    }

}
