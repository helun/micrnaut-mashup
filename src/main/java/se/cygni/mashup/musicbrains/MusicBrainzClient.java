package se.cygni.mashup.musicbrains;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Mono;

@Client("musicbrainz")
@Header(name = "user-agent", value = "hbd-mashup-client")
public interface MusicBrainzClient {

    @Get("/artist/{mbid}?&fmt=json&inc=url-rels+release-groups")
    Mono<MusicBrainzArtistInfo> getArtistInfo(String mbid);

}
