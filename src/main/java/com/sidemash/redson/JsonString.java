package com.sidemash.redson;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

public class JsonString implements JsonLiteral {

    private final String value;

    private JsonString(String value) {
        this.value = value;
    }


    public static JsonValue of(char character) {
        return new JsonString(String.valueOf(character));
    }

    public static JsonValue of(String s) {
        return new JsonString(s);
    }
    public static JsonValue of(CharSequence ch) {
        return new JsonString(ch.toString());
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
    public char asChar() {
        return asCharOptional().orElseThrow(ClassCastException::new);
    }

    @Override
    public Optional<Character> asCharOptional() {
        if(value.length() == 1) return Optional.of(value.charAt(0));
        else return Optional.empty();
    }

    @Override
    public char asCharOrDefault(char defaultValue) {
        return asCharOptional().orElse(defaultValue);
    }

    @Override
    public double asDouble() {
        return asDoubleOptional().orElseThrow(ClassCastException::new);
    }

    @Override
    public Optional<Double> asDoubleOptional() {
        Optional<Double> result;
        try{
            result = Optional.of(Double.parseDouble(value));
        }
        catch (Exception e){
            result =  Optional.empty();
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
        try{
            result = Optional.of(Float.parseFloat(value));
        }
        catch (Exception e){
            result =  Optional.empty();
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
        try{
            result = Optional.of(Integer.parseInt(value));
        }
        catch (Exception e){
            result =  Optional.empty();
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
        try{
            result = Optional.of(Long.parseLong(value));
        }
        catch (Exception e){
            result =  Optional.empty();
        }
        return result;
    }

    @Override
    public long asLongOrDefault(long defaultValue) {
        return asLongOptional().orElse(defaultValue);
    }

    @Override
    public <T> Optional<T> asOptionalOf(Class<T> c) {
        throw new ClassCastException();
    }

    @Override
    public short asShort() {
        return asShortOptional().orElseThrow(ClassCastException::new);
    }

    @Override
    public Optional<Short> asShortOptional() {
        Optional<Short> result;
        try{
            result = Optional.of(Short.parseShort(value));
        }
        catch (Exception e){
            result =  Optional.empty();
        }
        return result;
    }

    @Override
    public short asShortOrDefault(short defaultValue) {
        return asShortOptional().orElse(defaultValue);
    }

    @Override
    public String asString() {
        return value;
    }

    @Override
    public Optional<String> asStringOptional() {
        return Optional.of(value);
    }

    @Override
    public String asStringOrDefault(String defaultValue) {
        return value;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonString that = (JsonString) o;

        return value.equals(that.value);
    }

    @Override
    public String getValue() {
        return value;
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
        return false;
    }

    @Override
    public boolean isJsonString() {
        return true;
    }

    @Override
    public String prettyStringifyRecursive(int indent, int incrementAcc, boolean keepingNull, boolean emptyValuesToNull) {
        return stringify(keepingNull, emptyValuesToNull);
    }

    @Override
    public String stringify(boolean keepingNull, boolean emptyValuesToNull) {
        return  String.format("\"%s\"", value);
    }

    @Override
    public String toString() {
        return "JsonString{" +
                "value='" + value + '\'' +
                '}';
    }

}
