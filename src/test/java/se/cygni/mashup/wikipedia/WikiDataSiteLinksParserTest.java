package se.cygni.mashup.wikipedia;

import org.junit.jupiter.api.Test;
import se.cygni.mashup.ApiFixtures;
import se.cygni.mashup.wikipedia.WikiDataSiteLinksParser.WikipediaSite;
import static org.assertj.core.api.Assertions.assertThat;

class WikiDataSiteLinksParserTest {

    @Test
    void testParse() {
        var json = ApiFixtures.readAsJson(ApiFixtures.wikiDataSiteLinks());
        var id = new WikiDataId("Q11649");
        var title = WikiDataSiteLinksParser.getTitle(id, WikipediaSite.EN, json);
        assertThat(title).isEqualTo("Nirvana (band)");
    }

}