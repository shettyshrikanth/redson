package com.sidemash.redson;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;


public class JsonOptional implements JsonValue {

    public static final JsonOptional EMPTY = new JsonOptional(Optional.empty());
    private final Optional<JsonValue> value;

    private JsonOptional(Optional<JsonValue> value) {
        this.value = value;
    }


    public static<T> JsonOptional of(T value) {
        if(value == null)
            return JsonOptional.EMPTY;

        if(value instanceof Optional){
            Optional<?> optValue = (Optional<?>) value;
            return optValue
                    .map(v -> new JsonOptional(Optional.of(Json.toJsonValue(v))))
                    .orElse(JsonOptional.EMPTY);
        }
        return new JsonOptional(Optional.of(JsonValue.of(value)));
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
    public BigInteger asBigInteger() {
        throw new ClassCastException();
    }

    @Override
    public Optional<BigInteger> asBigIntegerOptional() {
        return Optional.empty();
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
    public byte asByte() {
        throw new ClassCastException();
    }

    @Override
    public Optional<Byte> asByteOptional() {
        return Optional.empty();
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
    public double asDouble() {
        throw new ClassCastException();
    }

    @Override
    public Optional<Double> asDoubleOptional() {
        return Optional.empty();
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
    public <T> List<T> asListOf(Class<T> cl, List<T> list) {
        if(value.isPresent())
            list.add(Json.fromJsonValue(value.get(), cl));

        return list;
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
    public Optional<JsonValue> asOptional() {
        return value;
    }

    @Override
    public <T> Optional<T> asOptionalOf(Class<T> c) {
       if(value.isPresent()) return Optional.of(Json.fromJsonValue(value.get(), c));
       else return Optional.empty();
    }

    @Override
    public <T> T asPojo(Class<T> cl) {
        return Json.fromJsonValue(value.get(), cl);
    }

    @Override
    public <T> Optional<T> asPojoOptional(Class<T> cl) {
        if(!value.isPresent())
            return Optional.empty();

        return Json.fromJsonValueOptional(value.get(), cl);
    }

    @Override
    public <T> Set<T> asSetOf(Class<T> cl, Set<T> set) {
        value.ifPresent((jsonValue) -> set.add(Json.fromJsonValue(jsonValue, cl)));
        return set;
    }

    @Override
    public <T> Set<T> asSetOf(Class<T> cl) {
        return asSetOf(cl, new HashSet<>());
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
    public String asString() {
        throw new ClassCastException();
    }

    @Override
    public <T> Map<String, T> asStringIndexedMapOf(Class<T> c) {
        return asStringIndexedMapOf(c, new LinkedHashMap<>());
    }

    @Override
    public <T> Map<String, T> asStringIndexedMapOf(Class<T> c, Map<String, T> map) {
        value.ifPresent(jsonValue -> map.put("0", Json.fromJsonValue(jsonValue, c)));
        return map;
    }

    @Override
    public Optional<String> asStringOptional() {
        return Optional.empty();
    }

    public boolean containsValue(Object value) {
        return this.value.isPresent() && this.value.get().equals(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonOptional that = (JsonOptional) o;

        return value.equals(that.value);
    }

    @Override
    public JsonValue get(int index) {
        throw new UnsupportedOperationException("Get item by index on JsonOptional");
    }

    public JsonValue get() {
        return value.get();
    }

    @Override
    public JsonValue get(String key) {
        throw new UnsupportedOperationException("Get item by key on JsonOptional");
    }

    @Override
    public Optional<JsonValue> getOptional() {
        return value;
    }

    @Override
    public Optional<JsonValue> getOptional(int index) {
        return Optional.empty();
    }

    @Override
    public Optional<JsonValue> getOptional(String key) {
        return Optional.empty();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
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
    public String prettyStringifyRecursive(int indent, int incrementAcc, boolean keepingNull, boolean emptyValuesToNull) {
        return stringify(keepingNull, emptyValuesToNull);
    }

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

}
