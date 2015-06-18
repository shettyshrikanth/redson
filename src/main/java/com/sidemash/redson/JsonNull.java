package com.sidemash.redson;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.NoSuchElementException;
import java.util.Optional;

public enum JsonNull implements JsonLiteral {

    INSTANCE;

    @Override
    public BigDecimal asBigDecimal() {
        return null;
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
        return null;
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
    public boolean asBoolean() {
        return  false;
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
        return (byte)0;
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
    public char asChar() {
        return '\u0000';
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
        return  0.0d;
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
        return  0.0f;
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
        return  0;
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
        return  0L;
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
    public Optional<? extends JsonValue> asOptional() {
        return Optional.empty();
    }

    @Override
    public <T> Optional<T> asOptionalOf(Class<T> c) {
        return Optional.empty();
    }

    @Override
    public <T> T asPojo(Class<T> cl) {
        return null;
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
        return (short)0;
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
        return  null;
    }

    @Override
    public Optional<String> asStringOptional() {
        return Optional.empty();
    }

    @Override
    public String asStringOrDefault(String defaultValue) {
        return defaultValue;
    }

    @Override
    public JsonValue get() {
        throw new NoSuchElementException(
                "This method is only available for instances of " +
                        " JsonOptional not JsonBoolean"
        );
    }

    @Override
    public Optional<JsonValue> getOptional() {
        return Optional.empty();
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

}
