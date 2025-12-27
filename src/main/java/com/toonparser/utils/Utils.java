package com.toonparser.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {

    private Utils() {
    }

    @SuppressWarnings("unchecked")
    public static boolean checkIfArrayIsNonUniform(List<?> input) {
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
                    boolean isNotPrimitive = !(isMapValuePrimitiveType(lvEntry));
                    if (isNotPrimitive) return true;
                }
            }
        }

        return false;
    }

    public static boolean checkIfListValueIsPrimitiveType(List<?> input) {
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
    public static String extractHeaders(List<?> input) {
        StringBuilder sb = new StringBuilder();
        if (input.get(0) instanceof Map) {
            Map<String, Object> lv = (Map<String, Object>) input.get(0);
            int count = 0;
            for (Map.Entry entry : lv.entrySet()) {
                count++;
                if (lv.size() == count) {
                    sb.append(entry.getKey());
                } else {
                    sb.append(entry.getKey()).append(IConstants.COMMA_SEPARATOR);
                }
            }
        }

        return sb.toString();
    }

    public static String getIndent(Integer depth, Integer dashLevel) {
        return IConstants.INDENT.repeat(Math.max(0, depth)) +
                IConstants.INDENT.repeat(Math.max(0, dashLevel));
    }

    public static boolean isMapValuePrimitiveType(Map.Entry entry) {
        return entry.getValue() != null && (entry.getValue() instanceof Boolean
                || entry.getValue() instanceof Character
                || entry.getValue() instanceof CharSequence
                || entry.getValue() instanceof Number);
    }

    public static boolean isObjectPrimitiveType(Object o) {
        return ((o instanceof Boolean)
                || (o instanceof Character)
                || (o instanceof CharSequence)
                || (o instanceof Number));
    }
}
