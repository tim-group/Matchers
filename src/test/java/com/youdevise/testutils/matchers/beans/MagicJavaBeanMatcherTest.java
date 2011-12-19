package com.youdevise.testutils.matchers.beans;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.youdevise.testutils.matchers.Contains;
import com.youdevise.testutils.matchers.MatcherMatcher;

import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_that_matches;
import static java.util.Calendar.YEAR;
import static org.hamcrest.MatcherAssert.assertThat;

public class MagicJavaBeanMatcherTest {
    private TimeZone originalJavaTimeZone;

    private final Mockery context = new Mockery();

    @SuppressWarnings("unchecked")
    private final JavaBeanMatcher<Bean> matcher = context.mock(JavaBeanMatcher.class);

    @Before public void set_fixed_default_time_zone() {
        originalJavaTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @After public void reset_time_zone() {
        TimeZone.setDefault(originalJavaTimeZone);
    }

    @SuppressWarnings("unchecked")
    @Test public void
    translates_method_calls_to_property_matchers() {
        final MagicJavaBeanMatcher<Bean> magicMatcher = MagicJavaBeanMatcher.forMatcher(matcher);

        context.checking(new Expectations() {{
            oneOf(matcher).withProperty(with("age"), (Matcher<Integer>) with(a_matcher_that_matches(23)));
        }});

        magicMatcher.getProxy(ABean.MatcherBuilder.class).with_age(23);
        context.assertIsSatisfied();
    }

    @SuppressWarnings("unchecked")
    @Test public void
    strips_participles() {
        final MagicJavaBeanMatcher<Bean> magicMatcher = MagicJavaBeanMatcher.forMatcher(matcher);

        context.checking(new Expectations() {{
            oneOf(matcher).withProperty(with("name"), (Matcher<String>) with(a_matcher_that_matches("Mr Neab")));
        }});

        magicMatcher.getProxy(ABean.MatcherBuilder.class).with_the_name("Mr Neab");
        context.assertIsSatisfied();
    }

    @SuppressWarnings("unchecked")
    @Test public void
    strips_underscores_and_camel_cases() {
        final MagicJavaBeanMatcher<Bean> magicMatcher = MagicJavaBeanMatcher.forMatcher(matcher);

        context.checking(new Expectations() {{
            oneOf(matcher).withProperty(with("firstName"), (Matcher<String>) with(a_matcher_that_matches("Humphrey")));
        }});

        magicMatcher.getProxy(ABean.MatcherBuilder.class).with_a_first_name_of("Humphrey");
        context.assertIsSatisfied();
    }

    @SuppressWarnings("unchecked")
    @Test public void
    allows_property_name_rewriting_with_addresses_property_attribute() {
        final MagicJavaBeanMatcher<Bean> magicMatcher = MagicJavaBeanMatcher.forMatcher(matcher);

        context.checking(new Expectations() {{
            oneOf(matcher).withProperty(with("dob"), (Matcher<Date>) with(a_matcher_that_matches(january(1, 1900))));
        }});

        magicMatcher.getProxy(ABean.MatcherBuilder.class).with_date_of_birth(a_date_in_the_year(1900));
        context.assertIsSatisfied();
    }

    @SuppressWarnings("unchecked")
    @Test public void
    allows_fancy_matching_with_extension_classes() {
        final MagicJavaBeanMatcher<Bean> magicMatcher = MagicJavaBeanMatcher.forMatcher(matcher);

        context.checking(new Expectations() {{
            oneOf(matcher).withProperty(with("name"), (Matcher<String>) with(a_matcher_that_matches("Mr whoever")));
        }});

        magicMatcher.getProxy(ABean.MatcherBuilder.class).is_addressed_as(Title.MR);
        context.assertIsSatisfied();
    }

    @Test public void
    creates_a_matcher_that_matches_a_matching_bean() {
        assertThat(ABean.with_the_name("Mr Bean")
                        .with_a_first_name_of("Aloysius")
                        .is_addressed_as(Title.MR)
                        .with_age(42)
                        .with_date_of_birth(a_date_in_the_year(1900)),
                   MatcherMatcher.a_matcher_that_matches(new Bean()));
    }

    @Test public void
    creates_a_matcher_that_does_not_match_a_mismatching_bean() {
        assertThat(ABean.with_the_name("Mr Neab")
                        .with_a_first_name_of("Humphrey")
                        .is_addressed_as(Title.MRS)
                        .with_age(23)
                        .with_date_of_birth(a_date_in_the_year(1984)),
                   MatcherMatcher.a_matcher_giving_a_mismatch_description_of(new Bean(),
                                                                             Matchers.containsString("Mr Bean")));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    doesnt_bork_when_used_with_contains_in_any_order() {
        final List<Bean> twoBeans = Arrays.asList(new Bean(), new Bean());

        final Matcher<Iterable<Bean>> matchesBothBeans = Contains.inAnyOrder(
            ABean.with_the_name("Mr Bean")
                .with_a_first_name_of("Aloysius")
                .is_addressed_as(Title.MR)
                .with_age(42)
                .with_date_of_birth(a_date_in_the_year(1900)),
            ABean.with_the_name("Mr Bean")
                .with_a_first_name_of("Aloysius")
                .is_addressed_as(Title.MR)
                .with_age(42)
                .with_date_of_birth(a_date_in_the_year(1900)));

        assertThat(twoBeans, matchesBothBeans);
    }

    @SuppressWarnings("unused")
    private static class Bean {
        public String getName() { return "Mr Bean"; }
        public String getFirstName() { return "Aloysius"; }
        public int getAge() { return 42; }
        public Date getDob() { return january(1, 1900); }
    }

    private static final class ABean {
       private ABean() { }

       public static ABean.MatcherBuilder with_the_name(String name) {
           final MagicJavaBeanMatcher<Bean> magicMatcher = MagicJavaBeanMatcher.forClass(Bean.class, "A bean");
           return magicMatcher.getProxy(MatcherBuilder.class).with_the_name(name);
       }

       public static interface MatcherBuilder extends Matcher<Bean> {
           MatcherBuilder with_the_name(String name);
           MatcherBuilder with_the_name(Matcher<? super String> nameMatcher);
           MatcherBuilder with_a_first_name_of(String firstName);
           MatcherBuilder with_a_first_name_of(Matcher<? super String> firstNameMatcher);
           MatcherBuilder with_age(int age);
           MatcherBuilder with_age(Matcher<? super Integer> ageMatcher);

           @AddressesProperty("dob")
           MatcherBuilder with_date_of_birth(Date date);

           @AddressesProperty("dob")
           MatcherBuilder with_date_of_birth(Matcher<? super Date> dateMatcher);

           @MatchesWith(TitleChecker.class)
           MatcherBuilder is_addressed_as(Title title);
       }
    }

    public static enum Title {
        MR("Mr"),
        MRS("Mrs"),
        MS("MS");

        private final String representation;
        private Title(String representation) {
            this.representation = representation;
        }
        @Override public String toString() { return representation; }
    }

    public static final class TitleChecker implements JavaBeanPropertyMatcherMaker<Bean> {
        @Override
        public void makeProperty(JavaBeanMatcher<Bean> matcher, Object[] args) {
            final Title title = (Title) args[0];
            matcher.withProperty("name", Matchers.startsWith(title.toString()));
        }
    }

    private Matcher<? super Date> a_date_in_the_year(final int year) {
        return new TypeSafeDiagnosingMatcher<Date>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("a date in the year ").appendValue(year);
            }

            @Override
            protected boolean matchesSafely(Date date, Description mismatchDescription) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                final int actualYear = calendar.get(YEAR);
                mismatchDescription.appendText("a date in the year ").appendValue(actualYear);
                return year == actualYear;
            }
        };
    }

    protected static Date january(int day, int year) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, 1, day);
        return calendar.getTime();
    }
}
