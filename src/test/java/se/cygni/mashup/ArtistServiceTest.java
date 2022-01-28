package se.cygni.mashup;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import se.cygni.mashup.coverart.CoverArtClient;
import se.cygni.mashup.musicbrains.MusicBrainzArtistInfo;
import se.cygni.mashup.musicbrains.MusicBrainzClient;
import se.cygni.mashup.wikipedia.WikipediaResult;
import se.cygni.mashup.wikipedia.WikipediaService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArtistServiceTest {

    MusicBrainzClient musicBrainzClient = mock(MusicBrainzClient.class);
    WikipediaService wikipediaService = mock(WikipediaService.class);
    CoverArtClient coverArtClient = mock(CoverArtClient.class);

    ArtistService service = new ArtistService(musicBrainzClient, wikipediaService, coverArtClient);
    String mbid = Fixtures.VALID_MBID;

    @Test
    void happyPath() {
        MusicBrainzArtistInfo mbInfo = setupSuccessfulMusicBrainzResponse();
        setupSuccessfulWikipediaResponse();
        setupSuccessfulCoverArtResponse();

        var result = service.findArtist(mbid).block();
        assertThat(result).isNotNull();
        assertThat(result.mbid()).isEqualTo(mbInfo.id());
        assertThat(result.name()).isEqualTo(mbInfo.name());
        assertThat(result.description()).isEqualTo(Fixtures.DESCRIPTION);

        var expectedAlbum = Fixtures.basicAlbumBuilder().build();
        assertThat(result.albums()).containsExactly(expectedAlbum);
    }

    @Test
    void mbCallFails() {
        when(musicBrainzClient.getArtistInfo(mbid)).thenReturn(Mono.error(new Exception("boom!")));
        assertThatThrownBy(() -> service.findArtist(mbid).block())
            .hasMessage("java.lang.Exception: boom!");
    }

    @Test
    void missingCoverArt() {
        setupSuccessfulMusicBrainzResponse();
        setupSuccessfulWikipediaResponse();
        when(coverArtClient.getImages(any())).thenReturn(Mono.empty());

        var result = service.findArtist(mbid).block();
        assertThat(result).isNotNull();

        var expectedAlbum = Fixtures.basicAlbumBuilder()
            .image(null)
            .build();
        assertThat(result.albums()).containsExactly(expectedAlbum);
    }

    @Test
    void coverArtCallFails() {
        setupSuccessfulMusicBrainzResponse();
        setupSuccessfulWikipediaResponse();
        when(coverArtClient.getImages(any())).thenReturn(Mono.error(new Exception("boom!")));

        var result = service.findArtist(mbid).block();
        assertThat(result).isNotNull();

        var expectedAlbum = Fixtures.basicAlbumBuilder()
            .image(null)
            .build();
        assertThat(result.albums()).containsExactly(expectedAlbum);
    }

    @Test
    void missingDescription() {
        MusicBrainzArtistInfo mbInfo = setupSuccessfulMusicBrainzResponse();
        setupSuccessfulCoverArtResponse();
        when(wikipediaService.getWikipediaDescription(any())).thenReturn(Mono.just(WikipediaResult.empty()));

        var result = service.findArtist(mbid).block();
        assertThat(result).isNotNull();
        assertThat(result.mbid()).isEqualTo(mbInfo.id());
        assertThat(result.name()).isEqualTo(mbInfo.name());
        assertThat(result.description()).isNull();
        assertThat(result.albums()).isNotEmpty();
    }

    private void setupSuccessfulCoverArtResponse() {
        var coverArtResponse = Fixtures.basicCoverArtResponse();
        when(coverArtClient.getImages(any())).thenReturn(Mono.just(coverArtResponse));
    }

    private void setupSuccessfulWikipediaResponse() {
        when(wikipediaService.getWikipediaDescription(any())).thenReturn(Mono.just(new WikipediaResult(Fixtures.DESCRIPTION)));
    }

    private MusicBrainzArtistInfo setupSuccessfulMusicBrainzResponse() {
        var mbInfo = Fixtures.basicMbArtistInfo(mbid);
        when(musicBrainzClient.getArtistInfo(mbid)).thenReturn(Mono.just(mbInfo));
        return mbInfo;
    }

}