package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static final String FILEPATH = "/Users/parthkansara/Projects/Toon/Toon/src/main/resources/";

    public static void main(String[] args) {


        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("Hello and welcome!");
        System.out.println("###################################################");

        Map<String, Object> jsonOb2 = loadFile("Sample.json");
        System.out.println(jsonOb2);
        List<String> result2 = (List) ToonParser.parse(jsonOb2);
        result2.forEach(System.out::println);

        System.out.println("###################################################");
        System.out.println();
        System.out.println();

        Map<String, Object> jsonOb3 = loadFile("JsonSample.json");
        System.out.println(jsonOb3);
        List<String> result3 = (List) ToonParser.parse(jsonOb3);
        result3.forEach(System.out::println);

        System.out.println("###################################################");
        System.out.println();
        System.out.println();

        Map<String, Object> jsonOb4 = loadFile("CollegeSample.json");
        System.out.println(jsonOb4);
        List<String> result4= (List) ToonParser.parse(jsonOb4);
        result4.forEach(System.out::println);
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
