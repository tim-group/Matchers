package com.youdevise.testutils.matchers;

import org.junit.Test;

import static com.youdevise.testutils.matchers.ExceptionMatcher.anExceptionOfType;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_with_description;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

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
}
