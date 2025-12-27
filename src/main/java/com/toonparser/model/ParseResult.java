package com.toonparser.model;
import java.util.Collections;
import java.util.List;

public class ParseResult {

    private final List<String> lines;

    public ParseResult(List<String> lines) {
        this.lines = Collections.unmodifiableList(lines);
    }

    public List<String> lines() {
        return lines;
    }

    public int size() {
        return lines.size();
    }

    @Override
    public String toString() {
        return String.join("\n", lines);
    }
}

