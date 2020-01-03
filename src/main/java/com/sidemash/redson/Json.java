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
import java.util.stream.BaseStream;

@SuppressWarnings("unchecked")
public final class Json {

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
        registerDefaultConverter(boolean.class,    BooleanConverter.INSTANCE);
        registerDefaultConverter(Byte.class,       ByteConverter.INSTANCE);
        registerDefaultConverter(byte.class,       ByteConverter.INSTANCE);
        registerDefaultConverter(Character.class,  CharacterConverter.INSTANCE);
        registerDefaultConverter(char.class,  CharacterConverter.INSTANCE);
        registerDefaultConverter(Double.class,     DoubleConverter.INSTANCE);
        registerDefaultConverter(double.class,     DoubleConverter.INSTANCE);
        registerDefaultConverter(Float.class,      FloatConverter.INSTANCE);
        registerDefaultConverter(float.class,      FloatConverter.INSTANCE);
        registerDefaultConverter(Integer.class,    IntegerConverter.INSTANCE);
        registerDefaultConverter(int.class,         IntegerConverter.INSTANCE);
        registerDefaultConverter(Long.class,       LongConverter.INSTANCE);
        registerDefaultConverter(long.class,       LongConverter.INSTANCE);
        registerDefaultConverter(Short.class,      ShortConverter.INSTANCE);
        registerDefaultConverter(short.class,      ShortConverter.INSTANCE);
        registerDefaultConverter(BigDecimal.class, BigDecimalConverter.INSTANCE);
        registerDefaultConverter(BigInteger.class, BigIntegerConverter.INSTANCE);
        registerDefaultConverter(String.class,     StringConverter.INSTANCE);
        registerDefaultConverter(Object.class,     DefaultObjectConverter.INSTANCE);
        registerDefaultConverter(JsonValue.class,  JsonValueConverter.INSTANCE);
        registerDefaultConverter(Enum.class,       EnumConverter.INSTANCE);
        registerDefaultConverter(JsonNode.class,   JsonNodeConverter.INSTANCE);
        registerDefaultConverter(Array.class,      ArrayConverter.INSTANCE);

