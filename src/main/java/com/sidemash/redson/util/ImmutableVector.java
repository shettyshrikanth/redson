package com.sidemash.redson.util;


import com.sidemash.redson.JsonValue;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public interface ImmutableVector<T>  {

    <U> U foldLeft(U seed, BiFunction<U, T, U> op);

    default  <U> U foldRight(U seed, BiFunction<T, U, U> op) {
        // Since parameters are reversed for 'op' function ( in foldLeft and foldRight )
        // we need to create an adapter function if we want to reuse foldLeft implementation
        BiFunction<U, T, U> adapterFunc = (acc, value) -> op.apply(value, acc);

        return this.reverse().foldLeft(seed,adapterFunc);
    }

    T get(int index);

    T getHead();

    T getLast();

    ImmutableVector<T> getTail();

    boolean isDefinedAt(int index);

    ImmutableVector<T> reverse();

    Iterator<T> valuesIterator();

    Stream<T> streamValues();

    ImmutableVector<T> updateValue(int index, T elem);

    ImmutableVector<T> updateValue(int index, UnaryOperator<T> elem);
/*
    ImmutableVector<T> updateWhile(Predicate<? super T> predicate, T elem);
    ImmutableVector<T> updateWhile(Predicate<? super T> predicate, UnaryOperator<T> elem);

    ImmutableVector<T> updateFirst(Predicate<? super T> predicate, T elem);
    ImmutableVector<T> updateFirst(Predicate<? super T> predicate, UnaryOperator<T> elem);

    ImmutableVector<T> updateWhere(Predicate<? super T> predicate, T elem);
    ImmutableVector<T> updateWhere(Predicate<? super T> predicate, UnaryOperator<T> elem);

    int firstIndexWhere(Predicate<? super T> predicate);
    int firstIndexWhere(Predicate<? super T> predicate, int from);

    List<Integer> indexWhere(Predicate<? super T> predicate);
    List<Integer> indexWhere(Predicate<? super T> predicate, int from);

    int firstIndexOf(T elem);
    int firstIndexOf(T elem, int from);

    List<Integer> indexOf(T elem);
    List<Integer> indexOf(T elem, int from);

    ImmutableVector<T> slice(int from, int until);
    */


    ImmutableVector<T> filterNot(Predicate<? super T> predicate);

    ImmutableVector<T> filter(Predicate<? super T> predicate);

    ImmutableVector<T> distinct();

    ImmutableVector<T> skip(int nb);

    ImmutableVector<T> skipRight(int nb);

    ImmutableVector<T> limit(int nb);

    ImmutableVector<T> limitRight(int nb);

    default ImmutableVector<T> take(int nb) {
        return limit(nb);
    }

    ImmutableVector<T> takeRight(int nb);

    ImmutableVector<T> skipWhile(Predicate<? super T> predicate);

    ImmutableVector<T> takeWhile(Predicate<? super T> predicate);

    ImmutableVector<T> sorted(Comparator<? super T> comparator);


/*
    boolean exists(Predicate<? super T> predicate);

    ImmutableVector<T> count(Predicate<? super T> predicate);


    Optional<T> max(Comparator<? super T> comparator);

    Optional<T> min(Comparator<? super T> comparator);

*/

}
