package com.sidemash.redson;

import java.util.*;
import java.util.function.Supplier;

public class Main {

    public static void main(String[] args) {
/*
        JsonValue jsValue =
                JsonObject.of("user",
                        JsonObject.of("age",  JsonNumber.of(10))
                );
*/
        JsonValue jsValue1 =
                JsonArray.of(JsonNumber.of(10), JsonObject.of("age",  JsonNumber.of(10)), JsonString.of("Serge"));

        System.out.println(jsValue1.prettyStringify());
        System.out.println();
        System.out.println();
        JsonObject jsValue2 = JsonObject.EMPTY;

        Object[] array = new Object[]{ "Alice", "Jerome"};



        List<List<Map<String, Object>>> listOfString = new ArrayList<>();
        List<Map<String, Object>> l2 = new ArrayList<>();
        listOfString.add(l2);

        Map<String, Object> mapOfString = new HashMap<>();
        mapOfString.put("1", Optional.empty());
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
/*
        JsonObject jsValue3 =
                JsonObject.of(
                        JsonObject.of("age", 1),
                        JsonObject.of("naming", "Serge"),
                        JsonObject.of("friends", new TestValue()) // Homogeneous List
                );

*/
        System.out.println(JsonValue.of(listOfString).prettyStringify());
        TestValue.TestValue2 test = new TestValue.TestValue2(12, "Cailloux");
        synchronized (test) {
            timedOperation(() -> {
                System.out.println(JsonValue.of(new TestValue.TestValue3(13, "pains")).prettyStringify());
                //System.out.println(JsonValue.of(test).asPojo(TestValue.TestValue2.class).equals(test));
                //System.out.println(JsonValue.of(test).prettyStringify());
                return null;
            });
        }



        // System.out.println(Json.fromJsonValue(jsValue3)); //.prettyStringify());
        // Cannot transform nested classes to Json by reflexion
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


    public static class TestValue {
        public static int serge = 2;
        public char c = 'e';
        public String martial = "Rer";


        public static class TestValue3 extends TestValue2 {
            public String janvier = "5 Janvier 2014";
            public TestValue3(int steph, String martial) {
                super(steph, martial);
            }
        }
        public static class TestValue2 extends TestValue {
            public int steph = 8;
            public String martial = "5";

            public TestValue2(int steph, String martial) {
                this.steph = steph;
                this.martial = martial;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                TestValue2 that = (TestValue2) o;

                if (steph != that.steph) return false;
                return martial.equals(that.martial);
            }

            @Override
            public int hashCode() {
                int result = steph;
                result = 31 * result + martial.hashCode();
                return result;
            }

            @Override
            public String toString() {
                return "TestValue2{" +
                        "steph=" + steph +
                        ", martial='" + martial + '\'' +
                        '}';
            }

            static {
                Json.registerWriter(TestValue2.class, (TestValue2 o, JsonValue js) -> {
                    System.out.println("I have been callled");
                    return JsonObject.EMPTY;
                });
                Json.registerReaderFromJson(TestValue2.class, (JsonValue js) ->
                                new TestValue2(js.get("steph").asInt(), js.get("martial").asString())
                );
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
