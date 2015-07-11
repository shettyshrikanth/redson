package com.sidemash.redson;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The root interface in the <i>Redson json hierarchy</i>. JsonValue is a supertype
 * of all json types.
 *
 */
@SuppressWarnings("unused")
public interface JsonValue {

    /**
     * Returns a json representation from an <tt>object</tt>.
     *
     * @param o the object which will be converted to json
     * @return a JsonValue representation of the object
     */
    static JsonValue of(final Object o){
        return Json.toJsonValue(o);
    }

    /**
     * Method to deserialize JSON content by parsing an array of bytes.
     *
     * @param bytes the array of bytes representing the object
     * @return a jsonValue representation of the object
     * @throws IOException
     */
    static  JsonValue parse(byte[] bytes) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return JsonValue.of(mapper.readTree(bytes));
    }

    /**
     * Method to deserialize JSON content store in a file.
     *
     * @param file the parsed file which contents the JSON data
     * @return a jsonValue representation of the object
     * @throws IOException
     */
    static JsonValue parse(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return JsonValue.of(mapper.readTree(file));
    }

    /**
     * Method to deserialize JSON content stored in a file at the given path.
     *
     * @param path the path where the file to parse is located
     * @return a jsonValue representation of the object
     * @throws IOException
     */
    static  JsonValue parse(Path path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return JsonValue.of(mapper.readTree(path.toFile()));
    }

    /**
     * Method to deserialize JSON content given from an input stream
     *
     * @param in the input stream which streams JSON data
     * @return a jsonValue representation of the object
     * @throws IOException
     */
    static  JsonValue parse(InputStream in) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return JsonValue.of(mapper.readTree(in));
    }

    /**
     * Method to deserialize JSON content given from a reader
     *
     * @param reader the reader who reads JSON data
     * @return a jsonValue representation of the object
     * @throws IOException
     */
    static  JsonValue parse(Reader reader) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return JsonValue.of(mapper.readTree(reader));
    }

    /**
     * Method to deserialize a JSON string.
     *
     * @param string <tt>String</tt> representation of the JSON data
     * @return a jsonValue representation of the object
     * @throws IOException
     */
    static JsonValue parse(String string) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return JsonValue.of(mapper.readTree(string));
    }

    /**
     * Method which convert a <tt>JsonValue</tt> to an <tt>Object</tt> type passed as parameter.
     *
     * @param type the class we want to convert JsonValue to.
     * @param <T> the return type (same as parameter)
     * @return the converted object
     */
    default <T> T asType(Type type) {
        return Json.fromJsonValue(this, type);
    }
    default <T> T asTypeOrDefault(Type type, T defaultValue) {
        return this.<T>asTypeOptional(type).orElse(defaultValue);
    }
    default <T> Optional<T> asTypeOptional(Type type) {
        Optional<T> result;
        try {
            result = Optional.of(asType(type));
        } catch (Exception ex) {
            result = Optional.empty();
        }
        return result;
    }

    /**
     * Method which convert a <tt>JsonValue</tt> to a parametrized <tt>Object</tt> type passed as parameter.
     *
     * @param typeReference the parametrized class we want to convert JsonValue to.
     * @param <T> the return type (same as parameter)
     * @return the converted object
     */
    default <T> T asType(TypeReference<T> typeReference) {
        return Json.fromJsonValue(this, typeReference);
    }
    default <T> T asTypeOrDefault(TypeReference<T> typeReference, T defaultValue) {
        return this.<T>asTypeOptional(typeReference).orElse(defaultValue);
    }
    default <T> Optional<T> asTypeOptional(TypeReference<T> typeReference) {
        Optional<T> result;
        try {
            result = Optional.of(asType(typeReference));
        } catch (Exception ex) {
            result = Optional.empty();
        }
        return result;
    }

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>BigDecimal</tt>.
     *
     * @return the converted object
     */
    BigDecimal asBigDecimal();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>BigDecimal</tt> wrapped in an option.
     *
     * @return an option containing the <tt>BigDecimal</tt> value.
     */
    Optional<BigDecimal> asBigDecimalOptional();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>BigDecimal</tt> if it
     * can be converted or the default <tt>BigDecimal</tt> value passes as
     * parameter if it can't.
     *
     * @return the resulting <tt>BigDecimal</tt>.
     */
    default BigDecimal asBigDecimalOrDefault(BigDecimal defaultValue) {
        return asBigDecimalOptional().orElse(defaultValue);
    }

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>BigInteger</tt>.
     *
     * @return the converted <tt>BigDecimal</tt> value.
     */
    BigInteger asBigInteger();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>BigInteger</tt> wrapped in an option.
     *
     * @return an option containing the <tt>BigInteger</tt> value.
     */
    Optional<BigInteger> asBigIntegerOptional();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>BigInteger</tt> if it
     * can be converted or the default <tt>BigInteger</tt> value passes as
     * parameter if it can't.
     *
     * @return the resulting <tt>BigInteger</tt>.
     */
    default BigInteger asBigIntegerOrDefault(BigInteger defaultValue) {
        return asBigIntegerOptional().orElse(defaultValue);
    }

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Boolean</tt>.
     *
     * @return the converted <tt>Boolean</tt> value.
     */
    boolean asBoolean();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Boolean</tt> wrapped in an option.
     *
     * @return an option containing the <tt>Boolean</tt> value.
     */
    Optional<Boolean> asBooleanOptional();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Boolean</tt> if it
     * can be converted or the default <tt>Boolean</tt> value passes as
     * parameter if it can't.
     *
     * @return the resulting <tt>Boolean</tt>.
     */
    default boolean asBooleanOrDefault(boolean defaultValue) {
        return asBooleanOptional().orElse(defaultValue);
    }

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Byte</tt>.
     *
     * @return the converted <tt>Byte</tt> value.
     */
    byte asByte();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Byte</tt> wrapped in an option.
     *
     * @return an option containing the <tt>Byte</tt> value.
     */
    Optional<Byte> asByteOptional();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Byte</tt> if it
     * can be converted or the default <tt>Byte</tt> value passes as
     * parameter if it can't.
     *
     * @return the resulting <tt>Byte</tt>.
     */
    default byte asByteOrDefault(byte defaultValue) {
        return asByteOptional().orElse(defaultValue);
    }

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Character</tt>.
     *
     * @return the converted <tt>Character</tt> value.
     */
    char asChar();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Character</tt> wrapped in an option.
     *
     * @return an option containing the <tt>Character</tt> value.
     */
    Optional<Character> asCharOptional();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Character</tt> if it
     * can be converted or the default <tt>Character</tt> value passes as
     * parameter if it can't.
     *
     * @return the resulting <tt>Character</tt>.
     */
    default char asCharOrDefault(char defaultValue) {
        return asCharOptional().orElse(defaultValue);
    }

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Double</tt>.
     *
     * @return the converted <tt>Double</tt> value.
     */
    double asDouble();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Double</tt> wrapped in an option.
     *
     * @return an option containing the <tt>Double</tt> value.
     */
    Optional<Double> asDoubleOptional();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Double</tt> if it
     * can be converted or the default <tt>Double</tt> value passes as
     * parameter if it can't.
     *
     * @return the resulting <tt>Double</tt>.
     */
    default double asDoubleOrDefault(double defaultValue) {
        return asDoubleOptional().orElse(defaultValue);
    }

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Float</tt>.
     *
     * @return the converted <tt>Float</tt> value.
     */
    float asFloat();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Float</tt> wrapped in an option.
     *
     * @return an option containing the <tt>Float</tt> value.
     */
    Optional<Float> asFloatOptional();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Float</tt> if it
     * can be converted or the default <tt>Float</tt> value passes as
     * parameter if it can't.
     *
     * @return the resulting <tt>Float</tt>.
     */
    default float asFloatOrDefault(float defaultValue) {
        return asFloatOptional().orElse(defaultValue);
    }

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Integer</tt>.
     *
     * @return the converted <tt>Integer</tt> value.
     */
    int asInt();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Integer</tt> wrapped in an option.
     *
     * @return an option containing the <tt>Integer</tt> value.
     */
    Optional<Integer> asIntOptional();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Integer</tt> if it
     * can be converted or the default <tt>Integer</tt> value passes as
     * parameter if it can't.
     *
     * @return the resulting <tt>Integer</tt>.
     */
    default int asIntOrDefault(int defaultValue) {
        return asIntOptional().orElse(defaultValue);
    }

    /**
     * Method which converts the JsonValue a List whose type
     * is type of the second parameter. Each element of the list will be
     * instance of the class passed as first parameter.
     *
     * @param cl class of object contained in the list
     * @param list the type of the result list
     * @param <T> the parameter type for the list
     * @return the resulting List
     */
    <T> List<T> asListOf(Class<T> cl, List<T> list);

    /**
     * Method which converts the JsonValue to an ArrayList.
     * Element of the list of will be instance of the parameter type.
     *
     * @param cl class of the element of the list
     * @param <T> type of element of the list
     * @return the resulting ArrayList
     */
    default <T> List<T> asListOf(Class<T> cl) {
        return asListOf(cl, new ArrayList<>());
    }

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Long</tt>.
     *
     * @return the converted <tt>Long</tt> value.
     */
    long asLong();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Long</tt> wrapped in an option.
     *
     * @return an option containing the <tt>Long</tt> value.
     */
    Optional<Long> asLongOptional();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Long</tt> if it
     * can be converted or the default <tt>Long</tt> value passes as
     * parameter if it can't.
     *
     * @return the resulting <tt>Long</tt>.
     */
    default long asLongOrDefault(long defaultValue) {
        return asLongOptional().orElse(defaultValue);
    }

    /**
     * Method to convert a JsonValue to the Map type passed as parameter.
     *
     * @param cl the class of values stored in the map.
     * @param map the Map type we want to convert to.
     * @param <T> the paramtrized type of the map.
     * @return the converted map.
     */
    <T> Map<String, T> asMapOf(Class<T> cl,  Map<String, T> map);

    /**
     * Method to convert a JsonValue to the HashMap.
     *
     * @param cl the class of values stored in the map.
     * @param <T> the paramtrized type of the map.
     * @return the converted Map.
     */
    default <T> Map<String, T> asMapOf(Class<T> cl){
        return asMapOf(cl, new HashMap<>());
    }

    /**
     * Method wich converts the JsonValue as an Optional which
     * contains the JsonValue if it is equals to not JsonNull or
     * JsonOptional.EMPTY.
     *
     * @return the resulting Optional.
     */
    default Optional<? extends JsonValue> asOptional() {
        return Optional.of(this);
    }

    /**
     * Method wich converts the JsonValue as an Optional which
     * contains the JsonValue if it is equals to not
     * JsonNull or JsonOptional.EMPTY.
     * The JsonValue will be casted as instance of the first parameter
     *
     * @param cl
     * @param <T>
     * @return
     */
    default <T> Optional<T> asOptionalOf(Class<T> cl){
        return Json.fromJsonValueOptional(this, cl);
    }

    default <T> T asType(Class<T> cl) {
        return Json.fromJsonValue(this, cl);
    }

    default <T> Optional<T> asTypeOptional(Class<T> cl){
        return Json.fromJsonValueOptional(this, cl);
    }

    default <T> T asTypeOrDefault(Class<T> cl, T defaultValue) {
        return Json.fromJsonValueOrDefault(this, cl, defaultValue);
    }

    /**
     * Method which converts the JsonValue a Set whose type
     * is type of the second parameter. Each element of the set will be
     * instance of the class passed as first parameter.
     *
     * @param cl class of object contained in the list
     * @param set the type of the result set
     * @param <T> the parameter type for the set
     * @return the resulting Set
     */
    <T> Set<T> asSetOf(Class<T> cl,  Set<T> set);

    /**
     * Method which converts the JsonValue to an LinkedHashSet.
     * Element of the set of will be instance of the parameter type.
     *
     * @param cl class of the element of the set
     * @param <T> type of element of the set
     * @return the resulting LinkedHashSet
     */
    default <T> Set<T> asSetOf(Class<T> cl){
        return asSetOf(cl, new LinkedHashSet<>());
    }

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Short</tt>.
     *
     * @return the converted <tt>Short</tt> value.
     */
    short asShort();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Short</tt> wrapped in an option.
     *
     * @return an option containing the <tt>Short</tt> value.
     */
    Optional<Short> asShortOptional();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>Short</tt> if it
     * can be converted or the default <tt>Short</tt> value passes as
     * parameter if it can't.
     *
     * @return the resulting <tt>Short</tt>.
     */
    default short asShortOrDefault(short defaultValue) {
        return asShortOptional().orElse(defaultValue);
    }

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>String</tt>.
     *
     * @return the converted <tt>String</tt> value.
     */
    String  asString();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>String</tt> wrapped in an option.
     *
     * @return an option containing the <tt>String</tt> value.
     */
    Optional<String> asStringOptional();

    /**
     * Method which convert a <tt>JsonValue</tt> to a <tt>String</tt> if it
     * can be converted or the default <tt>String</tt> value passes as
     * parameter if it can't.
     *
     * @return the resulting <tt>String</tt>.
     */
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

    /**
     * Method which test if the JsonValue is empty
     *
     * @return true if the JsonValue is empty
     */
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

    /**
     * Method to test if a JsonValue is instance of a JsonArray.
     *
     * @return true if the JsonValue is a JsonArray.
     */
    boolean isJsonArray();

    /**
     * Method to test if a JsonValue is instance of a JsonBoolean.
     *
     * @return true if the JsonValue is a JsonBoolean.
     */
    boolean isJsonBoolean();

    /**
     * Method to test if a JsonValue is instance of a JsonLiteral
     * (ie javascript primitive type: String, Number, Boolean, null, Undefined).
     *
     * @return true if the JsonValue is a JsonLiteral.
     */
    boolean isJsonLiteral();

    /**
     * Method to test if a JsonValue is instance of a JsonNull.
     *
     * @return true if the JsonValue is a JsonNull.
     */
    boolean isJsonNull();

    /**
     * Method to test if a JsonValue is instance of a JsonNumber.
     *
     * @return true if the JsonValue is a JsonNumber.
     */
    boolean isJsonNumber();

    /**
     * Method to test if a JsonValue is instance of a JsonObject.
     *
     * @return true if the JsonValue is a JsonObject.
     */
    boolean isJsonObject();

    /**
     * Method to test if a JsonValue is instance of a JsonOptional.
     *
     * @return true if the JsonValue is instance of a JsonOptional.
     */
    boolean isJsonOptional();

    /**
     * Method to test if a JsonValue is instance of a JsonString.
     *
     * @return true if the JsonValue is instance of a JsonString.
     */
    boolean isJsonString();

    /**
     * Method to test if a JsonValue is instance of a JsonArray or a JsonObject.
     *
     * @return true if the JsonValue is instance of a JsonArray or a JsonObject.
     */
    boolean isJsonStructure();

    /**
     * Method which test if the JsonValue is not empty
     *
     * @return true if the JsonValue is not empty
     */
    default boolean isNotEmpty() {
        return (!isEmpty());
    }

    /**
     * Method which print the JsonValue as a well formatted string.
     *
     * @return the formatted String.
     */
    default String prettyStringify() {
        final int indent = 3;
        return prettyStringify(indent);
    }

    /**
     * Method which print the JsonValue as a well formatted String.
     * It take as parameter the number of spaces used for indentation.
     *
     * @param indent the number of spaces used for indentation.
     * @return the formatted String.
     */
    default String prettyStringify(int indent) {
        final boolean keepingNull = false;
        final boolean emptyValuesToNull = false;
        return prettyStringify(indent, keepingNull, emptyValuesToNull);
    }

    /**
     * Method which print the JsonValue as a well formatted String.
     * It take as parameter the boolean which specifies whether null
     * should be keep in the Json representation.
     *
     * @param keepingNull true if we want to keep null values.
     * @return the formatted String.
     */
    default String prettyStringify(boolean keepingNull) {
        final int indent = 3;
        final boolean emptyValuesToNull = false;
        return prettyStringify(indent, keepingNull, emptyValuesToNull);
    }

    /**
     * Method which print the JsonValue as a well formatted String.
     * It take as first parameter the boolean which specifies whether
     * null should be keep in the Json representation. It takes
     * as second parameter a boolean that speciies if we want to convert empty
     * values to null.
     *
     * @param keepingNull true if we want to keep null values.
     * @param emptyValuesToNull true if we want to convert empty values to null.
     * @return the formatted String.
     */
    default String prettyStringify(boolean keepingNull, boolean emptyValuesToNull) {
        final int indent = 3;
        return prettyStringify(indent, keepingNull, emptyValuesToNull);
    }

    /**
     * Method which print the JsonValue as a well formatted String.
     * It take as first parameter the boolean which specifies whether
     * null should be keep in the Json representation. It takes
     * as second parameter a boolean that speciies if we want to convert empty
     * values to null.
     *
     * @param indent true if we want to keep null values.
     * @param keepingNull true if we want to keep null values.
     * @param emptyValuesToNull true if we want to convert empty values to null.
     * @return the formatted String.
     */
    default String prettyStringify(int indent, boolean keepingNull, boolean emptyValuesToNull) {
        return prettyStringifyRecursive(indent, indent, keepingNull, emptyValuesToNull);
    }

    String prettyStringifyRecursive(int indent, int incrementAcc, boolean keepingNull, boolean emptyValuesToNull);

    /**
     * Return a string representation of the JsonValue.
     *
     * @return the stringified json
     */
    default String stringify(){
        final boolean keepingNull = false;
        final boolean emptyValuesToNull = false;
        return stringify(keepingNull, emptyValuesToNull);
    }

    /**
     * Return a string representation of the JsonValue.
     *
     * @param keepingNull true to keep null value in the json string.
     * @param emptyValuesToNull true to convert empty values into null.
     * @return the stringified json
     */
    String stringify(boolean keepingNull, boolean emptyValuesToNull);

    /**
     * Return a string representation of the JsonValue.
     *
     * @param keepingNull true to keep null value in the json string.
     * @return the stringified json
     */
    default String stringify(boolean keepingNull){
        final boolean emptyValuesToNull = false;
        return stringify(keepingNull, emptyValuesToNull);
    }

    /**
     * Method which convert the JsonValue to a Map whose instance
     * of the class of the map passed as parameter. Class of element of the
     * result map will be the first parameter. The result map will be indexed
     * by the order of the field in the JsonValue.
     *
     * @param c class of the element stored in the map.
     * @param map the map type we will used for result type.
     * @param <T> Map parameter result type.
     * @return the resulting Map.
     */
    <T> Map<Integer, T> toIntIndexedMapOf(Class<T> c, Map<Integer, T> map);

    /**
     * Method which convert the JsonValue to a LinkedHashMap.
     * Class of element of the result map will be the first parameter. The
     * result map will be indexed by the order of the field in the JsonValue.
     *
     * @param c class of the element stored in the map.
     * @param <T> LinkedHashMap parameter result type.
     * @return the resulting LinkedHashMap.
     */
    default <T> Map<Integer, T> toIntIndexedMapOf(Class<T> c) {
        return toIntIndexedMapOf(c, new LinkedHashMap<>());
    }

    /**
     * Convert the JsonValue to a JsonNode.
     *
     * @return the resulting JsonNode.
     */
    JsonNode toJsonNode();

    /**
     * Method which convert the JsonValue to a LinkedHashMap. Class of
     * element of the result map will be the first parameter. The result map
     * will be indexed by the name of the field in the JsonValue.
     *
     * @param cl class of the element stored in the map.
     * @param <T> LinkedHashMap parameter result type.
     * @return the resulting LinkedHashMap.
     */
    default <T> Map<String, T> toStringIndexedMapOf(Class<T> cl){
        return toStringIndexedMapOf(cl, new LinkedHashMap<>());
    }

    /**
     * Method which convert the JsonValue to a Map whose instance
     * of the class of the map passed as parameter. Class of element of the
     * result map will be the first parameter. The result map will be indexed
     * by the name of the field in the JsonValue
     *
     * @param c class of the element stored in the map.
     * @param map the map type we will used for result type.
     * @param <T> Map parameter result type.
     * @return the resulting Map.
     */
    <T> Map<String, T> toStringIndexedMapOf(Class<T> c, Map<String, T> map);

}
