package com.youdevise.testutils.matchers;


import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_giving_a_mismatch_description_of;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_that_matches;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_with_description;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class ContainsInOrderTest {

    @Test public void
    reports_when_the_actual_collection_is_empty() {

        Matcher<Iterable<? extends String>> containsOne = Contains.inOrder("a");
        List<String> emptyList = Collections.emptyList();

        listOutput(containsOne, emptyList);

        assertThat(containsOne, Matchers.allOf(
                                is(not(a_matcher_that_matches(emptyList))),
                                is(a_matcher_with_description(containsString("[\"a\"]"))),
                                is(a_matcher_giving_a_mismatch_description_of(emptyList, containsString("actual collection was empty")))));
    }


    @SuppressWarnings("unchecked")
    @Test public void
    reports_when_expected_collection_was_empty_but_items_were_unexpectedly_returned() {

        Matcher<Iterable<? extends String>> containsEmpty = Contains.inOrder();
        List<String> listOfOne = Collections.singletonList("a");

        listOutput(containsEmpty, listOfOne);

        assertThat(containsEmpty, is(not(a_matcher_that_matches(listOfOne))));
        assertThat(containsEmpty, is(a_matcher_with_description(containsString("empty"))));
        assertThat(containsEmpty, is(a_matcher_giving_a_mismatch_description_of(listOfOne, containsString("[a]"))));
    }

    @Test public void
    reports_the_unmatched_expectations_when_actual_list_shorter_than_expected_list() {

        Matcher<Iterable<? extends String>> containsOneAndTwo = Contains.inOrder("a", "b");
        List<String> listOfOne = Collections.singletonList("a");

        listOutput(containsOneAndTwo, listOfOne);

        assertThat(containsOneAndTwo, is(not(a_matcher_that_matches(listOfOne))));
        assertThat(containsOneAndTwo, is(a_matcher_with_description(containsString("[\"a\", \"b\"]"))));
        assertThat(containsOneAndTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOne, containsString("expected size 2, actual size 1"))));
        assertThat(containsOneAndTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOne, containsString("Items that were expected, but not present"))));
        assertThat(containsOneAndTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOne, containsString("\"b\""))));
    }

    @Test public void
    reports_all_unmatched_expectations() {

        Matcher<Iterable<? extends String>> contains1234 = Contains.inOrder("a", "b", "c", "d");
        List<String> listOfOne = Collections.singletonList("a");

        listOutput(contains1234, listOfOne);

        assertThat(contains1234, is(not(a_matcher_that_matches(listOfOne))));
        assertThat(contains1234, is(a_matcher_with_description(containsString("[\"a\", \"b\", \"c\", \"d\"]"))));
        assertThat(contains1234, is(a_matcher_giving_a_mismatch_description_of(listOfOne, containsString("expected size 4, actual size 1"))));
        assertThat(contains1234, is(a_matcher_giving_a_mismatch_description_of(listOfOne, containsString("Items that were expected, but not present:"))));
        assertThat(contains1234, is(a_matcher_giving_a_mismatch_description_of(listOfOne, containsString("\"b\""))));
        assertThat(contains1234, is(a_matcher_giving_a_mismatch_description_of(listOfOne, containsString("\"c\""))));
        assertThat(contains1234, is(a_matcher_giving_a_mismatch_description_of(listOfOne, containsString("\"d\""))));
    }


    @Test public void
    reports_all_unexpected_items() {

        Matcher<Iterable<? extends String>> containsOne = Contains.inOrder("a");
        List<String> listOf1234 = Arrays.asList("a", "b", "c", "d");

        listOutput(containsOne, listOf1234);

        assertThat(containsOne, is(not(a_matcher_that_matches(listOf1234))));
        assertThat(containsOne, is(a_matcher_with_description(containsString("[\"a\"]"))));
        assertThat(containsOne, is(a_matcher_giving_a_mismatch_description_of(listOf1234, containsString("expected size 1, actual size 4"))));
        assertThat(containsOne, is(a_matcher_giving_a_mismatch_description_of(listOf1234, containsString("Unexpected items:"))));
        assertThat(containsOne, is(a_matcher_giving_a_mismatch_description_of(listOf1234, containsString("\"b\""))));
        assertThat(containsOne, is(a_matcher_giving_a_mismatch_description_of(listOf1234, containsString("\"c\""))));
        assertThat(containsOne, is(a_matcher_giving_a_mismatch_description_of(listOf1234, containsString("\"d\""))));
    }

    @Test public void
    reports_when_right_items_are_found_but_in_the_wrong_order() {

        Matcher<Iterable<? extends String>> containsOneTwo = Contains.inOrder("a", "b");
        List<String> listOfTwoOne = Arrays.asList("b", "a");

        listOutput(containsOneTwo, listOfTwoOne);

        assertThat(containsOneTwo, is(not(a_matcher_that_matches(listOfTwoOne))));
        assertThat(containsOneTwo, is(a_matcher_with_description(containsString("[\"a\", \"b\"]"))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfTwoOne, containsString("actual list had the right items but in the wrong order"))));
    }

    @Test public void
    reports_each_unmatched_item() {

        Matcher<Iterable<? extends String>> containsOneTwo = Contains.inOrder("a", "b", "c");
        List<String> listOfOneThree = Arrays.asList("a", "c", "d");

        listOutput(containsOneTwo, listOfOneThree);

        assertThat(containsOneTwo, is(not(a_matcher_that_matches(listOfOneThree))));
        assertThat(containsOneTwo, is(a_matcher_with_description(containsString("[\"a\", \"b\", \"c\"]"))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneThree, containsString("Items that did not match their corresponding expectations:"))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneThree, containsString("<2> Expected (\"b\")\n but was \"c\""))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneThree, containsString("<3> Expected (\"c\")\n but was \"d\""))));
    }


    @Test public void
    test_reports_mismatches_and_missing_items() {

        Matcher<Iterable<? extends String>> containsOneTwo = Contains.inOrder("a", "b", "c", "d");
        List<String> listOfOneThree = Arrays.asList("a", "c", "d");

        listOutput(containsOneTwo, listOfOneThree);

        assertThat(containsOneTwo, is(not(a_matcher_that_matches(listOfOneThree))));
        assertThat(containsOneTwo, is(a_matcher_with_description(containsString("[\"a\", \"b\", \"c\", \"d\"]"))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneThree, containsString("Items that did not match their corresponding expectations:"))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneThree, containsString("<2> Expected (\"b\")\n but was \"c\""))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneThree, containsString("<3> Expected (\"c\")\n but was \"d\""))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneThree, containsString("Items that were expected, but not present:"))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneThree, containsString("\"d\""))));

    }
    
    @Test public void
    test_reports_mismatches_and_unexpected_items() {

        Matcher<Iterable<? extends String>> containsOneTwo = Contains.inOrder("a", "b", "c");
        List<String> listOfOneThree = Arrays.asList("a", "c", "c", "d");

        listOutput(containsOneTwo, listOfOneThree);

        assertThat(containsOneTwo, is(not(a_matcher_that_matches(listOfOneThree))));
        assertThat(containsOneTwo, is(a_matcher_with_description(containsString("[\"a\", \"b\", \"c\"]"))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneThree, containsString("Items that did not match their corresponding expectations:"))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneThree, containsString("<2> Expected (\"b\")\n but was \"c\""))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneThree, containsString("Unexpected items:"))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneThree, containsString("\"d\""))));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    matches_empty_with_empty() {
        List<String> emptyList = Collections.emptyList();

        Matcher<Iterable<? extends String>> containsEmpty = Contains.inOrder();

        assertThat(containsEmpty, is(a_matcher_that_matches(emptyList)));
    }

    @Test public void
    matches_list_of_one_item() {
        List<String> listOfOne = Collections.singletonList("a");

        Matcher<Iterable<? extends String>> containsOne = Contains.inOrder("a");

        assertThat(containsOne, is(a_matcher_that_matches(listOfOne)));
    }
    
    @Test public void
    handles_and_reports_actual_null_values() {

        Matcher<Iterable<? extends String>> containsOneTwo = Contains.inOrder("a", "b");
        List<String> listOfOneNull = Arrays.asList("a", null);

        listOutput(containsOneTwo, listOfOneNull);

        assertThat(containsOneTwo, is(not(a_matcher_that_matches(listOfOneNull))));
        assertThat(containsOneTwo, is(a_matcher_with_description(containsString("[\"a\", \"b\"]"))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneNull, containsString("Items that did not match their corresponding expectations:"))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneNull, containsString("<2> Expected (\"b\")\n but was null"))));
  
    }
    
    @Test public void
    handles_and_reports_extra_actual_null_values() {
        Matcher<Iterable<? extends String>> containsOneTwo = Contains.inOrder("a", "b");
        List<String> listOfOneTwoNull = Arrays.asList("a", "b", null);

        listOutput(containsOneTwo, listOfOneTwoNull);

        assertThat(containsOneTwo, is(not(a_matcher_that_matches(listOfOneTwoNull))));
        assertThat(containsOneTwo, is(a_matcher_with_description(containsString("[\"a\", \"b\"]"))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneTwoNull, containsString("Unexpected items:"))));
        assertThat(containsOneTwo, is(a_matcher_giving_a_mismatch_description_of(listOfOneTwoNull, containsString("null"))));

    }
    
    @Test public void
    matches_on_null_elements_in_lists() {
        Matcher<Iterable<? extends String>> containsNull = Contains.inOrder("a", null);
        List<String> listNull = Arrays.asList("a", null);

        assertThat(containsNull, is(a_matcher_that_matches(listNull)));
    }
    
    @Test public void
    reports_when_null_element_was_expected_but_not_seen() {
        Matcher<Iterable<? extends String>> containsaNull = Contains.inOrder("a", null);
        List<String> listOfab = Arrays.asList("a", "b");

        listOutput(containsaNull, listOfab);

        assertThat(containsaNull, is(not(a_matcher_that_matches(listOfab))));
        assertThat(containsaNull, is(a_matcher_with_description(containsString("[\"a\", null]"))));
        assertThat(containsaNull, is(a_matcher_giving_a_mismatch_description_of(listOfab, containsString("Items that did not match their corresponding expectations:"))));
        assertThat(containsaNull, is(a_matcher_giving_a_mismatch_description_of(listOfab, containsString("<2> Expected (null)\n but was \"b\""))));

    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    can_use_matchers_for_elements_in_list() {
        Matcher<Iterable<? extends String>> containsAB = Contains.inOrder(Matchers.containsString("a"), Matchers.containsString("b"));
        List<String> listOfAAABBB = Arrays.asList("aaaaaa", "bbbbbbbbbb");
        assertThat(containsAB, is(a_matcher_that_matches(listOfAAABBB)));
    }
    
    public static class ABean {
        private String name;
        private Integer length;
        public ABean(String name, Integer length) {
            this.setName(name);
            this.setLength(length);
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public Integer getLength() {
            return length;
        }
        public void setLength(Integer length) {
            this.length = length;
        }
    }
    
    public static class ABeanMatcher extends TypeSafeDiagnosingMatcher<ABean> {
        
        private Matcher<? extends String> expectedName = Matchers.any(String.class);
        private Matcher<? extends Integer> expectedLength = Matchers.any(Integer.class);

        public ABeanMatcher(Matcher<? extends String> expectedName, Matcher<? extends Integer> expectedLength) {
            this.expectedName = expectedName;
            this.expectedLength = expectedLength;
        }
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public static ABeanMatcher matching(Matcher expectedName, Matcher expectedLength) {
            return new ABeanMatcher(expectedName, expectedLength);
        }
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
        public static ABeanMatcher matching(String expectedName, Integer expectedLength) {
            return new ABeanMatcher((Matcher)Matchers.is(expectedName), (Matcher)Matchers.is(expectedLength));
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(" name:").appendDescriptionOf(expectedName);
            description.appendText(" length:").appendDescriptionOf(expectedLength);
        }

        @Override
        protected boolean matchesSafely(ABean item, Description mismatchDescription) {
            if (!expectedLength.matches(item.length)) {
                mismatchDescription.appendText("length ");
                expectedLength.describeMismatch(item.length, mismatchDescription);
                return false;
            }
            if (!expectedName.matches(item.name)) {
                mismatchDescription.appendText("name ");
                expectedName.describeMismatch(item.name, mismatchDescription);
                return false;
            }
            return true;
        }
        
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    displays_details_of_which_field_of_a_complex_object_fails_to_match() {
        Matcher<Iterable<? extends ABean>> banana3apple1 = Contains.inOrder(ABeanMatcher.matching(containsString("an"), is(3)),
                                                                  ABeanMatcher.matching(containsString("ppl"), is(1)));
        
        List<ABean> aBigApple = Arrays.asList(new ABean("Banana", 3), new ABean("Apple", 2));
        
        assertThat(banana3apple1, is(not(a_matcher_that_matches(aBigApple))));
        assertThat(banana3apple1, is(a_matcher_giving_a_mismatch_description_of(aBigApple, containsString("Items that did not match their corresponding expectations:"))));
        assertThat(banana3apple1, is(a_matcher_giving_a_mismatch_description_of(aBigApple, containsString("length was <2>"))));
    }

    
    private static <T> void listOutput(Matcher<? super Iterable<? extends T>> containsOne, List<? extends T> emptyList) {
        StringDescription description = new StringDescription();
        containsOne.describeTo(description);
        StringDescription mismatchDescription = new StringDescription();
        containsOne.describeMismatch(emptyList, mismatchDescription);
//        System.out.println("EXPECTED: " + description + " BUT " + mismatchDescription);
    }

}
