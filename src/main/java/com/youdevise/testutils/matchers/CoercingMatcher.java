package com.youdevise.testutils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public abstract class CoercingMatcher<FROM, TO> extends TypeSafeDiagnosingMatcher<FROM> {
    
    private final Matcher<? super TO> innerMatcher;

    public CoercingMatcher(Matcher<? super TO> innerMatcher) {
        this.innerMatcher = innerMatcher;
    }
    
    protected abstract TO coerce(FROM from);
    
    @Override public void describeTo(Description description) {
        description.appendText("coercible to ");
        innerMatcher.describeTo(description);
    }

    @Override protected boolean matchesSafely(FROM item, Description mismatchDescription) {
        TO coerced = coerce(item);
        mismatchDescription.appendText("coerced item ");
        innerMatcher.describeMismatch(coerced, mismatchDescription);
        return innerMatcher.matches(coerced);
    }
}