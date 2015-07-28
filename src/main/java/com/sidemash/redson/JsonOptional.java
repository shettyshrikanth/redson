package com.sidemash.redson;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class JsonOptional implements JsonValue, Iterable<JsonValue> {

    public static final JsonOptional EMPTY = new JsonOptional(Optional.empty());
    private final Optional<JsonValue> value;

    private JsonOptional(Optional<JsonValue> value) {
        this.value = value;
    }


    public static<T> JsonOptional of(T value) {
        Objects.requireNonNull(value);
        if(value instanceof Optional)
            return of((Optional<?>) value);

        return new JsonOptional(Optional.of(JsonValue.of(value)));
    }


    public static<T> JsonOptional ofNullable(Optional<T> value) {
        if(value == null)
            return JsonOptional.EMPTY;

        return value
                .map(v -> new JsonOptional(Optional.of(Json.toJsonValue(v))))
                .orElse(JsonOptional.EMPTY);
    }

    /**
     * Turn the Optional passed as parameter as an instance of JsonOptional.
     * if the value Optional is empty then JsonOptional.EMPTY will be return
     * If not, a JsonOptional containing the same element converted to JsonValue.
     * Example :
     * JsonOptional.of( Optional.of("Hello") ) will return JsonOptional(JsonString("Hello"))
     * JsonOptional.of( Optional.of(1) ) will return  JsonOptional(JsonNumber(1))
     * JsonOptional.of( Optional.empty() ) will return  JsonOptional.EMPTY
     *
     * @param value Optional value
     * @param <T> Type of element contained in the Optional value
     * @throws NullPointerException if the value param is null
     * @return JsonOptional.EMPTY if the Optional value were empty and a JsonOptional
     *          containing the same element as the Optional value otherwise.
     */
    public static<T> JsonOptional of(Optional<T> value) {
        return value
                .map(v -> new JsonOptional(Optional.of(Json.toJsonValue(v))))
                .orElse(JsonOptional.EMPTY);
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
    public Optional<Object> asDefaultObject() {
        return value.map(JsonValue::asDefaultObject);
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
        throw new ClassCastException(
                String.format(
                        "instance of %s could not be get as Long",
                        this.getClass().getSimpleName()
                )
        );
    }


    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<JsonValue> iterator() {
        return value
                .map(jsonValue -> Collections.singleton(jsonValue).iterator())
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void forEach(Consumer<? super JsonValue> action) {
        value.ifPresent(action);
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
    public <T> Optional<T> asOptionalOf(Class<T> c) {
        return value.map(v -> Json.fromJsonValue(v, c));
    }

    @Override
    public <T> T asType(Class<T> cl) {
        return Json.fromJsonValue(value.get(), cl);
    }

    @Override
    public <T> Optional<T> asTypeOptional(Class<T> cl) {
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

    public JsonOptional filter(Predicate<? super JsonValue> predicate) {
        return new JsonOptional(value.filter(predicate));
    }

    public <U> JsonOptional flatMap(Function<? super JsonValue, Optional<U>> mapper) {
        return JsonOptional.of(value.flatMap(mapper));
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

    public void ifPresent(Consumer<? super JsonValue> consumer) {
        value.ifPresent(consumer);
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

    public boolean isPresent() {
        return value.isPresent();
    }

    public <U> JsonOptional map(Function<? super JsonValue, ? extends U> mapper) {
        return new JsonOptional(value.map(mapper.andThen(JsonValue::of)));
    }

    public JsonValue orElse(JsonValue other) {
        return value.orElse(other);
    }

    public JsonValue orElseGet(Supplier<? extends JsonValue> other) {
        return value.orElseGet(other);
    }

    public <X extends Throwable> JsonValue orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return value.orElseThrow(exceptionSupplier);
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
            return JsonNull.INSTANCE.stringify(keepingNull, true);
        else
            throw new UnsupportedOperationException("stringify an empty JsonOptional");
    }

    @Override
    public <T> Map<Integer, T> toIntIndexedMapOf(Class<T> cl, Map<Integer, T> map) {
        value.ifPresent(elem -> map.put(0, elem.asType(cl)));
        return map;
    }

    @Override
    public JsonNode toJsonNode() {
        return value
                .map(JsonValue::toJsonNode)
                .orElse(MissingNode.getInstance());
    }

    @Override
    public String toString() {
        return "JsonOptional{" +
                "value=" + value +
                '}';
    }

    @Override
    public <T> Map<String, T> toStringIndexedMapOf(Class<T> c) {
        return toStringIndexedMapOf(c, new LinkedHashMap<>());
    }

    @Override
    public <T> Map<String, T> toStringIndexedMapOf(Class<T> c, Map<String, T> map) {
        value.ifPresent(jsonValue -> map.put("0", Json.fromJsonValue(jsonValue, c)));
        return map;
    }

}
