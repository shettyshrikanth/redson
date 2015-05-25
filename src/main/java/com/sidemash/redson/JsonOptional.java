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
    public <T> T asPojoOrDefault(Class<T> cl, T defaultValue) {
        if(!value.isPresent())
            return defaultValue;

        return Json.fromJsonValueOptional(value.get(), cl).orElse(defaultValue);
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
    public short asShortOrDefault(short defaultValue) {
        return defaultValue;
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

    @Override
    public String asStringOrDefault(String defaultValue) {
        return defaultValue;
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
        return this;
    }

    @Override
    public Set<Integer> getIndexSet() {
        Set<Integer> set = new HashSet<>();
        if(value.isPresent())
            set.add(0);

        return set;
    }

    @Override
    public Set<JsonEntry<Integer>> getIntIndexedEntrySet() {
        Set<JsonEntry<Integer>> set = new HashSet<>();
        if(value.isPresent())
            set.add(new JsonEntry<>(0, value.get()));

        return set;
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
        Set<String> result = new HashSet<>();
        if(value.isPresent())
            result.add("0");

        return result;
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
        value.ifPresent(jsonValue -> result.add(jsonValue));
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
    public long asLongOrDefault(long defaultValue) {
        return defaultValue;
    }
}
