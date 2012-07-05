package com.youdevise.testutils.matchers;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import com.google.common.base.Function;

public final class Coercible {
    
    private Coercible() { }
    
    public static class CoercingMatcherBinder<FROM, TO> {
        private final Function<FROM, TO> f;
        private CoercingMatcherBinder(Function<FROM, TO> f) {
            this.f = f;
        }
        public CoercingMatcher<FROM, TO> to(TO item) {
            return to(Matchers.equalTo(item));
        }
        
        public CoercingMatcher<FROM, TO> to(Matcher<? super TO> innerMatcher) {
            return new CoercingMatcher<FROM, TO>(innerMatcher) {
                @Override protected TO coerce(FROM from) {
                    return f.apply(from);
                }
            };
        }
    }
    
    public static <FROM, TO> CoercingMatcherBinder<FROM, TO> with(Function<FROM, TO> f) {
        return new CoercingMatcherBinder<FROM, TO>(f);
    }
}
