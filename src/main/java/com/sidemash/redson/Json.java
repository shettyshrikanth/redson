package com.sidemash.redson;

import com.sidemash.redson.converter.OptionalConverter;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Json {

    private static final Map<Class<?>, Function<JsonValue, ?>>
            readersFromJson =  new HashMap<>();
    private static final Map<Class<?>, BiFunction<?, JsonValue, JsonValue>>
            writerToJson =  new HashMap<>();

    private static final Map<Class<?>, Function<JsonValue, ?>>
            usersDefinedReadersFromJson =  new HashMap<>();
    private static final Map<Class<?>, BiFunction<?, JsonValue, JsonValue>>
            usersDefinedWriterToJson =  new HashMap<>();

    static {
        // Primitives
        writerToJson.put(Boolean.class,   (Boolean value, JsonValue jsonValue)    -> JsonBoolean.of(value));
        writerToJson.put(Byte.class,      (Byte value, JsonValue jsonValue)       -> JsonNumber.of(value));
        writerToJson.put(BigDecimal.class,(BigDecimal value, JsonValue jsonValue) -> JsonNumber.of(value));
        writerToJson.put(BigInteger.class,(BigInteger value, JsonValue jsonValue) -> JsonNumber.of(value));
        writerToJson.put(Character.class, (Character value, JsonValue jsonValue)  -> JsonString.of(value));
        writerToJson.put(Double.class,    (Double value, JsonValue jsonValue)     -> JsonNumber.of(value));
        writerToJson.put(Float.class,     (Float value, JsonValue jsonValue)      -> JsonNumber.of(value));
        writerToJson.put(Integer.class,   (Integer value, JsonValue jsonValue)    -> JsonNumber.of(value));
        writerToJson.put(Long.class,      (Long value, JsonValue jsonValue)       -> JsonNumber.of(value));
        writerToJson.put(Short.class,     (Short value, JsonValue jsonValue)      -> JsonNumber.of(value));
        // String
        writerToJson.put(String.class,   (String value, JsonValue jsonValue)     -> JsonString.of(value));
        // Optional
        writerToJson.put(Optional.class,  OptionalConverter::toJsonValue);
    }


    static {
        // Primitives
        readersFromJson.put(Boolean.class,    JsonValue::asBoolean);
        readersFromJson.put(Byte.class,       JsonValue::asByte);
        readersFromJson.put(BigDecimal.class, JsonValue::asBigDecimal);
        readersFromJson.put(BigInteger.class, JsonValue::asBigInteger);
        readersFromJson.put(Character.class,  JsonValue::asChar);
        readersFromJson.put(Double.class,     JsonValue::asDouble);
        readersFromJson.put(Float.class,      JsonValue::asFloat);
        readersFromJson.put(Integer.class,    JsonValue::asInt);
        readersFromJson.put(Long.class,       JsonValue::asLong);
        readersFromJson.put(Short.class,      JsonValue::asShort);
        // String
        readersFromJson.put(String.class,     JsonValue::asString);
        // Optional
        readersFromJson.put(Optional.class,   OptionalConverter::fromJsonValue);
    }



    private Json(){}

    public static String prettyStringify(Object o) {
        JsonValue jsonValue = (o instanceof  JsonValue) ? ((JsonValue) o) : toJsonValue(o);
        return jsonValue.prettyStringify();
    }

    public static String prettyStringify(Object o, boolean keepingNull) {
        final int indent = 3;
        final boolean emptyValuesToNull = false;
        JsonValue jsonValue = (o instanceof  JsonValue) ? ((JsonValue) o) : toJsonValue(o);

        return jsonValue.prettyStringify(indent, keepingNull,emptyValuesToNull);
    }

    public static String prettyStringify(Object o, boolean keepingNull, boolean emptyValuesToNull) {
        final int indent = 3;
        JsonValue jsonValue = (o instanceof  JsonValue) ? ((JsonValue) o) : toJsonValue(o);

        return jsonValue.prettyStringify(indent, keepingNull,emptyValuesToNull);
    }

    public static String prettyStringify(Object o, int indent, boolean keepingNull){
        final boolean emptyValuesToNull = false;
        JsonValue jsonValue = (o instanceof  JsonValue) ? ((JsonValue) o) : toJsonValue(o);

        return jsonValue.prettyStringify(indent, keepingNull, emptyValuesToNull);
    }

    public static String prettyStringify(Object o, int indent, boolean keepingNull, boolean emptyValuesToNull){
        JsonValue jsonValue = (o instanceof  JsonValue) ? ((JsonValue) o) : toJsonValue(o);

        return jsonValue.prettyStringify(indent, keepingNull, emptyValuesToNull);
    }

    public static String stringify(Object o) {
        JsonValue jsonValue = (o instanceof  JsonValue) ? ((JsonValue) o) : toJsonValue(o);
        return jsonValue.stringify();
    }

    public static String stringify(Object o, boolean keepingNull) {
        final boolean emptyValuesToNull = false;
        JsonValue jsonValue = (o instanceof  JsonValue) ? ((JsonValue) o) : toJsonValue(o);

        return jsonValue.stringify(keepingNull, emptyValuesToNull);
    }

    public static String stringify(Object o, boolean keepingNull, boolean emptyValuesToNull) {
        JsonValue jsonValue = (o instanceof  JsonValue) ? ((JsonValue) o) : toJsonValue(o);

        return jsonValue.stringify(keepingNull, emptyValuesToNull);
    }

    public static<T> void registerReaderFromJson(Class<T> cl, Function<JsonValue, T> readerFn){
        usersDefinedReadersFromJson.put(cl, readerFn);
    }

    public static<T> void registerWriterToJson(Class<T> cl,BiFunction<T, JsonValue, JsonValue> writerFn){
        usersDefinedWriterToJson.put(cl, writerFn);
    }


    // By default (  defaults conversions )
    // JsonObject -> LinkedHashMap
    // JsonArray -> ArrayList
    // JsonString -> String
    // JsonNumber -> BigDecimal
    // JsonBoolean -> Boolean
    // JsonNull -> null
    // JsonOptional -> Optional
    // JsonOptional.EMPTY -> Optional.empty()



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


    public static JsonValue toJsonValue(Object o){
        if(o == null)
            return JsonNull.INSTANCE;

        // First Check for User Defined conversion rules
        BiFunction<Object, JsonValue, JsonValue> fn;
        Class<?> cl = o.getClass();
        if (usersDefinedWriterToJson.containsKey(cl)) {
            fn = (BiFunction<Object, JsonValue, JsonValue>) usersDefinedWriterToJson.get(cl);
            return fn.apply(o, JsonObject.EMPTY);
        }

        // If not, then Apply default conversion strategies
        if (writerToJson.containsKey(cl)) {
           fn = (BiFunction<Object, JsonValue, JsonValue>) writerToJson.get(cl);
        }
        else {
            if(cl.isArray())
                fn = (BiFunction<Object, JsonValue, JsonValue>) writerToJson.get(Array.class);
            else if(o instanceof Iterable)
                fn = (BiFunction<Object, JsonValue, JsonValue>) writerToJson.get(Iterable.class);
            else if(o instanceof Map)
                fn = (BiFunction<Object, JsonValue, JsonValue>) writerToJson.get(Map.class);
            else if(cl.isEnum())
                fn = (BiFunction<Object, JsonValue, JsonValue>) writerToJson.get(Enum.class);
            else
                fn = (BiFunction<Object, JsonValue, JsonValue>) writerToJson.get(Object.class);
        }
        return fn.apply(o, JsonObject.EMPTY);
    }



    public static Object fromJsonValue(JsonValue jsonValue){

        if(jsonValue.isJsonNull())
            return null;

        Function<JsonValue, Object> fn = null;
        if(jsonValue.isJsonArray())
            fn = (Function<JsonValue, Object>) readersFromJson.get(List.class);

        else if(jsonValue.isJsonBoolean())
            fn = (Function<JsonValue, Object>) readersFromJson.get(Boolean.class);

        else if(jsonValue.isJsonNumber())
            fn = (Function<JsonValue, Object>) readersFromJson.get(BigDecimal.class);

        else if(jsonValue.isJsonString())
            fn = (Function<JsonValue, Object>) readersFromJson.get(String.class);

        else if(jsonValue.isJsonObject())
            fn = (Function<JsonValue, Object>) readersFromJson.get(Map.class);

        else if (jsonValue.isJsonOptional())
            fn = (Function<JsonValue, Object>) readersFromJson.get(Optional.class);

        // At this point fn is certainly not null as all the kind of JsonObject have been
        // included in if test.
        return fn.apply(jsonValue);
    }

    public static <T> T fromJsonValue(JsonValue JsonValue, Class<T> expectedClass){

        // First Check for User Defined conversion rules
        Function<JsonValue, Object> fn;
        if(usersDefinedReadersFromJson.containsKey(expectedClass))
           fn = (Function<JsonValue, Object>) usersDefinedReadersFromJson.get(expectedClass);

        // Then Check for Default conversion rules
        else if(readersFromJson.containsKey(expectedClass))
            fn = (Function<JsonValue, Object>) readersFromJson.get(expectedClass);
        else
            throw new UnsupportedOperationException(
                    String.format("Unable to convert from JsonValue because converter " +
                    "for class %s is not registered", expectedClass)
            );

        return (T) fn.apply(JsonValue);
    }



/*
    public static JsonValue parse(String s);
    public static Object parseAsObject(String s); // Will apply defaults conversions
    public static <T> T parseAsObject(String s, Class<T> cl); // Will apply registered conversions with default fallback

    // replace Void by JsonNode
    public static Void toJsonNode(JsonValue jsonValue);
    public static Void toJsonNode(JsonValue jsonValue,  Function<JsonValue, Void> converterFn));
    public static <T> Void toJsonNode(T instance);
    public static <T> Void toJsonNode(T instance , Function<JsonValue, Void> converterFn);
*/


}
