package com.toonparser.api;

import com.toonparser.parser.ParseEngine;

import java.util.List;

public class Toon {

    public List<?> parse(Object input) {
        return ParseEngine.parse(input);
    }
}
