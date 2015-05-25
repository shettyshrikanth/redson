package com.sidemash.redson;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.NoSuchElementException;
import java.util.Optional;

public enum JsonNull implements JsonLiteral {

    INSTANCE;

    @Override
    public Optional<? extends JsonValue> asOptional() {
        return Optional.empty();
    }

    @Override
    public boolean containsKey(String key) {
        throw new UnsupportedOperationException(
                String.format("this operation is not supported by instance of %s", this.getClass()));
    }

    @Override
    public boolean containsValue(Object value) {
        return value == null;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean isJsonBoolean() {
        return false;
    }

    @Override
    public boolean isJsonNull() {
        return true;
    }

    @Override
    public boolean isJsonNumber() {
        return false;
    }

    @Override
    public boolean isJsonString() {
        return false;
    }

    @Override
    public String prettyStringifyRecursive(int indent, int incrementAcc, boolean keepingNull, boolean emptyValuesToNull) {
        return stringify(keepingNull, emptyValuesToNull);
    }

    @Override
    public String stringify(boolean keepingNull, boolean emptyValuesToNull) {
        if(keepingNull) return  "null";
        else throw new UnsupportedOperationException("Stringify JsonNull while NOT keeping null value");
    }

    @Override
    public String toString() {
        return "JsonNull{}";
    }


    @Override
    public Object getValue() {
        throw new NoSuchElementException();
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
        throw new ClassCastException();
    }

    @Override
    public Optional<Byte> asByteOptional() {
        return Optional.empty();
    }

    @Override
    public byte asByteOrDefault(byte defaultValue) {
        return defaultValue;
    }

    @Override
    public BigDecimal asBigDecimal() {
        throw new ClassCastException();
    }

    @Override
    public Optional<BigDecimal> asBigDecimalOptional() {
        return Optional.empty();
    }

    @Override
    public BigDecimal asBigDecimalOrDefault(BigDecimal defaultValue) {
        return defaultValue;
    }

    @Override
    public BigInteger asBigInteger() {
        throw new ClassCastException();
    }

    @Override
    public Optional<BigInteger> asBigIntegerOptional() {
        return Optional.empty();
    }

    @Override
    public BigInteger asBigIntegerOrDefault(BigInteger defaultValue) {
        return defaultValue;
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
        throw new ClassCastException();
    }

    @Override
    public Optional<Double> asDoubleOptional() {
        return Optional.empty();
    }

    @Override
    public double asDoubleOrDefault(double defaultValue) {
        return defaultValue;
    }

    @Override
    public float asFloat() {
        throw new ClassCastException();
    }

    @Override
    public Optional<Float> asFloatOptional() {
        return Optional.empty();
    }

    @Override
    public float asFloatOrDefault(float defaultValue) {
        return defaultValue;
    }

    @Override
    public int asInt() {
        throw new ClassCastException();
    }

    @Override
    public Optional<Integer> asIntOptional() {
        return Optional.empty();
    }

    @Override
    public int asIntOrDefault(int defaultValue) {
        return defaultValue;
    }

    @Override
    public long asLong() {
        throw new ClassCastException();
    }

    @Override
    public Optional<Long> asLongOptional() {
        return Optional.empty();
    }

    @Override
    public long asLongOrDefault(long defaultValue) {
        return defaultValue;
    }

    @Override
    public <T> Optional<T> asOptionalOf(Class<T> c) {
        return Optional.empty();
    }

    @Override
    public <T> T asPojo(Class<T> cl) {
        throw new ClassCastException();
    }

    @Override
    public <T> Optional<T> asPojoOptional(Class<T> cl) {
        return Optional.empty();
    }

    @Override
    public <T> T asPojoOrDefault(Class<T> cl, T defaultValue) {
        return defaultValue;
    }

    @Override
    public short asShort() {
        throw new ClassCastException();
    }

    @Override
    public Optional<Short> asShortOptional() {
        return Optional.empty();
    }

    @Override
    public short asShortOrDefault(short defaultValue) {
        return defaultValue;
    }

    @Override
    public String asString() {
        throw new ClassCastException();
    }

    @Override
    public Optional<String> asStringOptional() {
        return Optional.empty();
    }

    @Override
    public String asStringOrDefault(String defaultValue) {
        return defaultValue;
    }

}
