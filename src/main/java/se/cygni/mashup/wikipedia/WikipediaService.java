package se.cygni.mashup.wikipedia;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import se.cygni.mashup.musicbrains.MusicBrainzArtistInfo;
import se.cygni.mashup.wikipedia.WikiDataSiteLinksParser.WikipediaSite;

@Singleton
public class WikipediaService {

    private final Logger log = LoggerFactory.getLogger(WikipediaService.class);
    private final WikiDataClient wikiDataClient;
    private final WikipediaClient wikipediaClient;

    public WikipediaService(final WikiDataClient wikiDataClient, final WikipediaClient wikipediaClient) {
        this.wikiDataClient = wikiDataClient;
        this.wikipediaClient = wikipediaClient;
    }

    public Mono<WikipediaResult> getWikipediaDescription(MusicBrainzArtistInfo mbInfo) {
        return resolveTitle(mbInfo)
            .flatMap(this::getDescription)
            .onErrorResume(this::handleWikipediaError)
            .switchIfEmpty(Mono.just(WikipediaResult.empty()));
    }

    private Mono<? extends WikipediaResult> handleWikipediaError(final Throwable err) {
        log.error("wikipedia called failed: {} {}", err.getMessage(), err.getClass());
        return Mono.just(WikipediaResult.empty());
    }

    private Mono<String> resolveTitle(MusicBrainzArtistInfo mbInfo) {
        return mbInfo.wikiDataID()
            .map(id -> wikiDataClient.getSiteLinks(id.value())
                .map(body -> WikiDataSiteLinksParser.getTitle(id, WikipediaSite.EN, body)))
            .orElse(Mono.empty());
    }

    private Mono<WikipediaResult> getDescription(String title) {
        return wikipediaClient.getPageData(title)
            .map(WikipediaPageParser::getDescription)
            .map(WikipediaResult::new);
    }

}
