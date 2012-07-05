package com.youdevise.testutils.matchers;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.base.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class CoercingMatcherTest {

    @Test public void
    uses_supplied_function_to_coerce_item_to_matched_type() {
        Function<Number, Integer> toInt = new Function<Number, Integer>() {
            @Override public Integer apply(Number arg0) {
                return arg0.intValue();
            }
        };
        
        Number item = 12.5;
        
        assertThat(Coercible.with(toInt).to(Matchers.lessThan(13)),
                   MatcherMatcher.a_matcher_with_description("coercible to a value less than <13>"));
        
        assertThat(Coercible.with(toInt).to(12),
                   MatcherMatcher.a_matcher_that_matches(item));
        
        assertThat(Coercible.with(toInt).to(13),
                   MatcherMatcher.a_matcher_giving_a_mismatch_description_of(item, containsString("coerced item was <12>")));
    }

}
