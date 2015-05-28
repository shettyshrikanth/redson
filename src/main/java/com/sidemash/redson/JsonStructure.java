package com.sidemash.redson;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

public interface JsonStructure extends JsonValue {

    @Override
    default boolean asBoolean() {
        throw new ClassCastException(String.format("instance of %s could not be get as Boolean", this.getClass()));
    }

    @Override
    default Optional<Boolean> asBooleanOptional() {
        return Optional.empty();
    }

    @Override
    default boolean asBooleanOrDefault(boolean defaultValue) {
        return defaultValue;
    }

    @Override
    default char asChar() {
        throw new ClassCastException(String.format("instance of %s could not be get as Character", this.getClass()));
    }

    @Override
    default Optional<Character> asCharOptional() {
        return Optional.empty();
    }

    @Override
    default char asCharOrDefault(char defaultValue) {
        return defaultValue;
    }

    @Override
    default double asDouble() {
        throw new ClassCastException(String.format("instance of %s could not be get as Double", this.getClass()));
    }

    @Override
    default Optional<Double> asDoubleOptional() {
        return Optional.empty();
    }

    @Override
    default double asDoubleOrDefault(double defaultValue) {
        return defaultValue;
    }

    @Override
    default float asFloat() {
        throw new ClassCastException(String.format("instance of %s could not be get as Float", this.getClass()));
    }

    @Override
    default Optional<Float> asFloatOptional() {
        return Optional.empty();
    }

    @Override
    default float asFloatOrDefault(float defaultValue) {
        return defaultValue;
    }

    @Override
    default int asInt() {
        throw new ClassCastException(String.format("instance of %s could not be get as Integer", this.getClass()));
    }

    @Override
    default Optional<Integer> asIntOptional() {
        return Optional.empty();
    }

    @Override
    default int asIntOrDefault(int defaultValue) {
        return defaultValue;
    }

    @Override
    default long asLong() {
        throw new ClassCastException(String.format("instance of %s could not be get as Long", this.getClass()));
    }

    @Override
    default Optional<Long> asLongOptional() {
        return Optional.empty();
    }

    @Override
    default long asLongOrDefault(long defaultValue) {
        return defaultValue;
    }

    @Override
    default Optional<? extends JsonValue> asOptional() {
        return Optional.of(this);
    }

    @Override
    default short asShort() {
        throw new ClassCastException(String.format("instance of %s could not be get as Short", this.getClass()));
    }

    @Override
    default Optional<Short> asShortOptional() {
        return Optional.empty();
    }

    @Override
    default short asShortOrDefault(short defaultValue) {
        return defaultValue;
    }

    @Override
    default String asString() {
        throw new ClassCastException(String.format("instance of %s could not be get as String", this.getClass()));
    }

    @Override
    default Optional<String> asStringOptional() {
        return Optional.empty();
    }

    @Override
    default String asStringOrDefault(String defaultValue) {
        return defaultValue;
    }

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

    @Override
    default byte asByte() {
        throw new ClassCastException(String.format("instance of %s could not be get as Byte", this.getClass()));
    }

    @Override
    default Optional<Byte> asByteOptional() {
        return Optional.empty();
    }

    @Override
    default byte asByteOrDefault(byte defaultValue) {
        return defaultValue;
    }

    @Override
    default BigDecimal asBigDecimal() {
        throw new ClassCastException(String.format("instance of %s could not be get as BigDecimal", this.getClass()));
    }

    @Override
    default Optional<BigDecimal> asBigDecimalOptional() {
        return Optional.empty();
    }

    @Override
    default BigDecimal asBigDecimalOrDefault(BigDecimal defaultValue) {
        return defaultValue;
    }

    @Override
    default BigInteger asBigInteger() {
        throw new ClassCastException(String.format("instance of %s could not be get as BigIntegerConverter", this.getClass()));
    }

    @Override
    default Optional<BigInteger> asBigIntegerOptional() {
        return Optional.empty();
    }

    @Override
    default BigInteger asBigIntegerOrDefault(BigInteger defaultValue) {
        return defaultValue;
    }

}
