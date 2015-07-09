package com.sidemash.redson;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sidemash.redson.util.ImmutableMap;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonObject implements JsonStructure, Iterable<JsonEntry<String>>, ImmutableMap<String, JsonValue> {


    public static final JsonObject EMPTY = new JsonObject(Collections.emptyMap());
    private final Map<String, JsonValue> items;

    // Constructors
    private JsonObject(final Map<String, JsonValue> items) {
        this.items = items;
    }

    private static Map<String, JsonValue> newItems() { return new LinkedHashMap<>(); }


    /**
     * A builder for a JsonObject to construct a JsonObject
     * by adding values step by step. This a implementation
     * of the Builder pattern.
     */
    public static class Builder {

        private final Map<String, JsonValue> items;

        private Builder() {
            this.items = new HashMap<>();
        }

        public Builder append(String key, JsonValue value) {
            this.items.put(key, value);
            return this;
        }

        public Builder append(JsonEntry<String> entry) {
            this.items.put(entry.getKey(), entry.getValue());
            return this;
        }

        public Builder append(Map<String, JsonValue> values) {
            this.items.putAll(values);
            return this;
        }

        public Builder append(List<JsonEntry<String>> entries) {
            entries.stream().forEach(entry -> this.append(entry));
            return this;
        }

        public JsonObject build() {
            if (this.items.isEmpty())
                return JsonObject.EMPTY;
            else
                return new JsonObject(items);
        }
    }

    /**
     * Method to get a builder for a JsonObject.
     *
     * @return the constructed builder for <type>JsonObject</type>.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a JsonObject from the Map passed as parameter
     *
     * @param map
     * @param <V>
     * @return
     */
    public static <V> JsonObject of(final Map<String, V> map) {
        return JsonObject.of(map, Function.<String>identity());
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
    public static <K, V> JsonObject of(final Map<K, V> map, final Function<K, String> kToString) {
        if(map.isEmpty())
            return EMPTY;

        Builder builder = JsonObject.builder();
        map.forEach((key, value) -> builder.append(kToString.apply(key), JsonValue.of(value)));

        return builder.build();
    }

    /**
     * Creates a JsonObject composed of one entry which contains the key and the value passed as parameter
     * @param key
     * @param value
     * @return A new JsonObject with only one entry { key : value }
     */
    public static JsonObject of(final String key, final Object value) {
        Objects.requireNonNull(key);
        return JsonObject.of(
                Collections.singletonMap(key, value),
                Function.<String>identity()
        );
    }

    /**
     * Create JsonObject from the Object passed as parameter
     * @param o
     * @throws ClassCastException if o can't be converted to a valid JsonObject
     * @return
     */
    public static JsonObject of(final Object o) {
        return  (JsonObject) JsonValue.of(o);
    }

    /**
     * Create JsonObject from an jsonEntry passed as parameter
     * @param jsonEntry
     * @return
     */
    public static JsonObject of(final JsonEntry<String> jsonEntry) {
        return JsonObject.of(
                Collections.singletonMap(jsonEntry.getKey(), jsonEntry.getValue()),
                Function.<String>identity()
        );
    }

    @SafeVarargs
    public static JsonObject of(final JsonEntry<String> jsonEntry1, final JsonEntry<String>... jsonEntries) {
        Objects.requireNonNull(jsonEntry1);
        List<JsonEntry<String>> itemsList = new ArrayList<>();
        itemsList.add(jsonEntry1);
        itemsList.addAll(Arrays.asList(jsonEntries));
        return of(itemsList.iterator());
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

        Builder builder = JsonObject.builder();
        it.forEachRemaining(entry -> builder.append(entry.getKey(), entry.getValue()));

        return builder.build();
    }

    public JsonObject append(String key, Object value) {
        Objects.requireNonNull(key);
        Builder builder = JsonObject.builder();
        return builder
                .append(this.items)
                .append(key, JsonValue.of(value))
                .build();
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
        this.stream()
            .forEachOrdered(entry -> map.put(entry.getKey(), entry.getValue().as(cl)));
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

    @SafeVarargs
    public final boolean containsAllEntries(final JsonEntry<String>... entry){
        return this.containsAllEntries(Arrays.asList(entry));
    }

    public boolean containsAllEntries(final Iterable<JsonEntry<String>> jsonEntries){
        for(JsonEntry<String> jsonEntry : jsonEntries){
            if(!this.containsEntry(jsonEntry))
                return false;
        }
        return true;
    }

    public boolean containsAllKeys(Iterable<String> allKeys) {
        for(String key : allKeys){
            if(!items.containsKey(key))
                return false;
        }
        return true;
    }

    public boolean containsAllKeys(String... allKeys) {
        return this.containsAllKeys(Arrays.asList(allKeys));
    }

    public <E> boolean containsAllValues(Iterable<E> allValues) {
        for(E value : allValues){
            if(!items.containsValue(value))
                return false;
        }
        return true;
    }

    public <E> boolean containsAllValues(E... allValues) {
        return this.containsAllValues(Arrays.asList(allValues));
    }

    public boolean containsEntry(JsonEntry<String> entry){
        return items.containsKey(entry.getKey()) && get(entry.getKey()).equals(entry.getValue());
    }

    public boolean containsKey(String key) {
        return items.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return items.containsValue(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonObject that = (JsonObject) o;

        return items.equals(that.items);
    }

    @Override
    public <U> U foldLeft(final U seed, final BiFunction<U, JsonEntry<String>, U> op) {
        U result = seed;
        for (Map.Entry<String, JsonValue> binding : items.entrySet()) {
            result = op.apply(result, JsonEntry.of(binding));
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

        return items.get(key);
    }

    /**
     * Get a JsonEntry by a key
     *
     * @param key the key of the entry
     * @return the entry which have key key
     */
    public JsonEntry<String> getEntry(String key) {
        return JsonEntry.of(key, get(key));
    }

    @Override
    public JsonEntry<String> getHead() {
        if(isEmpty())
            throw new NoSuchElementException("Head of an empty JsonObject");

        // FIXME find a better way
        return items.entrySet()
                .stream()
                .findFirst()
                .map(JsonEntry::of)
                .get();
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

        return items.entrySet()
                .stream()
                .skip(items.size() - 1)
                .findFirst()
                .map(JsonEntry::of)
                .get();
    }

    @Override
    public Optional<JsonValue> getOptional(int index) {
        return Optional.<JsonValue>empty();
    }

    @Override
    public Optional<JsonValue> getOptional(String key) {
        return (items.containsKey(key))
                ? Optional.of(this.get(key))
                : Optional.empty();
    }

    @Override
    public JsonValue getOrDefault(int index, JsonValue jsonValue) {
        return jsonValue;
    }

    @Override
    public JsonValue getOrDefault(String key, JsonValue jsonValue) {
        return items.getOrDefault(key, jsonValue);
    }

    public Set<JsonEntry<String>> getStringIndexedEntrySet() {
        Set<JsonEntry<String>> result = new LinkedHashSet<>();
        items.forEach((key, value) -> result.add(JsonEntry.of(key, value)) );
        return result;
    }

    @Override
    public JsonObject getTail() {
        if(this.isEmpty())
            throw new UnsupportedOperationException("Get tail of an EMPTY JsonObject");

        return JsonObject.of(
            items.entrySet()
                 .stream()
                 .skip(1)
                 .map(JsonEntry::of)
        );
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }

    public boolean isDefinedAt(String key) {
        return items.containsKey(key);
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
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
        return items.entrySet()
                    .stream()
                    .map(JsonEntry::of)
                    .iterator();
    }

    @Override
    public Iterator<String> keysIterator() {
        return this.keysStream().iterator();
    }

    public Set<String> keysSet() {
        Set<String> keySet = new LinkedHashSet<>();
        keySet.addAll(items.keySet());
        return keySet;
    }

    public Stream<String> keysStream() {
        return items.keySet().stream();
    }

    public <R> JsonObject map(Function<? super JsonEntry<String>, R> mapper) {
        return JsonObject.of(this.stream().map(mapper.andThen(JsonValue::of)));
    }

    public <R> JsonObject mapValues(Function<? super JsonValue, R> mapper) {
        return this.map(entry -> mapper.apply(entry.getValue()));
    }

    public <R> JsonObject mapKeys(Function<? super String, R> mapper) {
        return this.map(entry -> mapper.apply(entry.getKey()));
    }

    @Override
    public String toString() {
        return "JsonObject{" +
                "items=" + items +
                '}';
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

    /**
     * Return a new JsonObject where the key specified as parameter has
     * been removed.
     * Note : If this JsonObject is empty, then nothing will occur and the
     *          empty JsonObject will be returned as it since it does not
     *          contain the key specified as parameter.
     *
     * @param key the key to remove
     * @return a new JsonObject without the key passed in param
     */
    public JsonObject remove(String key) {
        if (!this.isDefinedAt(key))
            return this;

        Map<String, JsonValue> newItems = newItems();
        newItems.putAll(items);
        newItems.remove(key);
        return new JsonObject(newItems);
    }

    public JsonObject remove(String key, JsonValue oldJsonValue) {
        if (this.containsKey(key) && this.get(key).equals(oldJsonValue))
            return remove(key);

        return this;
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public JsonObject sorted(Comparator<? super JsonEntry<String>> comparator) {
        return JsonObject.of(this.stream().sorted(comparator));
    }

    public Stream<JsonEntry<String>> stream() {
        return items.entrySet()
                    .stream()
                    .map(JsonEntry::of);
    }

    @Override
    public String stringify(boolean keepingNull, boolean emptyValuesToNull) {
        if (this.isEmpty())
            return "{}";

        StringJoiner sj = new StringJoiner(",", "{", "}");
        JsonValue value;
        for(Map.Entry<String, JsonValue> entry : items.entrySet()){
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
        items.forEach((key, value) -> {
            result.set(key, value.toJsonNode());
        });
        return result;
    }

    public List<JsonEntry<String>> toList(){
        return this.stream().collect(Collectors.toList());
    }


    @Override
    public <T> Map<String, T> toStringIndexedMapOf(Class<T> cl, java.util.Map<String, T> map) {
        for (Map.Entry<String, JsonValue> entry : items.entrySet()) {
            map.put(entry.getKey(), entry.getValue().asPojo(cl));
        }
        return map;
    }


    public JsonObject union(JsonObject other) {

        if (this.isEmpty())
            return other;

        else if (other.isEmpty())
            return this;

        else{
            Builder builder = JsonObject.builder();
            return builder
                    .append(this.items)
                    .append(other.items)
                    .build();
        }
    }

    public JsonObject unionAll(List<JsonObject> jsonObjects) {
        Builder builder = JsonObject.builder();
        jsonObjects.forEach(jsonObject -> builder.append(jsonObject.items));
        return builder.build();
    }

    @Override
    public JsonObject updateEntry(String oldEntryKey, JsonEntry<String> newEntry) {
        return updateEntry(oldEntryKey, ignored -> newEntry);
    }

    @Override
    public JsonObject updateEntry(String oldEntryKey, UnaryOperator<JsonEntry<String>> operator) {
        if (!this.isDefinedAt(oldEntryKey))
            return this;

        Builder builder = builder();
        this.forEach(entry -> {
            if (entry.getKey().equals(oldEntryKey))
                builder.append(operator.apply(entry));
            else
                builder.append(entry);
        });

        return builder.build();

    }

    public JsonObject updateKey(String oldKey, String newKey) {
        if (!this.isDefinedAt(oldKey))
            return this;

        Builder builder = builder();
        this.forEach(entry -> {
            if (entry.getKey().equals(oldKey))
                builder.append(newKey, entry.getValue());
            else
                builder.append(entry);
        });

        return builder.build();
    }

    @Override
    public JsonObject updateKey(String oldKey, UnaryOperator<String> operator) {
        return updateKey(oldKey, operator.apply(oldKey));
    }

    public JsonObject updateKey(String oldKey, JsonValue oldJsonValue, String newKey) {
        if (this.isDefinedAt(oldKey) && oldJsonValue.equals(this.get(oldKey)))
            return updateKey(oldKey, newKey);

        return this;
    }

    public JsonObject updateValue(String key, JsonValue newJsonValue) {
        if (!this.isDefinedAt(key))
            return this;

        Map<String, JsonValue> newItems = newItems();
        newItems.putAll(this.items);
        newItems.put(key, newJsonValue);
        return new JsonObject(newItems);
    }

    public JsonObject updateValue(String key, Function<JsonEntry<String>, JsonValue> operator) {
        return updateValue(key, operator.apply(this.getEntry(key)));
    }

    public JsonObject updateValue(String key, JsonValue oldJsonValue, JsonValue newJsonValue) {
        if (this.isDefinedAt(key) && this.get(key).equals(oldJsonValue))
            return updateValue(key, newJsonValue);

        return this;

    }

    public Iterator<JsonValue> valuesIterator() {
        return items.values().iterator();
    }

    public Stream<JsonValue> valuesStream() {
        return items.values().stream();
    }

}
