package com.sidemash.redson;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
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
    public static JsonNumber of(long value){
        return JsonNumber.of(BigDecimal.valueOf(value));
    }
    public static JsonNumber of(float value){
        return JsonNumber.of(BigDecimal.valueOf(value));
    }
    public static JsonNumber of(double value){return JsonNumber.of(BigDecimal.valueOf(value)); }
    public static JsonNumber of(byte value){
        return JsonNumber.of(BigDecimal.valueOf(value));
    }
    public static JsonNumber of(short value){
        return JsonNumber.of(BigDecimal.valueOf(value));
    }
    public static JsonNumber of(String value){
        return JsonNumber.of(new BigDecimal(value));
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean asBoolean() {
        throw new ClassCastException();
    }

    @Override
    public Optional<Boolean> asBooleanOptional() {
        return Optional.empty();
    }

    @Override
    public boolean asBooleanOrDefault(boolean defaultValue) {
        return defaultValue;
    }

    @Override
    public byte asByte() {
        return asByteOptional().orElseThrow(ClassCastException::new);
    }

    @Override
    public Optional<Byte> asByteOptional() {
        Optional<Byte> result;
        try {
            result = Optional.of(value.byteValueExact());
        }
        catch (Exception e){
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public byte asByteOrDefault(byte defaultValue) {
        return asByteOptional().orElse(defaultValue);
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
    public BigDecimal asBigDecimalOrDefault(BigDecimal defaultValue) {
        return value;
    }

    @Override
    public BigInteger asBigInteger() {
        return asBigIntegerOptional().orElseThrow(ClassCastException::new);
    }

    @Override
    public Optional<BigInteger> asBigIntegerOptional() {
        Optional<BigInteger> result;
        try {
            result = Optional.of(value.toBigIntegerExact());
        }
        catch (Exception e){
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public BigInteger asBigIntegerOrDefault(BigInteger defaultValue) {
        return asBigIntegerOptional().orElse(defaultValue);
    }

    @Override
    public char asChar() {
        throw new ClassCastException();
    }

    @Override
    public Optional<Character> asCharOptional() {
        return Optional.empty();
    }

    @Override
    public char asCharOrDefault(char defaultValue) {
        return defaultValue;
    }

    @Override
    public double asDouble() {
        return asDoubleOptional().orElseThrow(ClassCastException::new);
    }

    @Override
    public Optional<Double> asDoubleOptional() {
        Optional<Double> result;
        try {
            result = Optional.of(value.doubleValue());
        }
        catch (Exception e){
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public double asDoubleOrDefault(double defaultValue) {
        return asDoubleOptional().orElse(defaultValue);
    }

    @Override
    public float asFloat() {
        return asFloatOptional().orElseThrow(ClassCastException::new);
    }

    @Override
    public Optional<Float> asFloatOptional() {
        Optional<Float> result;
        try {
            result = Optional.of(value.floatValue());
        }
        catch (Exception e){
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public float asFloatOrDefault(float defaultValue) {
        return asFloatOptional().orElse(defaultValue);
    }

    @Override
    public int asInt() {
        return asIntOptional().orElseThrow(ClassCastException::new);
    }

    @Override
    public Optional<Integer> asIntOptional() {
        Optional<Integer> result;
        try {
            result = Optional.of(value.intValueExact());
        }
        catch (Exception e){
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public int asIntOrDefault(int defaultValue) {
        return asIntOptional().orElse(defaultValue);
    }

    @Override
    public long asLong() {
        return asLongOptional().orElseThrow(ClassCastException::new);
    }

    @Override
    public Optional<Long> asLongOptional() {
        Optional<Long> result;
        try {
            result = Optional.of(value.longValueExact());
        }
        catch (Exception e){
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public long asLongOrDefault(long defaultValue) {
        return asLongOptional().orElse(defaultValue);
    }

    @Override
    public <T> Optional<T> asOptionalOf(Class<T> c) {
        return null;
    }

    @Override
    public short asShort() {
        return asShortOptional().orElseThrow(ClassCastException::new);
    }

    @Override
    public Optional<Short> asShortOptional() {
        Optional<Short> result;
        try {
            result = Optional.of(value.shortValueExact());
        }
        catch (Exception e){
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public short asShortOrDefault(short defaultValue) {
        return asShortOptional().orElse(defaultValue);
    }

    @Override
    public String asString() {
        return asStringOptional().orElseThrow(ClassCastException::new);
    }

    @Override
    public Optional<String> asStringOptional() {
        Optional<String> result;
        try {
            result = Optional.of(value.toString());
        }
        catch (Exception e){
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public String asStringOrDefault(String defaultValue) {
        return asStringOptional().orElse(defaultValue);
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
    public String stringify(boolean keepingNull, boolean emptyValuesToNull) {
        return value.toString();
    }

    @Override
    public String toString() {
        return "JsonNumber{" +
                "value=" + value +
                '}';
    }
}
