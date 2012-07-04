package com.youdevise.testutils.matchers;

import org.junit.Test;

import com.google.common.collect.Ordering;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import static com.google.common.collect.Lists.newArrayList;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_giving_a_mismatch_description_of;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_that_matches;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_with_description;

public class SortedTest {

    @Test public void
    uses_natural_ordering_for_types_extending_comparable() {
        assertThat(Sorted.<Integer>inAscendingOrder(),
                   a_matcher_with_description("an iterable in ascending order"));
        
        assertThat(Sorted.<Integer>inAscendingOrder(),
                   a_matcher_that_matches(newArrayList(1, 2, 3, 5, 8, 11)));
        
        assertThat(Sorted.<Integer>inAscendingOrder(),
                   a_matcher_giving_a_mismatch_description_of(newArrayList(1, 3, 2),
                                                              containsString("was not in order")));
    }
    
    @Test public void
    can_match_on_descending_order() {
        assertThat(Sorted.<Integer>inDescendingOrder(),
                   a_matcher_with_description("an iterable in descending order"));
        
        assertThat(Sorted.<Integer>inDescendingOrder(),
                   a_matcher_that_matches(newArrayList(11, 8, 5, 3, 2, 1)));
        
        assertThat(Sorted.<Integer>inDescendingOrder(),
                   a_matcher_giving_a_mismatch_description_of(newArrayList(3, 1, 2),
                                                              containsString("was not in order")));
    }
    
    @Test public void
    can_use_a_custom_ordering() {
        assertThat(Sorted.with(Ordering.usingToString()),
                   a_matcher_with_description("an iterable in custom order"));
        
        assertThat(Sorted.with(Ordering.usingToString()),
                   a_matcher_that_matches(newArrayList(1, 11, 2, 22)));
        
        assertThat(Sorted.with(Ordering.usingToString()),
                   a_matcher_giving_a_mismatch_description_of(newArrayList(1, 2, 11, 22),
                                                              containsString("was not in order")));
    }
}
