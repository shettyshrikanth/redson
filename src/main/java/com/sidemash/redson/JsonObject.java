package com.sidemash.redson;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sidemash.redson.util.ImmutableMap;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JsonObject implements JsonStructure, Iterable<JsonEntry<String>>, ImmutableMap<String, JsonValue> {


    public static final JsonObject EMPTY = new JsonObject(Collections.emptyList(), Collections.emptyMap());
    private final List<JsonEntry<String>> bindingsIntJsonValues;
    private final Map<String, Integer> bindingsKeysInt;

    private JsonObject(List<JsonEntry<String>> bindingsIntJsonValues, Map<String, Integer> bindingsKeysInt) {
        this.bindingsIntJsonValues = bindingsIntJsonValues;
        this.bindingsKeysInt = bindingsKeysInt;
    }

    private static List<JsonEntry<String>> newBindingJsonValues() { return new ArrayList<>(); }
    private static Map<String, Integer> newBindingKeys() { return new LinkedHashMap<>(); }
    // Constructors

    /**
     * Creates a JsonObject from the Map passed as parameter
     *
     * @param map
     * @param <V>
     * @return
     */
    public static <V> JsonObject of(Map<String, V> map) {
        if(map.isEmpty())
            return EMPTY;

        List<JsonEntry<String>> newBindings = newBindingJsonValues();
        Map<String, Integer> newBindingsKeys = newBindingKeys();
        int i = 0;
        for (Map.Entry<String, V> entry : map.entrySet()) {
            newBindings.add(JsonEntry.of(entry.getKey(), entry.getValue()));
            newBindingsKeys.put(entry.getKey(), i);
            i++;
        }
        return new JsonObject(newBindings, newBindingsKeys);
    }

    /**
     * Creates a JsonObject from the Map passed as parameter
     *
     * @param map
     * @param kToString Function to convert the Keys to a String
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> JsonObject of(Map<K, V> map, Function<K, String> kToString) {
        if(map.isEmpty())
            return EMPTY;

        List<JsonEntry<String>> newBindings = newBindingJsonValues();
        Map<String, Integer> newBindingsKeys = newBindingKeys();
        int i = 0;
        String tmpKey ;
        for (Map.Entry<K, V> entry : map.entrySet()) {
                tmpKey = kToString.apply(entry.getKey());
            if(!newBindingsKeys.containsKey(tmpKey)) {
                newBindings.add(JsonEntry.of(tmpKey, entry.getValue()));
                newBindingsKeys.put(tmpKey, i);
                i++;
            }
        }
        return new JsonObject(newBindings, newBindingsKeys);
    }

    /**
     * Creates a JsonObject composed of one entry which contains the key and the value passed as parameter
     * @param key
     * @param value
     * @return A new JsonObject with only one entry { key : value }
     */
    public static JsonObject of(String key, Object value) {
        List<JsonEntry<String>> newBindings = newBindingJsonValues();
        Map<String, Integer> newBindingsKeys = newBindingKeys();

        newBindings.add(JsonEntry.of(key, value));
        newBindingsKeys.put(key, 0);
        return new JsonObject(newBindings, newBindingsKeys);
    }

    /**
     * Create JsonObject from the Object passed as parameter
     * @param o
     * @throws ClassCastException if o can't be converted to a valid JsonObject
     * @return
     */
    public static JsonObject of(Object o) {
        return  (JsonObject) JsonValue.of(o);
    }

    public static JsonObject of(JsonEntry<String> jsonEntry) {
        Objects.requireNonNull(jsonEntry);

        List<JsonEntry<String>> newBindings = newBindingJsonValues();
        Map<String, Integer> newBindingsKeys = newBindingKeys();

        newBindings.add(jsonEntry);
        newBindingsKeys.put(jsonEntry.getKey(), 0);
        return new JsonObject(newBindings, newBindingsKeys);
    }

    @SafeVarargs
    public static JsonObject of(JsonEntry<String> jsonEntry1, JsonEntry<String>... jsonEntries) {
        List<JsonEntry<String>> newBindings = newBindingJsonValues();
        Map<String, Integer> newBindingsKeys = newBindingKeys();

        int i = 0;
        int tmpIndex;
        newBindings.add(Objects.requireNonNull(jsonEntry1));
        newBindingsKeys.put(jsonEntry1.getKey(), i);
        for (JsonEntry<String> entry : jsonEntries) {
            if(newBindingsKeys.containsKey(entry.getKey())) {
                tmpIndex = newBindingsKeys.get(entry.getKey());
                newBindings.remove(tmpIndex);
                newBindings.add(tmpIndex, Objects.requireNonNull(entry));
            }
            else {
                i++;
                newBindings.add(Objects.requireNonNull(entry));
                newBindingsKeys.put(entry.getKey(), i);
            }
        }
        return new JsonObject(newBindings, newBindingsKeys);
    }

    public static JsonObject of(Iterable<JsonEntry<String>> iterable) {
        return JsonObject.of(iterable.iterator());
    }


    public static JsonObject of(Stream<JsonEntry<String>> stream) {
        return JsonObject.of(stream.iterator());
    }


    public static JsonObject of(Iterator<JsonEntry<String>> it) {
        if (!it.hasNext())
            return JsonObject.EMPTY;

        List<JsonEntry<String>> newBindings = newBindingJsonValues();
        Map<String, Integer> newBindingsKeys = newBindingKeys();
        int i = 0;
        JsonEntry<String> entry;
        while (it.hasNext()) {
            entry = it.next();
            newBindings.add(Objects.requireNonNull(entry));
            newBindingsKeys.put(entry.getKey(), i);
            i++;
        }

        return new JsonObject(newBindings, newBindingsKeys);
    }


    public JsonObject append(String key, Object value) {
        List<JsonEntry<String>> newBindings = newBindingJsonValues();
        Map<String, Integer> newBindingsKeys = newBindingKeys();

        newBindings.addAll(bindingsIntJsonValues);
        newBindingsKeys.putAll(bindingsKeysInt);

        JsonEntry<String> entry = JsonEntry.of(key, value);
        newBindings.add(entry);
        newBindingsKeys.put(entry.getKey(), bindingsKeysInt.size());

        return new JsonObject(newBindings, newBindingsKeys);
    }

    @Override
    public <T> List<T> asListOf(Class<T> cl, List<T> list) {
        throw new ClassCastException(
                String.format(
                        "This %s could be interpreted as an instance of List<%s>",
                        this.getClass().getSimpleName(),
                        cl.getSimpleName()
                )
        );
    }

    @Override
    public <T> Map<String, T> asMapOf(Class<T> cl, Map<String, T> map) {
        this.stream().forEachOrdered(entry -> map.put(entry.getKey(), entry.getValue().as(cl)));
        return map;
    }

    @Override
    public <T> Set<T> asSetOf(Class<T> cl, Set<T> set) {
        throw new ClassCastException(
                String.format(
                        "This %s could be interpreted as an instance of Set<%s>",
                        this.getClass().getSimpleName(),
                        cl.getSimpleName()
                )
        );
    }

    public boolean containsAllKeys(List<String> allKeys) {
        return allKeys.stream().allMatch(bindingsKeysInt::containsKey);
    }

    public <E> boolean containsAllValues(List<E> allValues) {
        // FIXME find a better way
        return allValues.stream().allMatch(this::containsValue);
    }

    public boolean containsKey(String key) {
        return bindingsKeysInt.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return bindingsIntJsonValues.stream().anyMatch(entry -> entry.getValue().equals(value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonObject that = (JsonObject) o;

        if (!bindingsIntJsonValues.equals(that.bindingsIntJsonValues)) return false;
        return bindingsKeysInt.equals(that.bindingsKeysInt);

    }

    @Override
    public <U> U foldLeft(U seed, BiFunction<U, JsonEntry<String>, U> op) {
        U result = seed;
        for (JsonEntry<String> binding : bindingsIntJsonValues) {
            result = op.apply(result, binding);
        }
        return result;
    }

    @Override
    public JsonValue get(int index) {
        throw new UnsupportedOperationException("Get an item by index on a JsonObject");
    }

    @Override
    public JsonValue get(String key) {
        if(isEmpty())
            throw new NoSuchElementException(String.format("Impossible to find key '%s' in an empty JsonObject", key));

        return bindingsIntJsonValues.get(bindingsKeysInt.get(key)).getValue();
    }

    /**
     * Utility method to retrieve a JsonEntry by a key
     *
     * @param key the key of the entry
     * @return the entry which have key key
     */
    public JsonEntry<String> getEntry(String key) {
        return this.bindingsIntJsonValues.get(this.bindingsKeysInt.get(key));
    }

    @Override
    public JsonEntry<String> getHead() {
        if(isEmpty())
            throw new NoSuchElementException("Head of an empty JsonObject");

        return bindingsIntJsonValues.get(0);
    }

    public Set<JsonEntry<Integer>> getIntIndexedEntrySet() {
        throw new ClassCastException(
                String.format(
                        "A %s could not be interpreted as an Int indexed Map>",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    public JsonEntry<String> getLast() {
        if(isEmpty())
            throw new NoSuchElementException("Head of an empty JsonObject");

        return bindingsIntJsonValues.get(bindingsIntJsonValues.size() - 1);
    }

    @Override
    public Optional<JsonValue> getOptional(int index) {
        return Optional.<JsonValue>empty();
    }

    @Override
    public Optional<JsonValue> getOptional(String key) {
        return (bindingsKeysInt.containsKey(key))
                ? Optional.of(this.get(key))
                : Optional.empty();
    }

    @Override
    public JsonValue getOrDefault(int index, JsonValue jsonValue) {
        return jsonValue;
    }

    @Override
    public JsonValue getOrDefault(String key, JsonValue jsonValue) {
        return (bindingsKeysInt.containsKey(key))
                ? this.get(key)
                : jsonValue;
    }

    public Set<JsonEntry<String>> getStringIndexedEntrySet() {
        Set<JsonEntry<String>> result = new LinkedHashSet<>();
        result.addAll(bindingsIntJsonValues);
        return result;
    }

    @Override
    public JsonObject getTail() {
        if(this.isEmpty())
            throw new UnsupportedOperationException("Get tail of an EMPTY JsonObject");

        List<JsonEntry<String>> newBindings = bindingsIntJsonValues.subList(1, bindingsIntJsonValues.size());
        Map<String, Integer> newBindingsKeys = newBindingKeys();
        int i = 0;
        for(JsonEntry<String> entry : newBindings){
            newBindingsKeys.put(entry.getKey(), i);
            i++;
        }

        return new JsonObject(newBindings, newBindingsKeys);
    }

    @Override
    public int hashCode() {
        int result = bindingsIntJsonValues.hashCode();
        result = 31 * result + bindingsKeysInt.hashCode();
        return result;
    }

    public boolean isDefinedAt(String key) {
        return bindingsKeysInt.containsKey(key);
    }

    @Override
    public boolean isEmpty() {
        return bindingsKeysInt.isEmpty();
    }

    @Override
    public boolean isJsonArray() {
        return false;
    }

    @Override
    public boolean isJsonObject() {
        return true;
    }

    @Override
    public Iterator<JsonEntry<String>> iterator() {
        return bindingsIntJsonValues.iterator();
    }

    @Override
    public Iterator<String> keysIterator() {
        return new Iterator<String>() {

            final int length = bindingsIntJsonValues.size();
            int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return (currentIndex < length);
            }

            @Override
            public String next() {
                String next = bindingsIntJsonValues.get(currentIndex).getKey();
                currentIndex++;
                return next;
            }
        };
    }

    public Set<String> keysSet() {
        Set<String> keySet = new LinkedHashSet<>();
        keySet.addAll(bindingsKeysInt.keySet());
        return keySet;
    }

    public Stream<String> keysStream() {
        return StreamSupport.stream(
                Spliterators.spliterator(
                        keysIterator(),
                        bindingsKeysInt.size(),
                        Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.DISTINCT
                ),
                false
        );
    }

    @Override
    public String prettyStringifyRecursive(int indent,
                                           int incrementAcc,
                                           boolean keepingNull,
                                           boolean emptyValuesToNull) {
        String result;
        if (this.isEmpty()) {
            result = "{}";
        } else {
            StringBuilder startInc = new StringBuilder();
            for (int i = 0; i < incrementAcc; ++i) {
                startInc.append(" ");
            }
            StringBuilder endInc = new StringBuilder();
            for (int i = 0; i < incrementAcc - indent; ++i) {
                endInc.append(" ");
            }
            final String startIncrementation = startInc.toString();
            final String endIncrementation = endInc.toString();
            StringJoiner sj = new StringJoiner(",\n", "{\n", String.format("\n%s}", endIncrementation));
            JsonValue value;
            for (JsonEntry<String> entry : this) {
                value = entry.getValue();
                if (emptyValuesToNull && value.isJsonOptional() && value.isEmpty())
                    value = JsonNull.INSTANCE;
                if (Json.isEligibleForStringify(value, keepingNull)) {
                    sj = sj.add(
                            String.format("%s\"%s\":%s",
                                    startIncrementation,
                                    entry.getKey(),
                                    value.prettyStringifyRecursive(indent,
                                            incrementAcc + indent,
                                            keepingNull,
                                            emptyValuesToNull)
                            ));
                }

            }
            result = sj.toString();
            if(result.equals(String.format("{\n\n%s}", endIncrementation)))
                result = "{}";
        }
        return result;
    }

    public JsonObject remove(String key) {
        if (!this.isDefinedAt(key))
            return this;

        Map<String, Integer> newBindingsKeys = newBindingKeys();
        List<JsonEntry<String>> newBindings = newBindingJsonValues();
        newBindings.addAll(bindingsIntJsonValues);
        int indexToRemove = bindingsKeysInt.get(key);
        newBindings.remove(indexToRemove);
        int i = 0;
        for(JsonEntry<String> entry : newBindings){
            newBindingsKeys.put(entry.getKey(), i);
            i++;
        }

        return new JsonObject(newBindings, newBindingsKeys);
    }

    public JsonObject remove(String key, JsonValue oldJsonValue) {
        if (!this.containsKey(key) || this.get(key).equals(oldJsonValue))
            return this;

        return remove(key);
    }

    @Override
    public int size() {
        return bindingsKeysInt.size();
    }

    @Override
    public JsonObject sorted(Comparator<? super JsonEntry<String>> comparator) {
        return JsonObject.of(this.stream().sorted(comparator));
    }

    public Stream<JsonEntry<String>> stream() {
        return StreamSupport.stream(
                Spliterators.spliterator(
                        iterator(),
                        bindingsKeysInt.size(),
                        Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.DISTINCT
                ),
                false
        );
    }

    @Override
    public String stringify(boolean keepingNull, boolean emptyValuesToNull) {
        if (this.isEmpty())
            return "{}";

        StringJoiner sj = new StringJoiner(",", "{", "}");
        JsonValue value;
        for(JsonEntry<String> entry : bindingsIntJsonValues){
            value = entry.getValue();
            if (emptyValuesToNull && value.isJsonOptional() && value.isEmpty())
                value = JsonNull.INSTANCE;
            if (Json.isEligibleForStringify(value, keepingNull))
                sj = sj.add(String.format("\"%s\":%s", entry.getKey(), value.stringify(keepingNull, emptyValuesToNull)));
        }

        return sj.toString();
    }

    @Override
    public <T> Map<Integer, T> toIntIndexedMapOf(Class<T> cl, Map<Integer, T> map) {
        throw new ClassCastException(
                String.format(
                        "This %s could be interpreted as an instance of Map<Integer, %s>",
                        this.getClass().getSimpleName(),
                        cl.getSimpleName()
                )
        );
    }

    @Override
    public JsonNode toJsonNode() {
        ObjectNode result = JsonNodeFactory.instance.objectNode();
        bindingsIntJsonValues.stream().forEachOrdered(entry -> {
            result.set(entry.getKey(), entry.getValue().toJsonNode());
        });
        return result;
    }

    public List<JsonEntry<String>> toList(){
        return Collections.unmodifiableList(bindingsIntJsonValues);
    }

    @Override
    public String toString() {
        return "JsonObject{" +
                "bindings=" + bindingsIntJsonValues +
                '}';
    }

    @Override
    public <T> Map<String, T> toStringIndexedMapOf(Class<T> cl, java.util.Map<String, T> map) {
        for (JsonEntry<String> entry : bindingsIntJsonValues) {
            map.put(entry.getKey(), entry.getValue().as(cl));
        }

        return map;
    }

    public JsonObject union(JsonObject other) {

        if (this.isEmpty())
            return other;

        else if (other.isEmpty())
            return this;

        else{
            JsonObject result;

            Map<String, Integer> newBindingsKeys = newBindingKeys();
            List<JsonEntry<String>> newBindings = newBindingJsonValues();

            newBindings.addAll(this.bindingsIntJsonValues);
            newBindingsKeys.putAll(this.bindingsKeysInt);

            int i = newBindings.size();
            int indexToRemove;
            for(Map.Entry<String, Integer> entry : other.bindingsKeysInt.entrySet()){
                if(newBindingsKeys.containsKey(entry.getKey())){
                    indexToRemove = newBindingsKeys.get(entry.getKey());
                    newBindings.remove(indexToRemove);
                    newBindings.add(indexToRemove, JsonEntry.of(entry.getKey(), entry.getValue()));
                }
                else {
                    newBindings.add(JsonEntry.of(entry.getKey(), other.bindingsIntJsonValues.get(entry.getValue())));
                    newBindingsKeys.put(entry.getKey(), i);
                    i++;
                }
            }

            result = new JsonObject(newBindings, newBindingsKeys);

            return result;
        }

    }

    public JsonObject unionAll(List<JsonObject> jsonObjects) {

        JsonObject result = this;
        for(JsonObject other : jsonObjects){
            result = result.union(other);
        }
        return result;
    }

    @Override
    public JsonObject updateEntry(String oldEntryKey, JsonEntry<String> newEntry) {
        return updateEntry(oldEntryKey, ignored -> newEntry);
    }

    @Override
    public JsonObject updateEntry(String oldEntryKey, UnaryOperator<JsonEntry<String>> operator) {
        if (!this.isDefinedAt(oldEntryKey))
            return this;

        Map<String, Integer> newBindingsKeys = newBindingKeys();
        List<JsonEntry<String>> newBindingsValues = newBindingJsonValues();
        for(Map.Entry<String, Integer> entryKey : bindingsKeysInt.entrySet()) {
            int index = this.bindingsKeysInt.get(entryKey.getKey());
            JsonEntry<String> entry = this.bindingsIntJsonValues.get(index);
            if (entryKey.getKey().equals(oldEntryKey)) {
                JsonEntry<String> newValue = operator.apply(entry);
                newBindingsKeys.put(newValue.getKey(), index);
                newBindingsValues.add(index, newValue);
            }
            else {
                newBindingsKeys.put(entryKey.getKey(), index);
                newBindingsValues.add(index, entry);
            }
        }

        return new JsonObject(newBindingsValues, newBindingsKeys);
    }

    public JsonObject updateKey(String oldKey, String newKey) {
        if (!this.isDefinedAt(oldKey))
            return this;

        Map<String, Integer> newBindingsKeys = newBindingKeys();
        List<JsonEntry<String>> newBindingsValues = newBindingJsonValues();
        for (Map.Entry<String, Integer> entryKey : this.bindingsKeysInt.entrySet()) {
            int index = this.bindingsKeysInt.get(entryKey.getKey());
            if (entryKey.getKey().equals(oldKey)) {
                newBindingsKeys.put(newKey, index);
                newBindingsValues.add(index, JsonEntry.of(newKey, this.get(oldKey)));
            }
            else{
                newBindingsKeys.put(entryKey.getKey(), index);
                newBindingsValues.add(index, this.bindingsIntJsonValues.get(index));
            }
        }

        return new JsonObject(newBindingsValues, newBindingsKeys);
    }

    @Override
    public JsonObject updateKey(String oldKey, UnaryOperator<String> operator) {
        return updateKey(oldKey, operator.apply(oldKey));
    }

    public JsonObject updateKey(String oldKey, JsonValue oldJsonValue, String newKey) {
        if (!this.isDefinedAt(oldKey) || !oldJsonValue.equals(this.get(oldKey)))
            return this;
        else
            return updateKey(oldKey, newKey);
    }

    public JsonObject updateValue(String key, JsonValue newJsonValue) {
        if (!this.isDefinedAt(key))
            return this;

        List<JsonEntry<String>> newBindingsValues = newBindingJsonValues();
        for (Map.Entry<String, Integer> entryKey : this.bindingsKeysInt.entrySet()) {
            int index = this.bindingsKeysInt.get(key);
            if (entryKey.getKey().equals(key))
                newBindingsValues.add(index, JsonEntry.of(key, newJsonValue));
            else
                newBindingsValues.add(index, this.bindingsIntJsonValues.get(index));
        }

        return new JsonObject(newBindingsValues, this.bindingsKeysInt);
    }

    public JsonObject updateValue(String key, Function<JsonEntry<String>, JsonValue> operator) {
        return updateValue(key, operator.apply(this.getEntry(key)));
    }

    public JsonObject updateValue(String key, JsonValue oldJsonValue, JsonValue newJsonValue) {
        if (!this.isDefinedAt(key) || !this.get(key).equals(oldJsonValue))
            return this;
        else
            return updateValue(key, newJsonValue);

    }

    public Iterator<JsonValue> valuesIterator() {
        return new Iterator<JsonValue>() {

            final Iterator<JsonEntry<String>> iterator = bindingsIntJsonValues.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public JsonValue next() {
                return iterator.next().getValue();
            }
        };
    }

    public Stream<JsonValue> valuesStream() {
        return StreamSupport.stream(
                Spliterators.spliterator(
                        valuesIterator(),
                        bindingsKeysInt.size(),
                        Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.DISTINCT
                ),
                false
        );
    }

}
