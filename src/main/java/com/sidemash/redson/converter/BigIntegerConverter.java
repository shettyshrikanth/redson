package com.sidemash.redson.converter;

import com.sidemash.redson.JsonNumber;
import com.sidemash.redson.JsonValue;

import java.math.BigInteger;


public enum BigIntegerConverter implements  JsonConverter<BigInteger> {
    INSTANCE
    ;
    @Override
    public BigInteger fromJsonValue(JsonValue jsonValue) {
        return jsonValue.asBigInteger();
    }

    @Override
    public JsonValue toJsonValue(BigInteger obj, JsonValue jsonValue) {
        return JsonNumber.of(obj);
    }
}
