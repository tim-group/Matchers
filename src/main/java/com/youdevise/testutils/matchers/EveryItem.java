package com.youdevise.testutils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static com.google.common.collect.Iterables.filter;
import static com.youdevise.testutils.matchers.MatcherPredicates.notMatching;

public class EveryItem<T> extends TypeSafeDiagnosingMatcher<Iterable<T>> {
    
    public static <T> EveryItem<T>  matching(Matcher<? super T> matcher) {
        return new EveryItem<T>(matcher);
    }
    
    private final Matcher<? super T> matcher;
    
    private EveryItem(Matcher<? super T> matcher) {
        this.matcher = matcher;
    }

    @Override
    public void describeTo(Description arg0) {
        Matchers.everyItem(matcher).describeTo(arg0);
    }

    @Override
    protected boolean matchesSafely(Iterable<T> item, Description mismatchDescription) {
        if (Matchers.everyItem(matcher).matches(item)) {
            return true;
        }
        mismatchDescription.appendText("the items ")
            .appendValueList("[", ", ", "]", filter(item, notMatching(matcher)))
            .appendText(" were ")
            .appendDescriptionOf(matcher);
        return false;
    }

}
