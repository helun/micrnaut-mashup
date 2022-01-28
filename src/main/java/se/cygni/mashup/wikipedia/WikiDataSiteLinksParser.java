package se.cygni.mashup.wikipedia;

import com.fasterxml.jackson.databind.JsonNode;

public class WikiDataSiteLinksParser {

    public enum WikipediaSite {
        EN("enwiki");

        private final String siteKey;

        WikipediaSite(final String siteKey) {
            this.siteKey = siteKey;
        }
    }

    public static String getTitle(WikiDataId entityId, WikipediaSite site, JsonNode json) {
        var titleNode = json.at("/entities/%s/sitelinks/%s/title".formatted(entityId.value(), site.siteKey));
        if (titleNode.isMissingNode()) {
            throw new IllegalArgumentException("Could not resolve %s title for id %s".formatted(site, entityId));
        }
        return titleNode.textValue();
    }

}
