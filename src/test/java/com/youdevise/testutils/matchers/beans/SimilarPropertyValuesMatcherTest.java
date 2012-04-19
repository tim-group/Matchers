package com.youdevise.testutils.matchers.beans;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_giving_a_mismatch_description_of;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_that_matches;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_with_description;
import static com.youdevise.testutils.matchers.beans.SimilarPropertyValuesMatcher.similar_properties_as;

public class SimilarPropertyValuesMatcherTest {
    
    @Test public void
    matches_when_all_properties_are_the_same_with_no_exclusions() {
         MyBean first = new MyBean("a", 1, Arrays.asList(2, 3, 4));
         MyBean second = new MyBean("a", 1, Arrays.asList(2, 3, 4));
         
         assertThat(similar_properties_as(first), is(a_matcher_that_matches(second)));
    }
    
    @Test public void
    allows_properties_to_be_the_same_if_they_are_excluded() {
        MyBean first = new MyBean("a", 1, Arrays.asList(2, 3, 4));
        MyBean second = new MyBean("a", 1, Arrays.asList(2, 3, 4));
        
        assertThat(similar_properties_as(first).ignoring("myString"), is(a_matcher_that_matches(second)));
    }

    @Test public void
    allows_properties_to_be_different_if_they_are_excluded() {
        MyBean first = new MyBean("a", 1, Arrays.asList(2, 3, 4));
        MyBean second = new MyBean("b", 1, Arrays.asList(2, 3, 4));
        
        assertThat(similar_properties_as(first).ignoring("myString"), is(a_matcher_that_matches(second)));
    }

    @Test public void
    describes_mismatch_where_single_property_is_different() {
        MyBean first = new MyBean("a", 1, Arrays.asList(2, 3, 4));
        MyBean second = new MyBean("b", 1, Arrays.asList(2, 3, 4));
        
        assertThat(similar_properties_as(first), is(a_matcher_giving_a_mismatch_description_of(second, is("<whose myString was \"b\">"))));
    }

    @Test public void
    describes_mismatch_where_multiple_properties_are_different() {
        MyBean first = new MyBean("a", 1, Arrays.asList(2, 3, 4));
        MyBean second = new MyBean("b", 2, Arrays.asList(2, 3, 4));
        
        assertThat(similar_properties_as(first), is(a_matcher_giving_a_mismatch_description_of(second, is("<whose myInt was <2>>, <whose myString was \"b\">"))));
    }

    @Test public void
    describes_expected() {
        MyBean first = new MyBean("a", 1, Arrays.asList(2, 3, 4));
        
        assertThat(similar_properties_as(first), is(a_matcher_with_description(allOf(containsString("an object "),
                                                                                  containsString("whose myInt <1>"),
                                                                                  containsString("whose myList <[2, 3, 4]>"),
                                                                                  containsString("whose myString \"a\"")))));
    }
    
    @Test public void
    does_not_try_to_read_the_write_only_properties() {
         final WriteOnlyProperties first = new WriteOnlyProperties();
         final WriteOnlyProperties second = new WriteOnlyProperties();
         
         assertThat(similar_properties_as(first), is(a_matcher_that_matches(second)));
    }

    @Test public void
    describes_expected_including_the_list_of_ignored_properties() {
        MyBean first = new MyBean("a", 1, Arrays.asList(2, 3, 4));
        
        assertThat(similar_properties_as(first).ignoring("myInt", "myString"), is(a_matcher_with_description(allOf(containsString("an object "),
                                                                                  containsString("whose myList <[2, 3, 4]>"),
                                                                                  containsString("ignoring properties [\"myInt\", \"myString\"]")))));
    }
    
    public static class MyBean {
        private final String myString;
        private final int myInt;
        private final List<? extends Number> myList;

        public MyBean(String myString, int myInt, List<? extends Number> myList) {
            this.myString = myString;
            this.myInt = myInt;
            this.myList = myList;
        }

        public String getMyString() {
            return myString;
        }

        public int getMyInt() {
            return myInt;
        }

        public List<? extends Number> getMyList() {
            return myList;
        }
        
    }
    
    public static class WriteOnlyProperties {
        public void setFoo(@SuppressWarnings("unused") String foo) {  }
        public String getMyString() { return "MyString"; }
    }
    
}
