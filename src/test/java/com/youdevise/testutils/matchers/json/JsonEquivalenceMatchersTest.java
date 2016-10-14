package com.youdevise.testutils.matchers.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import org.junit.Test;

import static com.youdevise.testutils.matchers.json.JsonEquivalenceMatchers.equivalentJsonNode;
import static com.youdevise.testutils.matchers.json.JsonEquivalenceMatchers.equivalentTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class JsonEquivalenceMatchersTest {
    @Test
    public void matches_string() {
        assertThat("\"foo\"", equivalentTo("\"foo\""));
    }

    @Test
    public void matches_integer() {
        assertThat("123", equivalentTo("123"));
    }

    @Test
    public void matches_double() {
        assertThat("1.0", equivalentTo("1.0"));
    }

    @Test
    public void matches_boolean() {
        assertThat("true", equivalentTo("true"));
    }

    @Test
    public void matches_null() {
        assertThat("null", equivalentTo("null"));
    }

    @Test
    public void empty_string_is_not_equivalent_to_null() {
        assertThat("", not(equivalentTo("null")));
    }

    @Test
    public void matches_simple_object() {
        assertThat("{ \"a\": 1 }", equivalentTo("{ \"a\": 1 }"));
    }

    @Test
    public void item_being_matched_must_parse_strictly() {
        assertThat("{ a: 1 }", not(equivalentTo("{ \"a\": 1 }")));
    }

    @Test
    public void reference_data_may_parse_laxly() {
        assertThat("{ \"a\": 1 }", equivalentTo("{ a: 1 }"));
    }

    @Test
    public void matches_jackson_treenode() {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        assertThat(nodeFactory.objectNode().set("a", nodeFactory.numberNode(1)), equivalentJsonNode("{ a: 1 }"));
    }
}
