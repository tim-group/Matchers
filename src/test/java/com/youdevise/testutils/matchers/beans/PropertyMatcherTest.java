package com.youdevise.testutils.matchers.beans;

import org.hamcrest.Matchers;
import org.junit.Test;

import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_giving_a_mismatch_description_of;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_that_matches;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_with_description;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class PropertyMatcherTest {

    @Test public void
    describes_itself_with_implicit_is() {
        PropertyMatcher matcher = PropertyMatcher.an_object_with_property("propName", equalTo("bob"));

        assertThat(matcher, is(a_matcher_with_description(equalTo("whose propName is \"bob\""))));
    }

    @Test public void
    describes_itself_with_explicit_is() {
        PropertyMatcher matcher = PropertyMatcher.an_object_whose("propName", is("bob"));

        assertThat(matcher, is(a_matcher_with_description(equalTo("whose propName is \"bob\""))));
    }

    @Test public void
    describes_itself_with_contains_string() {
        PropertyMatcher matcher = PropertyMatcher.an_object_whose("propName").is(containsString("bob"));

        assertThat(matcher, is(a_matcher_with_description(equalTo("whose propName is a string containing \"bob\""))));
    }

    @Test public void
    matches_correctly() {
        Object value = new DummyBean("Runner");
        PropertyMatcher matcher = PropertyMatcher.an_object_with_property("name", equalTo("Runner"));

        assertThat(matcher, is(a_matcher_that_matches(value)));
    }

    @Test public void
    matches_null_value_correctly() {
        Object value = new DummyBean(null);
        PropertyMatcher matcher = PropertyMatcher.an_object_with_property("name", nullValue());

        assertThat(matcher, is(a_matcher_that_matches(value)));
    }

    @Test public void
    matches_correctly_for_boolean_special_case() {
        Object value = new DummyBean("Runner");
        PropertyMatcher matcher = PropertyMatcher.an_object_with_property("baked", equalTo(false));

        assertThat(matcher, is(a_matcher_that_matches(value)));
    }

    @Test public void
    describes_mismatch_when_the_value_is_wrong() {
        Object value = new DummyBean("Coffee");
        PropertyMatcher matcher = PropertyMatcher.an_object_with_property("name", equalTo("Runner"));

        assertThat(matcher, is(a_matcher_giving_a_mismatch_description_of(value,
                                   equalTo("whose name was \"Coffee\""))));
    }

    @Test public void
    describes_mismatch_when_the_boolean_property_value_is_wrong() {
        Object value = new DummyBean("Coffee");
        PropertyMatcher matcher = PropertyMatcher.an_object_with_property("baked", equalTo(true));

        assertThat(matcher, is(a_matcher_giving_a_mismatch_description_of(value,
                                   equalTo("whose baked was <false>"))));
    }

    @Test public void
    describes_mismatch_when_the_property_does_not_exist() {
        Object value = new DummyBean("Coffee");
        PropertyMatcher matcher = PropertyMatcher.an_object_with_property("size", equalTo(2));

        assertThat(matcher, is(a_matcher_giving_a_mismatch_description_of(value,
                                   equalTo("with no public property or field named \"size\""))));
    }

    @Test public void
    describes_mismatch_when_the_property_exists_but_is_null_when_expected_to_be_not_null_with_getter_method() {
        Object value = new DummyBean(null);
        PropertyMatcher matcher = PropertyMatcher.an_object_with_property("name", equalTo("Bob"));

        assertThat(matcher, is(a_matcher_giving_a_mismatch_description_of(value,
                                   Matchers.containsString(" was null"))));
    }

    @Test public void
    describes_mismatch_when_the_property_exists_but_is_null_when_expected_to_be_not_null_with_public_field() {
        Object value = new DummyStruct(null);
        PropertyMatcher matcher = PropertyMatcher.an_object_with_property("name", equalTo("Bob"));

        assertThat(matcher, is(a_matcher_giving_a_mismatch_description_of(value,
                                   Matchers.containsString(" was null"))));
    }

    @Test public void
    describes_mismatch_when_the_property_cannot_be_read() {
        Object value = new DummyBean("Coffee");
        PropertyMatcher matcher = PropertyMatcher.an_object_with_property("price", equalTo(2));

        assertThat(matcher, is(a_matcher_giving_a_mismatch_description_of(value,
                                   equalTo("with no getter method for the \"price\" property"))));
    }

    @Test public void
    describes_mismatch_when_the_property_read_operation_throws_an_exception() {
        Object value = new DummyBean("Coffee");
        PropertyMatcher matcher = PropertyMatcher.an_object_with_property("nasty", equalTo(2));

        assertThat(matcher, is(a_matcher_giving_a_mismatch_description_of(value,
                                   equalTo("whose \"nasty\" getter method threw an exception <java.lang.IllegalArgumentException: I'm Nasty>"))));
    }

    @Test public void
    matches_public_field_without_getter_method() {
        Object value = new DummyStruct("Retro");
        PropertyMatcher matcher = PropertyMatcher.an_object_with_property("name", equalTo("Retro"));

        assertThat(matcher, is(a_matcher_that_matches(value)));
    }

    @Test public void
    matches_null_public_field_without_getter_method() {
        Object value = new DummyStruct(null);
        PropertyMatcher matcher = PropertyMatcher.an_object_with_property("name", nullValue());

        assertThat(matcher, is(a_matcher_that_matches(value)));
    }

    @Test public void
    describes_mismatch_when_asked_to_match_on_a_nonexistent_property() {
        Object value = new DummyStruct("anything");
        PropertyMatcher matcher = PropertyMatcher.an_object_with_property("size", equalTo(5));

        assertThat(matcher, is(a_matcher_giving_a_mismatch_description_of(value,
                                   equalTo("with no public property or field named \"size\""))));
    }

    @Test public void
    describes_mismatch_when_asked_to_match_on_a_private_property() {
        Object value = new DummyStruct("anything");
        PropertyMatcher matcher = PropertyMatcher.an_object_with_property("password", equalTo("sausages"));

        assertThat(matcher, is(a_matcher_giving_a_mismatch_description_of(value,
                                   equalTo("whose \"password\" field could not be read, due to:<java.lang.IllegalAccessException: " +
                                              "Class com.youdevise.testutils.matchers.beans.PropertyMatcher can not access a member " +
                                              "of class com.youdevise.testutils.matchers.beans.PropertyMatcherTest$DummyStruct " +
                                              "with modifiers \"private final\">"))));
    }

    @Test public void
    matches_property_accessed_by_method_declared_in_superclass() {
         DummyBean value = new DummyBean("anything");
         PropertyMatcher matcher = PropertyMatcher.an_object_with_property("hardcodedString", equalTo("i'm hardcoded, yo"));

         assertThat(matcher, is(a_matcher_that_matches(value)));
    }

    @Test public void
    matches_public_field_declared_in_superclass() {
        DummyStruct value = new DummyStruct("anything");
        PropertyMatcher matcher = PropertyMatcher.an_object_with_property("hardcodedString", equalTo("i'm hardcoded, yo"));

        assertThat(matcher, is(a_matcher_that_matches(value)));
    }

    public static class DummyBeanSuperclass {
        public String getHardcodedString() {
            return "i'm hardcoded, yo";
        }
    }

    public static final class DummyBean extends DummyBeanSuperclass {
        private final String name;
        private final boolean baked;
        public DummyBean(String name) {
            this(name, false);
        }
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
        public String getNasty() {
            throw new IllegalArgumentException("I'm Nasty");
        }
        public void setPrice(@SuppressWarnings("unused") int price) {}
    }

    public static class DummyStructSuperclass {
        public String hardcodedString = "i'm hardcoded, yo";
    }

    public static final class DummyStruct extends DummyStructSuperclass {

        public final String name;
        @SuppressWarnings("unused")
        private final String password = "sausages";

        public DummyStruct(String name) {
            this.name = name;
        }
    }

}
