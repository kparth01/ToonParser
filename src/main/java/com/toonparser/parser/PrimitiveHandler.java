package com.toonparser.parser;

import com.toonparser.model.ParseContext;
import com.toonparser.utils.IConstants;
import com.toonparser.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PrimitiveHandler {

    static Map<String, Integer> handle(Map.Entry<String, Object> entry,
                       ParseContext ctx,
                       Integer inputMapSize,
                       StringBuilder sb,
                       Integer depth, Integer dashLevel,
                       boolean nonUniformArray,
                       boolean isKeyValue,
                       Integer count) {
        Map<String, Integer> result = new HashMap<>();
        if (isKeyValue) {
            formatKeyValue(entry, ctx,  depth, dashLevel);
        } else if (nonUniformArray) {
            result = formatNonUniformArray(entry, ctx, inputMapSize, sb, depth, dashLevel, count);
        } else {
            result = formatInline(entry, ctx, inputMapSize, sb, depth, dashLevel, count);
        }
        return result;
    }

    private static void formatKeyValue(Map.Entry<String, Object> entry, ParseContext ctx,
                                       Integer depth, Integer dashLevel) {
        ctx.add(
                Utils.getIndent(depth, dashLevel) +
                        entry.getKey() +
                        IConstants.SEMICOLON_SEPARATOR +
                        IConstants.EMPTY_SPACE +
                        entry.getValue());
    }

    private static Map<String, Integer> formatNonUniformArray(Map.Entry<String, Object> entry,
                                              ParseContext ctx,
                                              Integer inputMapSize,
                                              StringBuilder sb,
                                              Integer depth,
                                              Integer dashLevel,
                                              Integer count) {
        sb.append(Utils.getIndent(depth, dashLevel));
        if (Objects.equals(count, inputMapSize)) {
            sb.append(IConstants.DASH).append(" ");
            dashLevel = dashLevel + 1;
        }
        ctx.add(sb +
                entry.getKey() +
                IConstants.SEMICOLON_SEPARATOR +
                IConstants.EMPTY_SPACE +
                entry.getValue());
        sb.delete(0, sb.length());
        count = count - 1;
        return Map.of("count", count,
                "dashLevel", dashLevel);
    }

    private static Map<String, Integer> formatInline(Map.Entry<String, Object> entry,
                                     ParseContext ctx,
                                     Integer inputMapSize,
                                     StringBuilder sb,
                                     Integer depth,
                                     Integer dashLevel,
                                     Integer count) {
        if (Objects.equals(count, inputMapSize)) sb.append(Utils.getIndent(depth, dashLevel));
        sb.append(entry.getValue());
        if (count > 1) sb.append(IConstants.COMMA_SEPARATOR);
        if (count == 1) {
            ctx.add(sb.toString());
            sb.delete(0, sb.length());
        }
        count = count - 1;
        return Map.of("count", count);
    }
}
