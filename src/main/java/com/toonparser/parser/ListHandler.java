package com.toonparser.parser;

import com.toonparser.model.ParseContext;
import com.toonparser.utils.IConstants;
import com.toonparser.utils.Utils;

import java.util.List;
import java.util.Map;

public class ListHandler {

    static void handle(Map.Entry<String, Object> entry, ParseContext ctx,
                       Integer depth, Integer dashLevel) {
        List listOfItems = (List) entry.getValue();
        StringBuilder sb1 = new StringBuilder();

        // Build header line
        sb1.append(Utils.getIndent(depth, dashLevel))
                .append(entry.getKey())
                .append("[")
                .append(listOfItems.size())
                .append("]");

        boolean arrayIsNonUniform = Utils.checkIfArrayIsNonUniform(listOfItems);

        if (arrayIsNonUniform) {
            sb1.append(IConstants.SEMICOLON_SEPARATOR);
        } else {
            String headers = Utils.extractHeaders(listOfItems);
            if (!headers.isEmpty()) {
                sb1.append("{").append(headers).append("}").append(IConstants.SEMICOLON_SEPARATOR);
            } else {
                sb1.append(IConstants.SEMICOLON_SEPARATOR);
            }
        }

        // Handle primitive lists
        if (Utils.checkIfListValueIsPrimitiveType(listOfItems)) {
            appendPrimitiveList(listOfItems, sb1);
        }

        // Add header line to output
        ctx.add(sb1.toString());

        handleNestedObjects(listOfItems, ctx, arrayIsNonUniform,
                depth, dashLevel);
    }

    static void handle(List<?> list, ParseContext ctx,
                       Integer depth, Integer dashLevel) {
        StringBuilder sb = new StringBuilder();
        sb.append(Utils.getIndent(depth, dashLevel))
                .append("[")
                .append(list.size())
                .append("]")
                .append(IConstants.SEMICOLON_SEPARATOR);

        // Handle primitive lists
        if (Utils.checkIfListValueIsPrimitiveType(list)) {
            appendPrimitiveList(list, sb);
        }

        ctx.add(sb.toString());

        boolean arrayIsNonUniform = Utils.checkIfArrayIsNonUniform(list);

        handleNestedObjects(list, ctx, arrayIsNonUniform,
                depth, dashLevel);

    }

    private static void appendPrimitiveList(List<?> listOfItems, StringBuilder sb) {
        int cnt = 1;
        for (Object listOfItem : listOfItems) {
            if (cnt == 1) sb.append(IConstants.EMPTY_SPACE);
            if (cnt == listOfItems.size()) sb.append(listOfItem);
            else sb.append(listOfItem).append(IConstants.COMMA_SEPARATOR);
            cnt++;
        }
    }

    private static void handleNestedObjects(List list, ParseContext ctx, boolean nonUniformArray,
                                            Integer depth, Integer dashLevel) {
        // call same function with depth + 1
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof Map) {
                Map<String, Object> lv = (Map<String, Object>) list.get(i);
                depth = depth + 1;
                //parse(lv, ctx.depth++, ctx.dashLevel, arrayIsNonUniform, false);
                new NodeDispatcher().dispatch(lv, ctx,
                        depth, dashLevel, nonUniformArray, false);
                depth--;
            } else if (list.get(i) instanceof List) {
                List li = (List) list.get(i);
                new NodeDispatcher().dispatch(li, ctx,
                        depth, dashLevel, nonUniformArray, true);
            }
        }
    }
}
