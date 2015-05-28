package com.sidemash.redson;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Main {

    public static void main(String[] args) {

        JsonValue jsValue =
                JsonObject.of("user",
                        JsonObject.of("age",  JsonNumber.of(10))
                );

        JsonValue jsValue1 =
                JsonArray.of(JsonNumber.of(10), JsonObject.EMPTY, JsonNumber.of(10));

        JsonObject jsValue2 = JsonObject.EMPTY;

        Object[] array = new Object[]{ "Alice", "Jerome"};



        List<List<Map<BigInteger, Object>>> listOfString = new ArrayList<>();
        List<Map<BigInteger, Object>> l2 = new ArrayList<>();
        listOfString.add(l2);

        Map<BigInteger, Object> mapOfString = new HashMap<>();
        mapOfString.put(new BigInteger("1"), "Beau");
        l2.add(mapOfString);
        /*
        List<String> s = new ArrayList<>();
        s.add("MArtial");
        listOfString.add(new ArrayList<>());
        listOfString.add(s);*/
        //  JsonObject.of("friends", mapOfString) convert mapString into Map<String, Object> by calling toString() on the Key
        // and will return "friends":{ "1":"Beau" }
        // if you want to override this behaviour, write JsonObject.of("friends", JsonObject.of(mapString, intKey -> String.valueOf(intKey-1))
        // and will return "friends":{ "0":"Beau" }
        JsonObject jsValue3 =
                JsonObject.of(
                        JsonObject.of("age", 1),
                        JsonObject.of("naming", "Serge"),
                        JsonObject.of("friends", new TestValue()) // Homogeneous List
                );

            timedOperation(() -> {
                System.out.println(jsValue1.prettyStringify());
                return null;
            });


        // System.out.println(Json.fromJsonValue(jsValue3)); //.prettyStringify());
        // Implements monadic methods on JsonValue
        // Give sense to all Exceptions
        // Implements equals and hashcode
        // Test
        // Document
        // Implement ordered converter list Many Converetr for exemple for String
        // Implement converter for date
        // Handle conversion to List Map, Arrays OK
        // Handle conversion of recursive Parent classes OK
        // Handle conversion of recursive data structures ( List<List<String>>, List<List<Map<String, List<String>>>> ) OK
    }


    public static class TestValue{
        public static int serge = 2;
        public String martial = "Rer";


        public static class TestValue2 extends TestValue {
            public int steph = 8;
            public String martial = "5";

            static {
                Json.registerWriter(TestValue2.class, (o, js) -> JsonNumber.of(o.steph));
            }
        }
    }



    public static void timedOperation(Supplier<Void> fn){
        long startTime = System.nanoTime();
        fn.get();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Total time : " + (duration/1000000 ) + "ms");
    }
}
