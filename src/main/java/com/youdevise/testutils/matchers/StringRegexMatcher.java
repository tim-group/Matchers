package com.youdevise.testutils.matchers;

import java.util.regex.Pattern;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public final class StringRegexMatcher extends TypeSafeDiagnosingMatcher<String> {
    private final String pattern;

    private StringRegexMatcher(String pattern) {
        this.pattern = pattern;
    }

    @Factory
    public static StringRegexMatcher matchesRegex(final String pattern) {
        return new StringRegexMatcher(pattern);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("matches ").appendValue(pattern);
    }

    @Override
    protected boolean matchesSafely(String item, Description mismatchDescription) {
        mismatchDescription.appendText("was ").appendValue(item);
        return Pattern.compile(pattern, Pattern.DOTALL).matcher(item).find();
    }
}
