package com.youdevise.testutils.matchers;

import java.util.List;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static java.util.stream.Collectors.toList;

public class Mappable<A, B> extends TypeSafeDiagnosingMatcher<Iterable<A>> {

    public static <A, B> MapperBinder<A, B> with(Function<? super A, ? extends B> mapper) {
        return new MapperBinder<>(mapper);
    }
    
    public static final class MapperBinder<A, B> {
        private final Function<? super A, ? extends B> mapper;
        
        public MapperBinder(Function<? super A, ? extends B> mapper) {
            this.mapper = mapper;
        }

        @SafeVarargs
        public final Mappable<A, B> to(B...expectedItems) {
            return to(Contains.inOrder(expectedItems));
        }

        @SafeVarargs
        public final Mappable<A, B> to(Matcher<B>...expected) {
            return to(Contains.inOrder(expected));
        }
        
        public final Mappable<A, B> to(Iterable<Matcher<? super B>> expected) {
            return to(Contains.inOrder(expected));
        }
        
        public final Mappable<A, B> to(Matcher<? super Iterable<B>> mappedIterableMatcher) {
            return new Mappable<>(mapper, mappedIterableMatcher);
        }
    }
    
    private final Function<? super A, ? extends B> mapper;
    private final Matcher<? super Iterable<B>> mappedIterableMatcher;

    public Mappable(Function<? super A, ? extends B> mapper, Matcher<? super Iterable<B>> mappedIterableMatcher) {
        this.mapper = mapper;
        this.mappedIterableMatcher = mappedIterableMatcher;
    }
        
    @Override
    public void describeTo(Description description) {
        description.appendText("an iterable which maps to ").appendDescriptionOf(mappedIterableMatcher);
    }

    @Override
    protected boolean matchesSafely(Iterable<A> item, Description mismatchDescription) {
        List<B> mappedItem = StreamSupport.stream(item.spliterator(), false).map(mapper::apply).collect(toList());
        mappedIterableMatcher.describeMismatch(mappedItem, mismatchDescription);
        return mappedIterableMatcher.matches(mappedItem);
    }
}
