package com.sidemash.redson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.sidemash.redson.converter.*;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class Json {

    private static final Map<Class<?>, Function<JsonValue, ?>>
            readersFromJson =  new HashMap<>();
    private static final Map<Class<?>, BiFunction<JsonValue, Type, ?>>
            containerReadersFromJson =  new HashMap<>();
    private static final Map<Class<?>, BiFunction<?, JsonValue, JsonValue>>
            writersToJson =  new HashMap<>();

    private static final Map<Class<?>, BiFunction<JsonValue,Type, ?>>
            usersDefinedContainerReadersFromJson =  new HashMap<>();
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
        registerDefaultConverter(Object.class,     DefaultObjectConverter.INSTANCE);
        registerDefaultConverter(JsonValue.class,  JsonValueConverter.INSTANCE);
        registerDefaultConverter(Enum.class,       EnumConverter.INSTANCE);
        registerDefaultConverter(JsonNode.class,   JsonNodeConverter.INSTANCE);

        registerDefaultConverter(List.class,       new ListConverter<>());
        registerDefaultConverter(Stream.class,     new StreamConverter<>());
        registerDefaultConverter(Set.class,        new SetConverter<>());
        registerDefaultConverter(Queue.class,      new QueueConverter<>());
        registerDefaultConverter(Array.class,      new ArrayConverter<>());
        registerDefaultConverter(Optional.class,   new OptionalConverter<>());
        registerDefaultConverter(Map.class,        new MapConverter<>());
        registerDefaultConverter(IteratorConverter.class, new IteratorConverter<>());
    }


    private Json(){}

    public static JsonValue toJsonValue(Object o){

        if(o == null)
            return JsonNull.INSTANCE;

        if(o instanceof JsonValue)
            return (JsonValue)o;

        BiFunction<Object, JsonValue, JsonValue> fnResult;
        fnResult = (BiFunction<Object, JsonValue, JsonValue>) getJsonWriterFunctionFor(standardizeClass(o.getClass()));
        return fnResult.apply(o, JsonObject.EMPTY);
    }

    public static BiFunction<?, JsonValue, JsonValue> getJsonWriterFunctionFor(Class<?> cl){
        return getFunctionFrom(cl,writersToJson, usersDefinedWritersToJson );
    }

    public static Function<JsonValue, Object> getJsonReaderFunctionFor(Class<?> cl){
        return getFunctionFrom(cl,readersFromJson, usersDefinedReadersFromJson );
    }

    public static BiFunction<JsonValue,Type, Object> getJsonContainerReaderFunctionFor(Class<?> cl){
        return getFunctionFrom(cl,containerReadersFromJson, usersDefinedContainerReadersFromJson );
    }

    private static<T> T getFunctionFrom(Class<?> cl, Map<?,?> map, Map<?,?> userDefinedMap){
        //Function<JsonValue, Object> fnResult;
        T fnResult;
        if (userDefinedMap.containsKey(cl)) {
            fnResult = (T) userDefinedMap.get(cl);
        }
        // If not, then Apply default conversion strategies
        else if (map.containsKey(cl)) {
            fnResult = (T) map.get(cl);
        }
        else {

            if( cl.isArray() ||  Map.class.isAssignableFrom(cl) ||  Iterable.class.isAssignableFrom(cl) )
                throw new UnsupportedOperationException(
                        String.format("Impossible to convert this JsonValue to %s. Try to pass this expression :" +
                                " 'new TypeReference<T>{}' instead of 'T.class' for conversion ", cl)
                );

            if( cl.isEnum() )
                fnResult = (T) map.get(cl);
            else if(JsonValue.class.isAssignableFrom(cl))
                fnResult = (T) map.get(JsonValue.class);
            else
                fnResult = (T) map.get(Object.class);
        }

        return fnResult;
    }

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

    public static<T> void registerReader(Class<T> cl, Function<JsonValue, T> readerFn){
        usersDefinedReadersFromJson.put(cl, readerFn);
    }

    public static<T> void registerReader(Class<T> cl,
                                         BiFunction<JsonValue, Type,  T> readerFn){
        usersDefinedContainerReadersFromJson.put(cl, readerFn);
    }

    public static<T> void registerWriter(Class<T> cl, BiFunction<T, JsonValue, JsonValue> writerFn){
        usersDefinedWritersToJson.put(cl, writerFn);
    }

    public static<T> void registerConverter(Class<T> cl, JsonConverter converter){
        usersDefinedWritersToJson.put(cl, converter::toJsonValue);
        usersDefinedContainerReadersFromJson.put(cl, converter::fromJsonValue);
        usersDefinedReadersFromJson.put(cl, converter::fromJsonValue);
    }

    private static<T> void registerDefaultConverter(Class<T> cl,JsonConverter converter){
        readersFromJson.put(cl, converter::fromJsonValue);
        containerReadersFromJson.put(cl, converter::fromJsonValue);
        writersToJson.put(cl, converter::toJsonValue);
    }

    public static <T> T  fromJsonValue(JsonValue jsonValue, Type type){
        if(jsonValue.isJsonNull())
            return null;

        if (type instanceof Class) {
            return fromJsonValue(jsonValue, (Class<T>) type);
        }
        else if (type instanceof GenericArrayType) {
            Function<JsonValue, Object> fn;
            fn = getJsonReaderFunctionFor(Array.class);

            @SuppressWarnings("unchecked")
            T result =  (T) fn.apply(jsonValue);
            return result;
        }
        else if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) type;
            BiFunction<JsonValue, Type, Object> fn;
            fn = getJsonContainerReaderFunctionFor((Class<?>) p.getRawType());

            @SuppressWarnings("unchecked")
            T result =  (T) fn.apply(jsonValue, type);
            return result;
        }
        else {
            throw new UnsupportedOperationException(
                    String.format("Unable to convert from JsonValue because converter " +
                            "for :  %s is not registered", type)
            );
        }
    }

    private static<T> Class<?> standardizeClass(Class<T> initialClass){
        if(JsonValue.class.isAssignableFrom(initialClass))
            return JsonValue.class;
        else if( List.class.isAssignableFrom(initialClass) )
            return List.class;
        else if( Stream.class.isAssignableFrom(initialClass) )
            return Stream.class;
        else if( Set.class.isAssignableFrom(initialClass) )
            return Set.class;
        else if( initialClass.isArray() )
            return Array.class;
        else if( JsonNode.class.isAssignableFrom(initialClass) )
            return JsonNode.class;
        else if( Queue.class.isAssignableFrom(initialClass) )
            return Queue.class;
        else if( Iterator.class.isAssignableFrom(initialClass) )
            return Iterator.class;
        else if( Map.class.isAssignableFrom(initialClass) )
            return Map.class;
        else if(Iterable.class.isAssignableFrom(initialClass) )
            return List.class;
        else
            return initialClass;
    }

    public static <T> T fromJsonValue(JsonValue jsonValue, TypeReference<T> typeRef){
        return fromJsonValue(jsonValue, typeRef.getType());
    }

    public static <T> T fromJsonValue(JsonValue jsonValue, Class<T> expectedClass){
        Function<JsonValue, Object> fn;
        fn = getJsonReaderFunctionFor(expectedClass);

        @SuppressWarnings("unchecked")
        T result =  (T) fn.apply(jsonValue);
        return result;
    }

    public static <T> Optional<T> fromJsonValueOptional(JsonValue jsonValue, Class<T> expectedClass){

        Optional<T> result;
        try {
            result = Optional.of(fromJsonValue(jsonValue, expectedClass));
        } catch(UnsupportedOperationException | ClassCastException e ) {
            result = Optional.empty();
        }

        return result ;
    }


    public static <T> T fromJsonValueOrDefault(JsonValue jsonValue, Class<T> expectedClass, T defaultValue){
        T result;
        try {
            result = fromJsonValue(jsonValue, expectedClass);
        } catch(UnsupportedOperationException | ClassCastException e ) {
            result = defaultValue;
        }
        return result;
    }


   static boolean isEligibleForStringify(JsonValue jsonValue, boolean keepingNull){
       // If we add a value to the final string, either :
       //      1 - The value is null and we want to keep null values
       //      2 - The value is NOT an Empty instance of JsonOptional
       if(!keepingNull && jsonValue.isJsonNull())
           return false;
       return !(jsonValue.isJsonOptional() && jsonValue.isEmpty());

   }

    public static boolean isWriterRegisteredFor(Class<?> cl) {
        return usersDefinedWritersToJson.containsKey(cl) || writersToJson.containsKey(cl);
    }

    public static boolean isReaderRegisteredFor(Class<?> cl) {
        return usersDefinedReadersFromJson.containsKey(cl) || readersFromJson.containsKey(cl);
    }

