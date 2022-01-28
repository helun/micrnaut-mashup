package se.cygni.mashup.wikipedia;

import com.fasterxml.jackson.databind.JsonNode;

public class WikipediaPageParser {

    public static String getDescription(JsonNode json) {
        var pagesNode = json.at("/query/pages");
        var n = pagesNode.elements().next();
        return n.path("extract").textValue();
    }
}
