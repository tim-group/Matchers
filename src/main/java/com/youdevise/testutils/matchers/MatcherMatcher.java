package com.youdevise.testutils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public final class MatcherMatcher<T> extends TypeSafeDiagnosingMatcher<Matcher<T>> {

    private enum MatchType { HAS_DESCRIPTION, MATCHES, MISMATCHES }

    private final MatchType type;
    private final T target;
    private final Matcher<?> mismatchDescriptionMatcher;
    private final Matcher<?> descriptionMatcher;

    private MatcherMatcher(MatchType type, T target, Matcher<? super String> description, Matcher<? super String> mismatchDescription) {
        this.type = type;
        this.target = target;
        this.descriptionMatcher = description;
        this.mismatchDescriptionMatcher = mismatchDescription;
    }

    // CHECKSTYLE_OFF: Name
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Factory
    public static Matcher<Object> a_matcher_with_description(Matcher<? super String> description) {
        return new MatcherMatcher(MatchType.HAS_DESCRIPTION, null, description, null);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Factory
    public static Matcher<Object> a_matcher_that_matches(Object matchedItem) {
        return new MatcherMatcher(MatchType.MATCHES, matchedItem, null, null);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Factory
    public static Matcher<Object> a_matcher_giving_a_mismatch_description_of(Object mismatchingItem,
                                                                             Matcher<? super String> mismatchDescription)
    {
        return new MatcherMatcher(MatchType.MISMATCHES, mismatchingItem, null, mismatchDescription);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a Matcher ");
        switch (type) {
        case HAS_DESCRIPTION:
            description.appendText("with description ").appendDescriptionOf(descriptionMatcher);
            break;
        case MATCHES:
            description.appendText("that matches ").appendValue(target);
            break;
        case MISMATCHES:
            description.appendText("that does not match ").appendValue(target)
                       .appendText(" and gives a mismatch description of ").appendDescriptionOf(mismatchDescriptionMatcher);
            break;
        default:
            break;
        }
    }

    // CHECKSTYLE_OFF: ReturnCount
    @Override
    protected boolean matchesSafely(Matcher<T> item, Description mismatchDescription) {
        mismatchDescription.appendText("was a Matcher ");

        if (MatchType.HAS_DESCRIPTION.equals(type)) {
            final String desc = matcherDescriptionOf(item);
            if (descriptionMatcher.matches(desc)) {
                return true;
            }
            mismatchDescription.appendText("whose description ");
            descriptionMatcher.describeMismatch(desc, mismatchDescription);
            return false;
        }

        if (MatchType.MATCHES.equals(type)) {
            if (item.matches(target)) {
                return true;
            }
            mismatchDescription.appendText("that did not match, and instead gave a mismatch description of ");
            mismatchDescription.appendValue(mismatchDescriptionOf(item, target));
            return false;
        }

        if (item.matches(target)) {
            mismatchDescription.appendText("that matched.");
            return false;
        }

        final String mismatchDesc = mismatchDescriptionOf(item, target);
        if (mismatchDescriptionMatcher.matches(mismatchDesc)) {
            return true;
        }

        mismatchDescription.appendText("whose mismatch description ");
        mismatchDescriptionMatcher.describeMismatch(mismatchDesc, mismatchDescription);
        return false;
    }

    private String matcherDescriptionOf(Matcher<?> matcher) {
        StringDescription description = new StringDescription();
        matcher.describeTo(description);
        return description.toString();
    }

    private String mismatchDescriptionOf(Matcher<?> matcher, Object value) {
        StringDescription description = new StringDescription();
        matcher.describeMismatch(value, description);
        return description.toString();
    }

}
