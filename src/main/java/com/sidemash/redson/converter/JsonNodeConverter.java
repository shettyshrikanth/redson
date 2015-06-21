package com.sidemash.redson.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.sidemash.redson.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public enum JsonNodeConverter implements JsonConverter<JsonNode> {
    INSTANCE
    ;

    @Override
    public JsonNode fromJsonValue(JsonValue jsonValue) {
        return jsonValue.toJsonNode();
    }

    @Override
    public JsonValue toJsonValue(JsonNode obj, JsonValue jsonValue) {

        if(obj.isArray())
            return JsonArray.of(obj.elements());

        else if(obj.isBinary())
            return JsonString.of(obj.asText());

        else if(obj.isBoolean())
            return JsonBoolean.of(obj.asBoolean());

        else if(obj.isMissingNode())
            return JsonOptional.EMPTY;

        else if(obj.isNull())
            return JsonNull.INSTANCE;

        else if(obj.isNumber())
            return JsonNumber.of(obj.numberValue());

        else if(obj.isPojo())
            return JsonValue.of(((POJONode)obj).getPojo());

        else if(obj.isObject()){
            Iterator<Map.Entry<String, JsonNode>> it = obj.fields();
            List<JsonEntry<String>> entries = new ArrayList<>();
            Map.Entry<String, JsonNode> next;
            while (it.hasNext()){
                next = it.next();
                entries.add(JsonEntry.of(next.getKey(), next.getValue()));
            }
            return JsonObject.of(entries);
        }

        else if(obj.isTextual())
            return JsonString.of(obj.textValue());

        else {
            throw new UnsupportedOperationException("Unable to convert JsonNode instance of Node type : " + obj.getNodeType());
        }

    }
}
