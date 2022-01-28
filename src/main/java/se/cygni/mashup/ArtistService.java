package se.cygni.mashup;

import jakarta.inject.Singleton;
import java.util.function.Consumer;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.cygni.mashup.api.Album;
import se.cygni.mashup.api.Artist;
import se.cygni.mashup.coverart.CoverArtClient;
import se.cygni.mashup.coverart.CoverArtClient.Image;
import se.cygni.mashup.musicbrains.MusicBrainzArtistInfo;
import se.cygni.mashup.musicbrains.MusicBrainzArtistInfo.ReleaseGroup;
import se.cygni.mashup.musicbrains.MusicBrainzClient;
import se.cygni.mashup.wikipedia.WikipediaService;

@Singleton
public class ArtistService {

    private final Logger log = LoggerFactory.getLogger(ArtistService.class);

    private final MusicBrainzClient musicBrainzClient;
    private final WikipediaService wikipediaService;
    private final CoverArtClient coverArtClient;

    public ArtistService(
        final MusicBrainzClient musicBrainzClient,
        final WikipediaService wikipediaService,
        final CoverArtClient coverArtClient
    ) {
        this.musicBrainzClient = musicBrainzClient;
        this.wikipediaService = wikipediaService;
        this.coverArtClient = coverArtClient;
    }

    public Mono<Artist> findArtist(String mbid) {
        return musicBrainzClient.getArtistInfo(mbid)
            .doOnError(logMbError(mbid))
            .flatMap(this::addData);
    }

    private Mono<Artist> addData(MusicBrainzArtistInfo mbInfo) {
        var futureWikipediaResult = wikipediaService.getWikipediaDescription(mbInfo);
        var futureAlbums = Flux.fromIterable(mbInfo.releaseGroups())
            .flatMap(this::toAlbum)
            .collectList();

        return Mono.zip(
            futureWikipediaResult,
            futureAlbums,
            (wikipediaResult, albums) -> new Artist(
                mbInfo.id(),
                mbInfo.name(),
                wikipediaResult.description(),
                albums.stream().sorted().toList()
            )
        );
    }

    private Mono<Album> toAlbum(ReleaseGroup rg) {
        var builder = Album.builder()
            .id(rg.id())
            .title(rg.title())
            .firstReleaseDate(rg.firstReleaseDate());
        return coverArtClient.getImages(rg.id())
            .map(response ->
                     builder.image(response.fistFrontImage()
                             .map(Image::image)
                             .orElse(null))
                         .build()
            )
            .onErrorResume(handleCoverArtError(rg))
            .switchIfEmpty(Mono.just(builder.build()));
    }

    private Consumer<Throwable> logMbError(final String mbid) {
        return err -> log.error(
            "MusicBrainz called failed for mbid {}, {}, {}",
            mbid,
            err.getMessage(),
            err.getClass()
        );
    }

    private Function<Throwable, Mono<? extends Album>> handleCoverArtError(ReleaseGroup rg) {
        return (err) -> {
            log.error(
                "Cover art call failed for release group: {}, {}, {}",
                rg.id(),
                err.getMessage(),
                err.getClass()
            );
            return Mono.empty();
        };
    }

}
