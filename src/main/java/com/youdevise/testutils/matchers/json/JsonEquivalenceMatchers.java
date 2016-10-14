package com.youdevise.testutils.matchers.json;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.base.Throwables;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.hamcrest.Matchers.equalTo;

public final class JsonEquivalenceMatchers {
    static final ObjectMapper MAPPER = new ObjectMapper();
    static final ObjectReader STRICT_READER = MAPPER.reader().with(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
    static final ObjectReader LAX_READER = MAPPER.reader().with(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
                                                          .with(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);

    public static Matcher<JsonNode> equivalentJsonNode(String expectedJsonSource) {
        return new JsonRepresentationMatcher<JsonNode>(expectedJsonSource) {
            @Override
            protected String jsonToString(JsonNode item) {
                try {
                    return MAPPER.writeValueAsString(item);
                } catch (JsonProcessingException e) {
                    throw Throwables.propagate(e);
                }
            }
        };
    }

    public static abstract class JsonRepresentationMatcher<T> extends TypeSafeDiagnosingMatcher<T> {
        private final Matcher<String> jsonMatcher;

        public JsonRepresentationMatcher(String expectedJsonSource) {
            jsonMatcher = equivalentTo(expectedJsonSource);
        }

        @Override
        protected boolean matchesSafely(T item, Description mismatchDescription) {
            String json = jsonToString(item);
            jsonMatcher.describeMismatch(json, mismatchDescription);
            return jsonMatcher.matches(json);
        }

        @Override
        public void describeTo(Description description) {
            description.appendDescriptionOf(jsonMatcher);
        }

        protected abstract String jsonToString(T item);
    }

    public static Matcher<String> equivalentTo(final String expectedJsonSource) {
        return new TypeSafeDiagnosingMatcher<String>() {
            private final JsonNode expectedJson;
            {
                try {
                    expectedJson = LAX_READER.readTree(expectedJsonSource);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Invalid reference JSON", e);
                }
            }
            private final Matcher<JsonNode> matcher = equalTo(expectedJson);

            @Override
            protected boolean matchesSafely(String item, Description mismatchDescription) {
                JsonNode actualJson;
                try {
                    actualJson = STRICT_READER.readTree(item);
                } catch (IOException e) {
                    mismatchDescription.appendText("Invalid JSON: ").appendValue(e);
                    return false;
                }
                matcher.describeMismatch(actualJson, mismatchDescription);
                return matcher.matches(actualJson);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("JSON ").appendValue(expectedJsonSource);
            }
        };
    }

    public static Matcher<byte[]> bytesEquivalentTo(final String expectedJsonSource) {
        return new TypeSafeDiagnosingMatcher<byte[]>() {
            private final JsonNode expectedJson;
            {
                try {
                    expectedJson = LAX_READER.readTree(expectedJsonSource);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Invalid reference JSON", e);
                }
            }
            private final Matcher<JsonNode> matcher = equalTo(expectedJson);

            @Override
            protected boolean matchesSafely(byte[] item, Description mismatchDescription) {
                JsonNode actualJson;
                try {
                    actualJson = STRICT_READER.readTree(new ByteArrayInputStream(item));
                } catch (IOException e) {
                    mismatchDescription.appendText("Invalid JSON: ").appendValue(e);
                    return false;
                }
                matcher.describeMismatch(actualJson, mismatchDescription);
                return matcher.matches(actualJson);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("JSON ").appendValue(expectedJsonSource);
            }
        };
    }

    private JsonEquivalenceMatchers() {
    }
}
