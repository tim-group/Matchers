package com.youdevise.testutils.matchers;

import com.youdevise.testutils.operations.ActionResult;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class ActionResultExceptionMatcher extends TypeSafeDiagnosingMatcher<ActionResult> {
    private final Matcher<? super Exception> exceptionMatcher;

    private ActionResultExceptionMatcher(Matcher<? super Exception> exceptionMatcher) {
        this.exceptionMatcher = exceptionMatcher;
    }

    public static ActionResultExceptionMatcher throwsException(Matcher<? super Exception> exceptionMatcher) {
        return new ActionResultExceptionMatcher(exceptionMatcher);
    }

    public static ActionResultExceptionMatcher throwsException(final Exception expectedException) {
        return new ActionResultExceptionMatcher(new TypeSafeDiagnosingMatcher<Exception>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("the exception ").appendValue(expectedException);
            }

            @Override
            protected boolean matchesSafely(Exception actualException, Description mismatchDescription) {
                mismatchDescription.appendText("was the exception ").appendValue(actualException);
                return expectedException == actualException;
            }
        });
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("throws ").appendDescriptionOf(exceptionMatcher);
    }

    @Override
    protected boolean matchesSafely(ActionResult result, Description mismatchDescription) {
        if (result.isSuccess()) {
            mismatchDescription.appendText("did not throw an exception");
            return false;
        }

        Exception thrownException = result.getException();
        mismatchDescription.appendText("threw an exception that ");
        exceptionMatcher.describeMismatch(thrownException, mismatchDescription);
        return exceptionMatcher.matches(thrownException);
    }
}
