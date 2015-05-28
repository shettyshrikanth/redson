package com.sidemash.redson.converter;


import com.sidemash.redson.JsonNumber;
import com.sidemash.redson.JsonValue;

import java.math.BigDecimal;

public enum  BigDecimalConverter implements  JsonConverter<BigDecimal> {
    INSTANCE
    ;
    @Override
    public BigDecimal fromJsonValue(JsonValue jsonValue) {
        return jsonValue.asBigDecimal();
    }

    @Override
    public JsonValue toJsonValue(BigDecimal obj, JsonValue jsonValue) {
        return JsonNumber.of(obj);
    }
}
