package com.youdevise.testutils.matchers.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.hamcrest.collection.IsIterableContainingInOrder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.youdevise.testutils.matchers.json.JsonEquivalenceMatchers.equivalentJsonNode;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

public final class JsonStructureMatchers {
    @SuppressWarnings("unchecked")
    public static Matcher<JsonNode> jacksonTree(Matcher<? extends JsonNode> matcher) {
        return (Matcher<JsonNode>) matcher;
    }

    public static abstract class JsonRepresentationMatcher<T> extends TypeSafeDiagnosingMatcher<T> {
        private final Matcher<String> jsonMatcher;

        public JsonRepresentationMatcher(Matcher<? extends JsonNode> matcher) {
            jsonMatcher = json(matcher);
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

    public static <T> JsonRepresentationMatcher<T> makesJsonStructuredAs(Function<? super T, String> toJson,
                                                                         Matcher<? extends JsonNode> matcher) {
        return new JsonRepresentationMatcher<T>(matcher) {
            @Override
            protected String jsonToString(T item) {
                return toJson.apply(item);
            }
        };
    }

    public static Matcher<String> json(final Matcher<? extends JsonNode> matcher) {
        return new TypeSafeDiagnosingMatcher<String>() {
            @Override
            protected boolean matchesSafely(String item, Description mismatchDescription) {
                JsonNode node;
                try {
                    node = JsonEquivalenceMatchers.STRICT_READER.readTree(item);
                } catch (IOException e) {
                    mismatchDescription.appendText("Invalid JSON: ").appendValue(e);
                    return false;
                }
                matcher.describeMismatch(node, mismatchDescription);
                return matcher.matches(node);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("JSON ").appendDescriptionOf(matcher);
            }
        };
    }

    public static Matcher<byte[]> jsonBytes(final Matcher<? extends JsonNode> matcher) {
        return new TypeSafeDiagnosingMatcher<byte[]>() {
            @Override
            protected boolean matchesSafely(byte[] item, Description mismatchDescription) {
                JsonNode node;
                try {
                    node = JsonEquivalenceMatchers.STRICT_READER.readTree(new ByteArrayInputStream(item));
                } catch (IOException e) {
                    mismatchDescription.appendText("Invalid JSON: ").appendValue(e);
                    return false;
                }
                matcher.describeMismatch(node, mismatchDescription);
                return matcher.matches(node);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("JSON ").appendDescriptionOf(matcher);
            }
        };
    }

    public static ObjectNodeMatcher jsonObject() {
        return new ObjectNodeMatcher();
    }

    public static Matcher<ObjectNode> jsonAnyObject() {
        return any(ObjectNode.class);
    }

    public static class ObjectNodeMatcher extends TypeSafeDiagnosingMatcher<ObjectNode> {
        private final Map<String, Matcher<? extends JsonNode>> propertyMatchers = Maps.newLinkedHashMap();
        private final Set<String> excludedProperties = Sets.newHashSet();
        private boolean failOnUnexpectedProperties = true;

        @Override
        protected boolean matchesSafely(ObjectNode item, Description mismatchDescription) {
            Set<String> remainingFieldNames = Sets.newHashSet(item.fieldNames());
            for (Map.Entry<String, Matcher<? extends JsonNode>> e : propertyMatchers.entrySet()) {
                if (!item.has(e.getKey())) {
                    mismatchDescription.appendText(e.getKey()).appendText(" was not present");
                    return false;
                }
                JsonNode value = item.get(e.getKey());
                if (!e.getValue().matches(value)) {
                    mismatchDescription.appendText(e.getKey()).appendText(": ");
                    e.getValue().describeMismatch(value, mismatchDescription);
                    return false;
                }
                remainingFieldNames.remove(e.getKey());
            }
            if (failOnUnexpectedProperties && !remainingFieldNames.isEmpty()) {
                mismatchDescription.appendText("unexpected properties: ").appendValue(remainingFieldNames);
                return false;
            }
            for (String excludedProperty : excludedProperties) {
                if (item.has(excludedProperty)) {
                    mismatchDescription.appendText(excludedProperty).appendText(" was ").appendValue(item.get(excludedProperty));
                    return false;
                }
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("{ ");
            boolean first = true;
            for (Map.Entry<String, Matcher<? extends JsonNode>> e : propertyMatchers.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    description.appendText(", ");
                }
                description.appendValue(e.getKey()).appendText(": ").appendDescriptionOf(e.getValue());
            }
            for (String excludedProperty : excludedProperties) {
                if (first) {
                    first = false;
                } else {
                    description.appendText(", ");
                }
                description.appendValue(excludedProperty).appendText(": /*forbidden*/");
            }
            if (!failOnUnexpectedProperties) {
                if (first) {
                    first = false;
                } else {
                    description.appendText(", ");
                }
                description.appendText("/* others... */");
            }
            description.appendText(" }");
        }

        public ObjectNodeMatcher withProperty(String key, Matcher<? extends JsonNode> value) {
            Preconditions.checkNotNull(key, "property key must not be null");
            propertyMatchers.put(key, value);
            return this;
        }

        public ObjectNodeMatcher withProperty(String key, int value) {
            Preconditions.checkNotNull(key, "property key must not be null");
            propertyMatchers.put(key, jsonInt(value));
            return this;
        }

        public ObjectNodeMatcher withProperty(String key, double value) {
            Preconditions.checkNotNull(key, "property key must not be null");
            propertyMatchers.put(key, jsonDouble(value));
            return this;
        }

        public ObjectNodeMatcher withProperty(String key, boolean value) {
            Preconditions.checkNotNull(key, "property key must not be null");
            propertyMatchers.put(key, jsonBoolean(value));
            return this;
        }

        public ObjectNodeMatcher withProperty(String key, String value) {
            Preconditions.checkNotNull(key, "property key must not be null");
            propertyMatchers.put(key, jsonString(value));
            return this;
        }

        public ObjectNodeMatcher withPropertyJSON(String key, String json) {
            Preconditions.checkNotNull(key, "property key must not be null");
            propertyMatchers.put(key, equivalentJsonNode(json));
            return this;
        }

        public ObjectNodeMatcher withoutProperty(String key) {
            Preconditions.checkNotNull(key, "property key must not be null");
            excludedProperties.add(key);
            return this;
        }

        public ObjectNodeMatcher withAnyOtherProperties() {
            failOnUnexpectedProperties = false;
            return this;
        }
    }

    public static Matcher<TextNode> jsonAnyString() {
        return jsonString(any(String.class));
    }

    public static Matcher<TextNode> jsonString(String value) {
        return jsonString(equalTo(value));
    }

    public static Matcher<TextNode> jsonString(final Matcher<String> valueMatcher) {
        return new TypeSafeDiagnosingMatcher<TextNode>() {
            @Override
            protected boolean matchesSafely(TextNode item, Description mismatchDescription) {
                String text = item.asText();
                mismatchDescription.appendText("text ");
                valueMatcher.describeMismatch(text, mismatchDescription);
                return valueMatcher.matches(text);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("text ").appendDescriptionOf(valueMatcher);
            }
        };
    }

    public static Matcher<JsonNode> jsonAny() {
        return any(JsonNode.class);
    }

    public static Matcher<NullNode> jsonNull() {
        return new TypeSafeDiagnosingMatcher<NullNode>() {
            @Override
            protected boolean matchesSafely(NullNode item, Description mismatchDescription) {
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("null");
            }
        };
    }

    public static Matcher<NumericNode> jsonAnyNumber() {
        return any(NumericNode.class);
    }

    public static Matcher<NumericNode> jsonNumber(long n) {
        return jsonNumberLong(equalTo(n));
    }

    public static Matcher<NumericNode> jsonNumberLong(final Matcher<Long> matcher) {
        return new TypeSafeDiagnosingMatcher<NumericNode>() {
            @Override
            protected boolean matchesSafely(NumericNode item, Description mismatchDescription) {
                long value = item.asLong();
                mismatchDescription.appendText("long value ");
                matcher.describeMismatch(value, mismatchDescription);
                return matcher.matches(value);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("number ").appendDescriptionOf(matcher);
            }
        };
    }

    public static Matcher<NumericNode> jsonNumber(final Matcher<Double> matcher) {
        return new TypeSafeDiagnosingMatcher<NumericNode>() {
            @Override
            protected boolean matchesSafely(NumericNode item, Description mismatchDescription) {
                double value = item.asDouble();
                mismatchDescription.appendText("numeric value ");
                matcher.describeMismatch(value, mismatchDescription);
                return matcher.matches(value);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("JSON numeric ").appendDescriptionOf(matcher);
            }
        };
    }

    public static Matcher<IntNode> jsonInt(int n) {
        return jsonInt(equalTo(n));
    }

    public static Matcher<IntNode> jsonInt(final Matcher<Integer> matcher) {
        return new TypeSafeDiagnosingMatcher<IntNode>() {
            @Override
            protected boolean matchesSafely(IntNode item, Description mismatchDescription) {
                int value = item.asInt();
                mismatchDescription.appendText("integer value ");
                matcher.describeMismatch(value, mismatchDescription);
                return matcher.matches(value);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("int ").appendDescriptionOf(matcher);
            }
        };
    }

    public static Matcher<LongNode> jsonLong(long n) {
        return jsonLong(equalTo(n));
    }

    public static Matcher<LongNode> jsonLong(final Matcher<Long> matcher) {
        return new TypeSafeDiagnosingMatcher<LongNode>() {
            @Override
            protected boolean matchesSafely(LongNode item, Description mismatchDescription) {
                long value = item.asLong();
                mismatchDescription.appendText("long value ");
                matcher.describeMismatch(value, mismatchDescription);
                return matcher.matches(value);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("long ").appendDescriptionOf(matcher);
            }
        };
    }

    public static Matcher<DoubleNode> jsonDouble(double n) {
        return jsonDouble(equalTo(n));
    }

    public static Matcher<DoubleNode> jsonDouble(double n, double tolerance) {
        return jsonDouble(closeTo(n, tolerance));
    }

    public static Matcher<DoubleNode> jsonDouble(final Matcher<Double> matcher) {
        return new TypeSafeDiagnosingMatcher<DoubleNode>() {
            @Override
            protected boolean matchesSafely(DoubleNode item, Description mismatchDescription) {
                double value = item.asDouble();
                mismatchDescription.appendText("double value ");
                matcher.describeMismatch(value, mismatchDescription);
                return matcher.matches(value);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("double ").appendDescriptionOf(matcher);
            }
        };
    }

    public static Matcher<BooleanNode> jsonBoolean(boolean n) {
        return jsonBoolean(equalTo(n));
    }

    public static Matcher<BooleanNode> jsonBoolean(final Matcher<Boolean> matcher) {
        return new TypeSafeDiagnosingMatcher<BooleanNode>() {
            @Override
            protected boolean matchesSafely(BooleanNode item, Description mismatchDescription) {
                boolean value = item.asBoolean();
                mismatchDescription.appendText("boolean value ");
                matcher.describeMismatch(value, mismatchDescription);
                return matcher.matches(value);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("boolean ").appendDescriptionOf(matcher);
            }
        };
    }

    public static class ArrayNodeMatcher extends TypeSafeDiagnosingMatcher<ArrayNode> {
        private Matcher<?> contentsMatcher = emptyIterable();

        @Override
        protected boolean matchesSafely(final ArrayNode item, Description mismatchDescription) {
            Iterable<JsonNode> iterable = item::elements;
            contentsMatcher.describeMismatch(iterable, mismatchDescription);
            return contentsMatcher.matches(iterable);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("array");
            if (contentsMatcher != null) {
                description.appendText(" containing ").appendDescriptionOf(contentsMatcher);
            }
        }

        @SafeVarargs
        final public <T extends JsonNode> ArrayNodeMatcher of(Matcher<? super T>... nodeMatchers) {
            if (nodeMatchers.length == 0) {
                contentsMatcher = emptyIterable();
            } else {
                contentsMatcher = IsIterableContainingInOrder.<T> contains(ImmutableList.copyOf(nodeMatchers));
            }
            return this;
        }

        final public <T extends JsonNode> ArrayNodeMatcher of(List<Matcher<? super T>> nodeMatchers) {
            if (nodeMatchers.isEmpty()) {
                contentsMatcher = emptyIterable();
            } else {
                contentsMatcher = IsIterableContainingInOrder.<T> contains(ImmutableList.copyOf(nodeMatchers));
            }
            return this;
        }

        @SafeVarargs
        final public <T extends JsonNode> ArrayNodeMatcher inAnyOrder(Matcher<? super T>... nodeMatchers) {
            contentsMatcher = IsIterableContainingInAnyOrder.<T> containsInAnyOrder(ImmutableList.copyOf(nodeMatchers));
            return this;
        }

        public <T extends JsonNode> ArrayNodeMatcher including(final Matcher<? super T> nodeMatcher) {
            contentsMatcher = hasItem(nodeMatcher);
            return this;
        }

        @SafeVarargs
        final public <T extends JsonNode> ArrayNodeMatcher startingWith(Matcher<? super T>... nodeMatchers) {
            contentsMatcher = prefix(nodeMatchers.length, IsIterableContainingInAnyOrder.<T> containsInAnyOrder(ImmutableList.copyOf(nodeMatchers)));
            return this;
        }
    }

    private static <T> Matcher<Iterable<T>> prefix(final int prefixLength, final Matcher<Iterable<? extends T>> contentsMatcher) {
        return new TypeSafeDiagnosingMatcher<Iterable<T>>() {
            @Override
            protected boolean matchesSafely(Iterable<T> item, Description mismatchDescription) {
                Iterable<T> limited = Iterables.limit(item, prefixLength);
                contentsMatcher.describeMismatch(limited, mismatchDescription);
                return contentsMatcher.matches(limited);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("first ").appendValue(prefixLength).appendText(" of ");
            }
        };
    }

    public static ArrayNodeMatcher jsonArray() {
        return new ArrayNodeMatcher();
    }

    public static Matcher<ArrayNode> jsonAnyArray() {
        return any(ArrayNode.class);
    }

    private JsonStructureMatchers() {
    }
}
