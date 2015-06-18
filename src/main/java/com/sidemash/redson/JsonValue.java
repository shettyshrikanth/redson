package com.sidemash.redson;


import com.fasterxml.jackson.core.type.TypeReference;

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

    BigDecimal asBigDecimal();

    Optional<BigDecimal> asBigDecimalOptional();

    BigDecimal asBigDecimalOrDefault(BigDecimal defaultValue);

    BigInteger asBigInteger();

    Optional<BigInteger> asBigIntegerOptional();

    BigInteger asBigIntegerOrDefault(BigInteger defaultValue);

    boolean asBoolean();

    Optional<Boolean> asBooleanOptional();

    boolean asBooleanOrDefault(boolean defaultValue);

    byte asByte();

    Optional<Byte> asByteOptional();

    byte asByteOrDefault(byte defaultValue);

    char asChar();

    Optional<Character> asCharOptional();

    char asCharOrDefault(char defaultValue);

    double asDouble();

    Optional<Double> asDoubleOptional();

    double asDoubleOrDefault(double defaultValue);

    float asFloat();

    Optional<Float> asFloatOptional();

    float asFloatOrDefault(float defaultValue);

    int asInt();

    <T> Map<Integer, T> asIntIndexedMapOf(Class<T> c, Map<Integer, T> map);

    default <T> Map<Integer, T> asIntIndexedMapOf(Class<T> c) {
        return asIntIndexedMapOf(c, new LinkedHashMap<>());
    }

    Optional<Integer> asIntOptional();

    int asIntOrDefault(int defaultValue);

    <T> List<T> asListOf(Class<T> cl, List<T> list);

    // <C,T> Object asContainerOf(Class<C> containerClass, Class<T> objectClass);

    default <T> List<T> asListOf(Class<T> cl) {
        return asListOf(cl, new ArrayList<>());
    }

    long asLong();

    Optional<Long> asLongOptional();

    long asLongOrDefault(long defaultValue);

    default Optional<? extends JsonValue> asOptional() {
        return Optional.of(this);
    }

    default <T> Optional<T> asOptionalOf(Class<T> cl){
        return Json.fromJsonValueOptional(this, cl);
    }

    default <T> T as(Type type ) {
        return Json.fromJsonValue(this, type);
    }


    default <T> T as(TypeReference<T> typeReference) {
        return Json.fromJsonValue(this, typeReference);
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

    short asShortOrDefault(short defaultValue);

    String  asString();

    default <T> Map<String, T> asStringIndexedMapOf(Class<T> cl){
        return asStringIndexedMapOf(cl, new LinkedHashMap<>());
    }

    <T> Map<String, T> asStringIndexedMapOf(Class<T> c,  Map<String, T> map);

    Optional<String> asStringOptional();

    String  asStringOrDefault(String defaultValue);


    JsonValue get(int index);

    JsonValue get(String key);

    Optional<JsonValue> getOptional(int index);

    Optional<JsonValue> getOptional(String key);

    JsonValue getOrDefault(int index, JsonValue jsonValue);

    JsonValue getOrDefault(String key, JsonValue jsonValue);

    Object getValue();
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


}
