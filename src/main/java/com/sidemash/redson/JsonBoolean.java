package com.sidemash.redson;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

public enum JsonBoolean implements JsonLiteral {

    TRUE(true){
        @Override
        public String prettyStringifyRecursive(int indent, int incrementAcc, boolean keepingNull, boolean emptyValuesToNull) {
            return  "true";
        }

        @Override
        public String stringify(boolean keepingNull, boolean emptyValuesToNull) {
            return  "true";
        }
    },

    FALSE(false) {

        @Override
        public String prettyStringifyRecursive(int indent, int incrementAcc, boolean keepingNull, boolean emptyValuesToNull) {
            return  "false";
        }

        @Override
        public String stringify(boolean keepingNull, boolean emptyValuesToNull) {
            return  "false";
        }
    };

    private final boolean value;

    JsonBoolean(boolean value) {
        this.value = value;
    }

    public static JsonBoolean  of(boolean b) {
        if(b) return JsonBoolean.TRUE; else return JsonBoolean.FALSE;
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
    public boolean asBoolean() {
        return value;
    }

    @Override
    public Optional<Boolean> asBooleanOptional() {
        return Optional.of(value);
    }

    @Override
    public boolean asBooleanOrDefault(boolean defaultValue) {
        return value;
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


    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public boolean isJsonBoolean() {
        return true;
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
        return false;
    }

    @Override
    public String prettyStringifyRecursive(int indent, int incrementAcc, boolean keepingNull, boolean emptyValuesToNull) {
        return String.valueOf(value);
    }

    @Override
    public String stringify(boolean keepingNull, boolean emptyValuesToNull) {
        return String.valueOf(value);
    }

    @Override
    public String toString() {
        return "JsonBoolean{" +
                "value=" + value +
                '}';
    }
}
