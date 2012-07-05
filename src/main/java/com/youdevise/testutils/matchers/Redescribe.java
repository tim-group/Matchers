package com.youdevise.testutils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.google.common.base.Function;

public class Redescribe<T> extends TypeSafeDiagnosingMatcher<T> {

    public static class RedescribeBinder<T> {
        private final Matcher<? super T> matcher;
        public RedescribeBinder(Matcher<? super T> matcher) {
            this.matcher = matcher;
        }
        
        public Redescribe<T> as(String descriptionText) {
            return with(new SelfDescribingString(descriptionText));
        }
        
        public Redescribe<T> with(final SelfDescribing describer) {
            return new Redescribe<T>(matcher, describer);
        }
    }
    
    public static <T> RedescribeBinder<T> theMatcher(Matcher<? super T> matcher) {
        return new RedescribeBinder<T>(matcher);
    }

    private final Matcher<? super T> matcher;
    private final SelfDescribing describer;
    private final Function<? super T, SelfDescribing> mismatchDescriber;
    
    public Redescribe(final Matcher<? super T> matcher, SelfDescribing describer) {
        this(matcher, describer, new Function<T, SelfDescribing>() {
            @Override public SelfDescribing apply(final T item) {
                return new SelfDescribing() {
                    @Override public void describeTo(Description mismatchDescription) {
                        matcher.describeMismatch(item, mismatchDescription);
                    }
                };
            }
        });
    }
    
    private Redescribe(final Matcher<? super T> matcher, SelfDescribing describer, Function<? super T, SelfDescribing> mismatchDescriber) {
        this.matcher = matcher;
        this.describer = describer;
        this.mismatchDescriber = mismatchDescriber;
    }
    
    @Override public void describeTo(Description description) {
        describer.describeTo(description);
    }
    
    public Redescribe<T> describingMismatchAs(final String mismatchDescriptionText) {
        return describingMismatchWith(new Function<T, SelfDescribing>() {
            @Override
            public SelfDescribing apply(final T item) {
                return new SelfDescribing() {
                    @Override public void describeTo(Description mismatchDescription) {
                        mismatchDescription.appendValue(item).appendText(" was ").appendText(mismatchDescriptionText);
                    }
                };
            }
        });
    }
    
    public <P extends T> Redescribe<P> describingMismatchWith(Function<P, SelfDescribing> newMismatchDescriber) {
        return new Redescribe<P>(matcher, describer, newMismatchDescriber);
    }

    @Override
    protected boolean matchesSafely(T item, Description mismatchDescription) {
        if (matcher.matches(item)) { return true; }
        mismatchDescriber.apply(item).describeTo(mismatchDescription);
        return false;
    }
    
}
