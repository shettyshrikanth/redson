package com.sidemash.redson;


import scala.collection.immutable.Vector;
import scala.collection.immutable.Vector$;
import scala.collection.immutable.VectorIterator;
import scala.collection.mutable.Builder;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JsonArray implements JsonStructure, Iterable<JsonValue> {

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

    @Override
    public JsonValue append(JsonValue jsValue) {
        return new JsonArray(items.appendBack(jsValue));
    }

    @Override
    public JsonValue append(String key, JsonValue jsValue) {
        throw new UnsupportedOperationException("This operation is not supported for JsonArray.");
    }

    @Override
    public JsonValue appendIfAbsent(String key, JsonValue jsValue) {
        throw new UnsupportedOperationException("This operation is not supported for JsonArray.");
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
        throw new UnsupportedOperationException("This operation is not supported for JsonArray.");
    }

    @Override
    public boolean containsValue(Object value) {
        return items.contains(value);
    }

    @Override
    public boolean isDefinedAt(int index) {
        return items.isDefinedAt(index);
    }

    @Override
    public boolean isDefinedAt(String key) {
        return false;
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
    public Iterator<JsonValue> iterator() {
        return new Iterator<JsonValue>() {

            scala.collection.Iterator<JsonValue> iterator = items.iterator();

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
            for (JsonValue jsonValue : this) {
                value = jsonValue;
                if (emptyValuesToNull && value.isJsonOptional() && value.isEmpty())
                    value = JsonNull.INSTANCE;
                if(Json.isEligibleForStringify(value, keepingNull, emptyValuesToNull)){
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

    public Stream<JsonValue> stream(){
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator(), Spliterator.NONNULL | Spliterator.IMMUTABLE),
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
            if(Json.isEligibleForStringify(value, keepingNull, emptyValuesToNull)){
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

    @Override
    public JsonArray union(JsonValue jsonValue) {
        if (!(jsonValue instanceof JsonArray)) {
            throw new IllegalArgumentException(
                    String.format("union is not possible between JsonArray and %s", jsonValue.getClass())
            );
        }
        JsonArray jsonArray = (JsonArray) jsonValue;
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

    @Override
    public JsonArray unionAll(List<? extends JsonValue> jsonValues) {
        Builder<JsonValue, Vector<JsonValue>> vectorBuilder = Vector$.MODULE$.<JsonValue>newBuilder();
        JsonArray temp = null;
        int i = 0;
        try {
            for(JsonValue jo : jsonValues) {
                temp = (JsonArray) jo;
                ++i;
                vectorBuilder.$plus$plus$eq(temp.items);
            }
        }
        catch (ClassCastException e) {
            throw new IllegalArgumentException(
                    String.format("union is not possible between JsonArray and %s (item %d of list)", temp.getClass(), i)
            );
        }
        return null;
    }

    @Override
    public Object getValue() {
        scala.collection.Iterator<JsonValue> iterator = items.iterator();
        final List<JsonValue> value = new ArrayList<>();
        while (iterator.hasNext()) {
            value.add(iterator.next());
        }
        return Collections.unmodifiableList(value);
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

    @Override
    public JsonValue distinct() {
        return new JsonArray(items.distinct().toVector());
    }

    @Override
    public Set<Integer> getIndexSet() {
        final Set<Integer> indexes = new LinkedHashSet<>();
        for (int i = 0; i < items.length(); i++)
            indexes.add(i);
        return indexes;
    }

    @Override
    public Set<JsonEntry<Integer>> getIntIndexedEntrySet() {
        final Set<JsonEntry<Integer>> indexes = new LinkedHashSet<>();
        for (int i = 0; i < items.length(); i++)
            indexes.add(new JsonEntry<>(i, items.apply(i)));
        return indexes;
    }

    @Override
    public Set<String> keySet() {
        final Set<String> indexes = new LinkedHashSet<>();
        for (int i = 0; i < items.length(); i++)
            indexes.add(String.valueOf(i));
        return indexes;
    }

    @Override
    public JsonValue prepend(JsonValue jsValue) {
        return new JsonArray(items.appendFront(jsValue));
    }

    @Override
    public JsonValue prepend(String key, JsonValue jsValue) {
        throw new UnsupportedOperationException(
                String.format("unsupported operation for instance of %s ", this.getClass()));
    }

    @Override
    public JsonValue prependIfAbsent(String key, JsonValue jsValue) {
        throw new UnsupportedOperationException(
                String.format("unsupported operation for instance of %s ", this.getClass()));
    }

    @Override
    public JsonValue reverse() {
        return new JsonArray(items.reverse().toVector());
    }

    @Override
    public int size() {
        return items.size();
    }

}
