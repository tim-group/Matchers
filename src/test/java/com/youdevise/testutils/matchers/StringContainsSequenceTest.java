package com.youdevise.testutils.matchers;

import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_giving_a_mismatch_description_of;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_that_matches;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_with_description;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;


public class StringContainsSequenceTest {
    
    @Test public void
    matches_strings_containing_the_sequence_of_strings() {
        StringContainsSequence sequenceMatcher = new StringContainsSequence("a", "b", "c");
        assertThat(sequenceMatcher, is(a_matcher_that_matches("abc")));
        assertThat(sequenceMatcher, is(a_matcher_that_matches("- a b c -")));
    }
    
    @Test public void
    describes_mismatches() {
        StringContainsSequence sequenceMatcher = new StringContainsSequence("a", "b", "c");
        assertThat(sequenceMatcher, is(a_matcher_giving_a_mismatch_description_of("", equalTo("was \"\""))));
        assertThat(sequenceMatcher, is(a_matcher_giving_a_mismatch_description_of("bca", equalTo("was \"bca\""))));
        assertThat(sequenceMatcher, is(a_matcher_giving_a_mismatch_description_of("ab", equalTo("was \"ab\""))));
    }

    @Test public void
    describes_itself() {
        StringContainsSequence sequenceMatcher = new StringContainsSequence("a", "b", "c");
        assertThat(sequenceMatcher, 
                   is(a_matcher_with_description(equalTo("a string contains the sequence of substrings [\"a\", \"b\", \"c\"]"))));
    }
    
}

