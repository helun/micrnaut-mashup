package se.cygni.mashup.wikipedia;

import org.junit.jupiter.api.Test;
import se.cygni.mashup.ApiFixtures;
import static org.assertj.core.api.Assertions.assertThat;

class WikipediaPageParserTest {

    @Test
    void testParse() {
        var json = ApiFixtures.readAsJson(ApiFixtures.wikipediaExtract());
        assertThat(WikipediaPageParser.getDescription(json)).startsWith("<p class=");
    }

}