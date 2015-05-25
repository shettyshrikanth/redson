package com.sidemash.redson;


import java.math.BigDecimal;

public class JsonNumber implements JsonLiteral {

    private final BigDecimal value;

    private JsonNumber(BigDecimal value) {
        this.value = value;
    }

    public static JsonNumber of(BigDecimal value){
        return new JsonNumber(value);
    }
    public static JsonNumber of(int value){
        return JsonNumber.of(BigDecimal.valueOf(value));
    }
    public static JsonNumber of(long value){
        return JsonNumber.of(BigDecimal.valueOf(value));
    }
    public static JsonNumber of(float value){
        return JsonNumber.of(BigDecimal.valueOf(value));
    }
    public static JsonNumber of(double value){
        return JsonNumber.of(BigDecimal.valueOf(value));
    }
    public static JsonNumber of(String value){
        return JsonNumber.of(new BigDecimal(value));
    }

    @Override
    public boolean containsKey(String key) {
        throw new UnsupportedOperationException(
                String.format("this operation is not supported by instance of %s", this.getClass()));
    }

    @Override
    public boolean containsValue(Object value) {
        return value.equals(value);
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
        return true;
    }

    @Override
    public boolean isJsonString() {
        return false;
    }

    @Override
    public String prettyStringifyRecursive(int indent, int incrementAcc, boolean keepingNull, boolean emptyValuesToNull) {
        return value.toString();
    }

    @Override
    public String toString() {
        return "JsonNumber{" +
                "value=" + value +
                '}';
    }

    @Override
    public String stringify(boolean keepingNull, boolean emptyValuesToNull) {
        return value.toString();
    }
}
