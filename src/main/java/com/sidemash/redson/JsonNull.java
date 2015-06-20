package com.sidemash.redson;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public enum JsonNull implements JsonLiteral {

    INSTANCE;

    @Override
    public BigDecimal asBigDecimal() {
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as BigDecimal",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    public Optional<BigDecimal> asBigDecimalOptional() {
        return Optional.empty();
    }

    @Override
    public BigInteger asBigInteger() {
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as BigInteger",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    public Optional<BigInteger> asBigIntegerOptional() {
        return Optional.empty();
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
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as Byte",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    public Optional<Byte> asByteOptional() {
        return Optional.empty();
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
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as Double",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    public Optional<Double> asDoubleOptional() {
        return Optional.empty();
    }

    @Override
    public float asFloat() {
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as Float",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    public Optional<Float> asFloatOptional() {
        return Optional.empty();
    }

    @Override
    public int asInt() {
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as Integer",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    public Optional<Integer> asIntOptional() {
        return Optional.empty();
    }

    @Override
    public long asLong() {
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as Long",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    public Optional<Long> asLongOptional() {
        return Optional.empty();
    }

    @Override
    public <T> Map<String, T> asMapOf(Class<T> cl, Map<String, T> map) {
        throw new ClassCastException(
                String.format(
                        "This %s could be interpreted as an instance of Map<String, %s>",
                        this.getClass().getSimpleName(),
                        cl.getSimpleName()
                )
        );
    }

    @Override
    public short asShort() {
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as Short",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    public Optional<Short> asShortOptional() {
        return Optional.empty();
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
