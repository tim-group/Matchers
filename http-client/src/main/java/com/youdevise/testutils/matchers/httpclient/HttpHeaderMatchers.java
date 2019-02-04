package com.youdevise.testutils.matchers.httpclient;

import java.net.HttpCookie;
import java.util.List;

import org.apache.http.Header;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public final class HttpHeaderMatchers {
    private HttpHeaderMatchers() {
    }

    public static Matcher<Header> newCookie(final String name, final Matcher<? super String> valueMatcher, final Matcher<? super String> pathMatcher) {
        return singleCookie(new TypeSafeDiagnosingMatcher<HttpCookie>() {
            @Override
            protected boolean matchesSafely(HttpCookie item, Description mismatchDescription) {
                if (!item.getName().equals(name)) {
                    mismatchDescription.appendText("Cookie name was ").appendValue(item.getName());
                    return false;
                }
                if (!valueMatcher.matches(item.getValue())) {
                    mismatchDescription.appendText("Cookie ").appendValue(name).appendText(" value ");
                    valueMatcher.describeMismatch(item.getValue(), mismatchDescription);
                    return false;
                }
                if (!pathMatcher.matches(item.getPath())) {
                    mismatchDescription.appendText("Cookie ").appendValue(name).appendText(" path ");
                    pathMatcher.describeMismatch(item.getPath(), mismatchDescription);
                    return false;
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Set cookie ").appendValue(name).appendText(" to ").appendDescriptionOf(valueMatcher);
            }
        });
    }

    public static Matcher<Header> removeCookie(final String name) {
        return singleCookie(new TypeSafeDiagnosingMatcher<HttpCookie>() {
            @Override
            protected boolean matchesSafely(HttpCookie item, Description mismatchDescription) {
                if (!item.getName().equals(name)) {
                    mismatchDescription.appendText("Cookie was ").appendValue(item);
                    return false;
                }
                if (item.getMaxAge() > 0) {
                    mismatchDescription.appendText("Cookie ").appendValue(item.getName()).appendText(" has positive max-age");
                    return false;
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Remove cookie ").appendValue(name);
            }
        });
    }

    public static Matcher<Header> anyCookieHeader() {
        return new TypeSafeDiagnosingMatcher<Header>() {
            @Override
            protected boolean matchesSafely(Header item, Description mismatchDescription) {
                mismatchDescription.appendText("Contained ").appendValue(item);
                return item.getName().equalsIgnoreCase("Set-Cookie");
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("any cookie header");
            }
        };
    }

    public static Matcher<Header> singleCookie(final Matcher<? super HttpCookie> cookieMatcher) {
        return new TypeSafeDiagnosingMatcher<Header>() {
            @Override
            protected boolean matchesSafely(Header item, Description mismatchDescription) {
                if (!item.getName().equalsIgnoreCase("Set-Cookie")) {
                    mismatchDescription.appendText("Not a cookie header: ").appendValue(item);
                    return false;
                }
                List<HttpCookie> parsed;
                try {
                    parsed = HttpCookie.parse(item.toString());
                } catch (Exception e) {
                    mismatchDescription.appendText("Unparseable cookie header: ").appendValue(item).appendText(": ").appendValue(e);
                    return false;
                }
                if (parsed.size() != 1) {
                    mismatchDescription.appendText("Didn't get a unique cookie from header: ").appendValue(parsed);
                    return false;
                }
                HttpCookie cookie = parsed.get(0);
                if (!cookieMatcher.matches(cookie)) {
                    cookieMatcher.describeMismatch(cookie, mismatchDescription);
                    return false;
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("single cookie ").appendDescriptionOf(cookieMatcher);
            }
        };
    }
}
