package se.cygni.mashup.musicbrains;

import java.net.URI;
import org.junit.jupiter.api.Test;
import se.cygni.mashup.ApiFixtures;
import se.cygni.mashup.wikipedia.WikiDataId;
import static org.assertj.core.api.Assertions.assertThat;

class MusicBrainzArtistInfoTest {

    @Test
    void fromJson() {
        var source = ApiFixtures.musicBrainzArtistInfo();
        var artistInfo = ApiFixtures.readAs(source, MusicBrainzArtistInfo.class);
        assertThat(artistInfo.name()).isEqualTo("Nirvana");
        assertThat(artistInfo.wikiDataID()).contains(
            WikiDataId.fromUri(URI.create("https://www.wikidata.org/wiki/Q11649"))
        );
        assertThat(artistInfo.releaseGroups()).isNotEmpty();
        var firstRelease = artistInfo.releaseGroups().get(0);
        assertThat(firstRelease.id()).isEqualTo("f1afec0b-26dd-3db5-9aa1-c91229a74a24");
        assertThat(firstRelease.title()).isEqualTo("Bleach");
        assertThat(firstRelease.firstReleaseDate()).isEqualTo("1989-06-01");

    }
}