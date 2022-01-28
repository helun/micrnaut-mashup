package se.cygni.mashup.coverart;

import java.net.URI;
import org.junit.jupiter.api.Test;
import se.cygni.mashup.ApiFixtures;
import se.cygni.mashup.coverart.CoverArtClient.CoverArtResponse;
import se.cygni.mashup.coverart.CoverArtClient.Image;
import static org.assertj.core.api.Assertions.assertThat;

class CoverArtClientTest {

    @Test
    void fromJson() {
        var source = ApiFixtures.coverArtResponse();
        var response = ApiFixtures.readAs(source, CoverArtResponse.class);
        assertThat(response.images()).isNotEmpty();
        assertThat(response.fistFrontImage()).contains(new Image(
            URI.create("http://coverartarchive.org/release/a146429a-cedc-3ab0-9e41-1aaf5f6cdc2d/3012495605.jpg"),
            true
        ));
    }

}