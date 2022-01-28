package se.cygni.mashup;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import static io.micronaut.http.HttpRequest.GET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest
class ArtistControllerTest {

    @Inject
    EmbeddedServer embeddedServer;

    @Inject
    ArtistService artistService;

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void getHappy() {
        var mbid = Fixtures.VALID_MBID;
        var artist = Fixtures.basicArtist(mbid);
        when(artistService.findArtist(anyString())).thenReturn(Mono.just(artist));

        var response = client.toBlocking().retrieve(GET("/artist/" + mbid));
        assertThat(response).isNotBlank();
    }

    @Test
    void shouldValidateMbid() {
        assertThatThrownBy(() -> client.toBlocking().retrieve(GET("/artist/invalid-mbid")))
            .satisfies(err -> {
                if (err instanceof HttpClientResponseException httpException) {
                    assertThat(httpException.getStatus().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());
                }
            });
    }

    @Test
    void emptyResultShouldResultIn404() {
        when(artistService.findArtist(anyString())).thenReturn(Mono.empty());

        assertThatThrownBy(() -> client.toBlocking().retrieve(GET("/artist/e1f1e33e-2e4c-4d43-b91b-7064068d3283")))
            .satisfies(err -> {
                if (err instanceof HttpClientResponseException httpException) {
                    assertThat(httpException.getStatus().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
                }
            });
    }

    @MockBean(ArtistService.class)
    ArtistService artistService() {
        return mock(ArtistService.class);
    }
}