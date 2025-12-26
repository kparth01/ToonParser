package com.toonparser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toonparser.api.Toon;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    static final String FILEPATH = "/Users/parthkansara/Projects/Toon/Toon/src/main/resources/";

    public static void main(String[] args) {

        processFile("Sample.json");

        System.out.println("###################################################");
        System.out.println();
        System.out.println();

        processFile("JsonSample.json");

        System.out.println("###################################################");
        System.out.println();
        System.out.println();
    }

    private static void processFile(String fileName) {
        Map<String, Object> jsonOb = loadFile(fileName);
        System.out.println(jsonOb);
        System.out.println("################## Toon API #################################");
        Toon toon = new Toon();
        List<?> result = toon.parse(jsonOb);
        result.forEach(System.out::println);
    }

    private static Map loadFile(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(FILEPATH + fileName);

        try {
            Map result = objectMapper.readValue(file, HashMap.class);
            System.out.println("Loaded User: " + result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
