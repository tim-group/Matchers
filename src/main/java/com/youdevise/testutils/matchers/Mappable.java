package com.youdevise.testutils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class Mappable<A, B> extends TypeSafeDiagnosingMatcher<Iterable<A>> {

    public static <A, B> MapperBinder<A, B> with(Function<A, B> mapper) {
        return new MapperBinder<A, B>(mapper);
    }
    
    public static final class MapperBinder<A, B> {
        private final Function<A, B> mapper;
        
        public MapperBinder(Function<A, B> mapper) {
            this.mapper = mapper;
        }
        
        public Mappable<A, B> to(B...expectedItems) {
            return to(Contains.inOrder(expectedItems));
        }
        
        public Mappable<A, B> to(Matcher<B>...expected) {
            return to(Contains.inOrder(expected));
        }
        
        public Mappable<A, B> to(Iterable<Matcher<? super B>> expected) {
            return to(Contains.inOrder(expected));
        }
        
        public Mappable<A, B> to(Matcher<? super Iterable<B>> mappedIterableMatcher) {
            return new Mappable<A, B>(mapper, mappedIterableMatcher);
        }
    }
    
    private final Function<A, B> mapper;
    private final Matcher<? super Iterable<B>> mappedIterableMatcher;

    public Mappable(Function<A, B> mapper, Matcher<? super Iterable<B>> mappedIterableMatcher) {
        this.mapper = mapper;
        this.mappedIterableMatcher = mappedIterableMatcher;
    }
        
    @Override
    public void describeTo(Description description) {
        description.appendText("an iterable which maps to ").appendDescriptionOf(mappedIterableMatcher);
    }

    @Override
    protected boolean matchesSafely(Iterable<A> item, Description mismatchDescription) {
        Iterable<B> mappedItem = Iterables.transform(item, mapper);
        if (mappedIterableMatcher.matches(mappedItem)) {
            return true;
        }
        mappedIterableMatcher.describeMismatch(mappedItem, mismatchDescription);
        return false;
    }
}
