package com.sidemash.redson;

import scala.NotImplementedError;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Json {

    private static final Map<Class<?>, Function<JsonValue, ?>>
            readersFromJson =  new HashMap<>();
    private static final Map<Class<?>, BiFunction<?, JsonValue, JsonValue>>
            writerToJson =  new HashMap<>();

    static {

        // Primitives Types
        writerToJson.put(Boolean.class,  (Boolean val, JsonValue jsonValue)    -> JsonBoolean.of(val));
        writerToJson.put(Byte.class,     (Byte val, JsonValue jsonValue)       -> JsonNumber.of(val));
        writerToJson.put(Character.class,(Character val, JsonValue jsonValue)  -> JsonString.of(val));
        writerToJson.put(Double.class,   (Double val, JsonValue jsonValue)     -> JsonNumber.of(val));
        writerToJson.put(Float.class,    (Float val, JsonValue jsonValue)      -> JsonNumber.of(val));
        writerToJson.put(Integer.class,  (Integer val, JsonValue jsonValue)    -> JsonNumber.of(val));
        writerToJson.put(Long.class,     (Long val, JsonValue jsonValue)       -> JsonNumber.of(val));
        writerToJson.put(Short.class,    (Short val, JsonValue jsonValue)      -> JsonNumber.of(val));
        writerToJson.put(String.class,   (String val, JsonValue jsonValue)     -> JsonString.of(val));

        // Class
        // FallBack Strategy -> JsonObject with All the getters

        // Enums
        // FallBack strategy -> if there is a getter same as Class strategie else  String.

        // Arrays
        // JsonArray

        // Iterable
        // JsonArray

        // Map
        // JsonObject


        // Optional
        writerToJson.put(Optional.class,(Object opt, JsonValue jsonValue) -> {
            Optional<?> val = (Optional<?>) opt;
            if (val.isPresent()) {
                BiFunction<Object, JsonValue, JsonValue> fn =
                        (BiFunction<Object, JsonValue, JsonValue>) writerToJson.get(val.get().getClass());
                return fn.apply(val.get(), JsonObject.EMPTY);
            }
            else
                return JsonNull.INSTANCE;
        });
    }

    private Json(){}

    private static String prettyStringify(JsonValue jsonValue) {
        final int indent = 3;
        final boolean keepNull = false;
        return prettyStringify(jsonValue, indent, keepNull);
    }

    private static String prettyStringify(JsonValue jsonValue, boolean keepNull) {
        final int indent = 3;
        return prettyStringify(jsonValue, indent, keepNull);
    }

    private static String prettyStringify(JsonValue jsonValue, int indent, boolean keepNull){
        return prettyStringifyRecursive(jsonValue, indent, indent, keepNull);
    }

    private static String prettyStringifyRecursive(JsonValue jsonValue, int indent, int incrementAcc, boolean keepNull){
        throw new NotImplementedError();
    }

    public static<T> void registerReaderFromJson(Class<T> cl, Function<JsonValue, T> readerFn){
        readersFromJson.put(cl, readerFn);
    }

    public static<T> void registerWriterToJson(Class<T> cl,BiFunction<T, JsonValue, JsonValue> writerFn){
        writerToJson.put(cl, writerFn);
    }

/*

    public static JsonValue parse(String s);
    public static <T> T parse(String s, Class<T> cl);

    public static String stringify(JsonValue jsonValue);
    public static String stringify(JsonValue jsonValue, Function<JsonValue, String> converterFn);
    public static <T> String stringify(T instance);
    public static <T> String stringify(T instance,  Function<T, String> converterFn);

    public static String prettyStringify(JsonValue jsonValue);
    public static String prettyStringify(JsonValue jsonValue, int indent);
    public static <T> String prettyStringify(T instance, int indent);

    // replace Void by JsonNode
    public static Void toJsonNode(JsonValue jsonValue);
    public static Void toJsonNode(JsonValue jsonValue,  Function<JsonValue, Void> converterFn));
    public static <T> Void toJsonNode(T instance);
    public static <T> Void toJsonNode(T instance , Function<JsonValue, Void> converterFn);

*/
    public static JsonValue toJsonValue(Object o){
        BiFunction<Object, JsonValue, JsonValue> fn =
                (BiFunction<Object, JsonValue, JsonValue>) writerToJson.get(o.getClass());
        return fn.apply(o, JsonObject.EMPTY);
    }

    private String jsonStringOf(JsonValue jsonValue){
        throw new NotImplementedError();
    }

}
