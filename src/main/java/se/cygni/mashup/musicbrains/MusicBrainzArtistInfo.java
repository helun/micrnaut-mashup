package se.cygni.mashup.musicbrains;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import se.cygni.mashup.wikipedia.WikiDataId;

@Introspected
public record MusicBrainzArtistInfo(String id,
                                    String name,
                                    List<Relation> relations,
                                    @JsonProperty("release-groups")
                                    List<ReleaseGroup> releaseGroups) {

    public static final String WIKIDATA_REL_TYPE = "wikidata";

    public Optional<WikiDataId> wikiDataID() {
        return relations.stream()
            .filter(r -> WIKIDATA_REL_TYPE.equals(r.type))
            .findFirst()
            .map(r -> WikiDataId.fromUri(r.url.resource()));
    }

    @Introspected
    public record Relation(String type,
                           Url url) {
        public record Url(URI resource) {
            public static Url create(String uri) {
                return new Url(URI.create(uri));
            }
        }
    }

    @Introspected
    public record ReleaseGroup(
        String id,
        String title,
        @JsonProperty("first-release-date")
        String firstReleaseDate) {
    }

}
