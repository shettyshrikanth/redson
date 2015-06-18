package com.sidemash.redson;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        /*
        JsonValue jsValue1 =
                JsonArray.of(JsonNumber.of(10), JsonObject.of("age",  JsonNumber.of(10)), JsonString.of("Serge"));

        System.out.println(jsValue1.prettyStringify());
        System.out.println();
        System.out.println();
        JsonObject jsValue2 = JsonObject.EMPTY;

        Object[] array = new Object[]{"Alice", "Jerome"};



        List<List<Map<String, Employee>>> listOfString = new ArrayList<>();
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


        System.out.println("Couu ->>>>>>>>>>>>>>>>>>>>>>>>><");
        System.out.println(
                JsonObject.of(new Person("Serge", 24, 1.90F)).stringify()
        );System.out.println(
                JsonObject.of(new Person(null, null, null)).stringify()
        );
        Tree<Person> tree =
                new Tree<>(new Person("Serge", 24, 1.90F));
        System.out.println(JsonValue.of(tree).stringify());
        System.out.println();

          */

        JsonValue value = JsonValue.of(Optional.of(new Person("Serge", 24, 1.90F)));

        Tree<Person> tree =  new Tree<>(new Person("Serge", 24, 1.90F), Arrays.asList(
                new Tree<>(new Person("Serdddse", 24, 1.90F)),
                new Tree<>(new Person("Serge", 2324, 1.90F)),
                new Tree<>(new Person("Martial", 124, 1.90F))
                ));

        List<List<Tree<Person>>> list = new ArrayList<>();
            List<Tree<Person>> innerList = new ArrayList<>();
            innerList.add(tree);
         list.add(innerList);

        /*
        list.add(new Person("Nguetta", 4, 18.90F));
        list.add(new Person("Kablan", 214, 61.90F));*/

        JsonValue val = JsonArray.of(list);
        List<List<Tree<Person>>> myList = val.as(new TypeReference<List<List<Tree<Person>>>>() {});


        JsonValue jvalue = JsonArray.of(1,2,3,4,5);
        List<Integer> intList = jvalue.asListOf(Integer.class);

        System.out.println(jvalue.asListOf(Integer.class));


        /*
        System.out.println(myList);*/

        // System.out.println(JsonObject.of(tree).prettyStringify());
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

}
