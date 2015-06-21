package com.sidemash.redson;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public interface JsonValue {

    static JsonValue of(Object o){
        return Json.toJsonValue(o);
    }

    default <T> T as(Type type) {
        return Json.fromJsonValue(this, type);
    }

    default <T> T as(TypeReference<T> typeReference) {
        return Json.fromJsonValue(this, typeReference);
    }

    BigDecimal asBigDecimal();

    Optional<BigDecimal> asBigDecimalOptional();

    default BigDecimal asBigDecimalOrDefault(BigDecimal defaultValue) {
        return asBigDecimalOptional().orElse(defaultValue);
    }

    BigInteger asBigInteger();

    Optional<BigInteger> asBigIntegerOptional();

    default BigInteger asBigIntegerOrDefault(BigInteger defaultValue) {
        return asBigIntegerOptional().orElse(defaultValue);
    }

    boolean asBoolean();

    Optional<Boolean> asBooleanOptional();

    default boolean asBooleanOrDefault(boolean defaultValue) {
        return asBooleanOptional().orElse(defaultValue);
    }

    byte asByte();

    Optional<Byte> asByteOptional();

    default byte asByteOrDefault(byte defaultValue) {
        return asByteOptional().orElse(defaultValue);
    }

    char asChar();

    Optional<Character> asCharOptional();

    default char asCharOrDefault(char defaultValue) {
        return asCharOptional().orElse(defaultValue);
    }

    double asDouble();

    Optional<Double> asDoubleOptional();

    default double asDoubleOrDefault(double defaultValue) {
        return asDoubleOptional().orElse(defaultValue);
    }

    float asFloat();

    Optional<Float> asFloatOptional();

    default float asFloatOrDefault(float defaultValue) {
        return asFloatOptional().orElse(defaultValue);
    }

    int asInt();

    Optional<Integer> asIntOptional();

    default int asIntOrDefault(int defaultValue) {
        return asIntOptional().orElse(defaultValue);
    }

    <T> List<T> asListOf(Class<T> cl, List<T> list);

    default <T> List<T> asListOf(Class<T> cl) {
        return asListOf(cl, new ArrayList<>());
    }

    long asLong();

    Optional<Long> asLongOptional();

    default long asLongOrDefault(long defaultValue) {
        return asLongOptional().orElse(defaultValue);
    }

    <T> Map<String, T> asMapOf(Class<T> cl,  Map<String, T> map);

    default <T> Map<String, T> asMapOf(Class<T> cl){
        return asMapOf(cl, new HashMap<>());
    }

    default Optional<? extends JsonValue> asOptional() {
        return Optional.of(this);
    }

    default <T> Optional<T> asOptionalOf(Class<T> cl){
        return Json.fromJsonValueOptional(this, cl);
    }

    default <T> T asPojo(Class<T> cl) {
        return Json.fromJsonValue(this, cl);
    }

    default <T> Optional<T> asPojoOptional(Class<T> cl){
        return Json.fromJsonValueOptional(this, cl);
    }

    default <T> T asPojoOrDefault(Class<T> cl, T defaultValue) {
        return Json.fromJsonValueOrDefault(this, cl, defaultValue);
    }

    <T> Set<T> asSetOf(Class<T> cl,  Set<T> set);

    default <T> Set<T> asSetOf(Class<T> cl){
        return asSetOf(cl, new LinkedHashSet<>());
    }

    short asShort();

    Optional<Short> asShortOptional();

    default short asShortOrDefault(short defaultValue) {
        return asShortOptional().orElse(defaultValue);
    }

    String  asString();

    Optional<String> asStringOptional();

    default String asStringOrDefault(String defaultValue) {
        return asStringOptional().orElse(defaultValue);
    }

    JsonValue get(int index);

    JsonValue get(String key);

    JsonValue get();

    Optional<JsonValue> getOptional(int index);

    Optional<JsonValue> getOptional(String key);

    Optional<JsonValue> getOptional();

    default JsonValue getOrDefault(int index, JsonValue defaultValue) {
        return getOptional(index).orElse(defaultValue);
    }

    default JsonValue getOrDefault(String key, JsonValue defaultValue) {
        return getOptional(key).orElse(defaultValue);
    }

    default JsonValue getOrDefault(JsonValue defaultValue) {
        return getOptional().orElse(defaultValue);
    }

    /**
     * Executes thenFn callback if conditionFn function is true, and elseFn if false.
     *
     * @param conditionFn
     * @param thenFn
     * @param elseFn
     * @param <T>
     * @return
     */
    default <T> T ifCondition(Supplier<Boolean> conditionFn,
                              Function<? super JsonValue, ? extends T> thenFn,
                              Function<? super JsonValue, ? extends T> elseFn) {
        if(conditionFn.get())
            return thenFn.apply(this);
        else
            return elseFn.apply(this);
    }

    /**
     * Executes thenFn callback if conditionFn function is true, returns current JsonValue if not.
     *
     * @param conditionFn
     * @param thenFn
     * @return
     */
    default JsonValue ifConditionElseThis(Supplier<Boolean> conditionFn,
                                          Function<? super JsonValue, ? extends JsonValue> thenFn) {
        if(conditionFn.get())
            return thenFn.apply(this);
        else
            return this;
    }

    /**
     * Executes thenFn callback if the object is a JsonStructure, and elseFn if not.
     *
     * @param thenFn
     * @param elseFn
     * @param <T>
     * @return
     */
    default <T> T ifJsonStructure(Function<? super JsonValue, ? extends T> thenFn,
                                  Function<? super JsonValue, ? extends T> elseFn) {
        return ifCondition(this::isJsonStructure, thenFn, elseFn);
    }

    /**
     * Executes thenFn callback if the object is a JsonStructure, returns current JsonValue if not.
     *
     * @param thenFn
     * @return
     */
    default JsonValue ifJsonStructureElseThis(Function<? super JsonValue, ? extends JsonValue> thenFn) {
        return ifConditionElseThis(this::isJsonStructure, thenFn);
    }

    boolean isEmpty();
/*
    <T> T ifJsonArray(Function<? super JsonValue, ? extends T> thenFn,Function<? super JsonValue, ? extends T> elseFn);

    JsonValue ifJsonArrayElseThis(Function<? super JsonValue, ? extends JsonValue> thenFn);

    <T> T ifJsonBoolean(Function<? super JsonValue, ? extends T> thenFn,Function<? super JsonValue, ? extends T> elseFn);

    JsonValue ifJsonBooleanElseThis(Function<? super JsonValue, ? extends JsonValue> thenFn);

    <T> T ifJsonLiteral(Function<? super JsonValue, ? extends T> thenFn,Function<? super JsonValue, ? extends T> elseFn);

    JsonValue ifJsonLiteralElseThis(Function<? super JsonValue, ? extends JsonValue> thenFn);

    <T> T ifJsonNull(Function<? super JsonValue, ? extends T> thenFn,Function<? super JsonValue, ? extends T> elseFn);

    JsonValue ifJsonNullElseThis(Function<? super JsonValue, ? extends JsonValue> thenFn);

    <T> T ifJsonNumber(Function<? super JsonValue, ? extends T> thenFn,Function<? super JsonValue, ? extends T> elseFn);

    JsonValue ifJsonNumberElseThis(Function<? super JsonValue, ? extends JsonValue> thenFn);

    <T> T ifJsonObject(Function<? super JsonValue, ? extends T> thenFn, Function<? super JsonValue, ? extends T> elseFn);

    JsonValue ifJsonObjectElseThis(Function<? super JsonValue, ? extends JsonValue> thenFn);

    <T> T ifJsonString(Function<? super JsonValue, ? extends T> thenFn, Function<? super JsonValue, ? extends T> elseFn);

    JsonValue ifJsonStringElseThis(Function<? super JsonValue, ? extends JsonValue> thenFn);
*/

    boolean isJsonArray();

    boolean isJsonBoolean();

    boolean isJsonLiteral();

    boolean isJsonNull();

    boolean isJsonNumber();

    boolean isJsonObject();

    boolean isJsonOptional();

    boolean isJsonString();

    boolean isJsonStructure();

    default boolean isNotEmpty() {
        return (!isEmpty());
    }

    default String prettyStringify() {
        final int indent = 3;
        return prettyStringify(indent);
    }

    default String prettyStringify(int indent) {
        final boolean keepingNull = false;
        final boolean emptyValuesToNull = false;
        return prettyStringify(indent, keepingNull, emptyValuesToNull);
    }

    default String prettyStringify(boolean keepingNull) {
        final int indent = 3;
        final boolean emptyValuesToNull = false;
        return prettyStringify(indent, keepingNull, emptyValuesToNull);
    }

    default String prettyStringify(boolean keepingNull, boolean emptyValuesToNull) {
        final int indent = 3;
        return prettyStringify(indent, keepingNull, emptyValuesToNull);
    }

    default String prettyStringify(int indent, boolean keepingNull, boolean emptyValuesToNull){
        return prettyStringifyRecursive(indent, indent, keepingNull, emptyValuesToNull);
    }

    String prettyStringifyRecursive(int indent, int incrementAcc, boolean keepingNull, boolean emptyValuesToNull);

    default String stringify(){
        final boolean keepingNull = false;
        final boolean emptyValuesToNull = false;
        return stringify(keepingNull, emptyValuesToNull);
    }

    String stringify(boolean keepingNull, boolean emptyValuesToNull);

    default String stringify(boolean keepingNull){
        final boolean emptyValuesToNull = false;
        return stringify(keepingNull, emptyValuesToNull);
    }

    <T> Map<Integer, T> toIntIndexedMapOf(Class<T> c, Map<Integer, T> map);

    default <T> Map<Integer, T> toIntIndexedMapOf(Class<T> c) {
        return toIntIndexedMapOf(c, new LinkedHashMap<>());
    }

    JsonNode toJsonNode();

    default <T> Map<String, T> toStringIndexedMapOf(Class<T> cl){
        return toStringIndexedMapOf(cl, new LinkedHashMap<>());
    }

    <T> Map<String, T> toStringIndexedMapOf(Class<T> c, Map<String, T> map);


}
