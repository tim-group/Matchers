package com.youdevise.testutils.matchers;

import java.util.Comparator;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.google.common.collect.Ordering;

public class Sorted<T> extends TypeSafeDiagnosingMatcher<Iterable<? extends T>> {

    public static final String ASCENDING = "ascending";
    public static final String DESCENDING = "descending";
    public static final String CUSTOM = "custom";
    
    public static <T extends Comparable<T>> Sorted<T> inAscendingOrder() {
        Ordering<? super T> ordering = Ordering.natural();
        return with(ordering, ASCENDING);
    }
    
    public static <T extends Comparable<T>> Sorted<T> inDescendingOrder() {
        Ordering<? super T> ordering = Ordering.natural().reverse();
        return with(ordering, DESCENDING);
    }
    
    public static <T> Sorted<T> with(Comparator<? super T> comparator) {
        return with(comparator, CUSTOM);
    }
    
    public static <T> Sorted<T> with(Comparator<? super T> comparator, String orderType) {
        if (comparator instanceof Ordering) {
            return with(comparator, orderType);
        }
        return with(Ordering.from(comparator), orderType);
    }
    
    public static <T> Sorted<T> with(Ordering<? super T> ordering) {
        return with(ordering, CUSTOM);
    }
    
    public static <T> Sorted<T> with(Ordering<? super T> ordering, String orderType) {
        return new Sorted<>(ordering, orderType);
    }
    
    private final String orderType;
    private final Ordering<? super T> ordering;
    
    private Sorted(Ordering<? super T> ordering, String orderType) {
        this.ordering = ordering;
        this.orderType = orderType;
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText("an iterable in ").appendText(orderType).appendText(" order");
    }

    @Override
    protected boolean matchesSafely(Iterable<? extends T> item, Description mismatchDescription) {
        if (ordering.isOrdered(item)) {
            return true;
        }
        mismatchDescription.appendText("was not in order");
        return false;
    }
}