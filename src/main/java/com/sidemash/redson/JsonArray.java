package com.sidemash.redson;


import scala.collection.*;
import scala.collection.immutable.Vector;
import scala.collection.immutable.Vector$;
import scala.collection.immutable.VectorIterator;
import scala.collection.mutable.Builder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JsonArray implements JsonStructure, Iterable<JsonValue> {

    public static final JsonArray EMPTY = new JsonArray(Vector$.MODULE$.empty());
    private final Vector<JsonValue> items;
    private JsonArray(Vector<JsonValue> items) {
        this.items = items;
    }


    public static JsonArray of(JsonValue jsonValue1, JsonValue... jsonValues){
        Builder<JsonValue, Vector<JsonValue>> vectorBuilder = Vector$.MODULE$.<JsonValue>newBuilder();
        vectorBuilder.$plus$eq(jsonValue1);
        for(JsonValue jv : jsonValues){
            vectorBuilder.$plus$eq(jv);
        }
        return new JsonArray(vectorBuilder.result());
    }

    public static JsonArray of(List<? extends JsonValue> jsonValues){
        Builder<JsonValue, Vector<JsonValue>> vectorBuilder = Vector$.MODULE$.<JsonValue>newBuilder();
        for(JsonValue jv : jsonValues){
            vectorBuilder.$plus$eq(jv);
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
            result = toString();
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
                if (keepingNull && value.isJsonNull() || !value.isJsonNull()){
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
        StringJoiner sj = new StringJoiner(",", "[", "]");
        VectorIterator<JsonValue> iterator = items.iterator();
        JsonValue value;
        while(iterator.hasNext()){
            value = iterator.next();
            if (emptyValuesToNull && value.isJsonOptional() && value.isEmpty())
                value = JsonNull.INSTANCE;
            if (keepingNull && value.isJsonNull() || !value.isJsonNull()){
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

}
