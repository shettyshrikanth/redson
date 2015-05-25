package com.sidemash.redson;


public class JsonString implements JsonLiteral {

    private final String value;

    private JsonString(String value) {
        this.value = value;
    }


    public static JsonValue of(Character character) {
        return new JsonString(String.valueOf(character));
    }

    public static JsonValue of(String s) {
        return new JsonString(s);
    }

    @Override
    public boolean containsKey(String key) {
        throw new UnsupportedOperationException(
                String.format("this operation is not supported by instance of %s", this.getClass()));
    }

    @Override
    public boolean containsValue(Object value) {
        return this.value.equals(value);
    }

    @Override
    public boolean isJsonBoolean() {
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
    public boolean isJsonString() {
        return true;
    }

    @Override
    public String toString() {
        return "JsonString{" +
                "value='" + value + '\'' +
                '}';
    }

    @Override
    public String prettyStringifyRecursive(int indent, int incrementAcc, boolean keepingNull, boolean emptyValuesToNull) {
        return value;
    }

    @Override
    public String stringify(boolean keepingNull, boolean emptyValuesToNull) {
        return  value;
    }

}
