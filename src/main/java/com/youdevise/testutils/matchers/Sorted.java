package com.youdevise.testutils.matchers;

import java.util.Comparator;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.google.common.collect.Ordering;

public class Sorted<T> extends TypeSafeDiagnosingMatcher<Iterable<T>> {

    public static enum OrderType {
        ASCENDING,
        DESCENDING,
        CUSTOM
    }
    
    public static <T extends Comparable<T>> Sorted<T> inAscendingOrder() {
        Ordering<T> ordering = Ordering.natural();
        return with(ordering, OrderType.ASCENDING);
    }
    
    public static <T extends Comparable<T>> Sorted<T> inDescendingOrder() {
        Ordering<T> ordering = Ordering.natural().reverse();
        return with(ordering, OrderType.DESCENDING);
    }
    
    public static <T> Sorted<T> with(Comparator<T> comparator) {
        if (comparator instanceof Ordering) {
            return with((Ordering<T>) comparator);
        }
        return with(Ordering.from(comparator));
    }
    
    public static <T> Sorted<T> with(Ordering<T> ordering) {
        return with(ordering, OrderType.CUSTOM);
    }
    
    public static <T> Sorted<T> with(Ordering<T> ordering, OrderType orderType) {
        return new Sorted<T>(ordering, orderType);
    }
    
    private final OrderType orderType;
    private final Ordering<T> ordering;
    
    private Sorted(Ordering<T> ordering, OrderType orderType) {
        this.ordering = ordering;
        this.orderType = orderType;
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText("an iterable in ").appendText(orderType.name().toLowerCase()).appendText(" order");
    }

    @Override
    protected boolean matchesSafely(Iterable<T> item, Description mismatchDescription) {
        if (ordering.isOrdered(item)) {
            return true;
        }
        mismatchDescription.appendText("was not in order");
        return false;
    }
    
}