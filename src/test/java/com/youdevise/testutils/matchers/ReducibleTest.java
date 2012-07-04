package com.youdevise.testutils.matchers;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import static com.google.common.collect.Lists.newArrayList;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_giving_a_mismatch_description_of;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_that_matches;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_with_description;

public class ReducibleTest {

    @Test public void
    reducible_checks_sum_of_iterable() {
        assertThat(Reducible.with(Reducers.sumIntegers()).to(100),
                   a_matcher_with_description("an iterable with a total that matches <100>"));
        
        assertThat(Reducible.with(Reducers.sumIntegers()).to(100),
                   a_matcher_that_matches(newArrayList(20, 20, 20, 20, 20)));
        
        assertThat(Reducible.with(Reducers.sumIntegers()).to(100),
                   a_matcher_giving_a_mismatch_description_of(newArrayList(20, 15, 20, 20, 20),
                                                              containsString("the total was <95>")));
    }

}
