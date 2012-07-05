package com.youdevise.testutils.matchers;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;

public class SelfDescribingString implements SelfDescribing {
    private final String text;
    public SelfDescribingString(String text) {
        this.text = text;
    }
    @Override
    public void describeTo(Description description) {
        description.appendText(text);
    }
}