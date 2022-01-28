package se.cygni.mashup;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.jackson.JacksonConfiguration;
import io.micronaut.jackson.ObjectMapperFactory;
import java.io.IOException;
import java.net.URL;

public class ApiFixtures {

    private static final JacksonConfiguration jsonConfig = new JacksonConfiguration();
    private static final ObjectMapperFactory objectMapperFactory = new ObjectMapperFactory();
    private static final ObjectMapper jsonMapper =
        new ObjectMapperFactory().objectMapper(jsonConfig, objectMapperFactory.jsonFactory(jsonConfig));

    public static <T> T readAs(URL resource, Class<T> clazz) {
        try {
            return jsonMapper.readValue(resource, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode readAsJson(final URL resource) {
        try {
            return jsonMapper.readTree(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static URL musicBrainzArtistInfo() {
        return getResource("mb-artist-info.json");
    }

    public static URL wikiDataSiteLinks() {
        return getResource("wikidata-site-links.json");
    }

    public static URL wikipediaExtract() {
        return getResource("wikipedia-extract.json");
    }

    public static URL coverArtResponse() {
        return getResource("coverart-response.json");
    }

    public static URL getResource(String path) {
        return assertNotNull(
            ApiFixtures.class.getClassLoader().getResource(path),
            "Could not find resource with path: " + path
        );
    }

    private static <T> T assertNotNull(T input, String message) {
        if (input == null) {
            throw new IllegalStateException(message);
        }
        return input;
    }
}
