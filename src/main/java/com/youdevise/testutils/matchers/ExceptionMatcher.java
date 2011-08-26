package com.youdevise.testutils.matchers;

import com.youdevise.testutils.operations.ActionResult;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class ExceptionMatcher extends TypeSafeDiagnosingMatcher<ActionResult> {
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
    protected boolean matchesSafely(ActionResult result, Description mismatchDescription) {
        if (result.isSuccess()) {
            mismatchDescription.appendText("did not throw an exception");
            return false;
        }

        Exception thrownException = result.getException();
        mismatchDescription.appendText("threw the exception ").appendValue(thrownException);
        return thrownException == exception;
    }
}