/*
    public static JsonValue parse(String s);

    // if nullToOptional is false, then null values will be mapped to JsonNull.
    // and then, when doing type conversion (asString(), asInt(), ... )
    // The default value for the type will be returned hence asString() -> null ,
    //  asBool() -> false, asInt(), asLong() -> 0 , etc ...
    public static JsonValue parse(String s, boolean nullToOptional);

    // replace Void by JsonNode
    public static JsonNode toJsonNode(JsonValue jsonValue);
    public static JsonNode toJsonNode(JsonValue jsonValue,  Function<JsonValue, JsonNode> converterFn));
    public static <T> Void toJsonNode(T instance);
    public static <T> Void toJsonNode(T instance, Function<JsonValue, Void> converterFn);
*/









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





    /*
    public static Object fromJsonValue(JsonValue jsonValue){

        if(jsonValue.isJsonNull())
            return null;

        Function<JsonValue, Object> fn = null;
        if(jsonValue.isJsonArray())
            fn = (Function<JsonValue, Object>) readersFromJson.get(Iterable.class);

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
    }*/


/*
    public static Function<JsonValue, Object> getJsonReaderContainerFunctionFor(Class<?> cl){
        Function<JsonValue, Object> fnResult;

        if (usersDefinedReadersFromJson.containsKey(cl)) {
            fnResult = (Function<JsonValue, Object>) usersDefinedReadersFromJson.get(cl);
        }
        // If not, then Apply default conversion strategies
        else if (readersFromJson.containsKey(cl)) {
            fnResult = (Function<JsonValue, Object>) readersFromJson.get(cl);
        }
        else {

            if( cl.isArray() ||  Map.class.isAssignableFrom(cl) ||  Iterable.class.isAssignableFrom(cl) )
                throw new UnsupportedOperationException(
                        String.format("Impossible to convert this JsonValue to %s. Try to pass this expression :" +
                                " 'new TypeReference<T>{}' instead of 'T.class' for conversion ", cl)
                );

            if( cl.isEnum() )
                fnResult = (Function<JsonValue, Object>) readersFromJson.get(cl);
            else if(JsonValue.class.isAssignableFrom(cl))
                fnResult = (Function<JsonValue, Object>) readersFromJson.get(JsonValue.class);
            else
                fnResult = (Function<JsonValue, Object>) readersFromJson.get(Object.class);
        }

        return fnResult;
    }
*/


}
