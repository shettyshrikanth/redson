package com.sidemash.redson;


import java.util.Objects;

public class JsonEntry<T> {
    public final T key;
    public final JsonValue value;

    protected JsonEntry(T key, JsonValue value) {
        Objects.requireNonNull(key, "key must not be null");
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonEntry<?> jsonEntry = (JsonEntry<?>) o;

        if (!key.equals(jsonEntry.key)) return false;
        return value.equals(jsonEntry.value);

    }

    public T getKey() {
        return key;
    }

    public JsonValue getValue() {
        return value;
    }


    public static <T>  JsonEntry<T> of(T key, JsonValue jsonValue){
        return (jsonValue == null)
                ? new JsonEntry<>(key, JsonNull.INSTANCE)
                : new JsonEntry<>(key, jsonValue) ;
    }

    public static <T>  JsonEntry<T> of(T key, Object o){
        return new JsonEntry<>(key, Json.toJsonValue(o));
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "JsonEntry{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
