package com.toonparser.parser;

import com.toonparser.model.ParseContext;
import com.toonparser.utils.IConstants;
import com.toonparser.utils.Utils;

import java.util.Map;

public class MapHandler {

    static void handle(Map.Entry<String, Object> entry, ParseContext ctx,
                       Integer depth, Integer dashLevel) {
        Map<String, Object> obMap = (Map<String, Object>) entry.getValue();
        ctx.add(Utils.getIndent(depth, dashLevel)
                + entry.getKey()
                + IConstants.SEMICOLON_SEPARATOR);
        // call same function with depth + 1
        depth = depth + 1;
        boolean nonUniformArray = false;
        boolean isKeyValue = true;
        // parse(obMap, ctx.depth, ctx.dashLevel, false, true);
        new NodeDispatcher().dispatch(obMap, ctx,
                depth, dashLevel, nonUniformArray, isKeyValue, false);
        --depth;
    }
}
