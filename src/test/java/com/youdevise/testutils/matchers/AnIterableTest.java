package com.youdevise.testutils.matchers;

import java.util.Collection;
import java.util.Collections;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import static com.google.common.collect.Lists.newArrayList;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_giving_a_mismatch_description_of;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_that_matches;
import static com.youdevise.testutils.matchers.MatcherMatcher.a_matcher_with_description;

public class AnIterableTest {

    public static class TestClass { }
    public static class TestSubClass extends TestClass { }
    
    @Test public void
    matches_an_iterable_containing_instances_of_the_given_type() {
        Collection<String> collection = Lists.newArrayList();
        collection.add("A string");
        
        assertThat(AnIterable.of(String.class),
                   a_matcher_that_matches(collection));
        
        assertThat(AnIterable.of(String.class),
                   a_matcher_with_description(String.format("an iterable of %s", String.class.getName())));
    }
    
    @Test public void
    matches_an_untyped_iterable_containing_instances_of_the_given_type() {
        Collection<Object> collection = Lists.newArrayList();
        collection.add("A string");
        
        assertThat(AnIterable.of(String.class),
                   a_matcher_that_matches(collection));
        
        assertThat(AnIterable.of(String.class),
                   a_matcher_with_description(String.format("an iterable of %s", String.class.getName())));
    }
    
    @Test public void
    matches_an_iterable_typed_to_superclasses_of_the_given_type() {
        TestSubClass item1 = new TestSubClass();
        TestSubClass item2 = new TestSubClass();
        Collection<TestClass> collection = ImmutableList.<TestClass>builder().add(item1).add(item2).build();
        
        assertThat(AnIterable.of(TestSubClass.class).which(Contains.inOrder(item1, item2)),
                   a_matcher_that_matches(collection));
        
        assertThat(AnIterable.of(TestSubClass.class).which(Contains.inOrder(item1, item2)),
                   a_matcher_with_description(containsString(String.format("an iterable of %s", TestSubClass.class.getName()))));
    }
    
    @Test public void
    matches_an_iterable_typed_to_subclass_of_the_given_type() {
        TestSubClass item1 = new TestSubClass();
        TestSubClass item2 = new TestSubClass();
        Collection<TestSubClass> collection = ImmutableList.of(item1, item2);
        
        TestClass match1 = item1;
        TestClass match2 = item2;
        
        assertThat(AnIterable.of(TestClass.class).which(Contains.inOrder(match1, match2)),
                   a_matcher_that_matches(collection));
    }
    
    @Test public void
    fails_to_match_if_the_iterable_contains_any_objects_of_incompatible_types() {
        Collection<Object> collection = Lists.newArrayList();
        collection.add(42);
        
        assertThat(AnIterable.of(String.class),
                   a_matcher_giving_a_mismatch_description_of(collection,
                       containsString("not all elements were instances of java.lang.String")));
    }
    
    @Test public void
    fails_to_match_if_the_iterable_contains_any_nulls() {
        Collection<Object> collection = Lists.newArrayList();
        collection.add(null);
        
        assertThat(AnIterable.of(String.class),
                   a_matcher_giving_a_mismatch_description_of(collection,
                       containsString("not all elements were instances of java.lang.String")));
    }
    
    @Test public void
    matcher_can_be_extended_with_a_condition() {
        Collection<String> collection = ImmutableList.of("eenie", "meanie", "minie", "moe");
                
        assertThat(AnIterable.of(String.class).which(Contains.theItem("moe")),
                   a_matcher_that_matches(collection));
        
        assertThat(AnIterable.of(String.class).which(Contains.theItem("moe")),
                   a_matcher_with_description(
                       containsString("an iterable of java.lang.String matching (a collection containing \"moe\")")));
    }
    
    @Test public void
    matcher_fails_if_condition_fails() {
        Collection<String> collection = ImmutableList.of("eenie", "meanie", "minie");
        
        assertThat(AnIterable.of(String.class).which(Contains.theItems("eenie", "meanie", "minie", "moe")),
                   a_matcher_giving_a_mismatch_description_of(collection, containsString("moe")));
        
    }
    
    @Test public void
    detects_duplicates() {
        assertThat(AnIterable.of(String.class).withoutDuplicates(),
                   a_matcher_that_matches(Collections.<String>emptyList()));
        
        assertThat(AnIterable.of(String.class).withoutDuplicates(),
                   a_matcher_that_matches(ImmutableList.of("fee", "fie", "foe", "fum")));
        
        assertThat(AnIterable.of(String.class).withoutDuplicates(),
                   a_matcher_giving_a_mismatch_description_of(ImmutableList.of("eenie", "meanie", "minie", "meanie"),
                           containsString("contained duplicates")));
    }
    
    @Test public void
    detects_emptiness() {
        assertThat(AnIterable.of(String.class).withoutContents(),
                   a_matcher_that_matches(Collections.<String>emptyList()));
        
        assertThat(AnIterable.of(String.class).withoutContents(),
                   a_matcher_giving_a_mismatch_description_of(ImmutableList.of("fee", "fie", "foe", "fum"),
                        containsString("was not empty")));
    }
    
    @Test public void
    in_sorted_order_tests_order_of_comparables() {
        assertThat(AnIterable.of(Integer.class).inAscendingOrder(),
                   a_matcher_that_matches(newArrayList(1,2,3,5,8,13,21)));
        
        assertThat(AnIterable.of(Integer.class).inAscendingOrder(),
                   a_matcher_giving_a_mismatch_description_of(ImmutableList.of(1,2,3,5,8,21,13),
                           containsString("an iterable in ascending order was not in order")));
    }
    
    @Test public void
    example_for_documentation() {
        Collection<Object> aCollectionOfFineMusicians = newArrayList();;
        aCollectionOfFineMusicians.add("Lee");
        aCollectionOfFineMusicians.add("Lifeson");
        aCollectionOfFineMusicians.add("Peart");
        
        assertThat(aCollectionOfFineMusicians, AnIterable.of(String.class).which(Contains.inAnyOrder("Peart", "Lee", "Lifeson")));
        assertThat(aCollectionOfFineMusicians, AnIterable.of(String.class).inAscendingOrder());
        assertThat(aCollectionOfFineMusicians, AnIterable.of(String.class).withoutDuplicates());
        
        aCollectionOfFineMusicians.clear();
        
        assertThat(aCollectionOfFineMusicians, AnIterable.of(String.class).withoutContents());
    }
}
