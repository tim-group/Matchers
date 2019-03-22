package com.youdevise.testutils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Test;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_that_matches;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;

public class JOptionalMatcherTest {
    @Test
    public void matches_empty() {
        assertThat(JOptionalMatcher.isEmpty(), a_matcher_that_matches(Optional.empty()));
        assertThat(JOptionalMatcher.isEmpty(), not(a_matcher_that_matches(Optional.of("test"))));
    }

    @Test
    public void matches_value() {
        assertThat(JOptionalMatcher.isValue("test"), a_matcher_that_matches(Optional.of("test")));
        assertThat(JOptionalMatcher.isValue(123), a_matcher_that_matches(Optional.<Number> of(123)));
        assertThat(JOptionalMatcher.isValue("test"), not(a_matcher_that_matches(Optional.of("other"))));
        assertThat(JOptionalMatcher.isValue("test"), not(a_matcher_that_matches(Optional.empty())));
    }

    @Test
    public void matches_value_using_matcher() {
        assertThat(JOptionalMatcher.isPresent(equalTo("test")), a_matcher_that_matches(Optional.of("test")));
        assertThat(JOptionalMatcher.isPresent(equalTo(123)), a_matcher_that_matches(Optional.<Number> of(123)));
        assertThat(JOptionalMatcher.isPresent(lessThan(200)), a_matcher_that_matches(Optional.<Number> of(123)));
        assertThat(JOptionalMatcher.isPresent(equalTo("test")), not(a_matcher_that_matches(Optional.of("other"))));
        assertThat(JOptionalMatcher.isPresent(equalTo("test")), not(a_matcher_that_matches(Optional.empty())));
    }

    @Test
    public void matches_empty_int() {
        assertThat(JOptionalMatcher.isEmptyInt(), a_matcher_that_matches(OptionalInt.empty()));
        assertThat(JOptionalMatcher.isEmptyInt(), not(a_matcher_that_matches(OptionalInt.of(123))));
    }

    @Test
    public void matches_int_value() {
        assertThat(JOptionalMatcher.isIntValue(123), a_matcher_that_matches(OptionalInt.of(123)));
        assertThat(JOptionalMatcher.isIntValue(123), not(a_matcher_that_matches(OptionalInt.of(456))));
        assertThat(JOptionalMatcher.isIntValue(123), not(a_matcher_that_matches(OptionalInt.empty())));
    }

    @Test
    public void matches_int_value_using_matcher() {
        assertThat(JOptionalMatcher.isPresentInt(equalTo(123)), a_matcher_that_matches(OptionalInt.of(123)));
        assertThat(JOptionalMatcher.isPresentInt(lessThan(200)), a_matcher_that_matches(OptionalInt.of(123)));
        assertThat(JOptionalMatcher.isPresentInt(equalTo(123)), not(a_matcher_that_matches(OptionalInt.of(456))));
        assertThat(JOptionalMatcher.isPresentInt(equalTo(123)), not(a_matcher_that_matches(OptionalInt.empty())));
    }

    @Test
    public void matches_empty_long() {
        assertThat(JOptionalMatcher.isEmptyLong(), a_matcher_that_matches(OptionalLong.empty()));
        assertThat(JOptionalMatcher.isEmptyLong(), not(a_matcher_that_matches(OptionalLong.of(123))));
    }

    @Test
    public void matches_long_value() {
        assertThat(JOptionalMatcher.isLongValue(123), a_matcher_that_matches(OptionalLong.of(123)));
        assertThat(JOptionalMatcher.isLongValue(123), not(a_matcher_that_matches(OptionalLong.of(456))));
        assertThat(JOptionalMatcher.isLongValue(123), not(a_matcher_that_matches(OptionalLong.empty())));
    }

    @Test
    public void matches_long_value_using_matcher() {
        assertThat(JOptionalMatcher.isPresentLong(equalTo(123L)), a_matcher_that_matches(OptionalLong.of(123)));
        assertThat(JOptionalMatcher.isPresentLong(longLessThan(200)), a_matcher_that_matches(OptionalLong.of(123)));
        assertThat(JOptionalMatcher.isPresentLong(equalTo(123L)), not(a_matcher_that_matches(OptionalLong.of(456))));
        assertThat(JOptionalMatcher.isPresentLong(equalTo(123L)), not(a_matcher_that_matches(OptionalLong.empty())));
    }

    @Test
    public void matches_empty_double() {
        assertThat(JOptionalMatcher.isEmptyDouble(), a_matcher_that_matches(OptionalDouble.empty()));
        assertThat(JOptionalMatcher.isEmptyDouble(), not(a_matcher_that_matches(OptionalDouble.of(123))));
    }

    @Test
    public void matches_double_value() {
        assertThat(JOptionalMatcher.isDoubleValue(123.0), a_matcher_that_matches(OptionalDouble.of(123)));
        assertThat(JOptionalMatcher.isDoubleValue(123.0), not(a_matcher_that_matches(OptionalDouble.of(456))));
        assertThat(JOptionalMatcher.isDoubleValue(123.0), not(a_matcher_that_matches(OptionalDouble.empty())));
    }

    @Test
    public void matches_double_value_using_matcher() {
        assertThat(JOptionalMatcher.isPresentDouble(equalTo(123.0)), a_matcher_that_matches(OptionalDouble.of(123)));
        assertThat(JOptionalMatcher.isPresentDouble(doubleLessThan(200.0)), a_matcher_that_matches(OptionalDouble.of(123)));
        assertThat(JOptionalMatcher.isPresentDouble(equalTo(123.0)), not(a_matcher_that_matches(OptionalDouble.of(456))));
        assertThat(JOptionalMatcher.isPresentDouble(equalTo(123.0)), not(a_matcher_that_matches(OptionalDouble.empty())));
    }

    private static Matcher<Long> longLessThan(long limit) {
        return new TypeSafeDiagnosingMatcher<Long>() {
            @Override
            protected boolean matchesSafely(Long item, Description mismatchDescription) {
                mismatchDescription.appendText("was ").appendValue(item);
                return item < limit;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("long less than ").appendValue(limit);
            }
        };
    }

    private static Matcher<Double> doubleLessThan(double limit) {
        return new TypeSafeDiagnosingMatcher<Double>() {
            @Override
            protected boolean matchesSafely(Double item, Description mismatchDescription) {
                mismatchDescription.appendText("was ").appendValue(item);
                return item < limit;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("double less than ").appendValue(limit);
            }
        };
    }
}
