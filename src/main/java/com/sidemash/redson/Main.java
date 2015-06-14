package com.sidemash.redson;

import java.io.CharArrayReader;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {

        JsonValue jsValue =
                JsonObject.of("user",
                        JsonObject.of("age",  JsonNumber.of(10))
                );

        /*
        JsonValue jsValue1 =
                JsonArray.of(JsonNumber.of(10), JsonObject.of("age",  JsonNumber.of(10)), JsonString.of("Serge"));

        System.out.println(jsValue1.prettyStringify());
        System.out.println();
        System.out.println();
        JsonObject jsValue2 = JsonObject.EMPTY;

        Object[] array = new Object[]{"Alice", "Jerome"};



        List<List<Map<String, Object>>> listOfString = new ArrayList<>();
        List<Map<String, Object>> l2 = new ArrayList<>();
        listOfString.add(l2);

        Map<String, Object> mapOfString = new HashMap<>();
        mapOfString.put("1", Optional.empty());
        l2.add(mapOfString);

        List<String> s = new ArrayList<>();
        s.add("MArtial");
        listOfString.add(new ArrayList<>());
        listOfString.add(s);
        //  JsonObject.of("friends", mapOfString) convert mapString into Map<String, Object> by calling toString() on the Key
        // and will return "friends":{ "1":"Beau" }
        // if you want to override this behaviour, write JsonObject.of("friends", JsonObject.of(mapString, intKey -> String.valueOf(intKey-1))
        // and will return "friends":{ "0":"Beau" }

        JsonObject jsValue3 =
                JsonObject.of(
                        JsonObject.of("age", 1),
                        JsonObject.of("naming", "Serge"),
                        JsonObject.of("friends", null) // Homogeneous List
                );

        List<String>  list = new ArrayList<>();

        System.out.println(jsValue3.prettyStringify());
        System.out.println(
                JsonArray.of(IntStream.range(1, 7))
                        .streamValues()
                        .mapToInt(JsonValue::asInt)
                        .max()
        );


        System.out.println(
                JsonArray.of(IntStream.range(1, 7)).reverse().stringify()
        );
        */

       // System.out.println(JsonArray.of(1, 2).stringify());
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", "Serge");
            map.put("pseudo", "The Elephant");

        List<List<Map<String, Person>>> listOfString = new ArrayList<>();
            List<Map<String, Person>> l2 = new ArrayList<>();
            listOfString.add(l2);
                Map<String, Person> mapOfString = new HashMap<>();
                mapOfString.put("1",new Person("Serge", 24,1.90F));
                l2.add(mapOfString);

        List<List<Integer>> li = new ArrayList<>();
        li.add(new ArrayList<>());
        System.out.println(
                JsonObject.of(new Person("Serge", 24, 1.90F)).prettyStringify()
        );
        System.out.println(
                JsonObject.of(new Person("Serge", 24, 1.90F)).remove("name")//.asListOf(List.class)
        );



        // Cannot transform nested classes to Json by reflexion
        // Implements monadic methods on JsonValue DONE with STREAM
        // Give sense to all Exceptions
        // Implements equals and hashcode OK
        // Test
        // Document
        // Implement ordered converter list, Many Converetr for exemple for String OK
        // Implement converter for date
        // Handle conversion to List Map, Arrays OK
        // Handle conversion of recursive Parent classes OK
        // Handle conversion of recursive data structures ( List<List<String>>, List<List<Map<String, List<String>>>> ) OK
    }


    public static void timedOperation(Supplier<Void> fn){
        long startTime = System.nanoTime();
        fn.get();

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Total time : " + (duration/1000000 ) + "ms");
    }
}
