package com.sidemash.redson;

import com.sidemash.redson.converter.*;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Json {

    private static final Map<Class<?>, Function<JsonValue, ?>>
            readersFromJson =  new HashMap<>();
    private static final Map<Class<?>, BiFunction<?, JsonValue, JsonValue>>
            writersToJson =  new HashMap<>();

    private static final Map<Class<?>, Function<JsonValue, ?>>
            usersDefinedReadersFromJson =  new HashMap<>();
    private static final Map<Class<?>, BiFunction<?, JsonValue, JsonValue>>
            usersDefinedWritersToJson =  new HashMap<>();


    static {
        registerDefaultConverter(Boolean.class,    BooleanConverter.INSTANCE);
        registerDefaultConverter(Byte.class,       ByteConverter.INSTANCE);
        registerDefaultConverter(BigDecimal.class, BigDecimalConverter.INSTANCE);
        registerDefaultConverter(BigInteger.class, BigIntegerConverter.INSTANCE);
        registerDefaultConverter(Character.class,  CharacterConverter.INSTANCE);
        registerDefaultConverter(Double.class,     DoubleConverter.INSTANCE);
        registerDefaultConverter(Float.class,      FloatConverter.INSTANCE);
        registerDefaultConverter(Integer.class,    IntegerConverter.INSTANCE);
        registerDefaultConverter(Long.class,       LongConverter.INSTANCE);
        registerDefaultConverter(Short.class,      ShortConverter.INSTANCE);
        registerDefaultConverter(String.class,     StringConverter.INSTANCE);
        registerDefaultConverter(Optional.class,   OptionalConverter.INSTANCE);
        registerDefaultConverter(Iterable.class,   IterableConverter.INSTANCE);
        registerDefaultConverter(Map.class,        DefaultMapConverter.INSTANCE);
        registerDefaultConverter(Object.class,     DefaultObjectConverter.INSTANCE);
        registerDefaultConverter(Enum.class,       EnumConverter.INSTANCE);


        // Each String{], int[], Object[]... is a separate type hence we register JsonReader
        // Arrays  are handled particularly because there is no base class of array in java
        // and JsonWriter separately.
        writersToJson.put(Array.class, (Object[] array, JsonValue jsonValue) -> JsonArray.of(array));
    }


    public static JsonValue toJsonValue(Object o){

        if(o == null)
            return JsonNull.INSTANCE;

        if(o instanceof JsonValue)
            return (JsonValue) o;

        // First Check for User Defined conversion rules
        BiFunction<Object, JsonValue, JsonValue> fn;
        Class<?> cl = o.getClass();
        if (usersDefinedWritersToJson.containsKey(cl)) {
            fn = (BiFunction<Object, JsonValue, JsonValue>) usersDefinedWritersToJson.get(cl);
            return fn.apply(o, JsonObject.EMPTY);
        }

        // If not, then Apply default conversion strategies
        if (writersToJson.containsKey(cl)) {
            fn = (BiFunction<Object, JsonValue, JsonValue>) writersToJson.get(cl);
        }
        else {
            if(cl.isArray())
                fn = (BiFunction<Object, JsonValue, JsonValue>) writersToJson.get(Array.class);
            else if(o instanceof Iterable)
                fn = (BiFunction<Object, JsonValue, JsonValue>) writersToJson.get(Iterable.class);
            else if(o instanceof Map)
                fn = (BiFunction<Object, JsonValue, JsonValue>) writersToJson.get(Map.class);
            else if(cl.isEnum())
                fn = (BiFunction<Object, JsonValue, JsonValue>) writersToJson.get(Enum.class);
            else
                fn = (BiFunction<Object, JsonValue, JsonValue>) writersToJson.get(Object.class);
        }

        return fn.apply(o, JsonObject.EMPTY);
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

    public static<T> void registerWriter(Class<T> cl, BiFunction<T, JsonValue, JsonValue> writerFn){
        usersDefinedWritersToJson.put(cl, writerFn);
    }


    public static<T> void registerConverter(Class<T> cl, JsonConverter converter){
        usersDefinedWritersToJson.put(cl, converter::toJsonValue);
        usersDefinedReadersFromJson.put(cl, converter::fromJsonValue);
    }


    public static<T> void registerConverter(Class<T> cl,
                                            Function<JsonValue, T> readerFn,
                                            BiFunction<T, JsonValue, JsonValue> writerFn){
        usersDefinedWritersToJson.put(cl, writerFn);
        usersDefinedReadersFromJson.put(cl,readerFn );
    }

    public static<T> void registerDefaultConverter(Class<T> cl,JsonConverter converter){
        writersToJson.put(cl, converter::toJsonValue);
        readersFromJson.put(cl, converter::fromJsonValue);
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

    public static <T> T fromJsonValue(JsonValue jsonValue, Class<T> expectedClass){
        return fromJsonValueOptional(jsonValue, expectedClass).orElseThrow(ClassCastException::new);
    }

    public static <T> Optional<T> fromJsonValueOptional(JsonValue JsonValue, Class<T> expectedClass){

        // First Check for User Defined conversion rules
        Function<JsonValue, Object> fn;
        Optional<T> result;
        try {
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

            result = Optional.of((T) fn.apply(JsonValue));
        } catch(UnsupportedOperationException | ClassCastException e ) {
            result = Optional.empty();
        }

        return result ;
    }


    public static <T> T fromJsonValueOrDefault(JsonValue jsonValue, Class<T> expectedClass, T defaultValue){
        return fromJsonValueOptional(jsonValue, expectedClass).orElse(defaultValue);
    }


   protected static boolean isEligibleForStringify(JsonValue jsonValue, boolean keepingNull, boolean emptyValuesToNull){
       // If we add a key/value to the final string, either :
       //      1 - The value is null and we want to keep null values
       //      2 - The value is NOT an Empty instance of JsonOptional
       if(!keepingNull  && jsonValue.isJsonNull())
           return false;
       if(jsonValue.isJsonOptional() && jsonValue.isEmpty())
           return false;

       return true;
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
