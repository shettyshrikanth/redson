package com.sidemash.redson;


import java.util.*;

public interface JsonLiteral extends JsonValue {

    @Override
    default JsonValue append(JsonValue jsValue) {
        throw new UnsupportedOperationException("This operation is not supported for JsonLiteral.");
    }

    @Override
    default JsonValue append(String key, JsonValue jsValue) {
        throw new UnsupportedOperationException("This operation is not supported for JsonLiteral.");
    }

    @Override
    default JsonValue appendIfAbsent(String key, JsonValue jsValue) {
        throw new UnsupportedOperationException("This operation is not supported for JsonLiteral.");
    }

    @Override
    default boolean containsAll(JsonValue... jsValues) {
        return containsAll(Arrays.asList(jsValues));
    }

    @Override
    default boolean containsAll(List<? extends JsonValue> jsValues) {
        if(jsValues.size() > 1)
            return false;
        else if (jsValues.size() == 1)
            return containsValue(jsValues.get(0));
        else
            return false;
    }

    @Override
    default boolean isDefinedAt(int index) {
        return (index == 0);
    }

    @Override
    default boolean isDefinedAt(String key) {
        return false;
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
    default String prettyStringify(int ident) {
        return toString();
    }

    @Override
    default JsonValue union(JsonValue jsonValue){
        throw new UnsupportedOperationException(
                String.format("this operation is not supported by instance of %s", this.getClass())
        );
    }

    @Override
    default JsonValue unionAll(List<? extends JsonValue> jsonValues){
        throw new UnsupportedOperationException(
                String.format("this operation is not supported by instance of %s", this.getClass())
        );
    }


    @Override
    default <T> Map<Integer, T> asIntIndexedMapOf(Class<T> cl, Map<Integer, T> map) {
        map.put(0, Json.fromJsonValue(this, cl));
        return map;
    }

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
    default <T> Set<T> asSetOf(Class<T> cl, Set<T> set) {
        set.add(Json.fromJsonValue(this, cl));
        return set;
    }

    @Override
    default <T> Set<T> asSetOf(Class<T> cl) {
        return asSetOf(cl, new LinkedHashSet<>());
    }

    @Override
    default <T> Map<String, T> asStringIndexedMapOf(Class<T> c) {
        return asStringIndexedMapOf(c, new LinkedHashMap<>());
    }

    @Override
    default <T> Map<String, T> asStringIndexedMapOf(Class<T> c, Map<String, T> map) {
        map.put("0", Json.fromJsonValue(this, c));
        return map;
    }

    @Override
    default JsonValue distinct() {
        return this;
    }

    @Override
    default Set<Integer> getIndexSet() {
        Set<Integer> set = new LinkedHashSet<>();
        set.add(0);
        return set;
    }

    @Override
    default Set<JsonEntry<Integer>> getIntIndexedEntrySet() {
        Set<JsonEntry<Integer>> set = new LinkedHashSet<>();
        set.add(new JsonEntry<>(0, this));
        return set;
    }

    @Override
    default Set<JsonEntry<String>> getStringIndexedEntrySet() {
        Set<JsonEntry<String>> set = new LinkedHashSet<>();
        set.add(new JsonEntry<>("0", this));
        return set;
    }

    @Override
    default Set<String> keySet() {
        Set<String> set = new LinkedHashSet<>();
        set.add("0");
        return set;
    }

    @Override
    default JsonValue prepend(JsonValue jsValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    default JsonValue prepend(String key, JsonValue jsValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    default JsonValue prependIfAbsent(String key, JsonValue jsValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    default JsonValue reverse() {
        return this;
    }

    @Override
    default int size() {
        return 1;
    }


}
