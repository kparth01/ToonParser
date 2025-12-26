package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ToonParser {

    private static final String COMMA_SEPARATOR = ",";
    private static final String SEMICOLON_SEPARATOR = ":";
    private static final String PIPE_SEPARATOR = "|";

    private static final String INDENT = "  ";
    private static final String DASH = "-";
    private static final String EMPTY_SPACE = " ";

    static List<String> resultStr = new ArrayList<>();

    public static Object parse(Object input) {
        return parse(input, 0, 0, false, false);
    }

    public static Object parse(Object input, Integer depth, Integer dashLevel,
                               boolean arrayIsNonUniformFlagForPrimitive, boolean isKeyValue) {
        if (depth == 0) resultStr.clear();
        if (input instanceof Map) {
            Map<String, Object> ipMap = (Map<String, Object>) input;
            StringBuilder sb = new StringBuilder();
            int count = ipMap.size();
            for (Map.Entry entry : ipMap.entrySet()) {
                if (isPrimitiveType(entry)) {
                    if (isKeyValue) {
                        resultStr.add(getIndent(depth, dashLevel) +
                                entry.getKey() +
                                SEMICOLON_SEPARATOR +
                                EMPTY_SPACE +
                                entry.getValue());
                    } else if (arrayIsNonUniformFlagForPrimitive) {
                        sb.append(getIndent(depth, dashLevel));
                        if (count == ipMap.size()) {
                            sb.append(DASH).append(" ");
                            dashLevel = dashLevel + 1;
                        }
                        resultStr.add(sb.toString() +
                                entry.getKey() +
                                SEMICOLON_SEPARATOR +
                                EMPTY_SPACE +
                                entry.getValue());
                        sb.delete(0, sb.length());
                        count--;
                    } else {
                        if (count == ipMap.size()) sb.append(getIndent(depth, dashLevel));
                        sb.append(entry.getValue());
                        if (count > 1) sb.append(COMMA_SEPARATOR);
                        if (count == 1) {
                            resultStr.add(sb.toString());
                            sb.delete(0, sb.length());
                        }
                        count--;
                    }
                } else if (entry.getValue() != null && entry.getValue() instanceof List<?>) {
                    List listOfItems = (List) entry.getValue();
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(getIndent(depth, dashLevel)).append(entry.getKey())
                            .append("[")
                            .append(listOfItems.size())
                            .append("]");
                    boolean arrayIsNonUniform = checkIfArrayIsNonUniform(listOfItems);
                    if (arrayIsNonUniform) {
                        sb1.append(SEMICOLON_SEPARATOR);
                    } else {
                        String headers = extractHeaders(listOfItems);
                        if (!headers.isEmpty()) {
                            sb1.append("{")
                                    .append(headers)
                                    .append("}")
                                    .append(SEMICOLON_SEPARATOR);
                        } else {
                            sb1.append(SEMICOLON_SEPARATOR);
                        }
                    }
                    if (checkIfListValueIsPrimitiveType(listOfItems)) {
                        int cnt = 1;
                        for (Object listOfItem : listOfItems) {
                            if (cnt == 1) sb1.append(EMPTY_SPACE);
                            if (cnt == listOfItems.size()) sb1.append(listOfItem);
                            else sb1.append(listOfItem).append(COMMA_SEPARATOR);
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
                    resultStr.add(getIndent(depth, dashLevel)
                            + entry.getKey()
                            + SEMICOLON_SEPARATOR);
                    // call same function with depth + 1
                    parse(obMap, ++depth, dashLevel, false, true);
                    --depth;
                }
            }
            return resultStr;
        }

        return null;
    }


    // if it returns false use "-" to populate list
    @SuppressWarnings("unchecked")
    private static boolean checkIfArrayIsNonUniform(List<?> input) {
        List<String> propertiesForFirstObject = new ArrayList<>();
        boolean isThereAnyAdditionalPropertyInAnyObjectOFInputList = false;

        for (int i = 0; i < input.size(); i++) {
            if (input.get(i) instanceof Map) {
                Map<String, Object> lv = (Map<String, Object>) input.get(i);
                for (Map.Entry lvEntry : lv.entrySet()) {

                    // check if all the object in list have same properties
                    if (i == 0) {
                        propertiesForFirstObject.add((String) lvEntry.getKey());
                    } else {
                        if (!propertiesForFirstObject.isEmpty() && !propertiesForFirstObject.contains(lvEntry.getKey())) {
                            isThereAnyAdditionalPropertyInAnyObjectOFInputList = true;
                            return isThereAnyAdditionalPropertyInAnyObjectOFInputList;
                        }
                    }

                    // check if property is not primitive type
                    boolean isNotPrimitive = !(isPrimitiveType(lvEntry));
                    if (isNotPrimitive) return true;
                }
            }
        }

        return false;
    }

    private static boolean checkIfListValueIsPrimitiveType(List<?> input) {
        for (Object o : input) {
            if ((o instanceof Boolean)
                    || (o instanceof Character)
                    || (o instanceof CharSequence)
                    || (o instanceof Number)) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private static String extractHeaders(List<?> input) {
        StringBuilder sb = new StringBuilder();
        if (input.get(0) instanceof Map) {
            Map<String, Object> lv = (Map<String, Object>) input.get(0);
            int count = 0;
            for (Map.Entry entry : lv.entrySet()) {
                count++;
                if (lv.size() == count) {
                    sb.append(entry.getKey());
                } else {
                    sb.append(entry.getKey()).append(COMMA_SEPARATOR);
                }
            }
        }

        return sb.toString();
    }

    private static String getIndent(Integer depth, Integer dashLevel) {
        return INDENT.repeat(Math.max(0, depth)) +
                INDENT.repeat(Math.max(0, dashLevel));
    }

    private static boolean isPrimitiveType(Map.Entry entry) {
        return entry.getValue() != null && (entry.getValue() instanceof String
                || entry.getValue() instanceof Integer
                || entry.getValue() instanceof Boolean
                || entry.getValue() instanceof Double
                || entry.getValue() instanceof Float
                || entry.getValue() instanceof Character
                || entry.getValue() instanceof CharSequence
                || entry.getValue() instanceof Number);
    }

}
