package com.youdevise.testutils.matchers.httpclient;

import org.apache.http.entity.ContentType;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.hamcrest.Matchers.equalToIgnoringCase;

public final class ContentTypeMatchers {
    private ContentTypeMatchers() {
    }

    public static Matcher<ContentType> mimeType(String type) {
        return mimeType(equalToIgnoringCase(type));
    }

    public static Matcher<ContentType> mimeType(Matcher<? super String> matcher) {
        return new TypeSafeDiagnosingMatcher<ContentType>() {
            @Override
            protected boolean matchesSafely(ContentType item, Description mismatchDescription) {
                matcher.describeMismatch(item.getMimeType(), mismatchDescription);
                return matcher.matches(item.getMimeType());
            }

            @Override
            public void describeTo(Description description) {
                description.appendDescriptionOf(matcher);
            }
        };
    }
}
