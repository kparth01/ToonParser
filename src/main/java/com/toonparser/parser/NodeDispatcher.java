package com.toonparser.parser;

import com.toonparser.model.ParseContext;
import com.toonparser.utils.Utils;

import java.util.List;
import java.util.Map;

public class NodeDispatcher {

    void dispatch(Object input,
                  ParseContext ctx,
                  Integer depth,
                  Integer dashLevel,
                  boolean nonUniformForPrimitive,
                  boolean isKeyValue,
                  boolean rootElement) {
        if (input instanceof Map<?, ?>) {
            parseMap((Map<String, Object>) input, ctx, depth, dashLevel, nonUniformForPrimitive, isKeyValue, rootElement);
        }
        if (input instanceof List<?>) {
            parseList((List<?>) input, ctx, depth, dashLevel);
        }
    }

    private void parseMap(Map<String, Object> map,
                          ParseContext ctx,
                          Integer depth,
                          Integer dashLevel,
                          boolean nonUniformForPrimitive,
                          boolean isKeyValue,
                          boolean rootElement) {
        StringBuilder sb = new StringBuilder();
        Integer inputMapSize = map.size();
        int count = map.size();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (Utils.isMapValuePrimitiveType(entry)) {
                Map<String, Integer> result = PrimitiveHandler.handle(entry, ctx, inputMapSize, sb,
                            depth, dashLevel, nonUniformForPrimitive, isKeyValue, count, rootElement);
                count = result.getOrDefault("count", count);
                dashLevel = result.getOrDefault("dashLevel", dashLevel);
            } else if (entry.getValue() instanceof List<?>) {
                ListHandler.handle(entry, ctx, depth, dashLevel);
            } else if (entry.getValue() instanceof Map<?, ?>) {
                MapHandler.handle(entry, ctx, depth, dashLevel);
            }
        }
    }

    private void parseList(List<?> list,
                           ParseContext ctx,
                           Integer depth, Integer dashLevel) {
        ListHandler.handle(list, ctx, depth, dashLevel);
    }
}
