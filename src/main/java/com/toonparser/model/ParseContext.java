package com.toonparser.model;

import java.util.ArrayList;
import java.util.List;

public class ParseContext {
    List<String> output = null;
    public ParseContext() {
        this.output = new ArrayList<>(1);
    }

    public List<String> getOutput() {
        return this.output;
    }

    public void add(String output) {
        this.output.add(output);
    }
}