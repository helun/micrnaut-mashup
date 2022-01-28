package se.cygni.mashup.api;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import java.net.URI;
import javax.validation.constraints.NotEmpty;

@Introspected
public record Album(
    @NotEmpty String title,
    @NotEmpty String id,
    @NotEmpty String firstReleaseDate,
    @Nullable
    URI image) implements Comparable<Album> {

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public int compareTo(final Album o) {
        return firstReleaseDate.compareTo(o.firstReleaseDate);
    }

    public static class Builder {

        private String title;
        private String id;
        private String firstReleaseDate;
        private URI image;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder firstReleaseDate(String d) {
            this.firstReleaseDate = d;
            return this;
        }

        public Builder image(URI uri) {
            this.image = uri;
            return this;
        }

        public Album build() {
            return new Album(title, id, firstReleaseDate, image);
        }
    }
}
