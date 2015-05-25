package com.sidemash.redson;


import java.util.Arrays;
import java.util.List;

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
    default String prettyStringifyRecursive(int indent, int incrementAcc) {
        return toString();
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
}
