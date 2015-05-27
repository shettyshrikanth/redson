package com.sidemash.redson;


public class JsonEntry<T> {
    private final T key;
    private final JsonValue value;

    public JsonEntry(T key, JsonValue value) {
        this.key = key;
        this.value = value;
    }

    public T getKey() {
        return key;
    }

    public JsonValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "JsonEntry{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }


}
