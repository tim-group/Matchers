package com.youdevise.testutils.matchers.httpclient;

import org.apache.http.StatusLine;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.hamcrest.Matchers.equalTo;

public final class StatusLineMatchers {
    private StatusLineMatchers() {
    }

    public static Matcher<StatusLine> code(int code) {
        return code(equalTo(code));
    }

    public static Matcher<StatusLine> code(Matcher<? super Integer> codeMatcher) {
        return new TypeSafeDiagnosingMatcher<StatusLine>() {
            @Override
            protected boolean matchesSafely(StatusLine item, Description mismatchDescription) {
                mismatchDescription.appendText("status code ");
                codeMatcher.describeMismatch(item.getStatusCode(), mismatchDescription);
                return codeMatcher.matches(item.getStatusCode());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("HTTP status ").appendDescriptionOf(codeMatcher);
            }
        };
    }
}
