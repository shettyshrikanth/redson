package com.sidemash.redson;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DecimalNode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.NoSuchElementException;
import java.util.Optional;

public class JsonNumber implements JsonLiteral {

    private final BigDecimal value;

    private JsonNumber(BigDecimal value) {
        this.value = value;
    }

    public static JsonNumber of(BigInteger value){
        return new JsonNumber(new BigDecimal(value));
    }

    public static JsonNumber of(BigDecimal value){
        return new JsonNumber(value);
    }

    public static JsonNumber of(int value){
        return JsonNumber.of(BigDecimal.valueOf(value));
    }

    public static JsonNumber of(Number value){

        // Protection if number is a Double or Float that is Infinity or NaN
        // Calling the toString() method would throw a NumberFormatException
        // @see http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Double.html#toString%28double%29
        if (value instanceof Double)
            return JsonNumber.of(BigDecimal.valueOf((Double) value));
        else if (value instanceof Float)
            return JsonNumber.of(BigDecimal.valueOf((Float) value));

        return JsonNumber.of(new BigDecimal(value.toString()));
    }

    public static JsonNumber of(long value) {
        return JsonNumber.of(BigDecimal.valueOf(value));
    }

    public static JsonNumber of(float value) {
        return JsonNumber.of(BigDecimal.valueOf(value));
    }

    public static JsonNumber of(double value) {
        return JsonNumber.of(BigDecimal.valueOf(value));
    }

    public static JsonNumber of(byte value) {
        return JsonNumber.of(BigDecimal.valueOf(value));
    }

    public static JsonNumber of(short value) {
        return JsonNumber.of(BigDecimal.valueOf(value));
    }

    public static JsonNumber of(String value) {
        return JsonNumber.of(new BigDecimal(value));
    }

    @Override
    public BigDecimal asBigDecimal() {
        return value;
    }

    @Override
    public Optional<BigDecimal> asBigDecimalOptional() {
        return Optional.of(value);
    }

    @Override
    public BigInteger asBigInteger() {
        return value.toBigIntegerExact();
    }

    @Override
    public Optional<BigInteger> asBigIntegerOptional() {
        Optional<BigInteger> result;
        try {
            result = Optional.of(value.toBigIntegerExact());
        } catch (Exception e) {
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public boolean asBoolean() {
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as Boolean",
                        this.getClass().getSimpleName()
                )
        );
    }


    @Override
    public Optional<Boolean> asBooleanOptional() {
        return Optional.empty();
    }

    @Override
    public byte asByte() {
        return value.byteValueExact();
    }

    @Override
    public Optional<Byte> asByteOptional() {
        Optional<Byte> result;
        try {
            result = Optional.of(value.byteValueExact());
        } catch (Exception e) {
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public char asChar() {
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as Character",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    public Optional<Character> asCharOptional() {
        return Optional.empty();
    }

    @Override
    public double asDouble() {
        return value.doubleValue();
    }

    @Override
    public Optional<Double> asDoubleOptional() {
        Optional<Double> result;
        try {
            result = Optional.of(value.doubleValue());
        } catch (Exception e) {
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public float asFloat() {
        return value.floatValue();
    }

    @Override
    public Optional<Float> asFloatOptional() {
        Optional<Float> result;
        try {
            result = Optional.of(value.floatValue());
        } catch (Exception e) {
            result = Optional.empty();
        }
        return result;
    }


    @Override
    public int asInt() {
        return value.intValueExact();
    }

    @Override
    public Optional<Integer> asIntOptional() {
        Optional<Integer> result;
        try {
            result = Optional.of(value.intValueExact());
        } catch (Exception e) {
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public long asLong() {
        return value.longValueExact();
    }

    @Override
    public Optional<Long> asLongOptional() {
        Optional<Long> result;
        try {
            result = Optional.of(value.longValueExact());
        } catch (Exception e) {
            result = Optional.empty();
        }
        return result;
    }



    @Override
    public short asShort() {
        return value.shortValueExact();
    }

    @Override
    public Optional<Short> asShortOptional() {
        Optional<Short> result;
        try {
            result = Optional.of(value.shortValueExact());
        } catch (Exception e) {
            result = Optional.empty();
        }
        return result;
    }


    @Override
    public String asString() {
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as String",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    public Optional<String> asStringOptional() {
        return Optional.empty();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonNumber that = (JsonNumber) o;

        return value.equals(that.value);
    }

    @Override
    public JsonValue get() {
        throw new NoSuchElementException(
                String.format(
                        "This method is only available for instances of JsonOptional not %s",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    public Optional<JsonValue> getOptional() {
        return Optional.empty();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
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
    public String stringify(boolean keepingNull, boolean emptyValuesToNull) {
        return value.toString();
    }

    @Override
    public JsonNode toJsonNode() {
        return DecimalNode.valueOf(value);
    }

    @Override
    public String toString() {
        return "JsonNumber{" +
                "value=" + value +
                '}';
    }
}
