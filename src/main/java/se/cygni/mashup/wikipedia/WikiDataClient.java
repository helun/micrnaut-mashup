package se.cygni.mashup.wikipedia;

import com.fasterxml.jackson.databind.JsonNode;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Mono;

@Client("wikidata")
public interface WikiDataClient {

    @Get("?action=wbgetentities&ids={dataId}&format=json&props=sitelinks")
    Mono<JsonNode> getSiteLinks(String dataId);
}
