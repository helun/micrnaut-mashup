package se.cygni.mashup.api;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Introspected
public record Artist(
    @NotEmpty String mbid,
    @NotEmpty String name,
    @Nullable
    String description,
    @NotNull List<Album> albums
) {
}
