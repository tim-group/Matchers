package com.youdevise.testutils.matchers;

import com.youdevise.testutils.operations.ActionResult;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class ActionResultExceptionMatcher extends TypeSafeDiagnosingMatcher<ActionResult> {
    private final Exception exception;

    private ActionResultExceptionMatcher(Exception exception) {
        this.exception = exception;
    }

    public static ActionResultExceptionMatcher throwsException(Exception exception) {
        return new ActionResultExceptionMatcher(exception);
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
