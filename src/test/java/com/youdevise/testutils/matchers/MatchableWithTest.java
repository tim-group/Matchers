package com.youdevise.testutils.matchers;

import org.hamcrest.Matcher;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import static com.google.common.base.Predicates.equalTo;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_giving_a_mismatch_description_of;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_that_matches;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_with_description;

public class MatchableWithTest {

    @Test public void
    matches_against_the_supplied_predicate() {
        Matcher<? super String> isTheMagicWord = Redescribe.theMatcher(MatchableWith.thePredicate(equalTo("xyzzy")))
                                                           .as("a word that would unpredictably modify the fabric of space and time")
                                                           .describingMismatchAs("frankly a disappointment");
        
        assertThat(isTheMagicWord,
                   a_matcher_with_description("a word that would unpredictably modify the fabric of space and time"));
        
        assertThat(isTheMagicWord,
                   a_matcher_that_matches("xyzzy"));
        
        assertThat(isTheMagicWord,
                   a_matcher_giving_a_mismatch_description_of("foo",
                                                              containsString("\"foo\" was frankly a disappointment")));
    }

}
