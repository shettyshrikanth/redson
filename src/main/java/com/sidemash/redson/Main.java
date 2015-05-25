package com.sidemash.redson;

import java.util.function.Supplier;

public class Main {

    public static void main(String[] args) {

        JsonValue jsValue =
                JsonObject.of("user",
                        JsonObject.of("age",  JsonNumber.of(10))
                );

        JsonValue jsValue1 =
                JsonArray.of(JsonNumber.of(10), JsonObject.EMPTY,  JsonNumber.of(10))
                ;
        JsonObject jsValue2 = JsonObject.EMPTY;

        JsonObject jsValue3 = JsonObject.of(
                                     JsonObject.of("name", JsonNumber.of(10)),
                                     JsonObject.of("age",  JsonNumber.of(10)),
                                    JsonObject.of("naming", JsonNumber.of(10)),
                                    JsonObject.of("text", JsonOptional.EMPTY)
                                );
        timedOperation(() -> { jsValue3.toString(); return null; });


        // JsonOptional.empty  -> Missing Field
        // JsonOptional.of(T)  -> field of type T
        // JsonNull   -> Field with a null value

        System.out.println(jsValue3);
    }

    public static void timedOperation(Supplier<Void> fn){
        long startTime = System.nanoTime();
        fn.get();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Total time : " + (duration/1000000 ) + "ms");
    }
}
