package com.youdevise.testutils.matchers;

import org.junit.Test;

import com.google.common.collect.Lists;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_giving_a_mismatch_description_of;

public class EveryItemTest {

    @Test
    public void
    lists_all_non_matching_items() {
        assertThat(EveryItem.matching(greaterThanOrEqualTo(0)),
                   a_matcher_giving_a_mismatch_description_of(Lists.newArrayList(1, 2, -3, 5, -7),
                       containsString("the items [<-3>, <-7>] were a value equal to or greater than <0>")));
    }

}
