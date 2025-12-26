package com.toonparser.parser;

import com.toonparser.utils.IConstants;
import com.toonparser.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParseEngine {

    static List<String> resultStr = new ArrayList<>();

    public static List<?> parse(Object input) {
        return parse(input, 0, 0, false, false);
    }

    public static List<?> parse(Object input, Integer depth, Integer dashLevel,
                               boolean arrayIsNonUniformFlagForPrimitive, boolean isKeyValue) {
        if (depth == 0) resultStr.clear();
        if (input instanceof Map) {
            Map<String, Object> ipMap = (Map<String, Object>) input;
            StringBuilder sb = new StringBuilder();
            int count = ipMap.size();
            for (Map.Entry entry : ipMap.entrySet()) {
                if (Utils.isPrimitiveType(entry)) {
                    if (isKeyValue) {
                        resultStr.add(Utils.getIndent(depth, dashLevel) +
                                entry.getKey() +
                                IConstants.SEMICOLON_SEPARATOR +
                                IConstants.EMPTY_SPACE +
                                entry.getValue());
                    } else if (arrayIsNonUniformFlagForPrimitive) {
                        sb.append(Utils.getIndent(depth, dashLevel));
                        if (count == ipMap.size()) {
                            sb.append(IConstants.DASH).append(" ");
                            dashLevel = dashLevel + 1;
                        }
                        resultStr.add(sb.toString() +
                                entry.getKey() +
                                IConstants.SEMICOLON_SEPARATOR +
                                IConstants.EMPTY_SPACE +
                                entry.getValue());
                        sb.delete(0, sb.length());
                        count--;
                    } else {
                        if (count == ipMap.size()) sb.append(Utils.getIndent(depth, dashLevel));
                        sb.append(entry.getValue());
                        if (count > 1) sb.append(IConstants.COMMA_SEPARATOR);
                        if (count == 1) {
                            resultStr.add(sb.toString());
                            sb.delete(0, sb.length());
                        }
                        count--;
                    }
                } else if (entry.getValue() != null && entry.getValue() instanceof List<?>) {
                    List listOfItems = (List) entry.getValue();
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(Utils.getIndent(depth, dashLevel)).append(entry.getKey())
                            .append("[")
                            .append(listOfItems.size())
                            .append("]");
                    boolean arrayIsNonUniform = Utils.checkIfArrayIsNonUniform(listOfItems);
                    if (arrayIsNonUniform) {
                        sb1.append(IConstants.SEMICOLON_SEPARATOR);
                    } else {
                        String headers = Utils.extractHeaders(listOfItems);
                        if (!headers.isEmpty()) {
                            sb1.append("{")
                                    .append(headers)
                                    .append("}")
                                    .append(IConstants.SEMICOLON_SEPARATOR);
                        } else {
                            sb1.append(IConstants.SEMICOLON_SEPARATOR);
                        }
                    }
                    if (Utils.checkIfListValueIsPrimitiveType(listOfItems)) {
                        int cnt = 1;
                        for (Object listOfItem : listOfItems) {
                            if (cnt == 1) sb1.append(IConstants.EMPTY_SPACE);
                            if (cnt == listOfItems.size()) sb1.append(listOfItem);
                            else sb1.append(listOfItem).append(IConstants.COMMA_SEPARATOR);
                            cnt++;
                        }
                        resultStr.add(sb1.toString());
                    } else {
                        resultStr.add(sb1.toString());
                    }
                    // call same function with depth + 1
                    for (int i = 0; i < listOfItems.size(); i++) {
                        if (listOfItems.get(i) instanceof Map) {
                            Map<String, Object> lv = (Map<String, Object>) listOfItems.get(i);
                            parse(lv, ++depth, dashLevel, arrayIsNonUniform, false);
                            --depth;
                        }
                    }
                } else if (entry.getValue() != null && entry.getValue() instanceof Map<?, ?>) {
                    Map<String, Object> obMap = (Map<String, Object>) entry.getValue();
                    resultStr.add(Utils.getIndent(depth, dashLevel)
                            + entry.getKey()
                            + IConstants.SEMICOLON_SEPARATOR);
                    // call same function with depth + 1
                    parse(obMap, ++depth, dashLevel, false, true);
                    --depth;
                }
            }
            return resultStr;
        }

        return null;
    }
}
