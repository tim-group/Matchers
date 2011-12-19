package com.youdevise.testutils.matchers.beans;

import com.youdevise.testutils.matchers.beans.BaseJavaBeanMatcher;

import org.junit.Test;

import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_giving_a_mismatch_description_of;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_that_matches;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_with_description;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class BaseJavaBeanMatcherTest {
    
    private final BaseJavaBeanMatcher<DummyBean> matcherCheckingNoProperties =
        new BaseJavaBeanMatcher<DummyBean>(DummyBean.class, "Bean") { };
        
    private final BaseJavaBeanMatcher<DummyBean> matcherCheckingOneProperty =
        new BaseJavaBeanMatcher<DummyBean>(DummyBean.class, "Bean") {{
            withProperty("name", equalTo("Heinz"));
        }};
    
    private final BaseJavaBeanMatcher<DummyBean> matcherCheckingTwoProperties =
        new BaseJavaBeanMatcher<DummyBean>(DummyBean.class, "Bean") {{
            withProperty("name", equalTo("Heinz"));
            withProperty("baked", equalTo(true));
        }};

    @Test public void
    describes_itself_correctly_when_there_are_no_property_matchers() {
        assertThat(matcherCheckingNoProperties, is(a_matcher_with_description(equalTo("a Bean"))));
    }
    
    @Test public void
    describes_itself_correctly_when_there_is_one_property_matcher() {
        assertThat(matcherCheckingOneProperty, is(a_matcher_with_description(
                                                      equalTo("a Bean (whose name is \"Heinz\")"))));
    }

    @Test public void
    describes_itself_correctly_when_there_are_two_property_matchers() {
        assertThat(matcherCheckingTwoProperties, is(a_matcher_with_description(
                                                        equalTo("a Bean (whose name is \"Heinz\" and whose baked is <true>)"))));
    }
    
    @Test public void
    matches_correctly_when_there_are_no_property_matchers() {
        assertThat(matcherCheckingNoProperties, is(a_matcher_that_matches(new DummyBean("", false))));
    }

    @Test public void
    matches_correctly_when_there_is_one_property_matcher() {
        assertThat(matcherCheckingOneProperty, is(a_matcher_that_matches(new DummyBean("Heinz", false))));
    }
    
    @Test public void
    matches_correctly_when_there_are_two_property_matchers() {
        assertThat(matcherCheckingTwoProperties, is(a_matcher_that_matches(new DummyBean("Heinz", true))));
    }
    
    @Test public void
    describes_mismatch_when_the_value_is_null() {
        Object value = null;
        assertThat(matcherCheckingNoProperties, is(a_matcher_giving_a_mismatch_description_of(value,
                                                       equalTo("was a <null> Bean"))));
    }
    
    @Test public void
    describes_mismatch_when_the_value_is_of_the_wrong_type() {
        Object value = "fred";
        assertThat(matcherCheckingNoProperties, is(a_matcher_giving_a_mismatch_description_of(value,
                                                       equalTo("was not a Bean"))));
    }
    
    @Test public void
    describes_mismatch_when_the_property_has_the_wrong_value() {
        Object value = new DummyBean("Green", true);
        assertThat(matcherCheckingOneProperty, is(a_matcher_giving_a_mismatch_description_of(value,
                                                      equalTo("was a Bean whose name was \"Green\""))));
    }
    
    @Test public void
    describes_mismatch_when_two_properties_have_the_wrong_value() {
        Object value = new DummyBean("Green", false);
        assertThat(matcherCheckingTwoProperties, is(a_matcher_giving_a_mismatch_description_of(value,
                                                        equalTo("was a Bean whose name was \"Green\""))));
    }
    
    @Test public void
    gives_was_null_message_when_the_property_is_null_and_is_not_expected_to_be() {
         assertThat(matcherCheckingOneProperty, is(a_matcher_giving_a_mismatch_description_of(new DummyBean(null, false), 
                                                                                              containsString(" was null"))));
    }

    public static final class DummyBean {
        private final String name;
        private final boolean baked;
        
        public DummyBean(String name, boolean baked) {
            this.name = name;
            this.baked = baked;
        }
        public String getName() {
            return name;
        }
        public boolean isBaked() {
            return baked;
        }
    }
}
