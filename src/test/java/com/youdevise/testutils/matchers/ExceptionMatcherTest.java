package com.youdevise.testutils.matchers;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import static com.youdevise.testutils.matchers.ExceptionMatcher.anExceptionOfType;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_giving_a_mismatch_description_of;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_that_matches;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_with_description;

public final class ExceptionMatcherTest {
    @Test public void
    describes_itself_with_the_exception_name() {
        assertThat(anExceptionOfType(Exception.class), is(a_matcher_with_description(equalTo("a <java.lang.Exception>"))));
    }

    @Test public void
    describes_itself_with_the_exception_name_and_message() {
        assertThat(anExceptionOfType(Exception.class).withTheMessage("OH NO!"),
                   is(a_matcher_with_description(equalTo("a <java.lang.Exception> with the message \"OH NO!\""))));
    }

    @Test public void
    describes_itself_with_the_exception_name_and_cause() {
        assertThat(anExceptionOfType(RuntimeException.class).causedBy(anExceptionOfType(UnsupportedOperationException.class)),
                   is(a_matcher_with_description(equalTo("a <java.lang.RuntimeException> caused by a <java.lang.UnsupportedOperationException>"))));
    }

    @Test public void
    describes_itself_with_everything() {
        assertThat(anExceptionOfType(Exception.class).withTheMessage("OH NO!").causedBy(anExceptionOfType(RuntimeException.class)),
                   is(a_matcher_with_description(equalTo("a <java.lang.Exception> with the message \"OH NO!\" caused by a <java.lang.RuntimeException>"))));
    }

    @Test public void
    does_not_match_an_exception_of_a_different_type() {
        assertThat(anExceptionOfType(IllegalStateException.class),
                   is(a_matcher_giving_a_mismatch_description_of(new IllegalArgumentException(), equalTo("was a <java.lang.IllegalArgumentException>"))));
    }

    @Test public void
    does_not_match_an_exception_with_a_different_message() {
        assertThat(anExceptionOfType(IllegalStateException.class).withTheMessage("Huh?"),
                   is(a_matcher_giving_a_mismatch_description_of(new IllegalStateException("What?"), equalTo("had the message \"What?\""))));
    }

    @Test public void
    does_not_match_an_exception_with_a_different_cause() {
        assertThat(anExceptionOfType(IllegalStateException.class).causedBy(anExceptionOfType(UnsupportedOperationException.class)),
                   is(a_matcher_giving_a_mismatch_description_of(new IllegalStateException(new Exception()), equalTo("was caused by an exception that was a <java.lang.Exception>"))));
    }

    @Test public void
    does_not_match_an_exception_with_a_different_message_and_cause() {
        assertThat(anExceptionOfType(IllegalStateException.class).withTheMessage("Hi!").causedBy(anExceptionOfType(UnsupportedOperationException.class)),
                   is(a_matcher_giving_a_mismatch_description_of(new IllegalStateException("Hello!", new Exception()), equalTo("had the message \"Hello!\" and was caused by an exception that was a <java.lang.Exception>"))));
    }

    @Test public void
    matches_an_exception_of_the_same_type() {
        assertThat(anExceptionOfType(IllegalStateException.class), is(a_matcher_that_matches((new IllegalStateException()))));
    }

    @Test public void
    matches_an_exception_of_the_same_type_with_the_same_message() {
        assertThat(anExceptionOfType(IllegalStateException.class).withTheMessage("Huh?"), is(a_matcher_that_matches(new IllegalStateException("Huh?"))));
    }

    @Test public void
    matches_an_exception_of_the_same_type_with_the_same_cause() {
        assertThat(anExceptionOfType(IllegalStateException.class).causedBy(anExceptionOfType(UnsupportedOperationException.class)),
                   is(a_matcher_that_matches(new IllegalStateException(new UnsupportedOperationException()))));
    }

    @Test public void
    matches_an_exception_of_the_same_type_with_the_same_message_and_cause() {
        assertThat(anExceptionOfType(IllegalStateException.class).withTheMessage("Hi!").causedBy(anExceptionOfType(UnsupportedOperationException.class)),
                   is(a_matcher_that_matches(new IllegalStateException("Hi!", new UnsupportedOperationException()))));
    }
}
