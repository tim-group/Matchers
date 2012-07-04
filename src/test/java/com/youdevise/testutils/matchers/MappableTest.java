package com.youdevise.testutils.matchers;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.base.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import static com.google.common.collect.Lists.newArrayList;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_giving_a_mismatch_description_of;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_that_matches;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_with_description;

public class MappableTest {
    
    @Test public void
    mappable_checks_map_of_iterable() {
        Function<String, Integer> stringLength = new Function<String, Integer>() {
            @Override public Integer apply(String arg0) {
                return arg0.length();
            }
        };
        
        assertThat(Mappable.with(stringLength).to(3, 5),
                   a_matcher_with_description("an iterable which maps to iterable over [<3>, <5>]"));
        
        assertThat(Mappable.with(stringLength).to(3, 5, 5, 3, 5, 4, 3, 4, 3),
                   a_matcher_that_matches(newArrayList("The quick brown fox jumps over the lazy hen".split("\\s"))));
        
        assertThat(Mappable.with(stringLength).to(2, 10, 2, 4, 2, 5),
                   a_matcher_giving_a_mismatch_description_of(newArrayList("My hovercraft is full of eels".split("\\s")),
                                                              Matchers.allOf(containsString("<6> Expected (<5>)"),
                                                                             containsString("but was <4>"))));
    }
}