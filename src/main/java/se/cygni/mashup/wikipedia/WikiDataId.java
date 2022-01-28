package se.cygni.mashup.wikipedia;

import java.net.URI;
import java.util.regex.Pattern;

public record WikiDataId(String value) {

    private final static Pattern WIKI_DATA_URI_PATTERN = Pattern.compile("/wiki/(.+)");

    public static WikiDataId fromUri(URI wikidataUri) {
        var path = wikidataUri.getPath();
        var matcher = WIKI_DATA_URI_PATTERN.matcher(path);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Could not resolve wikidata id from uri: " + wikidataUri);
        }
        return new WikiDataId(matcher.group(1));
    }
}
