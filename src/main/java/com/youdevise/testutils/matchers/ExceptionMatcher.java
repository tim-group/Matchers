package com.youdevise.testutils.matchers;

import com.youdevise.testutils.operations.Action;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class ExceptionMatcher extends TypeSafeDiagnosingMatcher<Action> {
    private final Exception exception;

    private ExceptionMatcher(Exception exception) {
        this.exception = exception;
    }

    public static ExceptionMatcher throwsException(Exception exception) {
        return new ExceptionMatcher(exception);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("throws the exception ").appendValue(exception);
    }

    @Override
    protected boolean matchesSafely(Action action, Description mismatchDescription) {
        try {
            action.execute();
        } catch (Exception thrownException) {
            mismatchDescription.appendText("threw the exception ").appendValue(thrownException);
            return thrownException == exception;
        }

        mismatchDescription.appendText("did not throw an exception");
        return false;
    }
}
