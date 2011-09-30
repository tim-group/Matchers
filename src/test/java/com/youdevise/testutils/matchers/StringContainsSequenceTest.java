package com.youdevise.testutils.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.hamcrest.StringDescription;
import org.junit.Test;


public class StringContainsSequenceTest {
    
    @Test public void
    matches_strings_containing_the_sequence_of_strings() {
        StringContainsSequence sequenceMatcher = new StringContainsSequence("a", "b", "c");
        assertThat(sequenceMatcher.matches("abc"), is(true));
        assertThat(sequenceMatcher.matches("- a b c -"), is(true));
        assertThat(sequenceMatcher.matches("bca"), is(false));
        assertThat(sequenceMatcher.matches("ab"), is(false));
    }

    @Test public void
    describes_itself_properly() {
        StringContainsSequence sequenceMatcher = new StringContainsSequence("a", "b", "c");
        StringDescription description = new StringDescription();
        sequenceMatcher.describeTo(description);
        assertThat(description.toString(), is("a string contains the sequence of substrings [\"a\", \"b\", \"c\"]"));
    }
    
    @Test public void
    describes_a_mismatch() {
        StringContainsSequence sequenceMatcher = new StringContainsSequence("a", "z");
        StringDescription description = new StringDescription();
        sequenceMatcher.describeMismatch("foo", description); 
        assertThat(description.toString(), is("was \"foo\""));
    }

}

