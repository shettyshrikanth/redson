package com.sidemash.redson;


import scala.Tuple2;
import scala.collection.immutable.Map;
import scala.collection.immutable.Map$;
import scala.collection.mutable.Builder;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JsonObject implements JsonStructure, Iterable<JsonEntry<String>> {

    public static final JsonObject EMPTY = new JsonObject(Map$.MODULE$.empty());
    private final Map<String, JsonValue> bindings;

    private JsonObject(Map<String, JsonValue> bindings) {
        this.bindings = bindings;
    }

    public static <V> JsonObject of(java.util.Map<String, V> map){
        Builder<Tuple2<String, JsonValue>, Map> mapBuilder = Map$.MODULE$.newBuilder();
        for(java.util.Map.Entry<String, V> entry : map.entrySet()){
            mapBuilder.$plus$eq(new Tuple2<>(entry.getKey(), Json.toJsonValue(entry.getValue())));
        }
        return new JsonObject(mapBuilder.result());
    }

    public static<K,V> JsonObject of(java.util.Map<K, V> map, Function<K, String> kToString){
        Builder<Tuple2<String, JsonValue>, Map> mapBuilder = Map$.MODULE$.newBuilder();
        for(java.util.Map.Entry<K, V> entry : map.entrySet()){
            mapBuilder.$plus$eq(new Tuple2<>(kToString.apply(entry.getKey()), Json.toJsonValue(entry.getValue())));
        }
        return new JsonObject(mapBuilder.result());
    }

    public static JsonObject of(String key, Object o){
        Tuple2<String, JsonValue> tuple = new Tuple2<>(key, Json.toJsonValue(o));
        Map<String, JsonValue> map = Map$.MODULE$.<String, JsonValue>empty();

        return new JsonObject(map.$plus(tuple));
    }

    public static JsonObject of(String key, JsonObject jsonValue1, JsonObject... jsonValues){
        JsonObject root = jsonValue1.unionAll(Arrays.asList(jsonValues));
        return JsonObject.of(key, root);
    }

    public static JsonObject of(JsonObject jsonValue1, JsonObject... jsonValues){
        return jsonValue1.unionAll(Arrays.asList(jsonValues));
    }

    @Override
    public JsonValue append(JsonValue jsValue) {
        throw new UnsupportedOperationException("This operation is not supported for JsonObject.");
    }

    @Override
    public JsonValue append(String key, JsonValue jsValue) {
        Tuple2<String, JsonValue> tuple = new Tuple2<>(key, jsValue);
        return new JsonObject(bindings.$plus(tuple));
    }

    @Override
    public JsonValue appendIfAbsent(String key, JsonValue jsValue) {
        if(bindings.contains(key))
            return this;
        else
            return append(key, jsValue);
    }

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

    @Override
    public boolean containsAll(List<? extends JsonValue> jsValues) {
        // FIXME find a better way
        for (JsonValue jsonValue: jsValues) {
            if(!containsValue(jsonValue))
                return false;
        }
        return true;
    }

    @Override
    public boolean containsKey(String key) {
        return bindings.contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        scala.collection.Iterator<JsonValue> iterator = bindings.valuesIterator();
        while(iterator.hasNext()){
            if(iterator.next().equals(value))
                return true;
        }
        return false;
    }

    @Override
    public JsonValue distinct() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonObject that = (JsonObject) o;

        return bindings.equals(that.bindings);
    }

    @Override
    public JsonValue get(int index) {
        throw new UnsupportedOperationException("Get an item by key on a JsonArray");

    }

    @Override
    public JsonValue get(String key) {
        return bindings.apply(key);
    }

    @Override
    public Set<Integer> getIndexSet() {
        final Set<Integer> indexes = new LinkedHashSet<>();
        for (int i = 0; i < bindings.size(); i++)
            indexes.add(i);
        return indexes;
    }

    @Override
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
    public Optional<JsonValue> getOptional(int index) {
        return  Optional.<JsonValue>empty() ;
    }

    @Override
    public Optional<JsonValue> getOptional(String key) {
        return (bindings.isDefinedAt(key)) ? Optional.of(bindings.apply(key)) : Optional.empty();
    }

    @Override
    public JsonValue getOrDefault(int index, JsonValue jsonValue) {
        return  jsonValue ;
    }

    @Override
    public JsonValue getOrDefault(String key, JsonValue jsonValue) {
        if (bindings.isDefinedAt(key))
            return bindings.apply(key);
        return jsonValue;
    }

    @Override
    public Set<JsonEntry<String>> getStringIndexedEntrySet() {
        Set<JsonEntry<String>> result = new LinkedHashSet<>();
        scala.collection.Iterator<Tuple2<String, JsonValue>> iterator = bindings.iterator();
        while (iterator.hasNext()) {
            Tuple2<String, JsonValue> element = iterator.next();
            result.add(new JsonEntry<>(element._1(), element._2()));
        }
        return result;
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

    @Override
    public boolean isDefinedAt(int index) {
        return false;
    }

    @Override
    public boolean isDefinedAt(String key) {
        return bindings.isDefinedAt(key);
    }

    @Override
    public boolean isEmpty(){
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

            final scala.collection.Iterator<Tuple2<String, JsonValue>> iterator =
                    (scala.collection.Iterator<Tuple2<String, JsonValue>>) bindings.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public JsonEntry<String> next() {
                Tuple2<String, JsonValue> next = iterator.next();
                return new JsonEntry<>(next._1(), next._2());
            }
        };
    }

    @Override
    public Set<String> keySet() {
        Set<String> indexes = new LinkedHashSet<>();
        scala.collection.Iterator<Tuple2<String, JsonValue>> iterator = bindings.iterator();
        while (iterator.hasNext())
            indexes.add(iterator.next()._1());
        return indexes;
    }

    @Override
    public JsonValue prepend(JsonValue jsValue) {
        throw new UnsupportedOperationException(
                String.format("unsupported operation for instance of %s ", this.getClass()));
    }

    @Override
    public JsonValue prepend(String key, JsonValue jsValue) {

        Builder<Tuple2<String,JsonValue>,Map> builder = Map$.MODULE$.<String, JsonValue>newBuilder();
        builder.$plus$eq(new Tuple2<>(key, jsValue));
        builder.$plus$plus$eq(bindings);
        return new JsonObject(builder.result());
    }

    @Override
    public JsonValue prependIfAbsent(String key, JsonValue jsValue) {
        if(bindings.contains(key))
            return this;
        else
            return prepend(key, jsValue);
    }

    @Override
    public String prettyStringifyRecursive(int indent,
                                            int incrementAcc,
                                            boolean keepingNull,
                                            boolean emptyValuesToNull) {
        String result;
        if (this.isEmpty()) {
            result = "{}";
        }
        else {
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
            for(JsonEntry entry : this){
                value = entry.getValue();
                if(emptyValuesToNull && value.isJsonOptional() && value.isEmpty())
                    value = JsonNull.INSTANCE;
                if(Json.isEligibleForStringify(value, keepingNull)){
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

    @Override
    public JsonValue reverse() {
        scala.collection.Iterator<Tuple2<String, JsonValue>> iterator = bindings.reversed().iterator();
        Builder<Tuple2<String,JsonValue>,Map> builder = Map$.MODULE$.<String, JsonValue>newBuilder();
        while (iterator.hasNext()) {
            builder.$plus$eq(iterator.next());
        }
        return new JsonObject(builder.result());
    }

    @Override
    public int size() {
        return bindings.size();
    }

    public Stream<JsonEntry> stream(){
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator(), Spliterator.NONNULL | Spliterator.IMMUTABLE),
                false
        );
    }

    @Override
    public String stringify(boolean keepingNull, boolean emptyValuesToNull){
        if (this.isEmpty())
            return "{}";

        StringJoiner sj = new StringJoiner(",", "{", "}");
        scala.collection.Iterator<Tuple2<String, JsonValue>> iterator =
                (scala.collection.Iterator<Tuple2<String, JsonValue>>) bindings.iterator();
        Tuple2<String, JsonValue> next;
        JsonValue value;
        while(iterator.hasNext()){
            next = iterator.next();
            value = next._2();
            if(emptyValuesToNull && value.isJsonOptional() && value.isEmpty())
                value = JsonNull.INSTANCE;
            if(Json.isEligibleForStringify(value, keepingNull))
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

    @Override
    public JsonObject union(JsonValue other){
        if( !(other instanceof JsonObject) )
            throw new IllegalArgumentException(
                    String.format("union is not possible between JsonObject and %s", other.getClass())
            );

        JsonObject otherJsObject = (JsonObject) other;
        JsonObject result;
        if(this.isEmpty()){
            result = otherJsObject;
        }
        else {
            result = new JsonObject(otherJsObject.bindings.$plus$plus(this.bindings));
        }
        return result;
    }

    @Override
    public JsonObject unionAll(List<? extends JsonValue> jsonValues){
        Builder<Tuple2<String, JsonValue>, Map> mapBuilder = Map$.MODULE$.<String, JsonValue>newBuilder();
        mapBuilder.$plus$plus$eq(this.bindings);
        JsonObject temp = null;
        int i = 0;
        try {
            for(JsonValue jo : jsonValues) {
                temp = (JsonObject) jo;
                ++i;
                if (temp.isNotEmpty())
                    mapBuilder.$plus$plus$eq(temp.bindings);
            }
        }
        catch (ClassCastException e) {
            throw new IllegalArgumentException(
                    String.format("union is not possible between JsonObject and %s (item %d of list)", temp.getClass(), i)
            );
        }
        return new JsonObject(mapBuilder.result());
    }



}
