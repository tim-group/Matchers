package com.youdevise.testutils.matchers;


import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_that_matches;

public class ContainsTheItemTest {
    
    @Test public void
    reports_reason_why_each_actual_item_does_not_match_the_expectation() {
        Matcher<Iterable<String>> containsA = Contains.theItem("a");
        List<String> listABC = Arrays.<String>asList("b", "c", "d");
        
        listOutput(containsA, listABC);
        
        assertThat(containsA, is(not(a_matcher_that_matches(listABC))));                                          
        assertThat(containsA, is(MatcherMatcher.a_matcher_giving_a_mismatch_description_of(listABC, containsString("<1> was \"b\""))));    
        assertThat(containsA, is(MatcherMatcher.a_matcher_giving_a_mismatch_description_of(listABC, containsString("<2> was \"c\""))));    
        assertThat(containsA, is(MatcherMatcher.a_matcher_giving_a_mismatch_description_of(listABC, containsString("<3> was \"d\""))));    
    }
    
        
    private void listOutput(Matcher<Iterable<String>> containsOne, List<String> emptyList) {
        StringDescription description = new StringDescription();
        containsOne.describeTo(description);
        StringDescription mismatchDescription = new StringDescription();
        containsOne.describeMismatch(emptyList, mismatchDescription);
        System.out.println("EXPECTED: " + description + " BUT " + mismatchDescription);
    }

}
