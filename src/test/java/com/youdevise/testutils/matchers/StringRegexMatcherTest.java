package com.youdevise.testutils.matchers;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_giving_a_mismatch_description_of;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_that_matches;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_with_description;
import static com.youdevise.testutils.matchers.StringRegexMatcher.matchesRegex;

public class StringRegexMatcherTest {
    @Test public void
    describes_itself() {
        assertThat(matchesRegex("something( [or][ro]\\s+other)?"), is(a_matcher_with_description(equalTo("matches \"something( [or][ro]\\s+other)?\""))));
    }

    @Test public void
    matches_nothing() {
        assertThat(matchesRegex(""), is(a_matcher_that_matches("")));
    }

    @Test public void
    does_not_match_nothing_when_it_has_content() {
        assertThat(matchesRegex("abc"), is(a_matcher_giving_a_mismatch_description_of("", equalTo("was \"\""))));
    }

    @Test public void
    matches_exactly() {
        assertThat(matchesRegex("hello"), is(a_matcher_that_matches("hello")));
    }

    @Test public void
    describes_the_original_string_when_it_fails() {
        assertThat(matchesRegex("what\\?"), is(a_matcher_giving_a_mismatch_description_of("who?", equalTo("was \"who?\""))));
    }

    @Test public void
    matches_a_substring() {
        assertThat(matchesRegex("hello"), is(a_matcher_that_matches("Why, hello there.")));
    }

    @Test public void
    matches_whitespace() {
        assertThat(matchesRegex("this\\s+is\\s+a\\s+sentence"), is(a_matcher_that_matches("this   is\ta\nsentence")));
    }

    @Test public void
    matches_a_character_set() {
        assertThat(matchesRegex("he[yt] [ft]here"), is(a_matcher_that_matches("hey there")));
    }
}
