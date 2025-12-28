package com.toonparser.utils;

import java.util.*;

public class Utils {

    private Utils() {
    }

    private enum ElementKind { PRIMITIVE, MAP, LIST, OTHER }


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

    public static boolean isMixedArrayWithPrimitive(List<?> mixedArray) {
        if (mixedArray == null || mixedArray.size() <= 1) return false;
        EnumSet<ElementKind> primitiveList = EnumSet.noneOf(ElementKind.class);
        EnumSet<ElementKind> nonPrimitiveList = EnumSet.noneOf(ElementKind.class);
        for (Object ma : mixedArray) {
            if (isObjectPrimitiveType(ma)) {
                ElementKind k = classify(ma);
                primitiveList.add(k);
            } else {
                ElementKind k = classify(ma);
                nonPrimitiveList.add(k);            }
        }
        return !primitiveList.isEmpty() && !nonPrimitiveList.isEmpty();
    }

//    public static boolean checkIfArrayIsNonUniform2(List<?> list) {
//        return checkIfArrayIsNonUniform2(list, false);
//    }
//
//    public static boolean checkIfArrayIsNonUniform2(List<?> list, boolean treatDifferentMapKeysAsNonUniform) {
//        if (list == null || list.size() <= 1) return false;
//
//        EnumSet<ElementKind> kinds = EnumSet.noneOf(ElementKind.class);
//        Set<Set<String>> mapKeySets = treatDifferentMapKeysAsNonUniform ? new HashSet<>() : null;
//
//        for (Object item : list) {
//            ElementKind k = classify(item);
//            kinds.add(k);
//
//            if (k == ElementKind.MAP) {
//                Map<?, ?> m = (Map<?, ?>) item;
//                // capture key set as strings to compare structure
//                Set<String> keys = new HashSet<>();
//                for (Object key : m.keySet()) {
//                    keys.add(String.valueOf(key));
//                }
//                mapKeySets.add(keys);
//                if (mapKeySets.size() > 1) {
//                    return true; // different map shapes -> non-uniform
//                }
//            }
//        }
//
//        // different kind of values means non-uniform PRIMITIVE and MAP
//        if (kinds.size() > 1) {
//            return true;
//        }
//
//        return false;
//    }

    private static ElementKind classify(Object o) {
        if (o == null) return ElementKind.PRIMITIVE;
        if (o instanceof Map) return ElementKind.MAP;
        if (o instanceof List) return ElementKind.LIST;
        if (isObjectPrimitiveType(o)) return ElementKind.PRIMITIVE;
        return ElementKind.OTHER;
    }


    public static boolean checkIfListValueIsPrimitiveType(List<?> input) {
        if (input == null || input.isEmpty())  return true;
        for (Object o : input) {
            if (o == null) continue;
            if (!isObjectPrimitiveType(o)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static String extractHeaders(List<?> input) {
        StringBuilder sb = new StringBuilder();
        if (!input.isEmpty() && (input.get(0) instanceof Map)) {
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
        return (entry.getValue() == null || (entry.getValue() instanceof Boolean
                || entry.getValue() instanceof Character
                || entry.getValue() instanceof CharSequence
                || entry.getValue() instanceof Number));
    }

    public static boolean isObjectPrimitiveType(Object o) {
        return ((o == null) || (o instanceof Boolean)
                || (o instanceof Character)
                || (o instanceof CharSequence)
                || (o instanceof Number));
    }
}
