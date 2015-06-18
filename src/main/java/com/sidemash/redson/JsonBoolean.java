package com.sidemash.redson;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.NoSuchElementException;
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
        public JsonBoolean negate(){
            return FALSE;
        }
        public boolean isTrue(){
            return true;
        }
        public boolean isFalse(){
            return false;
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
        public JsonBoolean negate(){
            return TRUE;
        }
        public boolean isTrue(){
            return false;
        }
        public boolean isFalse(){
            return true;
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
        return value;
    }

    @Override
    public Optional<Boolean> asBooleanOptional() {
        return Optional.of(value);
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
    public <T> Optional<T> asOptionalOf(Class<T> c) {
        return Optional.empty();
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
                "This method is only available for instances of " +
                        " JsonOptional not JsonBoolean"
        );
    }

    @Override
    public Optional<JsonValue> getOptional() {
        return Optional.empty();
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
