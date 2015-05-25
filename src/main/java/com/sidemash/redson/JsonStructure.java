package com.sidemash.redson;


import java.util.Arrays;

public interface JsonStructure extends JsonValue {

    default boolean containsAll(JsonValue... jsValues) {
        return containsAll(Arrays.asList(jsValues));
    }

    @Override
    default boolean isJsonBoolean() {
        return false;
    }

    @Override
    default boolean isJsonLiteral() {
        return false;
    }

    @Override
    default boolean isJsonNull() {
        return false;
    }

    @Override
    default boolean isJsonNumber() {
        return false;
    }

    @Override
    default boolean isJsonOptional() {
        return false;
    }

    @Override
    default boolean isJsonString() {
        return false;
    }

    @Override
    default boolean isJsonStructure() {
        return true;
    }
}
