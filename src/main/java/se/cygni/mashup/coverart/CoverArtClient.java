package se.cygni.mashup.coverart;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import reactor.core.publisher.Mono;

@Client(id = "coverart")
public interface CoverArtClient {

    @Get("/release-group/{mbid}")
    Mono<CoverArtResponse> getImages(String mbid);

    record CoverArtResponse(
        List<Image> images
    ) {
        public Optional<Image> fistFrontImage() {
            return images.stream()
                .filter(img -> img.front)
                .findFirst();
        }
    }

    record Image(
        URI image,
        boolean front
    ) {
    }
}
