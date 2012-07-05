package com.youdevise.testutils.matchers;

import org.hamcrest.Matcher;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public final class MatcherPredicates {

    private MatcherPredicates() { }
    
    public static <T> Predicate<T> matching(final Matcher<? super T> matcher) {
        return new Predicate<T>() {
            @Override
            public boolean apply(T item) {
                return matcher.matches(item);
            }
        };
    }
    
    public static <T> Predicate<T> notMatching(final Matcher<? super T> matcher) {
        Predicate<T> matching = matching(matcher);
        return Predicates.not(matching);
    }
    
}
