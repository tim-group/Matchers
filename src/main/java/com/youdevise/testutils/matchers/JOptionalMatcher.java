package com.youdevise.testutils.matchers;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.hamcrest.Matchers.equalTo;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class JOptionalMatcher {
    private JOptionalMatcher() {
    }

    public static <T> Matcher<Optional<? super T>> isValue(T value) {
        return isPresent(equalTo(value));
    }

    public static <T> Matcher<Optional<? super T>> isPresent(final Matcher<T> valueMatcher) {
        return new TypeSafeDiagnosingMatcher<Optional<? super T>>() {
            @Override
            protected boolean matchesSafely(Optional<? super T> item, Description mismatchDescription) {
                if (!item.isPresent()) {
                    mismatchDescription.appendText("value was empty");
                    return false;
                }
                if (!valueMatcher.matches(item.get())) {
                    valueMatcher.describeMismatch(item.get(), mismatchDescription);
                    return false;
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendDescriptionOf(valueMatcher);
            }
        };
    }

    public static <T> Matcher<Optional<? super T>> isEmpty(final Class<T> clazz) {
        return new TypeSafeDiagnosingMatcher<Optional<? super T>>() {
            @Override
            protected boolean matchesSafely(Optional<? super T> item, Description mismatchDescription) {
                if (item.isPresent()) {
                    mismatchDescription.appendText("value was present: ").appendValue(item.get());
                    return false;
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("empty ").appendValue(clazz);
            }
        };
    }

    public static <T> Matcher<Optional<T>> isEmpty() {
        return new TypeSafeDiagnosingMatcher<Optional<T>>() {
            @Override
            protected boolean matchesSafely(Optional<T> item, Description mismatchDescription) {
                if (item.isPresent()) {
                    mismatchDescription.appendText("value was present: ").appendValue(item.get());
                    return false;
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("empty");
            }
        };
    }

    public static <T> Matcher<OptionalInt> isIntValue(int value) {
        return equalTo(OptionalInt.of(value));
    }

    public static <T> Matcher<OptionalInt> isPresentInt(Matcher<Integer> valueMatcher) {
        return new TypeSafeDiagnosingMatcher<OptionalInt>() {
            @Override
            protected boolean matchesSafely(OptionalInt item, Description mismatchDescription) {
                if (!item.isPresent()) {
                    mismatchDescription.appendText("value was empty");
                    return false;
                }
                valueMatcher.describeMismatch(item.getAsInt(), mismatchDescription);
                return valueMatcher.matches(item.getAsInt());
            }

            @Override
            public void describeTo(Description description) {
                description.appendDescriptionOf(valueMatcher);
            }
        };
    }

    public static <T> Matcher<OptionalInt> isEmptyInt() {
        return equalTo(OptionalInt.empty());
    }

    public static <T> Matcher<OptionalLong> isLongValue(int value) {
        return equalTo(OptionalLong.of(value));
    }

    public static <T> Matcher<OptionalLong> isPresentLong(Matcher<Long> valueMatcher) {
        return new TypeSafeDiagnosingMatcher<OptionalLong>() {
            @Override
            protected boolean matchesSafely(OptionalLong item, Description mismatchDescription) {
                if (!item.isPresent()) {
                    mismatchDescription.appendText("value was empty");
                    return false;
                }
                valueMatcher.describeMismatch(item.getAsLong(), mismatchDescription);
                return valueMatcher.matches(item.getAsLong());
            }

            @Override
            public void describeTo(Description description) {
                description.appendDescriptionOf(valueMatcher);
            }
        };
    }

    public static <T> Matcher<OptionalLong> isEmptyLong() {
        return equalTo(OptionalLong.empty());
    }

    public static <T> Matcher<OptionalDouble> isDoubleValue(int value) {
        return equalTo(OptionalDouble.of(value));
    }

    public static <T> Matcher<OptionalDouble> isPresentDouble(Matcher<Double> valueMatcher) {
        return new TypeSafeDiagnosingMatcher<OptionalDouble>() {
            @Override
            protected boolean matchesSafely(OptionalDouble item, Description mismatchDescription) {
                if (!item.isPresent()) {
                    mismatchDescription.appendText("value was empty");
                    return false;
                }
                valueMatcher.describeMismatch(item.getAsDouble(), mismatchDescription);
                return valueMatcher.matches(item.getAsDouble());
            }

            @Override
            public void describeTo(Description description) {
                description.appendDescriptionOf(valueMatcher);
            }
        };
    }

    public static <T> Matcher<OptionalDouble> isEmptyDouble() {
        return equalTo(OptionalDouble.empty());
    }
}
