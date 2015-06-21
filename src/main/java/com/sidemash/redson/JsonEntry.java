package com.sidemash.redson;


import java.util.Map;
import java.util.Objects;

public abstract class JsonEntry<T> {
    private final T key;
    private final JsonValue value;

    private JsonEntry(T key, JsonValue value) {
        Objects.requireNonNull(key, "key must not be null");
        this.key = key;
        this.value = value;
    }

    public static JsonEntry<String> of(String key, JsonValue jsonValue){
        return (jsonValue == null)
                ? new StringIndexedJsonEntry(key, JsonNull.INSTANCE)
                : new StringIndexedJsonEntry(key, jsonValue) ;
    }

    public static JsonEntry<String> of(String key, Object o){
        return new StringIndexedJsonEntry(key, JsonValue.of(o));
    }

    public static JsonEntry<Integer> of(int key, Object o){
        return new IntIndexedJsonEntry(key, Json.toJsonValue(o));
    }

    public static JsonEntry<String> of(Map.Entry<String, JsonValue> entry){
        return new StringIndexedJsonEntry(entry.getKey(), entry.getValue());
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

    public abstract Map.Entry<T, JsonValue> toMapEntry();

    @Override
    public String toString() {
        return "JsonEntry{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

    private static class IntIndexedJsonEntry extends  JsonEntry<Integer> {

        private IntIndexedJsonEntry(Integer key, JsonValue value) {
            super(key, value);
        }

        @Override
        public Map.Entry<Integer, JsonValue> toMapEntry() {
            IntIndexedJsonEntry outer = this;
            return new Map.Entry<Integer, JsonValue>() {

                final Integer innerKey = outer.getKey() ;
                JsonValue innerValue = outer.getValue();

                @Override
                public Integer getKey() {
                    return innerKey;
                }

                @Override
                public JsonValue getValue() {
                    return innerValue;
                }

                @Override
                public JsonValue setValue(JsonValue newInnerValue) {
                    JsonValue oldInnerValue = innerValue;
                    this.innerValue = newInnerValue;
                    return oldInnerValue;
                }
            };
        }
    }

    private static class StringIndexedJsonEntry extends  JsonEntry<String> {

        private StringIndexedJsonEntry(String key, JsonValue value) {
            super(key, value);
        }

        @Override
        public Map.Entry<String, JsonValue> toMapEntry() {
            StringIndexedJsonEntry outer = this;
            return new Map.Entry<String, JsonValue>() {

                final String innerKey = outer.getKey() ;
                JsonValue innerValue = outer.getValue();

                @Override
                public String getKey() {
                    return innerKey;
                }

                @Override
                public JsonValue getValue() {
                    return innerValue;
                }

                @Override
                public JsonValue setValue(JsonValue newInnerValue) {
                    JsonValue oldInnerValue = innerValue;
                    this.innerValue = newInnerValue;
                    return oldInnerValue;
                }
            };
        }
    }
}
