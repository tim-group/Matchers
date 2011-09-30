package com.youdevise.testutils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class StringContainsSequence extends TypeSafeMatcher<String> {

    private final String[] substringSequence;

    protected StringContainsSequence(final String... substringSequence) {
        this.substringSequence = substringSequence;
    }

    @Override
    public boolean matchesSafely(String item) {
        int fromIndex = 0;
        for (String substring : substringSequence) {
            int matchIndex = item.indexOf(substring, fromIndex);
            if (matchIndex >= 0) {
                fromIndex = matchIndex + substring.length();
            } else {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void describeMismatchSafely(String item, Description mismatchDescription) {
      mismatchDescription.appendText("was \"").appendText(item).appendText("\"");
    }
    
    public void describeTo(Description description) {
        description.appendText("a string contains the sequence of substrings")
                   .appendText(" ")
                   .appendValue(substringSequence);
    }

    @Factory
    public static Matcher<String> containsStringSequence(String... substringSequence) {
        return new StringContainsSequence(substringSequence);
    }

}