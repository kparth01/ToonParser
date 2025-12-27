package com.toonparser.parser;

import com.toonparser.model.ParseContext;
import com.toonparser.model.ParseResult;

import java.util.List;

public class ParseEngine {

    private final NodeDispatcher dispatcher = new NodeDispatcher();

    public ParseResult parse(Object input) {
        ParseContext context = new ParseContext();
        dispatcher.dispatch(input, context, 0, 0, false, false);
        return new ParseResult(context.getOutput());
    }

    public ParseResult parse(List<?> input) {
        ParseContext context = new ParseContext();
        dispatcher.dispatch(input, context, 0, 0, true, false);
        return new ParseResult(context.getOutput());
    }

}
