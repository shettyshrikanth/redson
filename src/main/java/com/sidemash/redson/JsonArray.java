package com.sidemash.redson;


import com.sidemash.redson.util.ImmutableVector;
import scala.collection.immutable.Vector;
import scala.collection.immutable.Vector$;
import scala.collection.immutable.VectorIterator;
import scala.collection.mutable.Builder;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class JsonArray implements JsonStructure, Iterable<JsonEntry<Integer>>, ImmutableVector<JsonValue> {

    public static final JsonArray EMPTY = new JsonArray(Vector$.MODULE$.empty());
    private final Vector<JsonValue> items;
    private JsonArray(Vector<JsonValue> items) {
        this.items = items;
    }


    public static JsonArray of(Object... values){
        return JsonArray.of(Arrays.asList(values));
    }

    public static JsonArray of(Iterable<?> values){
        if(!values.iterator().hasNext())
            return JsonArray.EMPTY;

        Builder<JsonValue, Vector<JsonValue>> vectorBuilder = Vector$.MODULE$.<JsonValue>newBuilder();
        for(Object value : values){
            vectorBuilder.$plus$eq(Json.toJsonValue(value));
        }
        return new JsonArray(vectorBuilder.result());
    }

    public JsonArray append(JsonArray jsonArray) {
        return new JsonArray(items.appendBack(jsonArray));
    }

    @Override
    public <T> Map<Integer, T> asIntIndexedMapOf(Class<T> c, Map<Integer, T> map) {
        if (items.isEmpty())
            return map;
        for (int i = 0; i < items.length(); i++) {
            map.put(i, Json.fromJsonValue(items.apply(i), c));
        }
        return map;
    }

    @Override
    public <T> List<T> asListOf(Class<T> cl, List<T> list) {
        for (int i = 0; i < items.length(); i++) {
            list.add(Json.fromJsonValue(items.apply(i), cl));
        }
        return list;
    }

    @Override
    public <T> Set<T> asSetOf(Class<T> cl, Set<T> set) {
        final scala.collection.Iterator <JsonValue> iterator = items.iterator();
        while (iterator.hasNext())
            set.add(Json.fromJsonValue(iterator.next(), cl));
        return set;
    }

    @Override
    public <T> Map<String, T> asStringIndexedMapOf(Class<T> c, Map<String, T> map) {
        for (int i = 0; i < items.length(); i++)
            map.put(String.valueOf(i), Json.fromJsonValue(items.apply(i), c));
        return map;
    }

    public boolean containsAllValues(Object... values) {
        return containsAllValues(Arrays.asList(values));
    }

    public boolean containsAllValues(List<?> values) {
        return values.stream().allMatch(items::contains);
    }

    @Override
    public boolean containsValue(Object value) {
        return items.contains(value);
    }

    public JsonValue distinct() {
        return new JsonArray(items.distinct().toVector());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonArray that = (JsonArray) o;

        return items.equals(that.items);

    }

    @Override
    public JsonValue get(int index) {
        return items.apply(index);
    }

    @Override
    public JsonValue get(String key) {
        throw new UnsupportedOperationException("Get an item by key on a JsonArray");
    }

    public Set<Integer> getIndexSet() {
        final Set<Integer> indexes = new LinkedHashSet<>();
        for (int i = 0; i < items.length(); i++)
            indexes.add(i);
        return indexes;
    }

    public Set<JsonEntry<Integer>> getIntIndexedEntrySet() {
        final Set<JsonEntry<Integer>> indexes = new LinkedHashSet<>();
        for (int i = 0; i < items.length(); i++)
            indexes.add(new JsonEntry<>(i, items.apply(i)));
        return indexes;
    }

    @Override
    public Optional<JsonValue> getOptional(int index) {
        return (items.isDefinedAt(index)) ? Optional.of(items.apply(index)) : Optional.<JsonValue>empty() ;
    }

    @Override
    public Optional<JsonValue> getOptional(String key) {
        return Optional.empty();
    }

    @Override
    public JsonValue getOrDefault(int index, JsonValue jsonValue) {
        return (items.isDefinedAt(index)) ? items.apply(index) : jsonValue ;
    }

    @Override
    public JsonValue getOrDefault(String key, JsonValue jsonValue) {
        return jsonValue;
    }

    @Override
    public Set<JsonEntry<String>> getStringIndexedEntrySet() {
        Set<JsonEntry<String>> result = new LinkedHashSet<>();
        scala.collection.Iterator<JsonValue> iterator = items.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            result.add(new JsonEntry<>(String.valueOf(i), iterator.next()));
            i++;
        }
        return result;
    }

    @Override
    public List<JsonValue> getValue() {
        scala.collection.Iterator<JsonValue> iterator = items.iterator();
        final List<JsonValue> value = new ArrayList<>();
        while (iterator.hasNext()) {
            value.add(iterator.next());
        }
        return value;
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }

    public Iterator<Integer> indexesIterator() {
        return new Iterator<Integer>() {

            int index = -1;

            @Override
            public boolean hasNext() {
                return (index +1) < items.size() ;
            }

            @Override
            public Integer next() {
                return  (index++);
            }
        };
    }

    public IntStream indexesStream(){
        return IntStream.range(0, items.size());
    }

    public boolean isDefinedAt(int index) {
        return items.isDefinedAt(index);
    }

    @Override
    public boolean isEmpty(){
        return items.isEmpty();
    }

    @Override
    public boolean isJsonArray() {
        return true;
    }

    @Override
    public boolean isJsonObject() {
        return false;
    }

    @Override
    public Iterator<JsonEntry<Integer>> iterator() {
        return new Iterator<JsonEntry<Integer>>() {

            final scala.collection.Iterator<JsonValue> iterator = items.iterator();
            int index = -1;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public JsonEntry<Integer> next() {
                index++;
                return new JsonEntry<>(index, iterator.next());
            }
        };
    }

    public JsonValue prepend(JsonValue jsValue) {
        return new JsonArray(items.appendFront(jsValue));
    }

    @Override
    public String prettyStringifyRecursive(int indent, int incrementAcc, boolean keepingNull, boolean emptyValuesToNull)  {
        String result;
        if (this.isEmpty()) {
            result = "[]";
        }
        else {
            StringBuilder startInc = new StringBuilder();
            for (int i = 0; i < incrementAcc; ++i) {
                startInc.append(" ");
            }
            StringBuilder endInc  = new StringBuilder();
            for (int i = 0; i < incrementAcc - indent; ++i) {
                endInc.append(" ");
            }
            final String startIncrementation = startInc.toString();
            final String endIncrementation = endInc.toString();
            StringJoiner sj = new StringJoiner(",\n", "[\n", String.format("\n%s]", endIncrementation));
            JsonValue value;
            for (JsonEntry<Integer> jsonValue : this) {
                value = jsonValue.getValue();
                if (emptyValuesToNull && value.isJsonOptional() && value.isEmpty())
                    value = JsonNull.INSTANCE;
                if(Json.isEligibleForStringify(value, keepingNull)){
                    sj = sj.add(
                            String.format("%s%s",
                                    startIncrementation,
                                    value.prettyStringifyRecursive(indent,
                                            incrementAcc + indent,
                                            keepingNull,
                                            emptyValuesToNull))
                    );
                }
            }
            result = sj.toString();
        }

        return result;
    }

    public JsonArray removeAt(int index){
        Builder<JsonValue, Vector<JsonValue>> vectorBuilder = Vector$.MODULE$.<JsonValue>newBuilder();
        vectorBuilder.$plus$plus$eq(this.items.take(index));
        vectorBuilder.$plus$plus$eq(this.items.drop(index+1));
        return new JsonArray(vectorBuilder.result());
    }

    public JsonValue reverse() {
        return new JsonArray(items.reverse().toVector());
    }

    @Override
    public int size() {
        return items.size();
    }

    public Stream<JsonEntry<Integer>> stream(){
        return StreamSupport.stream(
                Spliterators.spliterator(
                        iterator(),
                        items.size(),
                        Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.DISTINCT
                ),
                false
        );
    }

    @Override
    public String stringify(boolean keepingNull, boolean emptyValuesToNull){
        if (this.isEmpty())
            return "[]";

        StringJoiner sj = new StringJoiner(",", "[", "]");
        VectorIterator<JsonValue> iterator = items.iterator();
        JsonValue value;
        while(iterator.hasNext()){
            value = iterator.next();
            if (emptyValuesToNull && value.isJsonOptional() && value.isEmpty())
                value = JsonNull.INSTANCE;
            if(Json.isEligibleForStringify(value, keepingNull)){
                sj = sj.add(value.stringify());
            }
        }
        return sj.toString();
    }

    @Override
    public String toString() {
        return "JsonArray{" +
                "items=" + items +
                '}';
    }

    public JsonArray union(JsonArray jsonArray) {
        JsonArray result;
        if(isEmpty()) {
            result  = jsonArray;
        }
        else {
            Builder<JsonValue, Vector<JsonValue>> vectorBuilder = Vector$.MODULE$.<JsonValue>newBuilder();
            vectorBuilder
                    .$plus$plus$eq(this.items)
                    .$plus$plus$eq(jsonArray.items);
            result = new JsonArray(vectorBuilder.result());
        }

        return result;
    }

    public JsonArray unionAll(List<JsonArray> jsonArrays) {
        Builder<JsonValue, Vector<JsonValue>> vectorBuilder = Vector$.MODULE$.<JsonValue>newBuilder();
        vectorBuilder.$plus$plus$eq(this.items);
        for(JsonArray array : jsonArrays)
           vectorBuilder.$plus$plus$eq(array.items);
        return new JsonArray(vectorBuilder.result());
    }

    public JsonArray updateValue(int index, JsonValue newJsonValue){
        return updateValue(index, UnaryOperator.identity());
    }

    public JsonArray updateValue(int index, UnaryOperator<JsonValue> operator){
        if(!items.isDefinedAt(index))
            return this;

       return this.updateValue(index, operator.apply(items.apply(index)));
    }

    public Iterator<JsonValue> valuesIterator() {
        return new Iterator<JsonValue>() {

            final scala.collection.Iterator<JsonValue> iterator = items.iterator();

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

    public Stream<JsonValue> valuesStream(){
        return StreamSupport.stream(
                Spliterators.spliterator(
                        valuesIterator(),
                        items.size(),
                        Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.DISTINCT
                ),
                false
        );
    }

}
