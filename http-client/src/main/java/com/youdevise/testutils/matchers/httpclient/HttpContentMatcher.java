package com.youdevise.testutils.matchers.httpclient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public abstract class HttpContentMatcher<T> extends TypeSafeDiagnosingMatcher<HttpEntity> {
    private final Matcher<? super ContentType> contentTypeMatcher;
    private final Matcher<? super T> contentMatcher;
    private boolean parsed;
    private T parsedValue;
    private Exception parseException;

    public HttpContentMatcher(Matcher<? super ContentType> contentTypeMatcher, Matcher<? super T> contentMatcher) {
        this.contentTypeMatcher = contentTypeMatcher;
        this.contentMatcher = contentMatcher;
    }

    @Override
    protected boolean matchesSafely(HttpEntity item, Description mismatchDescription) {
        Header contentTypeHeader = item.getContentType();
        ContentType contentType;
        if (contentTypeHeader != null) {
            contentType = ContentType.parse(contentTypeHeader.getValue());
        } else {
            contentType = null;
        }
        if (!contentTypeMatcher.matches(contentType)) {
            mismatchDescription.appendText("content type ");
            contentTypeMatcher.describeMismatch(contentType, mismatchDescription);
            return false;
        }
        T value = parse(item);
        if (!contentMatcher.matches(value)) {
            mismatchDescription.appendText("content ");
            contentMatcher.describeMismatch(value, mismatchDescription);
            return false;
        }
        return true;
    }

    protected T parse(HttpEntity item) {
        if (parsed) {
            if (parseException != null)
                throw new AssertionError("Failed to parse content", parseException);
            return parsedValue;
        }
        try {
            parsedValue = doParse(item);
            return parsedValue;
        } catch (Exception e) {
            parseException = e;
            throw new AssertionError("Failed to parse content", e);
        } finally {
            parsed = true;
        }
    }

    protected abstract T doParse(HttpEntity item) throws Exception;

    @Override
    public void describeTo(Description description) {
        description.appendText("type ").appendDescriptionOf(contentTypeMatcher).appendText(": ").appendDescriptionOf(contentMatcher);
    }
}
