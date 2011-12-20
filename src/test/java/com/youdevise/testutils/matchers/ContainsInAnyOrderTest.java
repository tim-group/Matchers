package com.youdevise.testutils.matchers;


import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_giving_a_mismatch_description_of;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_that_matches;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_with_description;
import static com.youdevise.testutils.matchers.StringContainsSequence.containsStringSequence;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.StringDescription;
import org.junit.Test;

public class ContainsInAnyOrderTest {

    @Test public void
    reports_when_the_actual_collection_is_empty() {

        Matcher<Iterable<String>> containsOne = Contains.inAnyOrder("a");
        List<String> emptyList = Arrays.<String>asList();

        listOutput(containsOne, emptyList);

        assertThat(containsOne, Matchers.allOf(
                                is(not(a_matcher_that_matches(emptyList))),
                                is(a_matcher_with_description(containsString("[\"a\"]"))),
                                is(a_matcher_giving_a_mismatch_description_of(emptyList, containsString("actual collection was empty")))));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    reports_when_expected_collection_was_empty_but_items_were_unexpectedly_returned() {

        Matcher<Iterable<String>> containsEmpty = Contains.inAnyOrder();
        List<String> listOfOne = Arrays.<String>asList("a");

        listOutput(containsEmpty, listOfOne);

        assertThat(containsEmpty, is(not(a_matcher_that_matches(listOfOne))));
        assertThat(containsEmpty, is(a_matcher_with_description(containsString("empty"))));
        assertThat(containsEmpty, is(a_matcher_giving_a_mismatch_description_of(listOfOne, containsString("[a]"))));
    }

    @Test public void
    reports_the_unmatched_expectations_when_actual_list_shorter_than_expected_list() {

        Matcher<Iterable<String>> containsOneAndTwo = Contains.inAnyOrder("a", "b");
        List<String> listOfOne = Arrays.<String>asList("a");

        listOutput(containsOneAndTwo, listOfOne);

        assertThat(containsOneAndTwo, is(not(a_matcher_that_matches(listOfOne))));
        assertThat(containsOneAndTwo, is(a_matcher_with_description(containsString("[\"a\", \"b\"]"))));
        assertThat(containsOneAndTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOne, containsString("expected size 2, actual size 1"))));
        assertThat(containsOneAndTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOne, 
                                                                                    containsStringSequence("Items that were expected, but not present",
                                                                                                           "\"b\""))));
    }

    @Test public void
    reports_all_unmatched_expectations() {

        Matcher<Iterable<String>> contains1234 = Contains.inAnyOrder("a", "b", "c", "d");
        List<String> listOfOne = Arrays.<String>asList("a");

        listOutput(contains1234, listOfOne);

        assertThat(contains1234, is(not(a_matcher_that_matches(listOfOne))));
        assertThat(contains1234, is(a_matcher_with_description(containsString("[\"a\", \"b\", \"c\", \"d\"]"))));
        assertThat(contains1234, is(a_matcher_giving_a_mismatch_description_of(listOfOne, containsString("expected size 4, actual size 1"))));
        assertThat(contains1234, is(a_matcher_giving_a_mismatch_description_of(listOfOne, 
                                                                               not(containsStringSequence("Items that were expected, but not present:", 
                                                                                                          "\"a\"")))));
        assertThat(contains1234, is(a_matcher_giving_a_mismatch_description_of(listOfOne, 
                                                                               containsStringSequence("Items that were expected, but not present:", 
                                                                                                      "\"b\"", 
                                                                                                      "\"c\"",
                                                                                                      "\"d\""))));
    }


    @Test public void
    reports_all_unexpected_items() {

        Matcher<Iterable<String>> containsOne = Contains.inAnyOrder("a");
        List<String> listOf1234 = Arrays.<String>asList("a", "b", "c", "d");

        listOutput(containsOne, listOf1234);

        assertThat(containsOne, is(not(a_matcher_that_matches(listOf1234))));
        assertThat(containsOne, is(a_matcher_with_description(containsString("[\"a\"]"))));
        assertThat(containsOne, is(a_matcher_giving_a_mismatch_description_of(listOf1234, containsString("expected size 1, actual size 4"))));
        assertThat(containsOne, is(a_matcher_giving_a_mismatch_description_of(listOf1234, containsStringSequence("Unexpected items:",
                                                                                                                 "\"b\"",
                                                                                                                 "\"c\"",
                                                                                                                 "\"d\""))));
    }

    @Test public void
    accepts_when_right_items_are_found_but_in_the_wrong_order() {

        Matcher<Iterable<String>> containsOneTwo = Contains.inAnyOrder("a", "b");
        List<String> listOfTwoOne = Arrays.<String>asList("b", "a");

        assertThat(containsOneTwo, is(a_matcher_that_matches(listOfTwoOne)));
    }

    @Test public void
    reports_each_unmatched_item() {

        Matcher<Iterable<String>> containsOneTwo = Contains.inAnyOrder("a", "b", "c");
        List<String> listOfOneThree = Arrays.<String>asList("a", "c", "d");

        listOutput(containsOneTwo, listOfOneThree);

        assertThat(containsOneTwo, is(not(a_matcher_that_matches(listOfOneThree))));
        assertThat(containsOneTwo, is(a_matcher_with_description(containsString("[\"a\", \"b\", \"c\"]"))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneThree, 
                                                                                 containsStringSequence("Unexpected items:", 
                                                                                                        "\"d\""))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneThree, 
                                                                                 containsStringSequence("Items that were expected, but not present:",
                                                                                                        "\"b\""))));
    }

    @Test public void
    reports_indices_of_unmatched_matchers_and_unexpected_items() {

        Matcher<Iterable<String>> expected = Contains.inAnyOrder("a", "b", "c", "e", "f");
        List<String> actual = Arrays.<String>asList("a", "c", "d", "e", "g");

        listOutput(expected, actual);

        assertThat(expected, is(not(a_matcher_that_matches(actual))));
        assertThat(expected, is(a_matcher_giving_a_mismatch_description_of(actual, containsStringSequence("Unexpected items:",
                                                                                                          "<3> \"d\"",
                                                                                                          "<5> \"g\""))));
        assertThat(expected, is(a_matcher_giving_a_mismatch_description_of(actual, containsStringSequence("Items that were expected, but not present:",
                                                                                                          "<2> <\"b\">",
                                                                                                          "<5> <\"f\">"))));
    }

    @Test public void
    test_reports_unexpected_and_missing_items() {

        Matcher<Iterable<String>> containsABCD = Contains.inAnyOrder("a", "b", "c", "d");
        List<String> listOfACDE = Arrays.<String>asList("a", "c", "d", "e");

        listOutput(containsABCD, listOfACDE);

        assertThat(containsABCD, is(not(a_matcher_that_matches(listOfACDE))));
        assertThat(containsABCD, is(a_matcher_with_description(containsString("[\"a\", \"b\", \"c\", \"d\"]"))));
        assertThat(containsABCD, is(a_matcher_giving_a_mismatch_description_of(listOfACDE, 
                                                                               containsStringSequence("Unexpected items:", "\"e\""))));
        assertThat(containsABCD, is(a_matcher_giving_a_mismatch_description_of(listOfACDE, 
                                                                               containsStringSequence("Items that were expected, but not present:", "\"b\""))));

    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    matches_empty_with_empty() {
        List<String> emptyList = Arrays.<String>asList();

        Matcher<Iterable<String>> containsEmpty = Contains.inAnyOrder();

        assertThat(containsEmpty, is(a_matcher_that_matches(emptyList)));
    }

    @Test public void
    matches_list_of_one_item() {
        List<String> listOfOne = Arrays.<String>asList("a");

        Matcher<Iterable<String>> containsOne = Contains.inAnyOrder("a");

        assertThat(containsOne, is(a_matcher_that_matches(listOfOne)));
    }
    
    @Test public void
    handles_and_reports_actual_null_values() {

        Matcher<Iterable<String>> containsOneTwo = Contains.inAnyOrder("a", "b");
        List<String> listOfOneNull = Arrays.<String>asList("a", null);

        listOutput(containsOneTwo, listOfOneNull);

        assertThat(containsOneTwo, is(not(a_matcher_that_matches(listOfOneNull))));
        assertThat(containsOneTwo, is(a_matcher_with_description(containsString("[\"a\", \"b\"]"))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneNull, 
                                                                                 containsStringSequence("Unexpected items:", "null"))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneNull, 
                                                                                 containsStringSequence("Items that were expected, but not present:", "\"b\""))));
  
    }
    
    @Test public void
    handles_and_reports_extra_actual_null_values() {
        Matcher<Iterable<String>> containsOneTwo = Contains.inAnyOrder("a", "b");
        List<String> listOfOneTwoNull = Arrays.<String>asList("a", "b", null);

        listOutput(containsOneTwo, listOfOneTwoNull);

        assertThat(containsOneTwo, is(not(a_matcher_that_matches(listOfOneTwoNull))));
        assertThat(containsOneTwo, is(a_matcher_with_description(containsString("[\"a\", \"b\"]"))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneTwoNull, 
                                                                                 containsStringSequence("Unexpected items:", "null"))));

    }
    

    @Test public void
    matches_on_null_elements_in_lists() {
        Matcher<Iterable<String>> containsNull = Contains.inAnyOrder("a", null);
        List<String> listNull = Arrays.<String>asList("a", null);

        assertThat(containsNull, is(a_matcher_that_matches(listNull)));
    }
    
    @Test public void
    reports_when_null_element_was_expected_but_not_seen() {
        Matcher<Iterable<String>> containsaNull = Contains.inAnyOrder("a", null);
        List<String> listOfab = Arrays.<String>asList("a", "b");

        listOutput(containsaNull, listOfab);

        assertThat(containsaNull, is(not(a_matcher_that_matches(listOfab))));
        assertThat(containsaNull, is(a_matcher_with_description(containsString("[\"a\", null]"))));
        assertThat(containsaNull, is(a_matcher_giving_a_mismatch_description_of(listOfab, 
                                                                                containsStringSequence("Unexpected items:", "\"b\""))));
        assertThat(containsaNull, is(a_matcher_giving_a_mismatch_description_of(listOfab, 
                                                                                containsStringSequence("Items that were expected, but not present:", "null"))));

    }
    
    @Test public void
    describes_possible_mismatch_when_only_one_unsatisfied_match_and_one_unexpected_item() {
        Matcher<Iterable<String>> matcher = Contains.inAnyOrder("a", "b");
        List<String> items = Arrays.<String>asList("b", "c");
        assertThat(matcher, is(not(a_matcher_that_matches(items))));
        assertThat(matcher, is(a_matcher_giving_a_mismatch_description_of(items, containsStringSequence("Possible mismatch between:", 
                                                                                                        "unsatisfied expectation <1> and unexpected <2>",
                                                                                                        "was \"c\""))));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    can_use_matchers_for_elements_in_list() {
        Matcher<Iterable<String>> containsAB = Contains.inAnyOrder(Matchers.containsString("a"), Matchers.containsString("b"));
        List<String> listOfAAABBB = Arrays.<String>asList("aaaaaa", "bbbbbbbbbb");
        assertThat(containsAB, is(a_matcher_that_matches(listOfAAABBB)));
    }
    
    private void listOutput(Matcher<Iterable<String>> containsOne, List<String> emptyList) {
        StringDescription description = new StringDescription();
        containsOne.describeTo(description);
        StringDescription mismatchDescription = new StringDescription();
        containsOne.describeMismatch(emptyList, mismatchDescription);
    }
    
}
