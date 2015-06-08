# Redson
A lightweight library to handle json for Java 8 with simplicity and functional style.

## What is Redson ? 
Redson is a new Java 8 Json library that aims to handle Json in the way that 
- Help removing null from the earth
- Simplify and reduce boilerplate whent convertiing to and from Json even with complex rules that describes these conversions
- Introduce a new JsonOptional to handle missing fields : Very useful for example to modelize the difference between a submitted null value and a missing one.
 
## What is not Redson ? 
- Not an implementation of [JSR  353](https://jcp.org/en/jsr/detail?id=353)
Highly inspired from this JSR, we unfortunately decided to not follow this specification because of the functionnal stae of our API. for example, our JsonArray does not implements List interface because we would like to provide an immutable data structure. Hence some methods signature were uncompatible. for exemple, the remove method 
```java
// Let suppose we have an List<E>
// in Java List interface 
// Removes the element at the specified position in this list
E remove(int index)

// in an immutable List
List<E> remove(in index)
```
Clearly, these 2 signatures clashe! So we have choose to not implement a List interface and picked the second second signature. This choice of not implementing an interface among others made redson not follow this JSR


## Quickstart

### The Json*  family

### Creating Json in Java

#### Creating JsonArray

#### Creating JsonObject

#### Creating JsonArray

### Converting Java to JsonValue

### Converting JsonValue to Java

### Main methods

#### as* methods

#### if* methods

#### get* methods

#### Functionals methods

#### Streams & Iterators methods


