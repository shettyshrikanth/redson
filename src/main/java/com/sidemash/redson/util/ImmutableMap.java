package com.sidemash.redson.util;


import com.sidemash.redson.JsonEntry;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public interface ImmutableMap<K,V>  {


    <U> U foldLeft(U seed, BiFunction<U, JsonEntry<String>, U> op);

    V get(String key);

    JsonEntry<String> getHead();

    JsonEntry<String> getLast();

    ImmutableMap<K,V> getTail();

    boolean isDefinedAt(String key);

    Iterator<JsonEntry<String>> iterator();

    Iterator<K> keysIterator();

    Stream<K> keysStream();

    /*

    default  <U> U foldRight(U seed, BiFunction<JsonEntry<String>, U, U> op) {
        // Since parameters are reversed for 'op' function ( in foldLeft and foldRight )
        // we need to create an adapter function if we want to reuse foldLeft implementation
        BiFunction<U, JsonEntry<String>, U> adapterFunc = (acc, value) -> op.apply(value, acc);

        return this.reverse().foldLeft(seed,adapterFunc);
    }

    ImmutableMap<K,V>  reverse();



    ImmutableMap<K,V> updateWhile(Predicate<? super T> predicate, T elem);
    ImmutableMap<K,V> updateWhile(Predicate<? super T> predicate, UnaryOperator<T> elem);

    ImmutableMap<K,V> updateFirst(Predicate<? super T> predicate, T elem);
    ImmutableMap<K,V> updateFirst(Predicate<? super T> predicate, UnaryOperator<T> elem);

    ImmutableMap<K,V> updateWhere(Predicate<? super T> predicate, T elem);
    ImmutableMap<K,V> updateWhere(Predicate<? super T> predicate, UnaryOperator<T> elem);

    String firstKeyWhere(Predicate<? super T> predicate);
    String firstKeyWhere(Predicate<? super T> predicate, int from);

    Set<String> keysWhere(Predicate<? super T> predicate);

    String keyOf(T elem);

    ImmutableMap<K,V> filterNot(Predicate<? super JsonEntry<K>> predicate);

    ImmutableMap<K,V> filter(Predicate<? super JsonEntry<K>> predicate);

    ImmutableMap<K,V> skip(int nb);

    ImmutableMap<K,V> skipRight(int nb);

    ImmutableMap<K,V> limit(int nb);

    ImmutableMap<K,V> limitRight(int nb);

    ImmutableMap<K,V> skipWhile(Predicate<? super JsonEntry<K>> predicate);

    ImmutableMap<K,V> limitWhile(Predicate<? super JsonEntry<K>> predicate);
*/
    ImmutableMap<K,V> sorted(Comparator<? super JsonEntry<K>> comparator);

    ImmutableMap<K, V> updateEntry(String oldEntryKey, JsonEntry<String> newEntry);

    ImmutableMap<K, V> updateEntry(String oldEntryKey, UnaryOperator<JsonEntry<String>> operator);

    ImmutableMap<K, V> updateKey(String oldKey, String newKey);

    ImmutableMap<K, V> updateKey(String oldKey, UnaryOperator<String> operator);

    ImmutableMap<K, V> updateValue(String key, V elem);

    Iterator<V> valuesIterator();

}
