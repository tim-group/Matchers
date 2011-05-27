package com.youdevise.sandpit.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.youdevise.sandpit.HelloWorld;


public final class HelloWorldTest {
    
    @Test public void
    saysHello() {
        assertThat(new HelloWorld().greeting(), is(equalTo("Hello, World!")));
    }
}
