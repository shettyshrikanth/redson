package com.sidemash.redson.util;


import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public interface ImmutableVector<T>  {

    ImmutableVector<T> distinct();

    ImmutableVector<T> filter(Predicate<? super T> predicate);

    ImmutableVector<T> filterNot(Predicate<? super T> predicate);

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

    ImmutableVector<T> limit(int nb);

    ImmutableVector<T> limitRight(int nb);

    ImmutableVector<T> reverse();

    ImmutableVector<T> skip(int nb);

    ImmutableVector<T> skipRight(int nb);

      /*
    ImmutableVector<T> updateWhile(Predicate<? super T> predicate, T elem);

    ImmutableVector<T> updateWhile(Predicate<? super T> predicate, UnaryOperator<T> operator);

    ImmutableVector<T> updateFirst(Predicate<? super T> predicate, T elem);

    ImmutableVector<T> updateFirst(Predicate<? super T> predicate, UnaryOperator<T> operator);




    int firstIndexOf(T elem);
    int firstIndexOf(T elem, int from);

    List<Integer> indexOf(T elem);
    List<Integer> indexOf(T elem, int from);

    */
      ImmutableVector<T> slice(int from, int until);

    ImmutableVector<T> sorted(Comparator<? super T> comparator);

    ImmutableVector<T> updateValue(int index, T elem);


}
