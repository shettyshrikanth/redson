# Redson
A lightweight library to handle json for Java 8 with simplicity and functional style.

## What is Redson ? 
Redson is a new Java 8 Json library that aims to handle Json in the way that : 
- Help removing `null` from the earth ( see [Avoiding null in your Java code](http://www.oracle.com/technetwork/articles/java/java8-optional-2175753.html) )
- Simplify and reduce boilerplate when converting to/from Json even with complex rules that describe these conversions
- Handle and manipulate Json as it was a first class data type
- Introduce a new JsonOptional to handle missing fields : Very useful for example to modelize the difference between a submitted null value and a missing one.
- Reuse internally the carefully tested Scala immutable collection API. 

## What is not Redson ? 
- Redson is Not an implementation of [JSR  353](https://jcp.org/en/jsr/detail?id=353)

Highly inspired from this JSR, we unfortunately decided to not follow this specification because of the functionnal state of our API. for example, our JsonArray does not implements List interface because we would like to provide an immutable data structure, Hence some methods signature were uncompatible between List and functionnal list. For exemple, lets see the remove method : 
```java
// Let suppose we have an List<E> from where we want to remove the element at the specified position

// in Java List interface, this is the signature of the remove operation
E remove(int index)

// in an immutable List, this would be the signature of the remove operation
// Remove the element and return a new List without the element at index passed
// as parameter.
List<E> remove(int index)
```
Clearly, these 2 signatures clashe on return type and we have to choose one! So we choosen not to implement the List interface and picked the second signature. This choice of making our JsonArray not implementing the List interface among others, de facto made redson not to follow this JSR. However, we took inspiration from it and many of redson methods names were directly inspired by this JSR. 


## Quickstart

### The Json*  family
According to [RFC 7159](https://tools.ietf.org/html/rfc7159#section-3), values in Json must be : array, object, number, string, false, true or  null.
Hence, we have defined 6 classes to represent Json values in Java. These are :  `JsonArray`, `JsonObject`, `JsonNumber`, `JsonString`, `JsonNull` and `JsonBoolean`.
Additionnally, we have defined `JsonOptional` to model a possible missing JsonValue, we have defined `JsonValue` interface wich is the supertype of all previous Json* classes and finally we have defined the class `JsonEntry` which is either a pair `(String, JsonValue)` that indexes JsonObject values by key or the pair `(Integer, JsonValue)` that indexes JsonArray values by index. (In fact, JsonEntry is a parametrized class defined as JsonEntry<T>  with T being a String or an Integer)

### Creating Json in Java
If you don't know the type of the variable (E.g. when using generics), or if you want rapid prototyping and test, you can use the `JsonValue.of()` factory method.
Because every Json* inherits from JsonValue, you can create all of these via JsonValue. Some examples : 
```java
JsonValue.of(1)                         // -> JsonNumber(1)
JsonValue.of("Hello world")             // -> JsonString("Hello world")
JsonValue.of(new ArrayList<Integer>())  // -> JsonArray.EMPTY
JsonValue.of(Optionnal.empty())         // -> JsonOptionnal.EMPTY
JsonValue.of(Optionnal.of(12L))         // -> JsonOptionnal(JsonNumber(12L))

// Assuming Person is an object whith a constructor taking a name as String
JsonValue.of(new Person("John DOE")) // -> JsonObject
```

#### Creating JsonArray
We have said that this library will be a simple one : Look at this (these result on the right are the result of calling the method `stringify()` that convert the JsonValue on the left to its string representation, method removed for clarity).
This is how you create an array
```java
JsonArray.of(1)                         // -> [1]
JsonArray.of(1,2,3)                     // -> [1, 2, 3]
```
Simple Right ?  
Let's complicate thing. Since in JSON, it allowed to have mixed element in array, you can create a such array
```java
JsonArray.of(1,2,true)                                // -> [1, 2, true]
JsonArray.of(1,2,true, new HashMap<String, Long>() )  // -> [1, 2, true, {}]

// Creating JsonArray from java List
List<String> list = new ArrayList<>();
list.add("Hello");
list.add("World");
JsonArray.of(list)  // -> ["Hello", "World"]

// Its possible to create a JsonArray from Java Array, Iterable, Iterator and Stream
JsonArray.of(IntStream.range(0,7))      // -> [0, 1, 2, 3, 4, 5, 6]
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
     JsonEntry.of("name", JsonString.of("John Doe")),
     JsonEntry.of("age", JsonNumber.of(99))
)
```
```java
JsonObject.of( 
    JsonEntry.of("user", JsonObject.of(
        JsonEntry.of("name", JsonString.of("John Doe")),
        JsonEntry.of("age", JsonNumber.of(99))
    )    
)
 


// You can also construct an JsonObject from any Map<String, V>
Map<String, Integer> lettersToDigits = new HashMap<>();
lettersToDigits.put("one", 1);
lettersToDigits.put("two", 2);
JsonObject.of(lettersToDigits);             // -> { "one" : 1, "two" : 2 }


Map<String, YourCustomClass> yourMap =   // Initialize this map the way you want to
JsonObject.of(yourMap);             

// If the key is not a string(E.g. you pass an instance of Map<Integer, YourCustomClass> or even 
// an instance of Map<YourCustomClass1, YourCustomClass2> to the JsonObject.of() ), you will have to provide a way
// to convert the YourCustomClass1 to a String

// Assume that we want to convert this Map to a JsonObject.
Map<Integer, Char> ascii = new HashMap<>();
ascii.put(65, 'A');
ascii.put(66, 'B');
ascii.put(67, 'C');

// Here we can't call JsonObject.of(ascii) because the ascii is not an Map indexed by String.
// We need to provide a way to convert the keys to String. So pass a lambda to convert Integer to String
JsonObject.of(ascii, num -> String.valueOf(num))     // -> { "65":"A", "66":"B", "67":"C" }

// If the key were an instance of YourCustomClass, all you have to do is to give a function that takes 
// an instance of YourCustomClass and return a String you want to see as key of your JsonObject.

```


#### Creating JsonNumber, JsonString and JsonOptional
You can create `JsonNumber` from : Short, Byte, Integer, Long, Float, Double, BigDecimal, BigInteger and String ( if and only if this String contains a valid number )

You can create `JsonString` from : String, Char and CharSequence

`JsonOptionnal`
Whenever you are dealing with JsonOptionnal, remember that JsonOptionnal contains optionnally an instance of JsonValue. Hence, when you create an instance of JsonOptionnal from an Optional, the value if any will be converted ( E.g; : a String will become JsonString etc . ) and stored in the JsonOptionnal.
The JsonOptional has severals methods of the Java Optional, so it is possible to manipulate as if it was an `Optional<JsonValue>`.


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
JsonArray array  // Assume this variable contains the previous Json

array.get(0).get("user").get("age").asInt()                 // -> 99
array.getHead().get("user").get("age").asInt()              // -> 99
array.get(0).get("user").get("name").asString()             // -> "John Doe"

array.get(0).get("user").get("name").asInt()                // -> ClassCastException
array.get(0).get("user").get("name").asIntOptional()        // -> Optional.empty()
array.get(0).get("user").get("name").asIntOrDefault(-1)     // -> -1
array.get(0).get("user").get("name").asString()             // -> "John Doe"
array.get(0).get("user").get("name").asStringOptional()     // -> Optional.of("John Doe")
array.get(0).get("user").get("name").asStringOrDefault("")  // -> "John Doe"

array.get(0).get("user").get("unknownKey")                  // -> NoSuchElementException 
array.get(0).get("user").getOptional("unknownKey")          // -> Optional.empty()
array.get(0).getOrDefault("unknownKey", "defaultValue")     // -> "defaultValue"
```



(Still In Progress)

### Converting Java to JsonValue

### Converting JsonValue to Java

### Main methods

#### as* methods

#### if* methods

#### get* methods

#### stringify() methods

#### Functionals methods

#### Streams & Iterators methods




## Authors
Serge Martial N.

Stephane Tankoua
