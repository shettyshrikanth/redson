package com.sidemash.redson;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class JsonOptional implements JsonValue {

    public static final JsonOptional EMPTY = new JsonOptional(Optional.empty());
    private final Optional<JsonValue> value;

    private JsonOptional(Optional<JsonValue> value) {
        this.value = value;
    }

    public static JsonOptional of(JsonValue jsonValue) {
        return new JsonOptional(Optional.of(jsonValue));
    }

    @Override
    public JsonValue append(JsonValue jsValue) {
        throw new UnsupportedOperationException("instance of JsonOptional does not permit this operation");
    }

    @Override
    public JsonValue append(String key, JsonValue jsValue) {
        throw new UnsupportedOperationException("instance of JsonOptional does not permit this operation");
    }

    @Override
    public JsonValue appendIfAbsent(String key, JsonValue jsValue) {
        throw new UnsupportedOperationException("instance of JsonOptional does not permit this operation");
    }

    @Override
    public boolean containsAll(JsonValue... jsValues) {
        return containsAll(Arrays.asList(jsValues));
    }

    @Override
    public boolean containsAll(List<? extends JsonValue> jsValues) {
        for (JsonValue value : jsValues){
            if(!this.containsValue(value))
                return false;
        }
        return true;
    }

    @Override
    public boolean containsKey(String key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return this.value.isPresent() && this.value.get().equals(value);
    }

    @Override
    public boolean isDefinedAt(int index) {
        return (index == 0 && value.isPresent());
    }

    @Override
    public boolean isDefinedAt(String key) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return (!value.isPresent());
    }

    @Override
    public boolean isJsonArray() {
        return false;
    }

    @Override
    public boolean isJsonBoolean() {
        return false;
    }

    @Override
    public boolean isJsonLiteral() {
        return false;
    }

    @Override
    public boolean isJsonNull() {
        return false;
    }

    @Override
    public boolean isJsonNumber() {
        return false;
    }

    @Override
    public boolean isJsonObject() {
        return false;
    }

    @Override
    public boolean isJsonOptional() {
        return true;
    }

    @Override
    public boolean isJsonString() {
        return false;
    }

    @Override
    public boolean isJsonStructure() {
        return false;
    }


    @Override
    public String prettyStringifyRecursive(int indent, int incrementAcc, boolean keepingNull, boolean emptyValuesToNull) {
        return stringify(keepingNull, emptyValuesToNull);
    }

    @Override
    public String toString() {
        return "JsonOptional{" +
                "value=" + value +
                '}';
    }

    @Override
    public String stringify(boolean keepingNull, boolean emptyValuesToNull) {
        if(value.isPresent())
            return value.get().stringify(keepingNull, emptyValuesToNull);
        else if(emptyValuesToNull)
            return JsonNull.INSTANCE.stringify(keepingNull, emptyValuesToNull);
        else
            throw new UnsupportedOperationException("stringify an empty JsonOptional");
    }

    @Override
    public JsonValue union(JsonValue jsonValue) {
        throw new UnsupportedOperationException("Union on instance of JsonOptional");
    }

    @Override
    public JsonValue unionAll(List<? extends JsonValue> jsonValues) {
        throw new UnsupportedOperationException("UnionAll on instance of JsonOptional");
    }
}
