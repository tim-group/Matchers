package com.youdevise.testutils.matchers;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public class ContainsInAnyOrder<T> extends CollectionMatcher<T> {
    
    public ContainsInAnyOrder(Matcher<T>[] expected) {
        super(expected, (expected == null || expected.length == 0) ? Matchers.<T>emptyIterable() : Matchers.<T>containsInAnyOrder(expected));
    }

    protected void diagnoseFailures(Iterable<T> actual, Description mismatchDescription, Matcher<T>[] expected) {
        List<T> actualList = listOf(actual);
        
        if (actualCollectionIsEmpty(mismatchDescription, actualList)) {
            return;
        }
        
        if (actualList.size() != expected.length)  {
            mismatchDescription.appendText(String.format("expected size %d, actual size %d; ", expected.length, actualList.size()));
        }
        boolean first = true;
        for (int i = 0; i < expected.length; i++) {
            if ( !Matchers.hasItem(expected[i]).matches(actualList) ) {
                if (first) {
                    mismatchDescription.appendText("\n\tItems that were expected, but not present: ");
                    first = false;
                }
                mismatchDescription.appendText("\n\t  ").appendValue(i + 1).appendText(" ").appendValue(expected[i]);
            }
        }
        first = true;
        for (int i = 0; i < actualList.size(); i++) {
            if ( !Matchers.anyOf(expected).matches(actualList.get(i))) {  
                if (first) {            
                    mismatchDescription.appendText("\n\tUnexpected items: ");
                    first = false;
                }
                mismatchDescription.appendText("\n\t  ").appendValue(i + 1).appendText(" ").appendValue(actualList.get(i));
            }
        }
    }


}