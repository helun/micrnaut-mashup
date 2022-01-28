package se.cygni.mashup.wikipedia;

import java.net.URI;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class WikiDataIdTest {

    @Test
    void testParseUri() {
        assertThat(WikiDataId.fromUri(URI.create("https://www.wikidata.org/wiki/Q11649")))
            .isEqualTo(new WikiDataId("Q11649"));
    }

}