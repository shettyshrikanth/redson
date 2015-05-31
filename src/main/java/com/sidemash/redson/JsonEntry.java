package com.sidemash.redson;


import java.util.Objects;

public class JsonEntry<T> {
    private final T key;
    private final JsonValue value;

    public JsonEntry(T key, JsonValue value) {
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
