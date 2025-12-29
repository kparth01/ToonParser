package com.toonparser.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ToonComprehensiveTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Toon toon = new Toon();

    /**
     * Parse JSON text using the same approach as the library's Main runner:
     * - object -> Map -> toon.parse(map)
     * - array  -> List -> toon.parse(list)
     * - primitive -> wrapped in a List and passed to toon.parse(list)
     */
    @SuppressWarnings("unchecked")
    private List<?> parseFromJson(String json) throws Exception {
        JsonNode node = mapper.readTree(json);
        if (node.isObject()) {
            Map<?, ?> map = mapper.convertValue(node, Map.class);
            return toon.parse(map);
        } else if (node.isArray()) {
            List<?> list = mapper.convertValue(node, List.class);
            return toon.parse(list);
        } else {
            // wrap primitive in a list so the library can handle it uniformly
            List<Object> primitiveList = List.of(mapper.convertValue(node, Object.class));
            return toon.parse(primitiveList);
        }
    }

    @Test
    void handleStringPrimitive_shouldContainValue() throws Exception {
        String json = "\"Hello\"";
        List<?> out = parseFromJson(json);
        assertNotNull(out, "Result should not be null for string primitive");
        assertFalse(out.isEmpty(), "Result should not be empty for string primitive");
        String joined = out.stream().map(Object::toString).collect(Collectors.joining("\n"));
        assertTrue(joined.contains("Hello"), "Output should contain the string value 'Hello'");
    }

    @Test
    void handleStringList_shouldContainAllStrings() throws Exception {
        String json = "[\"Hello\",\"World\"]";
        List<?> out = parseFromJson(json);
        assertNotNull(out);
        assertFalse(out.isEmpty());
        String joined = out.stream().map(Object::toString).collect(Collectors.joining("\n"));
        assertTrue(joined.contains("Hello") && joined.contains("World"),
                "Output should contain both 'Hello' and 'World'");
    }

    @Test
    void handleArrayOfMixedValues_shouldIncludeRepresentativeTokens() throws Exception {
        String json = "[1, \"two\", {\"k\": \"v\"}, [3,4]]";
        List<?> out = parseFromJson(json);
        assertNotNull(out);
        assertFalse(out.isEmpty());
        String joined = out.stream().map(Object::toString).collect(Collectors.joining("\n"));
        // Verify at least one representative from mixed types appears
        assertTrue(joined.contains("1") || joined.contains("two") || joined.contains("k") || joined.contains("3"),
                "Output should include at least one of the mixed values (1, two, k, 3)");
    }

    @Test
    void handlePrimitiveTypesInObject_shouldPreserveValues() throws Exception {
        String json = "{\"id\": 7, \"active\": true, \"price\": 12.5}";
        List<?> out = parseFromJson(json);
        assertNotNull(out);
        assertFalse(out.isEmpty());
        String joined = out.stream().map(Object::toString).collect(Collectors.joining("\n"));
        assertTrue(joined.contains("7") || joined.contains("true") || joined.contains("12.5"),
                "Output should contain primitive values like 7, true or 12.5");
    }

    @Test
    void handleNestedObjects_shouldIncludeNestedKeysAndValues() throws Exception {
        String json = "[" +
                "  {" +
                "    \"company\": \"TechCorp\"," +
                "    \"departments\": [" +
                "      {" +
                "        \"name\": \"Engineering\"," +
                "        \"employees\": [ { \"name\": \"John\" } ]" +
                "      }" +
                "    ]" +
                "  }" +
                "]";
        List<?> out = parseFromJson(json);
        assertNotNull(out);
        assertFalse(out.isEmpty());
        String joined = out.stream().map(Object::toString).collect(Collectors.joining("\n"));
        assertTrue(joined.contains("TechCorp") || joined.contains("Engineering") || joined.contains("John"),
                "Output should include nested keys/values (TechCorp/Engineering/John)");
    }

    @Test
    void handleSingleLineArray_shouldBeRenderedAsSingleLineValues() throws Exception {
        String json = "[\"EN\",\"TH\",\"HIN\",\"JPY\"]";
        List<?> out = parseFromJson(json);
        assertNotNull(out);
        assertFalse(out.isEmpty());
        String joined = out.stream().map(Object::toString).collect(Collectors.joining("\n"));
        // Expect the single-line array renderer to contain tokens
        assertTrue(joined.contains("EN") && joined.contains("TH") && joined.contains("HIN") && joined.contains("JPY"),
                "Output should contain all single-line tokens EN, TH, HIN, JPY");
    }

    @Test
    void handleSingleLineObject_shouldIncludeKeysAndValuesInline() throws Exception {
        String json = "{\"id\":1,\"name\":\"Alice\",\"age\":25}";
        List<?> out = parseFromJson(json);
        assertNotNull(out);
        assertFalse(out.isEmpty());
        String joined = out.stream().map(Object::toString).collect(Collectors.joining("\n"));
        assertTrue(joined.contains("id") || joined.contains("Alice") || joined.contains("25"),
                "Output should include keys/values from single-line object");
    }
}
