package se.cygni.mashup;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import reactor.core.publisher.Mono;
import se.cygni.mashup.api.Artist;
import se.cygni.mashup.api.MusicBrainzId;

@Controller
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(final ArtistService artistService) {
        this.artistService = artistService;
    }

    @Get("/artist/{mbid}")
    @Operation(summary = "Get info about an artist",
        description = """
            Fetches artist info from MusicBrainz and adds description from Wikipedia and
            cover art URL's from Cover Art Archive if available.
            """
    )

    @ApiResponse(responseCode = "400", description = "Invalid MBID Supplied")
    @ApiResponse(responseCode = "404", description = "Artist not found")
    public Mono<Artist> getArtist(
        @Parameter(description = "A MusicBrainz ID (MBID)", example = "541f16f5-ad7a-428e-af89-9fa1b16d3c9c")
        @PathVariable @MusicBrainzId String mbid
    ) {
        return artistService.findArtist(mbid);
    }

}
