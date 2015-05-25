package com.sidemash.redson;


import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiFunction;

public class JsonOptional implements JsonValue {

    public static final JsonOptional EMPTY = new JsonOptional(Optional.empty());
    private final Optional<JsonValue> value;

    private JsonOptional(Optional<JsonValue> value) {
        this.value = value;
    }

    public static JsonOptional of(JsonValue jsonValue) {
        return new JsonOptional(Optional.of(jsonValue));
    }

    @Override
    public JsonValue append(JsonValue jsValue) {
        throw new UnsupportedOperationException("instance of JsonOptional does not permit this operation");
    }

    @Override
    public JsonValue append(String key, JsonValue jsValue) {
        throw new UnsupportedOperationException("instance of JsonOptional does not permit this operation");
    }

    @Override
    public JsonValue appendIfAbsent(String key, JsonValue jsValue) {
        throw new UnsupportedOperationException("instance of JsonOptional does not permit this operation");
    }

    @Override
    public Optional<? extends JsonValue> asOptional() {
        return value;
    }

    @Override
    public <T> Optional<T> asOptionalOf(Class<T> c) {
        return null;
    }

    @Override
    public <T> T asPojo(Class<T> cl) {
        return null;
    }

    @Override
    public <T> Optional<T> asPojoOptional() {
        return null;
    }

    @Override
    public <T> T asPojoOrDefault(T defaultValue) {
        return null;
    }

    @Override
    public <T> Set<T> asSetOf(Class<T> cl, Set<T> set) {
        return null;
    }

    @Override
    public <T> Set<T> asSetOf(Class<T> cl) {
        return null;
    }

    @Override
    public short asShort() {
        return 0;
    }

    @Override
    public Optional<Short> asShortOptional() {
        return null;
    }

    @Override
    public short asShortOrDefault(short defaultValue) {
        return 0;
    }

    @Override
    public String asString() {
        return null;
    }

    @Override
    public <T> Map<String, T> asStringIndexedMapOf(Class<T> c) {
        return null;
    }

    @Override
    public <T> Map<String, T> asStringIndexedMapOf(Class<T> c, Map<String, T> map) {
        return null;
    }

    @Override
    public Optional<String> asStringOptional() {
        return null;
    }

    @Override
    public String asStringOrDefault(String defaultValue) {
        return null;
    }

    @Override
    public boolean containsAll(JsonValue... jsValues) {
        return containsAll(Arrays.asList(jsValues));
    }

    @Override
    public boolean containsAll(List<? extends JsonValue> jsValues) {
        for (JsonValue value : jsValues){
            if(!this.containsValue(value))
                return false;
        }
        return true;
    }

    @Override
    public boolean containsKey(String key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return this.value.isPresent() && this.value.get().equals(value);
    }

    @Override
    public JsonValue distinct() {
        return null;
    }

    @Override
    public <T> T foldBreadth(T seed, BiFunction<T, ? super JsonEntry, T> fn) {
        return null;
    }

    @Override
    public <T> T foldDepth(T seed, BiFunction<T, ? super JsonEntry, T> fn) {
        return null;
    }

    @Override
    public JsonValue foldDepth(BiFunction<? super JsonValue, ? super JsonEntry, ? extends JsonValue> fn) {
        return null;
    }

    @Override
    public Set<Integer> getIndexSet() {
        return null;
    }

    @Override
    public Set<JsonEntry<Integer>> getIntIndexedEntrySet() {
        return null;
    }

    @Override
    public Set<JsonEntry<String>> getStringIndexedEntrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDefinedAt(int index) {
        return (index == 0 && value.isPresent());
    }

    @Override
    public boolean isDefinedAt(String key) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return (!value.isPresent());
    }

    @Override
    public boolean isJsonArray() {
        return false;
    }

    @Override
    public boolean isJsonBoolean() {
        return false;
    }

    @Override
    public boolean isJsonLiteral() {
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
    public boolean isJsonObject() {
        return false;
    }

    @Override
    public boolean isJsonOptional() {
        return true;
    }

    @Override
    public boolean isJsonString() {
        return false;
    }

    @Override
    public boolean isJsonStructure() {
        return false;
    }

    @Override
    public Set<String> keySet() {
        Set<String> result =
    }

    @Override
    public JsonValue prepend(JsonValue jsValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonValue prepend(String key, JsonValue jsValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonValue prependIfAbsent(String key, JsonValue jsValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String prettyStringifyRecursive(int indent, int incrementAcc, boolean keepingNull, boolean emptyValuesToNull) {
        return stringify(keepingNull, emptyValuesToNull);
    }

    @Override
    public JsonValue reverse() {
        return this;
    }

    @Override
    public int size() {
        return (value.isPresent()) ? 1 : 0;
    }

    @Override
    public String stringify(boolean keepingNull, boolean emptyValuesToNull) {
        if(value.isPresent())
            return value.get().stringify(keepingNull, emptyValuesToNull);
        else if(emptyValuesToNull)
            return JsonNull.INSTANCE.stringify(keepingNull, emptyValuesToNull);
        else
            throw new UnsupportedOperationException("stringify an empty JsonOptional");
    }

    @Override
    public String toString() {
        return "JsonOptional{" +
                "value=" + value +
                '}';
    }

    @Override
    public JsonValue union(JsonValue jsonValue) {
        throw new UnsupportedOperationException("Union on instance of JsonOptional");
    }

    @Override
    public JsonValue unionAll(List<? extends JsonValue> jsonValues) {
        throw new UnsupportedOperationException("UnionAll on instance of JsonOptional");
    }

    @Override
    public Collection<? extends JsonValue> values() {
        List<JsonValue> result = new ArrayList<>();
        value.ifPresent((jsonValue) -> result.add(jsonValue));
        return result;
    }

    @Override
    public Iterator<? extends JsonValue> valuesIterator() {
        return new Iterator<JsonValue>() {
            Optional<JsonValue> optJsValue = value;

            @Override
            public boolean hasNext() {
                return optJsValue.isPresent();
            }

            @Override
            public JsonValue next() {
                JsonValue result = optJsValue.orElseThrow(IllegalStateException::new);
                optJsValue = Optional.empty();
                return result;
            }
        };
    }

    @Override
    public Object getValue() {
        if(value.isPresent()) return Optional.of(value.get().getValue());
        else return Optional.empty();
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
    public <T> Map<Integer, T> asIntIndexedMapOf(Class<T> c, Map<Integer, T> map) {
        if(!value.isPresent())
           return  map;

        T instance = Json.fromJsonValue(value.get(), c);
        map.put(0, instance);
        return map;
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
    public <T> List<T> asListOf(Class<T> cl, List<T> list) {

    }

    @Override
    public <T> List<T> asListOf(Class<T> cl) {
        return asListOf(cl, new ArrayList<>());
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
}
