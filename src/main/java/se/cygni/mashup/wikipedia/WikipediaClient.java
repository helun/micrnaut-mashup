package se.cygni.mashup.wikipedia;

import com.fasterxml.jackson.databind.JsonNode;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Mono;

@Client("wikipedia")
public interface WikipediaClient {

    @Get(value = "?action=query&format=json&prop=extracts&exintro=true&redirects=true&titles={title}")
    Mono<JsonNode> getPageData(String title);

}
