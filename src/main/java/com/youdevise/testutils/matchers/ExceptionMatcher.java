package com.youdevise.testutils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class ExceptionMatcher extends TypeSafeDiagnosingMatcher<Exception> {
    private final Class<? extends Exception> expectedExceptionClass;
    private String expectedMessage;
    private Matcher<? extends Exception> expectedCause;

    private ExceptionMatcher(Class<? extends Exception> exceptionClass) {
        this.expectedExceptionClass = exceptionClass;
    }

    public static ExceptionMatcher anExceptionOfType(Class<? extends Exception> exceptionClass) {
        return new ExceptionMatcher(exceptionClass);
    }

    public ExceptionMatcher withTheMessage(String message) {
        this.expectedMessage = message;
        return this;
    }

    public ExceptionMatcher causedBy(Matcher<? extends Exception> cause) {
        this.expectedCause = cause;
        return this;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a <").appendText(expectedExceptionClass.getName()).appendText(">");
        if (expectedMessage != null) {
            description.appendText(" with the message ").appendValue(expectedMessage);
        }
        if (expectedCause != null) {
            description.appendText(" caused by ").appendDescriptionOf(expectedCause);
        }
    }

    @Override
    protected boolean matchesSafely(Exception exception, Description mismatchDescription) {
        Class<? extends Exception> actualExceptionClass = exception.getClass();
        if (!expectedExceptionClass.equals(actualExceptionClass)) {
            mismatchDescription.appendText("was a <").appendText(actualExceptionClass.getName()).appendText(">");
            return false;
        }

        String actualMessage = exception.getMessage();
        if (expectedMessage != null && !expectedMessage.equals(actualMessage)) {
            mismatchDescription.appendText("had the message ").appendValue(actualMessage);
        }

        Throwable actualCause = exception.getCause();
        if (expectedCause != null && !expectedCause.matches(actualCause)) {
            if (!mismatchDescription.toString().isEmpty()) {
                mismatchDescription.appendText(" and ");
            }

            mismatchDescription.appendText("was caused by an exception that ");
            expectedCause.describeMismatch(actualCause, mismatchDescription);
            return false;
        }

        return false;
    }
}
