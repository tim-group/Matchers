package com.youdevise.testutils.matchers;

import java.util.Optional;
import java.util.OptionalInt;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.hamcrest.Matchers.equalTo;

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

    public static <T> Matcher<OptionalInt> isEmptyInt() {
        return equalTo(OptionalInt.empty());
    }
}
