package com.toonparser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toonparser.api.Toon;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {

    static final String FILEPATH = "/Users/parthkansara/Projects/Toon/Toon/src/main/resources/";

    public static void main(String[] args) throws IOException {

        processFile("allkind.json");
        processFile("flat.json");
        processFile("lang.json");
        processFile("nested.json");
        processFile("toplevel.json");
        processFile("Sample.json");
        processFile("JsonSampleSmall.json");
        processFile("JsonSample.json");

    }

    private static void processFile(String fileName) throws IOException {
        Object jsonOb = loadFile(fileName);
        Toon toon = new Toon();
        System.out.println("LOAD NEW JSON: " + jsonOb);
        if (jsonOb instanceof Map) {
            System.out.println  ("################## Toon API Refactored for Map Filename: " + fileName + " ########################");
            List<?> result2 = toon.parse(jsonOb);
            result2.forEach(System.out::println);
        } else if (jsonOb instanceof List) {
            System.out.println("################## Toon API Refactored for List Filename: " + fileName + " ########################");
            List<?> result2 = toon.parse((List) jsonOb);
            result2.forEach(System.out::println);

        }
    }

    private static Object loadFile(String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(FILEPATH + fileName);
        try {
            JsonNode node = objectMapper.readTree(file);
            if (node.isObject()) {
                Map<String, Object> map = objectMapper.convertValue(node, Map.class);
                return map;
            } else if (node.isArray()) {
                List<Map<String, Object>> list = objectMapper.convertValue(node, List.class);
                return list;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
