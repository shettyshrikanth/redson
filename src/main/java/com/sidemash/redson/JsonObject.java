package com.sidemash.redson;


import com.sidemash.redson.util.ImmutableMap;
import scala.Tuple2;
import scala.collection.Traversable;
import scala.collection.immutable.Map;
import scala.collection.immutable.Map$;
import scala.collection.mutable.Builder;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JsonObject implements JsonStructure, Iterable<JsonEntry<String>>, ImmutableMap<String, JsonValue> {


    public static final JsonObject EMPTY = new JsonObject(Map$.MODULE$.empty());
    private final Map<String, JsonValue> bindings;

    private JsonObject(Map<String, JsonValue> bindings) {
        this.bindings = bindings;
    }

    // Constructors

    public static <V> JsonObject of(java.util.Map<String, V> map) {
        if(map.isEmpty())
            return EMPTY;

        Builder<Tuple2<String, JsonValue>, Map> mapBuilder = Map$.MODULE$.newBuilder();
        for (java.util.Map.Entry<String, V> entry : map.entrySet()) {
            mapBuilder.$plus$eq(new Tuple2<>(entry.getKey(), Json.toJsonValue(entry.getValue())));
        }
        return new JsonObject(mapBuilder.result());
    }

    public static <K, V> JsonObject of(java.util.Map<K, V> map, Function<K, String> kToString) {
        if(map.isEmpty())
            return EMPTY;

        Builder<Tuple2<String, JsonValue>, Map> mapBuilder = Map$.MODULE$.newBuilder();
        for (java.util.Map.Entry<K, V> entry : map.entrySet()) {
            mapBuilder.$plus$eq(new Tuple2<>(kToString.apply(entry.getKey()), Json.toJsonValue(entry.getValue())));
        }
        return new JsonObject(mapBuilder.result());
    }

    public static JsonObject of(String key, Object o) {
        Tuple2<String, JsonValue> tuple = new Tuple2<>(key, Json.toJsonValue(o));
        Map<String, JsonValue> map = Map$.MODULE$.<String, JsonValue>empty();

        return new JsonObject(map.$plus(tuple));
    }

    public static JsonObject of(Object o) {
        JsonObject result = (JsonObject) Json.toJsonValue(o);
        return result;
    }

    @SafeVarargs
    public static JsonObject of(JsonEntry<String> jsonEntry1, JsonEntry<String>... jsonEntries) {
        Builder<Tuple2<String, JsonValue>, Map> mapBuilder = Map$.MODULE$.newBuilder();
        mapBuilder.$plus$eq(new Tuple2<>(jsonEntry1.getKey().toString(), jsonEntry1.getValue()));
        for (JsonEntry<String> entry : jsonEntries) {
            mapBuilder.$plus$eq(new Tuple2<>(entry.getKey(), entry.getValue()));
        }
        return new JsonObject(mapBuilder.result());
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

        Builder<Tuple2<String, JsonValue>, Map> mapBuilder = Map$.MODULE$.newBuilder();
        while (it.hasNext()) {
            mapBuilder.$plus$eq(jsonEntry2Tuple2(it.next()));
        }
        return new JsonObject(mapBuilder.result());
    }

    // traversers

    // getters

    private static JsonEntry<String> tuple2ToJsonEntry(Tuple2<String, JsonValue> tuple2) {
        return JsonEntry.of(tuple2._1(), tuple2._2());
    }

    private static Tuple2<String, JsonValue> jsonEntry2Tuple2(JsonEntry<String> jsonEntry) {
        return new Tuple2<>(jsonEntry.getKey(), jsonEntry.getValue());
    }

    // others operations
    public JsonObject append(String key, JsonValue jsValue) {
        Tuple2<String, JsonValue> tuple = new Tuple2<>(key, jsValue);
        return new JsonObject(bindings.$plus(tuple));
    }

    // class casters
    @Override
    public <T> java.util.Map<Integer, T> asIntIndexedMapOf(Class<T> c, java.util.Map<Integer, T> map) {
        if (bindings.isEmpty())
            return map;
        scala.collection.Iterator<JsonValue> valueIterator = bindings.valuesIterator();
        int i = 0;
        while (valueIterator.hasNext()) {
            map.put(i, Json.fromJsonValue(valueIterator.next(), c));
            ++i;
        }
        return map;
    }

    // setters

    // functionnals methods

    @Override
    public <T> List<T> asListOf(Class<T> cl, List<T> list) {
        final scala.collection.Iterator<JsonValue> valueIterator = bindings.valuesIterator();
        while (valueIterator.hasNext())
            list.add(Json.fromJsonValue(valueIterator.next(), cl));
        return list;
    }

    @Override
    public <T> Set<T> asSetOf(Class<T> cl, Set<T> set) {
        final scala.collection.Iterator<JsonValue> valuesIterator = bindings.valuesIterator();
        while (valuesIterator.hasNext())
            set.add(Json.fromJsonValue(valuesIterator.next(), cl));
        return set;
    }

    @Override
    public <T> java.util.Map<String, T> asStringIndexedMapOf(Class<T> c, java.util.Map<String, T> map) {
        scala.collection.Iterator<Tuple2<String, JsonValue>> iterator = bindings.iterator();
        while (iterator.hasNext()) {
            Tuple2<String, JsonValue> element = iterator.next();
            map.put(element._1(), Json.fromJsonValue(element._2(), c));
        }
        return map;
    }

    public boolean containsAllKeys(List<String> keys) {
        return keys.stream().allMatch(bindings::contains);
    }
    // set operations ( operations e,semblistes cardinal, union, intersect, differences,  )

    // general operations on Map

    // iterators & streamers

    public <E> boolean containsAllValues(List<E> values) {
        // FIXME find a better way
        return values.stream().allMatch(this::containsValue);
    }

    public boolean containsKey(String key) {
        return bindings.contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        scala.collection.Iterator<JsonValue> iterator = bindings.valuesIterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(value))
                return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonObject that = (JsonObject) o;

        return bindings.equals(that.bindings);
    }

    @Override
    public <U> U foldLeft(U seed, BiFunction<U, JsonEntry<String>, U> op) {
        U result = seed;
        scala.collection.Iterator<Tuple2<String, JsonValue>> iterator = bindings.iterator();
        while (iterator.hasNext()) {
            result = op.apply(result, tuple2ToJsonEntry(iterator.next()));
        }
        return result;
    }

    @Override
    public JsonValue get(int index) {
        throw new UnsupportedOperationException("Get an item by index on a JsonObject");
    }

    @Override
    public JsonValue get(String key) {
        return bindings.apply(key);
    }

    @Override
    public JsonEntry<String> getHead() {
        return tuple2ToJsonEntry(bindings.head());
    }

    public Set<JsonEntry<Integer>> getIntIndexedEntrySet() {
        Set<JsonEntry<Integer>> indexes = new LinkedHashSet<>();
        scala.collection.Iterator<Tuple2<String, JsonValue>> iterator = bindings.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Tuple2<String, JsonValue> element = iterator.next();
            indexes.add(new JsonEntry<>(i, element._2()));
            ++i;
        }
        return indexes;
    }

    @Override
    public JsonEntry<String> getLast() {
        return tuple2ToJsonEntry(bindings.last());
    }

    @Override
    public Optional<JsonValue> getOptional(int index) {
        return Optional.<JsonValue>empty();
    }

    @Override
    public Optional<JsonValue> getOptional(String key) {
        return (bindings.isDefinedAt(key)) ? Optional.of(bindings.apply(key)) : Optional.empty();
    }

    @Override
    public JsonValue getOrDefault(int index, JsonValue jsonValue) {
        return jsonValue;
    }

    @Override
    public JsonValue getOrDefault(String key, JsonValue jsonValue) {
        if (bindings.isDefinedAt(key))
            return bindings.apply(key);
        return jsonValue;
    }

    public Set<JsonEntry<String>> getStringIndexedEntrySet() {
        Set<JsonEntry<String>> result = new LinkedHashSet<>();
        scala.collection.Iterator<Tuple2<String, JsonValue>> iterator = bindings.iterator();
        Tuple2<String, JsonValue> element;
        while (iterator.hasNext()) {
            element = iterator.next();
            result.add(new JsonEntry<>(element._1(), element._2()));
        }
        return result;
    }

    @Override
    public JsonObject getTail() {
        if(this.isEmpty())
            throw new UnsupportedOperationException("Get tail of an EMPTY JsonObject");

        return this.remove(bindings.head()._1());
    }

    @Override
    public java.util.Map<String, JsonValue> getValue() {
        scala.collection.Iterator<Tuple2<String, JsonValue>> iterator = bindings.iterator();
        final java.util.Map<String, JsonValue> value = new LinkedHashMap<>();
        while (iterator.hasNext()) {
            Tuple2<String, JsonValue> element = iterator.next();
            value.put(element._1(), element._2());
        }
        return value;
    }

    @Override
    public int hashCode() {
        return bindings.hashCode();
    }

    public boolean isDefinedAt(String key) {
        return bindings.isDefinedAt(key);
    }

    @Override
    public boolean isEmpty() {
        return bindings.isEmpty();
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
        return new Iterator<JsonEntry<String>>() {

            final scala.collection.Iterator<Tuple2<String, JsonValue>> iterator = bindings.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public JsonEntry<String> next() {
                return tuple2ToJsonEntry(iterator.next());
            }
        };
    }

    @Override
    public Iterator<String> keysIterator() {
        return new Iterator<String>() {

            final scala.collection.Iterator<String> iterator = bindings.keysIterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public String next() {
                return iterator.next();
            }
        };
    }

    public Set<String> keysSet() {
        Set<String> keySet = new LinkedHashSet<>();
        scala.collection.Iterator<String> iterator = bindings.keysIterator();
        while (iterator.hasNext())
            keySet.add(iterator.next());
        return keySet;
    }

    public Stream<String> keysStream() {
        return StreamSupport.stream(
                Spliterators.spliterator(
                        keysIterator(),
                        bindings.size(),
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
        }
        return result;
    }

    public JsonObject remove(String key) {
        if (!bindings.isDefinedAt(key))
            return this;

        return JsonObject.of(this.stream().filter(entry -> !entry.getKey().equals(key)));
    }

    public JsonObject remove(String key, JsonValue oldJsonValue) {
        if (!bindings.isDefinedAt(key) || bindings.apply(key).equals(oldJsonValue))
            return this;

        return JsonObject.of(
                this.stream().filter(entry ->
                                !(entry.getKey().equals(key) && entry.getValue().equals(oldJsonValue))
                )
        );

    }

    @Override
    public int size() {
        return bindings.size();
    }

    @Override
    public JsonObject sorted(Comparator<? super JsonEntry<String>> comparator) {
        return JsonObject.of(this.stream().sorted(comparator));
    }

    public Stream<JsonEntry<String>> stream() {
        return StreamSupport.stream(
                Spliterators.spliterator(
                        iterator(),
                        bindings.size(),
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
        scala.collection.Iterator<Tuple2<String, JsonValue>> iterator =
                (scala.collection.Iterator<Tuple2<String, JsonValue>>) bindings.iterator();
        Tuple2<String, JsonValue> next;
        JsonValue value;
        while (iterator.hasNext()) {
            next = iterator.next();
            value = next._2();
            if (emptyValuesToNull && value.isJsonOptional() && value.isEmpty())
                value = JsonNull.INSTANCE;
            if (Json.isEligibleForStringify(value, keepingNull))
                sj = sj.add(String.format("\"%s\":%s", next._1(), next._2().stringify(keepingNull, emptyValuesToNull)));
        }

        return sj.toString();
    }

    @Override
    public String toString() {
        return "JsonObject{" +
                "bindings=" + bindings +
                '}';
    }

    public JsonObject union(JsonObject other) {

        if (this.isEmpty())
            return other;

        else if (other.isEmpty())
            return this;

        else
            return new JsonObject(this.bindings.$plus$plus(other.bindings));

    }

    public JsonObject unionAll(List<JsonObject> jsonObjects) {
        Builder<Tuple2<String, JsonValue>, Map> mapBuilder = Map$.MODULE$.<String, JsonValue>newBuilder();
        mapBuilder.$plus$plus$eq(this.bindings);
        jsonObjects.stream()
                .filter(JsonValue::isNotEmpty)
                .forEachOrdered(jsonObject -> mapBuilder.$plus$plus$eq(jsonObject.bindings));

        return new JsonObject(mapBuilder.result());
    }

    @Override
    public JsonObject updateEntry(String oldEntryKey, JsonEntry<String> newEntry) {
        return updateEntry(oldEntryKey, ignored -> newEntry);
    }

    @Override
    public JsonObject updateEntry(String oldEntryKey, UnaryOperator<JsonEntry<String>> operator) {
        if (!bindings.isDefinedAt(oldEntryKey))
            return this;

        JsonValue value = bindings.apply(oldEntryKey);
        Builder<Tuple2<String, JsonValue>, Map> mapBuilder = Map$.MODULE$.newBuilder();
        mapBuilder.$plus$plus$eq(
                bindings.$minus(oldEntryKey)
                        .$plus(jsonEntry2Tuple2(operator.apply(JsonEntry.of(oldEntryKey, value))))
        );
        return new JsonObject(mapBuilder.result());
    }

    public JsonObject updateKey(String oldKey, String newKey) {
        if (!bindings.isDefinedAt(oldKey))
            return this;

        JsonValue value = bindings.apply(oldKey);
        Builder<Tuple2<String, JsonValue>, Map> mapBuilder = Map$.MODULE$.newBuilder();
        mapBuilder.$plus$plus$eq(
                bindings.$minus(oldKey)
                        .$plus(new Tuple2<>(newKey, value))
        );
        return new JsonObject(mapBuilder.result());
    }

    @Override
    public JsonObject updateKey(String oldKey, UnaryOperator<String> operator) {
        return updateKey(oldKey, operator.apply(oldKey));
    }

    public JsonObject updateKey(String oldKey, JsonValue oldJsonValue, String newKey) {
        if (!bindings.isDefinedAt(oldKey) || !bindings.get(oldKey).equals(oldJsonValue))
            return this;

        JsonValue value = bindings.apply(oldKey);
        Builder<Tuple2<String, JsonValue>, Map> mapBuilder = Map$.MODULE$.newBuilder();
        mapBuilder.$plus$plus$eq(
                bindings.$minus(oldKey)
                        .$plus(new Tuple2<>(newKey, value))
        );
        return new JsonObject(mapBuilder.result());
    }

    public JsonObject updateValue(String key, JsonValue newJsonValue) {
        return updateValue(key, ignored -> JsonEntry.of(key, newJsonValue));
    }

    @Override
    public JsonObject updateValue(String key, UnaryOperator<JsonEntry<String>> operator) {
        if (!bindings.isDefinedAt(key))
            return this;

        JsonValue value = bindings.apply(key);
        return new JsonObject(
                bindings.$plus(jsonEntry2Tuple2(operator.apply(JsonEntry.of(key, value))))
        );
    }

    public JsonObject updateValue(String key, JsonValue oldJsonValue, JsonValue newJsonValue) {
        if (!bindings.isDefinedAt(key) || !bindings.get(key).equals(oldJsonValue))
            return this;

        return new JsonObject(bindings.$plus(new Tuple2<>(key, newJsonValue)));
    }


    public Iterator<JsonValue> valuesIterator() {
        return new Iterator<JsonValue>() {

            final scala.collection.Iterator<JsonValue> iterator = bindings.valuesIterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public JsonValue next() {
                return iterator.next();
            }
        };
    }
}
