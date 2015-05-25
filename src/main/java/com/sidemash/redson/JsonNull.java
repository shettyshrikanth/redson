package com.sidemash.redson;


public enum JsonNull implements JsonLiteral {

    INSTANCE;

    @Override
    public boolean containsKey(String key) {
        throw new UnsupportedOperationException(
                String.format("this operation is not supported by instance of %s", this.getClass()));
    }

    @Override
    public boolean containsValue(Object value) {
        return value == null;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean isJsonBoolean() {
        return false;
    }

    @Override
    public boolean isJsonNull() {
        return true;
    }

    @Override
    public boolean isJsonNumber() {
        return false;
    }

    @Override
    public boolean isJsonString() {
        return false;
    }

    @Override
    public String prettyStringifyRecursive(int indent, int incrementAcc, boolean keepingNull, boolean emptyValuesToNull) {
        return stringify(keepingNull, emptyValuesToNull);
    }

    @Override
    public String stringify(boolean keepingNull, boolean emptyValuesToNull) {
        if(keepingNull) return  "null";
        else throw new UnsupportedOperationException("Stringify JsonNull while NOT keeping null value");
    }

    @Override
    public String toString() {
        return "JsonNull{}";
    }
}
