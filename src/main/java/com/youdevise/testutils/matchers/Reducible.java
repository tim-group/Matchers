package com.youdevise.testutils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;


public class Reducible<A, B> extends TypeSafeDiagnosingMatcher<Iterable<A>> {

    public static class ReducibleBinder<A, B> {
        private final Reducer<A, B> reducer;
        public ReducibleBinder(Reducer<A, B> reducer) {
            this.reducer = reducer;
        }
        
        public Reducible<A, B> to(B total) {
            return to(Matchers.equalTo(total));
        }
        
        public Reducible<A, B> to(Matcher<? super B> totalMatcher) {
            return new Reducible<A, B>(reducer, totalMatcher);
        }
    }
    
    public static <A, B> ReducibleBinder<A, B> with(Reducer<A, B> reducer) {
        return new ReducibleBinder<A, B>(reducer);
    }
    
    private final Reducer<A, B> reducer;
    private final Matcher<? super B> totalMatcher;

    private Reducible(Reducer<A, B> reducer, Matcher<? super B> totalMatcher) {
        this.reducer = reducer;
        this.totalMatcher = totalMatcher;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("an iterable with a total that matches ").appendDescriptionOf(totalMatcher);
    }

    @Override
    protected boolean matchesSafely(Iterable<A> item, Description mismatchDescription) {
        B total = Reducers.reduce(item, reducer);
        if (totalMatcher.matches(total)) {
            return true;
        }
        mismatchDescription.appendText("the total ");
        totalMatcher.describeMismatch(total, mismatchDescription);
        return false;
    }
}