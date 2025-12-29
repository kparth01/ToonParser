package com.toonparser.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ToonIntegrationTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Toon toon = new Toon();

    @Test
    void processAllExampleResources_shouldReturnNonEmptyLines() throws Exception {
        String[] files = {
                "allkind.json",
                "flat.json",
                "lang.json",
                "nested.json",
                "toplevel.json",
                "Sample.json",
                "JsonSampleSmall.json",
                "JsonSample.json"
        };

        for (String fileName : files) {
            try (InputStream is = getClass().getResourceAsStream("/" + fileName)) {
                assertNotNull(is, "Resource not found on classpath: /" + fileName);

                JsonNode node = mapper.readTree(is);
                List<?> result;
                if (node.isObject()) {
                    Map<?, ?> map = mapper.convertValue(node, Map.class);
                    result = toon.parse(map);
                } else if (node.isArray()) {
                    List<?> list = mapper.convertValue(node, List.class);
                    result = toon.parse(list);
                } else {
                    fail("Unsupported JSON node type for file: " + fileName);
                    return;
                }

                assertNotNull(result, "Parsed result should not be null for: " + fileName);
                assertFalse(result.isEmpty(), "Parsed result should not be empty for: " + fileName);
            }
        }
    }
}
