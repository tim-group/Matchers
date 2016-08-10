package com.youdevise.testutils.matchers;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_giving_a_mismatch_description_of;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_that_matches;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_with_description;

public class ReducibleTest {

    @Test public void
    reducible_checks_sum_of_iterable_using_direct_reducer() {
        Reducer<Integer, Integer> r = new EndoReducer<Integer>() {
            @Override
            public Integer identity() {
                return 0;
            }

            @Override
            public Integer product(Integer a, Integer b) {
                return a + b;
            }
        };
        assertThat(Reducible.with(r).to(100),
                   a_matcher_with_description("an iterable with a total that matches <100>"));

        assertThat(Reducible.with(r).to(100),
                   a_matcher_that_matches(ImmutableList.of(20, 20, 20, 20, 20)));

        assertThat(Reducible.with(r).to(100),
                   a_matcher_giving_a_mismatch_description_of(ImmutableList.of(20, 15, 20, 20, 20),
                                                              containsString("the total was <95>")));
    }

    @Test public void
    reducible_checks_sum_of_iterable_using_binary_operator() {
        assertThat(Reducible.with(0, Integer::sum).to(100),
                   a_matcher_with_description("an iterable with a total that matches <100>"));

        assertThat(Reducible.with(0, Integer::sum).to(100),
                   a_matcher_that_matches(ImmutableList.of(20, 20, 20, 20, 20)));

        assertThat(Reducible.with(0, Integer::sum).to(100),
                   a_matcher_giving_a_mismatch_description_of(ImmutableList.of(20, 15, 20, 20, 20),
                                                              containsString("the total was <95>")));
    }

    @Test public void
    reducible_checks_sum_of_iterable_using_accumulator_and_combiner() {
        assertThat(Reducible.with(0, Integer::sum, Integer::sum).to(100),
                   a_matcher_with_description("an iterable with a total that matches <100>"));

        assertThat(Reducible.with(0, Integer::sum, Integer::sum).to(100),
                   a_matcher_that_matches(ImmutableList.of(20, 20, 20, 20, 20)));

        assertThat(Reducible.with(0, Integer::sum, Integer::sum).to(100),
                   a_matcher_giving_a_mismatch_description_of(ImmutableList.of(20, 15, 20, 20, 20),
                                                              containsString("the total was <95>")));
    }

}
