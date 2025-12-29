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
        StringBuilder sb = new StringBuilder();

        boolean arrayIsNonUniform = Utils.checkIfArrayIsNonUniform(listOfItems);
        boolean listValueIsPrimitiveType = Utils.checkIfListValueIsPrimitiveType(listOfItems);

        sb.append(Utils.getIndent(depth, dashLevel))
                .append(entry.getKey())
                .append("[")
                .append(listOfItems.size())
                .append("]");

        if (arrayIsNonUniform) {
            sb.append(IConstants.SEMICOLON_SEPARATOR);
        } else {
            String headers = Utils.extractHeaders(listOfItems);
            if (!headers.isEmpty()) {
                sb.append("{").append(headers).append("}").append(IConstants.SEMICOLON_SEPARATOR);
            } else {
                sb.append(IConstants.SEMICOLON_SEPARATOR);
            }
        }

        // Handle primitive lists
        if (listValueIsPrimitiveType) {
            appendPrimitiveList(listOfItems, sb);
        }

        // Add header line to output
        ctx.add(sb.toString());

        if (!listValueIsPrimitiveType) {
            handleNestedObjects(listOfItems, ctx, arrayIsNonUniform,
                    depth, dashLevel);
        }
    }

    static void handle(List<?> list, ParseContext ctx,
                       Integer depth, Integer dashLevel, boolean rootElement) {
        StringBuilder sb = new StringBuilder();

        boolean arrayIsNonUniform = Utils.checkIfArrayIsNonUniform(list);
        boolean listValueIsPrimitiveType = Utils.checkIfListValueIsPrimitiveType(list);

        if (listValueIsPrimitiveType && !rootElement) {
            sb.append(Utils.getIndent(depth, dashLevel))
                    .append(IConstants.DASH).append(IConstants.EMPTY_SPACE)
                    .append("[")
                    .append(list.size())
                    .append("]");
        } else {
            sb.append(Utils.getIndent(depth, dashLevel))
                    .append("[")
                    .append(list.size())
                    .append("]");
        }

        if (arrayIsNonUniform) {
            sb.append(IConstants.SEMICOLON_SEPARATOR);
        } else {
            String headers = Utils.extractHeaders(list);
            if (!headers.isEmpty()) {
                sb.append("{").append(headers).append("}").append(IConstants.SEMICOLON_SEPARATOR);
            } else {
                sb.append(IConstants.SEMICOLON_SEPARATOR);
            }
        }


        // Handle primitive lists
        if (listValueIsPrimitiveType) {
            appendPrimitiveList(list, sb);
        }

        ctx.add(sb.toString());

        if (!listValueIsPrimitiveType) {
            handleNestedObjects(list, ctx, arrayIsNonUniform, depth, dashLevel);
        }
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
        boolean isMixedArrayWithPrimitive = Utils.isMixedArrayWithPrimitive(list);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof Map) {
                Map<String, Object> lv = (Map<String, Object>) list.get(i);
                //parse(lv, ctx.depth++, ctx.dashLevel, arrayIsNonUniform, false);
                if (!isMixedArrayWithPrimitive) {
                    new NodeDispatcher().dispatch(lv, ctx,
                            depth + 1, dashLevel, nonUniformArray,
                            false, false);
                } else {
                    new NodeDispatcher().dispatch(lv, ctx,
                            depth + 1, dashLevel,
                            true, false, false);
                }
            } else if (list.get(i) instanceof List) {
                List li = (List) list.get(i);
                if (!isMixedArrayWithPrimitive) {
                    new NodeDispatcher().dispatch(li, ctx,
                            depth + 1, dashLevel,
                            nonUniformArray, true, false);
                } else {
                    new NodeDispatcher().dispatch(li, ctx,
                            depth + 1, dashLevel,
                            nonUniformArray, false, false);
                }

            } else if (isMixedArrayWithPrimitive) {
                if (Utils.isObjectPrimitiveType(list.get(i))) {
                    ctx.add(Utils.getIndent(depth + 1, dashLevel)
                            + IConstants.DASH + IConstants.EMPTY_SPACE
                            + list.get(i));
                }
            }
        }
    }
}
