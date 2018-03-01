package com.youdevise.testutils.matchers.httpclient;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.apache.http.Header;
import org.apache.http.HttpMessage;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.Set;

import static org.hamcrest.Matchers.equalTo;

public final class HttpMessageMatchers {
    private HttpMessageMatchers() {
    }

    public static Matcher<HttpMessage> messageWithHeaders(final String headerName, final Matcher<Set<String>> valuesMatcher) {
        return new TypeSafeDiagnosingMatcher<HttpMessage>() {
            @Override
            protected boolean matchesSafely(HttpMessage item, Description mismatchDescription) {
                Header[] headers = item.getHeaders(headerName);
                if (headers == null || headers.length == 0) {
                    mismatchDescription.appendText("no such header: ").appendValue(headerName);
                    return valuesMatcher.matches(ImmutableSet.of());
                }
                Set<String> values = Sets.newHashSet();
                for (Header header : headers) {
                    values.add(header.getValue());
                }
                mismatchDescription.appendText("in ").appendValue(headerName).appendText(" headers: ");
                valuesMatcher.describeMismatch(values, mismatchDescription);
                return valuesMatcher.matches(values);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with ").appendValue(headerName).appendText(" headers ").appendDescriptionOf(valuesMatcher);
            }
        };
    }

    public static Matcher<HttpMessage> messageWithSingleHeader(final String headerName, final Matcher<String> valueMatcher) {
        return new TypeSafeDiagnosingMatcher<HttpMessage>() {
            @Override
            protected boolean matchesSafely(HttpMessage item, Description mismatchDescription) {
                Header[] headers = item.getHeaders(headerName);
                if (headers == null || headers.length == 0) {
                    mismatchDescription.appendText("no such header: ").appendValue(headerName);
                    return valueMatcher.matches(null);
                }
                if (headers.length > 1) {
                    mismatchDescription.appendText("multiple headers: ").appendValue(headerName).appendText(" ")
                            .appendValue(ImmutableList.copyOf(headers));
                    return false;
                }
                String value = headers[0].getValue();
                mismatchDescription.appendText("in ").appendValue(headerName).appendText(" header: ");
                valueMatcher.describeMismatch(value, mismatchDescription);
                return valueMatcher.matches(value);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with ").appendValue(headerName).appendText(" header ").appendDescriptionOf(valueMatcher);
            }
        };
    }

    public static Matcher<HttpMessage> messageWithoutHeader(final String headerName) {
        return new TypeSafeDiagnosingMatcher<HttpMessage>() {
            @Override
            protected boolean matchesSafely(HttpMessage item, Description mismatchDescription) {
                Header[] headers = item.getHeaders(headerName);
                mismatchDescription.appendText("contained headers: ").appendValue(headerName).appendText(" ")
                        .appendValue(headers != null ? ImmutableList.copyOf(headers) : ImmutableList.of());
                return headers == null || headers.length == 0;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("without a ").appendValue(headerName).appendText(" header ");
            }
        };
    }

    public static Matcher<HttpMessage> containsHeader(final Matcher<Header> matcher) {
        return new TypeSafeDiagnosingMatcher<HttpMessage>() {
            @Override
            protected boolean matchesSafely(HttpMessage item, Description mismatchDescription) {
                mismatchDescription.appendText("not found: ").appendDescriptionOf(matcher);
                for (Header header : item.getAllHeaders()) {
                    if (matcher.matches(header))
                        return true;
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with header ").appendDescriptionOf(matcher);
            }
        };
    }

    public static Matcher<HttpMessage> messageWithSingleHeader(String name, String value) {
        return messageWithSingleHeader(name, equalTo(value));
    }

    public static Matcher<HttpMessage> messageWithHeader(String name, String value) {
        return messageWithHeaders(name, containing(value));
    }

    private static Matcher<Set<String>> containing(final String value) {
        return new TypeSafeDiagnosingMatcher<Set<String>>() {
            @Override
            protected boolean matchesSafely(Set<String> item, Description mismatchDescription) {
                mismatchDescription.appendText("contained ").appendValue(item);
                return item.contains(value);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("containing ").appendValue(value);
            }
        };
    }
}
