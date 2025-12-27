package com.toonparser.api;

import com.toonparser.parser.ParseEngine;

import java.util.List;

public class Toon {

    private static final ParseEngine ENGINE = new ParseEngine();

    public List<?> parse(Object input) {
        return ENGINE.parse(input).lines();
    }

    public List<?> parse(List<?> input) {
        return ENGINE.parse(input).lines();
    }
}
