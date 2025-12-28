package com.toonparser.parser;

import com.toonparser.model.ParseContext;
import com.toonparser.model.ParseResult;
import com.toonparser.utils.Utils;

import java.util.List;

public class ParseEngine {

    private final NodeDispatcher dispatcher = new NodeDispatcher();

    public ParseResult parse(Object input) {
        ParseContext context = new ParseContext();
        boolean nonUniformRoot = Utils.checkIfArrayIsNonUniform(List.of(input));
        dispatcher.dispatch(input, context, 0, 0, nonUniformRoot, false, true);
        return new ParseResult(context.getOutput());
    }

    public ParseResult parse(List<?> input) {
        ParseContext context = new ParseContext();
        boolean nonUniformRoot = Utils.checkIfArrayIsNonUniform(List.of(input));
        dispatcher.dispatch(input, context, 0, 0, nonUniformRoot, false, true);
        return new ParseResult(context.getOutput());
    }

}
