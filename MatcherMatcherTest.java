package com.youdevise.hip.testutils.matchers.core;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;


public class MatcherMatcherTest {

    private MatcherMatcher<?> matcher;
    
    private final Matcher<String> dummyNotNullValueMatcher = new BaseMatcher<String>() {
        @Override
        public boolean matches(Object item) {
            return item != null;
        }
        
        @Override
        public void describeMismatch(Object item, Description description) {
            description.appendText("the value was null");
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a non-null value");
        }
    };
    
    @Test public void
    describesItselfWhenConfiguredToMatchOnTheDescriptionOfTheAssessedMatcher() {
        matcher = MatcherMatcher.a_matcher_with_description(equalTo("matcher description"));
        
        assertThat(matcherDescription(), is("a Matcher with description \"matcher description\""));
    }
    
    @Test public void
    describesItselfWhenConfiguredToMatchOnTheSuccessfulMatchingOfTheAssessedMatcher() {
        matcher = MatcherMatcher.a_matcher_that_matches("Apples");
        
        assertThat(matcherDescription(), is("a Matcher that matches \"Apples\""));
    }
    
    @Test public void
    describesItselfWhenConfiguredToMatchOnTheMismatchDescriptionProvidedByTheAssessedMatcher() {
        matcher = MatcherMatcher.a_matcher_describing_the_mismatch_of("Apples", equalTo("was Bananas"));
        
        assertThat(matcherDescription(),
                   is("a Matcher that does not match \"Apples\" and gives a mismatch description of \"was Bananas\""));
    }
    
    @Test public void
    matchesTheDescriptionOfTheAssessedMatcher() {
        matcher = MatcherMatcher.a_matcher_with_description(equalTo("rubbish"));
        assertThat(matcher.matches(dummyNotNullValueMatcher), is(false));
        
        matcher = MatcherMatcher.a_matcher_with_description(equalTo("a non-null value"));
        assertThat(matcher.matches(dummyNotNullValueMatcher), is(true));
    }
    
    @Test public void
    matchesTheMatchingFunctionalityOfTheAssessedMatcher() {
        matcher = MatcherMatcher.a_matcher_that_matches("Apples");
        assertThat(matcher.matches(dummyNotNullValueMatcher), is(true));
        
        matcher = MatcherMatcher.a_matcher_that_matches(null);
        assertThat(matcher.matches(dummyNotNullValueMatcher), is(false));
    }
    
    @Test public void
    matchesTheMismatchDescriptionOfTheAssessedMatcher() {
        matcher = MatcherMatcher.a_matcher_describing_the_mismatch_of(null, equalTo("the value was null"));
        assertThat(matcher.matches(dummyNotNullValueMatcher), is(true));
        
        matcher = MatcherMatcher.a_matcher_describing_the_mismatch_of("", equalTo("the value was null"));
        assertThat(matcher.matches(dummyNotNullValueMatcher), is(false));
        
        matcher = MatcherMatcher.a_matcher_describing_the_mismatch_of(null, equalTo("the value was spaghetti"));
        assertThat(matcher.matches(dummyNotNullValueMatcher), is(false));
    }
    
    @Test public void
    reportsMismatchInTheDesctiptionOfTheAssessedMatcher() {
        matcher = MatcherMatcher.a_matcher_with_description(equalTo("rubbish"));
        assertThat(mismatchDescriptionOf(dummyNotNullValueMatcher),
                   is("was a Matcher whose description was \"a non-null value\""));
    }
    
    @Test public void
    reportsMismatchInTheMatchingFunctionalityOfTheAssessedMatcher() {
        matcher = MatcherMatcher.a_matcher_that_matches(null);
        assertThat(mismatchDescriptionOf(dummyNotNullValueMatcher),
                   is("was a Matcher that did not match, and instead gave a mismatch description of \"the value was null\""));
    }
    
    @Test public void
    reportsMismatchInTheMismatchDescriptionOfTheAssessedMatcher() {
        matcher = MatcherMatcher.a_matcher_describing_the_mismatch_of("", equalTo("the value was null"));
        assertThat(mismatchDescriptionOf(dummyNotNullValueMatcher), is("was a Matcher that matched."));
        
        matcher = MatcherMatcher.a_matcher_describing_the_mismatch_of(null, equalTo("the value was spaghetti"));
        assertThat(mismatchDescriptionOf(dummyNotNullValueMatcher), is("was a Matcher whose mismatch description was \"the value was null\""));
    }
    
    private String matcherDescription() {
        StringDescription description = new StringDescription();
        matcher.describeTo(description);
        return description.toString();
    }

    private String mismatchDescriptionOf(Object value) {
        StringDescription description = new StringDescription();
        matcher.describeMismatch(value, description);
        return description.toString();
    }
    
}

