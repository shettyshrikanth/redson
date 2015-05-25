package com.sidemash.redson;


public enum JsonBoolean implements JsonLiteral {

    TRUE(true){
        @Override
        public String prettyStringifyRecursive(int indent, int incrementAcc, boolean keepingNull, boolean emptyValuesToNull) {
            return  "true";
        }

        @Override
        public String stringify(boolean keepingNull, boolean emptyValuesToNull) {
            return  "true";
        }
    },

    FALSE(false) {
        @Override
        public String prettyStringifyRecursive(int indent, int incrementAcc, boolean keepingNull, boolean emptyValuesToNull) {
            return  "false";
        }

        @Override
        public String stringify(boolean keepingNull, boolean emptyValuesToNull) {
            return  "false";
        }
    };

    private boolean value;

    JsonBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public boolean containsKey(String key) {
        throw new UnsupportedOperationException("This operation is not supported for JsonBoolean.");
    }

    @Override
    public boolean containsValue(Object value) {
        return value.equals(value);
    }

    @Override
    public boolean isJsonBoolean() {
        return true;
    }

    @Override
    public boolean isJsonNumber() {
        return false;
    }

    @Override
    public boolean isJsonNull() {
        return false;
    }

    @Override
    public boolean isJsonString() {
        return false;
    }


    static JsonBoolean  of(boolean b) {
        if(b) return JsonBoolean.TRUE; else return JsonBoolean.FALSE;
    }

    @Override
    public String toString() {
        return "JsonBoolean{" +
                "value=" + value +
                '}';
    }
}
