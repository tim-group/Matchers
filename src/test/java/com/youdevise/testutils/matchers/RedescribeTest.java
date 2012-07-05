package com.youdevise.testutils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.Lists;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import static com.google.common.collect.Iterables.filter;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_giving_a_mismatch_description_of;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_that_matches;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_with_description;
import static com.youdevise.testutils.matchers.MatcherPredicates.notMatching;

public class RedescribeTest {

    @Test public void
    wraps_an_existing_matcher_replacing_its_description() {
        Matcher<? super String> containsTheMagicWord = Redescribe.theMatcher(containsString("please")).as("a polite string");
        
        assertThat(containsTheMagicWord,
                   a_matcher_with_description("a polite string"));
        
        assertThat(containsTheMagicWord,
                   a_matcher_that_matches("please match this string"));
        
        assertThat(containsTheMagicWord,
                   a_matcher_giving_a_mismatch_description_of("match this string or else",
                                                              containsString("was \"match this string or else\"")));
                                                              
    }
    
    @Test public void
    can_replace_mismatch_description_with_simple_text() {
        Matcher<? super String> containsTheMagicWord = Redescribe.theMatcher(containsString("please"))
                                                                 .as("a polite string")
                                                                 .describingMismatchAs("an impolite string");
        
        assertThat(containsTheMagicWord,
                   a_matcher_with_description("a polite string"));
        
        assertThat(containsTheMagicWord,
                   a_matcher_that_matches("please match this string"));
        
        assertThat(containsTheMagicWord,
                   a_matcher_giving_a_mismatch_description_of("match this string or else",
                                                              containsString("\"match this string or else\" was an impolite string")));
    }
    
    @Test public void
    can_replace_mismatch_description_using_describer_function() {
        MismatchDescriber<Iterable<Integer>> negativeNumberMismatchDescriber = new MismatchDescriber<Iterable<Integer>>() {
            @Override public void describeMismatch(Iterable<Integer> item, Description mismatchDescription) {
                mismatchDescription.appendText("the items ")
                                   .appendValueList("[", ", ", "]",
                                                    filter(item, notMatching(greaterThanOrEqualTo(0))))
                                   .appendText(" had negative values");
            }
        };
        
        Matcher<? super Iterable<Integer>> containsNoNegativeNumbers = Redescribe.theMatcher(EveryItem.matching(Matchers.greaterThanOrEqualTo(0)))
                                                                         .as("a collection of positive numbers")
                                                                         .describingMismatchWith(negativeNumberMismatchDescriber);
        
        assertThat(containsNoNegativeNumbers,
                   a_matcher_giving_a_mismatch_description_of(Lists.newArrayList(1, 2, -3, 5, -7),
                                                              containsString("the items [<-3>, <-7>] had negative values")));
    }

}
