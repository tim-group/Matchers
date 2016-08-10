package com.youdevise.testutils.matchers;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

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
            return new Reducible<>(reducer, totalMatcher);
        }
    }

    public static <A, B> ReducibleBinder<A, B> with(Reducer<A, B> reducer) {
        return new ReducibleBinder<>(reducer);
    }

    public static <A> ReducibleBinder<A, A> with(A identity, BinaryOperator<A> op) {
        return new ReducibleBinder<>(Reducers.endo(identity, op));
    }

    public static <A, B> ReducibleBinder<A, B> with(B identity, BinaryOperator<B> combiner,
            BiFunction<A, B, B> accumulator) {
        return new ReducibleBinder<>(Reducers.general(identity, combiner, accumulator));
    }

    private final Reducer<A, B> reducer;
    private final Matcher<? super B> totalMatcher;

    public Reducible(Reducer<A, B> reducer, Matcher<? super B> totalMatcher) {
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