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

The <code>Contains</code> family of matchers are less generous than the hamcrest collection matchers in their generic wildcarding. This is to avoid a common situation where the Eclipse Java compiler would accept an expression that the standard javac compiler would reject with a type error.

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
        
        assertThat(mySuperList, Contains.theItem(superHans)); // OK
        assertThat(mySuperList, Contains.theItem(subHans)); // Does not compile
        assertThat(mySuperList, Matchers.hasItem(subHans)); // OK
        assertThat(mySuperList, Matchers.hasItem(isSuperHans)); // Compiles in Eclipse, but not javac
        assertThat(mySuperList, Contains.theItem(isSuperHans)); // Does not compile
    }
}
```

By using stricter generics, <code>Contains</code> loses the ability to check for an instance of <code>MySubClass</code> in an <code>Iterable&lt;MySuperClass&gt;</code>, but also loses the ability to introduce a type error that will go undetected by Eclipse's more generics-savvy compiler. We've found the latter annoyance to be more severe than the former; YMMV.

AnIterable
----------

<code>AnIterable</code> is <code>Matchers.instanceOf(klass)</code> for collections, with a little sugar:

```java
    Collection<Object> aCollectionOfFineMusicians = newArrayList();
    aCollectionOfFineMusicians.put("Lee");
    aCollectionOfFineMusicians.put("Lifeson");
    aCollectionOfFineMusicians.put("Peart");

    assertThat(aCollectionOfFineMusicians, AnIterable.of(String.class).which(Contains.inAnyOrder("Peart", "Lee", "Lifeson")));
    assertThat(aCollectionOfFineMusicians, AnIterable.of(String.class).inAscendingOrder());
    assertThat(aCollectionOfFineMusicians, AnIterable.of(String.class).withoutDuplicates());

    aCollectionOfFineMusicians.clear();

    assertThat(aCollectionOfFineMusicians, AnIterable.of(String.class).withoutContents());
```

Coercible, Mappable, Reducible, Sorted
--------------------------------------

It's sometimes useful to be able to match against a transformed value, rather than against the value directly.

For example, given a collection of <code>Employees</code> and a <code>Function&lt;Employee, String&gt;</code> that returns each employee's name, we can say:

```java
    assertThat(allEmployees.get(1), Coercible.with(toName).to("Angel"));
    assertThat(allEmployees, Mappable.with(toName).to(Contains.inAnyOrder("Cordelia", "Wesley", "Charles", "Angel")));
```

<code>Reducible</code> is useful for summing over a collection, e.g.

```java
    assertThat(newArrayList(20, 30, 50), Reducible.with(Reducers.sumIntegers).to(100));
```

Sometimes we care about the sort order of a collection without especially caring what's in it:

```
    assertThat(someCollection, Sorted.inAscendingOrder());
    assertThat(someOtherCollection, Sorted.inDescendingOrder());
    assertThat(someWackyCollection, Sorted.with(wackyComparator, "wacky"));
```

Redescribing matchers
---------------------

Matchers build up from other matchers sometimes end up with rather unreadable descriptions / mismatch descriptions. It can be helpful to wrap the top-level matcher with a new description and mismatch describer.

```java
    @Test public void
    can_replace_mismatch_description_with_simple_text() {
        Matcher<? super String> containsTheMagicWord = Redescribe.theMatcher(containsString("please"))
                                                                 .as("a polite string")
                                                                 .describingMismatchAs("an impolite string");
        
        assertThat(containsTheMagicWord,
                   a_matcher_with_description("a polite string"));
        
        assertThat(containsTheMagicWord,
                   a_matcher_that_matches("please match this string"));
        
        assertThat(containsTheMagicWord,
                   a_matcher_giving_a_mismatch_description_of("match this string or else",
                                                              containsString("\"match this string or else\" was an impolite string")));
    }
'''

The mismatch describer may be a simple string, or may be an instance of <code>MosmatchDescriber</code> which takes the mismatched item and populates the mismatch description.