        registerDefaultConverter(List.class,       new ListConverter<>());
        registerDefaultConverter(BaseStream.class, new StreamConverter<>());
        registerDefaultConverter(Set.class,        new SetConverter<>());
        registerDefaultConverter(Queue.class,      new QueueConverter<>());
        registerDefaultConverter(Optional.class,   new OptionalConverter<>());
        registerDefaultConverter(Map.class,        new MapConverter<>());
        registerDefaultConverter(Iterator.class,   new IteratorConverter<>());
        registerDefaultConverter(Iterable.class,   new IterableConverter<>());
    }


    private Json(){}

    @SuppressWarnings("unchecked")
    public static JsonValue toJsonValue(Object o){

        if(o == null)
            return JsonNull.INSTANCE;

        if(o instanceof JsonValue)
            return (JsonValue)o;

        if(o.getClass().isArray()){

            if(o instanceof int[])
                return JsonArray.of((int[]) o);
            if(o instanceof char[])
                return JsonArray.of((char[]) o);
            if(o instanceof short[])
                return JsonArray.of((short[]) o);
            if(o instanceof byte[])
                return JsonArray.of((byte[]) o);
            if(o instanceof long[])
                return JsonArray.of((long[]) o);
            if(o instanceof float[])
                return JsonArray.of((float[]) o);
            if(o instanceof boolean[])
                return JsonArray.of((boolean[]) o);
            if(o instanceof double[])
                return JsonArray.of((double[]) o);
        }
        // If a Writer function to Json has been registered for the "o" class or its standardized form
        // then get the function and apply it with Empty JsonObject as Parameter.
        Optional<BiFunction<Object, JsonValue, JsonValue>> writerFn =
                getRegisteredWriterClassFor(standardizeClass(o.getClass())) ;
        if(writerFn.isPresent()){
            return writerFn.get().apply(o, JsonObject.EMPTY);
        }
        else {
            // Else if a Writer to Json has been registered for any super class
            // of the object.
            Optional<BiFunction<Object, JsonValue, JsonValue>> superClassWriterFn = getRegisteredWriterForSuperClassOf(o.getClass());
            if (superClassWriterFn.isPresent()) {
                return superClassWriterFn.get().apply(o, JsonObject.EMPTY);
            }
            // Else we will use the default Object writer to write this object to Json
            else {
                BiFunction<Object, JsonValue, JsonValue> fnResult;
                fnResult = (BiFunction<Object, JsonValue, JsonValue>) getJsonWriterFunctionFor(Object.class);
                return fnResult.apply(o, JsonObject.EMPTY);
            }
        }
    }

    private static Optional<BiFunction<Object,JsonValue,JsonValue>> getRegisteredWriterForSuperClassOf(Class<?> aClass) {
        List<Class<?>> classHierarchy = getClassHierarchyOf(aClass);
        Optional<BiFunction<Object,JsonValue,JsonValue>> resultFn;
        for(Class<?> cl : classHierarchy) {
            resultFn = getRegisteredWriterClassFor(cl) ;
            if (resultFn.isPresent())
                return resultFn;
        }

        return Optional.empty();
    }


    public static List<Class<?>> getClassHierarchyOf(Class<?> aClass){

        // Recursively build a list of all class in "aClass" class hierarchy
        List<Class<?>> classList = new ArrayList<>();
        Class<?> tmpClass = aClass;
        while (!tmpClass.equals(Object.class)) {
            classList.add(tmpClass);
            tmpClass = tmpClass.getSuperclass();
        }

        // Reverse the list to walk within it from super to sub classes
        Collections.reverse(classList);

        return classList;
    }


    private static Optional<BiFunction<Object, JsonValue, JsonValue>> getRegisteredWriterClassFor(Class<?> aClass) {
        Class<?> standardizedClass = standardizeClass(aClass);

        if(usersDefinedWritersToJson.containsKey(standardizedClass) )
            return Optional.of((BiFunction<Object, JsonValue, JsonValue>) usersDefinedWritersToJson.get(standardizedClass));

        else if(writersToJson.containsKey(standardizedClass) )
            return Optional.of((BiFunction<Object, JsonValue, JsonValue>) writersToJson.get(standardizedClass));
        else
            return Optional.empty();
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

            if( Map.class.isAssignableFrom(cl) ||  Iterable.class.isAssignableFrom(cl) )
                throw new UnsupportedOperationException(
                        String.format("Impossible to convert this JsonValue to %s. Try to pass this expression :" +
                                " 'new TypeReference<T>{}' instead of 'T.class' for conversion ", cl)
                );
            if( cl.isArray() )
                fnResult = (T) map.get(Array.class);
            else if( cl.isEnum() )
                fnResult = (T) map.get(cl);
            else if(JsonValue.class.isAssignableFrom(cl))
                fnResult = (T) map.get(JsonValue.class);
            else
                fnResult = (T) map.get(Object.class);
        }

        return fnResult;
    }

    public static BiFunction<?, JsonValue, JsonValue> getJsonWriterFunctionFor(Class<?> cl){
        return getFunctionFrom(cl,writersToJson, usersDefinedWritersToJson );
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
        else if( BaseStream.class.isAssignableFrom(initialClass) )
            return BaseStream.class;
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
            return Iterable.class;
        else
            return initialClass;
    }

    public static <T> T fromJsonValue(JsonValue jsonValue, TypeReference<T> typeRef){
        return fromJsonValue(jsonValue, typeRef.getType());
    }

    public static <T> T fromJsonValue(JsonValue jsonValue, Class<T> expectedClass){
        if(expectedClass.isArray()){
            @SuppressWarnings("unchecked")
            T result = (T) getJsonContainerReaderFunctionFor(expectedClass).apply(jsonValue, expectedClass);
            return result;
        }
        else {
            Function<JsonValue, Object> fn;
            fn = getJsonReaderFunctionFor(expectedClass);
            @SuppressWarnings("unchecked")
            T result = (T) fn.apply(jsonValue);
            return result;
        }
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
        return usersDefinedReadersFromJson.containsKey(cl) || readersFromJson.containsKey(cl) || containerReadersFromJson.containsKey(cl);
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


}
