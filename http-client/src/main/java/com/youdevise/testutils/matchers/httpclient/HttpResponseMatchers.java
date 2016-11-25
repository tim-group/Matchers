package com.youdevise.testutils.matchers.httpclient;

import java.net.URI;
import java.net.URISyntaxException;

import com.fasterxml.jackson.core.TreeNode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.dom4j.Document;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static com.youdevise.testutils.matchers.httpclient.HttpEntityMatchers.htmlInUtf8;
import static com.youdevise.testutils.matchers.httpclient.HttpEntityMatchers.json;
import static com.youdevise.testutils.matchers.httpclient.HttpEntityMatchers.validXmlDom4J;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_GONE;
import static org.apache.http.HttpStatus.SC_MOVED_PERMANENTLY;
import static org.apache.http.HttpStatus.SC_MOVED_TEMPORARILY;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_TEMPORARY_REDIRECT;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.equalTo;

public final class HttpResponseMatchers {
    private HttpResponseMatchers() {
    }

    public static Matcher<HttpResponse> positive() {
        return statusCode(either(equalTo(SC_OK)).or(equalTo(SC_NO_CONTENT)));
    }

    public static Matcher<HttpResponse> ok() {
        return statusCode(SC_OK);
    }

    public static Matcher<HttpResponse> notFound() {
        return statusCode(SC_NOT_FOUND);
    }

    public static Matcher<HttpResponse> forbidden() {
        return statusCode(SC_FORBIDDEN);
    }

    public static Matcher<HttpResponse> unauthorised() {
        return statusCode(SC_UNAUTHORIZED);
    }

    public static Matcher<HttpResponse> badRequest() {
        return statusCode(SC_BAD_REQUEST);
    }

    public static Matcher<HttpResponse> movedTemporarilyTo(Matcher<URI> targetMatcher) {
        return redirectTo(equalTo(SC_MOVED_TEMPORARILY), targetMatcher);
    }

    public static Matcher<HttpResponse> movedPermanentlyTo(Matcher<URI> targetMatcher) {
        return redirectTo(equalTo(SC_MOVED_PERMANENTLY), targetMatcher);
    }

    public static Matcher<HttpResponse> temporaryRedirectTo(Matcher<URI> targetMatcher) {
        return redirectTo(equalTo(SC_TEMPORARY_REDIRECT), targetMatcher);
    }

    public static Matcher<HttpResponse> gone() {
        return statusCode(SC_GONE);
    }

    public static Matcher<HttpResponse> statusCode(int code) {
        return status(StatusLineMatchers.code(code));
    }

    public static Matcher<HttpResponse> statusCode(Matcher<Integer> codeMatcher) {
        return status(StatusLineMatchers.code(codeMatcher));
    }

    public static Matcher<HttpResponse> status(Matcher<StatusLine> statusMatcher) {
        return new TypeSafeDiagnosingMatcher<HttpResponse>() {
            @Override
            protected boolean matchesSafely(HttpResponse item, Description mismatchDescription) {
                mismatchDescription.appendText("status ");
                statusMatcher.describeMismatch(item.getStatusLine(), mismatchDescription);
                return statusMatcher.matches(item.getStatusLine());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("status ").appendDescriptionOf(statusMatcher);
            }
        };
    }

    public static Matcher<HttpResponse> content(Matcher<? super HttpEntity> entityMatcher) {
        return new TypeSafeDiagnosingMatcher<HttpResponse>() {
            @Override
            protected boolean matchesSafely(HttpResponse item, Description mismatchDescription) {
                mismatchDescription.appendText("entity ");
                entityMatcher.describeMismatch(item.getEntity(), mismatchDescription);
                return entityMatcher.matches(item.getEntity());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("entity ").appendDescriptionOf(entityMatcher);
            }
        };
    }

    public static Matcher<HttpResponse> redirectTo(Matcher<Integer> codeMatcher, Matcher<URI> targetMatcher) {
        return new TypeSafeDiagnosingMatcher<HttpResponse>() {
            @Override
            protected boolean matchesSafely(HttpResponse item, Description mismatchDescription) {
                if (!codeMatcher.matches(item.getStatusLine().getStatusCode())) {
                    mismatchDescription.appendText("status was ").appendValue(item.getStatusLine());
                    return false;
                }
                if (!item.containsHeader("Location")) {
                    mismatchDescription.appendText("no Location header");
                    return false;
                }
                String locationString = item.getFirstHeader("Location").getValue();
                URI uri;
                try {
                    uri = new URI(locationString);
                } catch (URISyntaxException e) {
                    mismatchDescription.appendText("Location URI is invalid: ").appendValue(locationString);
                    return false;
                }
                mismatchDescription.appendText("target ");
                targetMatcher.describeMismatch(uri, mismatchDescription);
                return targetMatcher.matches(uri);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("temporary redirect to ").appendDescriptionOf(targetMatcher);
            }
        };
    }

    public static <T extends TreeNode> Matcher<HttpResponse> jsonContent(Matcher<T> contentMatcher) {
        return content(json(contentMatcher));
    }

    public static Matcher<HttpResponse> xmlContent(Matcher<Document> contentMatcher) {
        return content(validXmlDom4J(contentMatcher));
    }

    public static Matcher<HttpResponse> htmlInUtf8Content(Matcher<String> contentMatcher) {
        return content(htmlInUtf8(contentMatcher));
    }
}
