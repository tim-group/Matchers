TIM Group Matchers
==================

A collection of hamcrest matchers developed at TIM Group.

Contains
--------

The mismatch description for <code>Matchers.hasItem("a")</code> is not especially readable:

    EXPECTED: a collection containing "a"
    BUT: was "b", was "c", was "d"

<code>Contains.theItem("a")</code> lists out the non-matching items, together with their indices, and the complete iterable:

    EXPECTED: a collection containing "a"
    BUT:
        <1> was "b"
        <2> was "c"
        <3> was "d"
    Complete actual iterable: <[b, c, d]>

<code>Contains.theItems()</code> checks for the presence of more than one item, ignoring non-matching items.

<code>Contains.inOrder()</code> checks for a complete list of items, in the order given; <code>Contains.inAnyOrder()</code> checks for a complete list in any order (use this when matching against unordered collections such as <code>Set</code>).

Generics issues
---------------

The <code>Contains</code> family of matchers are less generous than the hamcrest collection matchers in their generic wildcarding. This is to avoid a common situation where the Eclipse Java compiler would accept an expression that the stanard javac compiler would reject.

Here's a scenario illustrating the trade-off:

```java
public class TestGenerics {

    public static class MySuperClass { }
    public static class MySubClass extends MySuperClass { }
    
    @Test public void
    will_not_compile() {
        MySuperClass superHans = new MySuperClass();
        MySubClass subHans = new MySubClass();
        
        List<MySuperClass> mySuperList = Lists.newArrayList(superHans, subHans);
        Matcher<? super MySuperClass> isSuperHans = Matchers.equalTo(superHans);
        
        assertThat(mySuperList, Contains.theItem(superHans)); // Will compile
	assertThat(mySuperList, Matchers.hasItem(subHans)); // Will compile
        assertThat(mySuperList, Contains.theItem(subHans)); // Will not compile
        assertThat(mySuperList, Contains.theItem(isSuperHans)); // Will not compile
        assertThat(mySuperList, Matchers.hasItem(isSuperHans)); // Will compile in Eclipse, but not javac
    }
}
```

By using stricter generics, <code>Contains</code> loses the ability to check for an instance of <code>MySubClass</code> in a <code>Collection<MySuperClass></code>, but also loses the ability to introduce a type error that will go undetected by Eclipse's more generics-savvy compiler. We've found the latter annoyance more severe than the former; YMMV.
