package com.sidemash.redson;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface JsonStructure extends JsonValue {

    @Override
    default BigDecimal asBigDecimal() {
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as BigDecimal",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    default <T> Optional<T> asOptionalOf(Class<T> cl){
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as Optional of %s",
                        this.getClass().getSimpleName(),
                        cl.getSimpleName()
                )
        );
    }

    @Override
    default Optional<BigDecimal> asBigDecimalOptional() {
        return Optional.empty();
    }

    @Override
    default BigInteger asBigInteger() {
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as BigInteger",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    default Optional<BigInteger> asBigIntegerOptional() {
        return Optional.empty();
    }

    @Override
    default boolean asBoolean() {
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as Boolean",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    default Optional<Boolean> asBooleanOptional() {
        return Optional.empty();
    }

    @Override
    default byte asByte() {
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as Byte",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    default Optional<Byte> asByteOptional() {
        return Optional.empty();
    }

    @Override
    default char asChar() {
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as Character",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    default Optional<Character> asCharOptional() {
        return Optional.empty();
    }

    @Override
    default double asDouble() {
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as Double",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    default Optional<Double> asDoubleOptional() {
        return Optional.empty();
    }

    @Override
    default float asFloat() {
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as Float",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    default Optional<Float> asFloatOptional() {
        return Optional.empty();
    }

    @Override
    default int asInt() {
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as Integer",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    default Optional<Integer> asIntOptional() {
        return Optional.empty();
    }

    @Override
    default long asLong() {
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as Long",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    default Optional<Long> asLongOptional() {
        return Optional.empty();
    }


    @Override
    default short asShort() {
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as Short",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    default Optional<Short> asShortOptional() {
        return Optional.empty();
    }

    @Override
    default String asString() {
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as String",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    default Optional<String> asStringOptional() {
        return Optional.empty();
    }

    boolean containsValue(Object value);

    @Override
    default JsonValue get() {
        throw new NoSuchElementException(
                String.format(
                        "This method is only available for instances of  JsonOptional not %s",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    default Optional<JsonValue> getOptional() {
        return Optional.empty();
    }

    Set<JsonEntry<String>> getStringIndexedEntrySet();

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

    default int length() { return size(); }

    int size();
}
