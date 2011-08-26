package com.youdevise.testutils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class ExceptionMatcher<T extends Exception> extends TypeSafeDiagnosingMatcher<T> {
    private final Class<T> exceptionClass;
    private String message;
    private Matcher<? extends Exception> cause;

    public ExceptionMatcher(Class<T> exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public static <T extends Exception> ExceptionMatcher<T> anExceptionOfType(Class<T> exceptionClass) {
        return new ExceptionMatcher<T>(exceptionClass);
    }

    public ExceptionMatcher<T> withTheMessage(String message) {
        this.message = message;
        return this;
    }

    public ExceptionMatcher<T> causedBy(Matcher<? extends Exception> cause) {
        this.cause = cause;
        return this;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a <").appendText(exceptionClass.getName()).appendText(">");
        if (message != null) {
            description.appendText(" with the message ").appendValue(message);
        }
        if (cause != null) {
            description.appendText(" caused by ").appendDescriptionOf(cause);
        }
    }

    @Override
    protected boolean matchesSafely(Exception exception, Description mismatchDescription) {
        throw new UnsupportedOperationException();
    }
}
