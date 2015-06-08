# Redson
A lightweight library to handle json for Java 8 with simplicity and functional style.

## What is Redson ? 
Redson is a new Java 8 Json library that aims to handle Json in the way that : 
- Help removing `null` from the earth
- Simplify and reduce boilerplate whent convertiing to and from Json even with complex rules that describes these conversions
- Handle and manipulate Json as it was a first class type
- Introduce a new JsonOptional to handle missing fields : Very useful for example to modelize the difference between a submitted null value and a missing one.
- Reuse internally the carefully tested Scala immutable collection API. 

## What is not Redson ? 
- Not an implementation of [JSR  353](https://jcp.org/en/jsr/detail?id=353)
Highly inspired from this JSR, we unfortunately decided to not follow this specification because of the functionnal stae of our API. for example, our JsonArray does not implements List interface because we would like to provide an immutable data structure. Hence some methods signature were uncompatible. for exemple, lets see the remove method : 
```java
// Let suppose we have an List<E> from where we want to remove the element at the specified position in this list

// in Java List interface this is the signature of the remove operation
E remove(int index)

// in an immutable List, this would be the signature of the remove operation
List<E> remove(in index)
```
Clearly, these 2 signatures clashes! So we choosen not to implement the List interface and picked the second signature. This choice of making our JsonArray not implementing the List interface among others de facto made redson not follow this JSR : We took inspiration from it and many of redson methods names were directly inspired by this JSR. 


## Quickstart

### The Json*  family
There are 7 Main Json* classes and there are : 
JsonValue, JsonArray, JsonObject, JsonNumber, JsonString, JsonOptional, JsonEntry<K>, JsonNull(only here for compatiblity reasons, we strongly encourage to avoid null in your models).

### Creating Json in Java
If you don't know the type of the variable (E.g. when using generics), or if you want rapid prototyping and test, you can use the `JsonValue.of()` factory method.
Because every Json* inherits from JsonValue, you can create all of these via JsonValue. Some examples : 
```java
JsonValue.of(1)                         // -> JsonNumber(1)
JsonValue.of("Hello world")             // -> JsonString.of("Hello world")
JsonValue.of(new ArrayList<Integer>())  // -> JsonArray.EMPTY
JsonValue.of(Optionnal.empty())         // -> JsonOptionnal.EMPTY
JsonValue.of(Optionnal.of(12L))         // -> JsonOptionnal.of(JsonNumber(12L))

// Assuming Person is an object whith a constructor taking a String as name
JsonValue.of(new Person("John DOE")) // -> JsonObject
```

#### Creating JsonArray
We said that this library will be a simple one : Look at this (these result on the right are the result of calling the method `stringify()` that convert the JsonValue on the left to its string representation, method removed for clarity).
```java
JsonArray.of(1)                         // -> [1]
JsonArray.of(1,2,3)                     // -> [1, 2, 3]
JsonArray.of(IntStream.range(0,7))      // -> [0, 1, 2, 3, 4, 5, 6]
```
Simple Right ?  
Let's complicate thing. Since in JSON, it allowed to have mixed element in array, you can create a such array
```java
JsonArray.of(1,2,true)                                // -> [1, 2, true]
JsonArray.of(1,2,true, new HashMap<String, Long>() )  // -> [1, 2, {}]

// Creating JsonArray from java List
List<String> list = new ArrayList<>();
list.add("Hello");
list.add("World");
JsonArray.of(list)  // -> ["Hello", "World"]

// Its possible to create a JsonArray from Java Array, Iterable, Iterator and Stream
```

#### Creating JsonObject
JsonObject is the default choice for representing in Json any Object and all classes that implements Java Map<K,V> interface. 
A simple example to begin
```java
JsonObject.of("greet", JsonString.of("Hello World"))  // -> { "greet" : "Hello World" }

// This example can be written more consisely as follow : 
JsonObject.of("greet", "Hello World")                // -> { "greet" : "Hello World" }
```
A more complicated example, look at the following Json object
```javascript
{
    "name" : "John Doe",
    "age"  : 99
}
```
```javascript
{
   "user" : {
      "name" : "John Doe",
      "age"  : 99
   }
}
```
We will construct these following Json with redson in a simply way
```java
JsonObject.of( 
     JsonObject.of("name", JsonString.of("John Doe")),
     JsonObject.of("age", JsonNumber.of(99))
)
```
```java
JsonObject.of("user", 
     JsonObject.of("name", JsonString.of("John Doe")),
     JsonObject.of("age", JsonNumber.of(99))
)
 


// You can also construct an JsonObject from a Map<String, ?>. If the key is not a string(E.g. you pass 
// an instance of Map<Integer, YourCustomClass> to the JsonObject.of() function ), automatically 
// the toString() method will be called on the key transform the map into a Map<String, YourCustomClass>
```


#### Creating JsonNumber, JsonString and JsonOptionnal
You can create JsonNumber from : Short, Byte, Integer, Long, Float, Double, BigDecimal, BigInteger and String(if and only this String contains number)

You can create JsonString from : String, Char and CharSequence

JsonOptionnal
Whenever you are dealing with JsonOptionnal, remember that JsonOptionnal contains optionnally an instance of JsonValue. Hence, when you create an instance of JsonOptionnal from an Optionnal, the value if any will be converted ( E.g; : a String will become JsonString etc . ) and stored in the JsonOptionnal.


### Traversing JsonValue
Let's assume we have this JSON 
```javascript
[
    {
       "user" : {
          "name" : "John Doe",
          "age"  : 99
       }
    }
]
```
If we were to manipulate it in java with redson, we will do the following : 
```java
JsonValue jsonValue  // Assume this variable contains the previous Json
jsonValue.get(0).get("user").get("age").asInt()     // -> 99
jsonValue.get(0).get("user").get("name").asString() // -> "John Doe"

jsonValue.get(0).get("user").get("name").asInt()    // -> ClassCastException
jsonValue.get(0).get("user").get("unknownKey")      // -> NoSuchElementException 
```
### Converting Java to JsonValue

### Converting JsonValue to Java

### Main methods

#### as* methods

#### if* methods

#### get* methods

#### stringify() methods

#### Functionals methods

#### Streams & Iterators methods


