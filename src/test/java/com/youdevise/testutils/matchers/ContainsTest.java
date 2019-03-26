package com.youdevise.testutils.matchers;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_that_matches;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class ContainsTest {

    @Test
    public void
    matches_an_empty_collection() {

        Matcher<Iterable<?>> containsNothing = Contains.nothing();
        List<String> emptyList = Collections.emptyList();

        assertThat(containsNothing, is(a_matcher_that_matches(emptyList)));
    }

    @Test
    public void
    reports_when_the_iterable_contains_items() {

        Matcher<Iterable<?>> containsNothing = Contains.nothing();
        List<String> list = Collections.singletonList("a");

        assertThat(containsNothing, is(not(a_matcher_that_matches(list))));
    }

}