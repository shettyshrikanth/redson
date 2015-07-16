package com.sidemash.redson;


import java.util.*;
import java.util.function.Consumer;

public interface JsonLiteral extends JsonValue {

    @Override
    default <T> List<T> asListOf(Class<T> cl, List<T> list) {
        list.add(Json.fromJsonValue(this, cl));
        return list;
    }

    @Override
    default <T> List<T> asListOf(Class<T> cl) {
        return asListOf(cl, new ArrayList<>());
    }

    @Override
    default <T> Map<String, T> asMapOf(Class<T> cl,  Map<String, T> map){
        throw new ClassCastException(
                String.format(
                        "This %s could be interpreted as an instance of Map<String, %s>",
                        this.getClass().getSimpleName(),
                        cl.getSimpleName()
                )
        );
    }

    @Override
    default <T> Set<T> asSetOf(Class<T> cl, Set<T> set) {
        set.add(Json.fromJsonValue(this, cl));
        return set;
    }

    @Override
    default <T> Set<T> asSetOf(Class<T> cl) {
        return asSetOf(cl, new LinkedHashSet<>());
    }

    @Override
    default JsonValue get(int index) {
        throw new UnsupportedOperationException(String.format("Get item by index " +
                "on JsonLiteral of type %s", this.getClass().getSimpleName()));
    }

    @Override
    default JsonValue get(String key) {
        throw new UnsupportedOperationException(String.format("Get item by key " +
                "on JsonLiteral of type %s", this.getClass().getSimpleName()));
    }

    @Override
    default Optional<JsonValue> getOptional(int index) {
        return Optional.empty();
    }

    @Override
    default Optional<JsonValue> getOptional(String key) {
        return Optional.empty();
    }

    @Override
    default JsonValue getOrDefault(int index, JsonValue jsonValue) {
        return jsonValue;
    }

    @Override
    default JsonValue getOrDefault(String key, JsonValue jsonValue) {
        return jsonValue;
    }

    @Override
    default boolean isEmpty() {
        return false;
    }

    @Override
    default boolean isJsonArray() {
        return false;
    }

    @Override
    default boolean isJsonLiteral() {
        return true;
    }

    @Override
    default boolean isJsonObject() {
        return false;
    }

    @Override
    default boolean isJsonOptional() {
        return false;
    }

    @Override
    default boolean isJsonStructure() {
        return false;
    }

    @Override
    default String prettyStringify() {
        return toString();
    }


    @Override
    default <T> Optional<T> asOptionalOf(Class<T> cl){
        if(this.isJsonNull())
            return Optional.empty();

        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as Optional of %s",
                        this.getClass().getSimpleName(),
                        cl.getSimpleName()
                )
        );
    }

    @Override
    default String prettyStringify(int ident) {
        return toString();
    }

    @Override
    default <T> Map<Integer, T> toIntIndexedMapOf(Class<T> cl, Map<Integer, T> map) {
        map.put(0, Json.fromJsonValue(this, cl));
        return map;
    }

    @Override
    default <T> Map<String, T> toStringIndexedMapOf(Class<T> c) {
        return toStringIndexedMapOf(c, new LinkedHashMap<>());
    }

    @Override
    default <T> Map<String, T> toStringIndexedMapOf(Class<T> c, Map<String, T> map) {
        map.put("0", Json.fromJsonValue(this, c));
        return map;
    }

}
