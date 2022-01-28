package se.cygni.mashup;

import java.net.URI;
import java.util.List;
import se.cygni.mashup.api.Album;
import se.cygni.mashup.api.Artist;
import se.cygni.mashup.coverart.CoverArtClient.CoverArtResponse;
import se.cygni.mashup.coverart.CoverArtClient.Image;
import se.cygni.mashup.musicbrains.MusicBrainzArtistInfo;
import se.cygni.mashup.musicbrains.MusicBrainzArtistInfo.Relation;
import se.cygni.mashup.musicbrains.MusicBrainzArtistInfo.Relation.Url;
import se.cygni.mashup.musicbrains.MusicBrainzArtistInfo.ReleaseGroup;

public class Fixtures {

    public static final String RELEASE_DATE = "1990-01-01";
    public static final String RELEASE_GROUP_ID = "album-id";
    public static final String ALBUM_TITLE = "album title";
    public static final String ARTIST_NAME = "artist name";
    public static final String DESCRIPTION = "a description";
    public static String VALID_MBID = "5d8050da-e5c9-3a1b-9e39-0a73620218c0";
    public static URI WIKIDATA_URI = URI.create("https://www.wikidata.org/wiki/Q11649");
    public static URI IMAGE_URI = URI.create("http.//example.com/image.jpg");

    public static Album.Builder basicAlbumBuilder() {
        return Album.builder()
            .id(RELEASE_GROUP_ID)
            .title(ALBUM_TITLE)
            .firstReleaseDate(RELEASE_DATE)
            .image(IMAGE_URI);
    }

    public static MusicBrainzArtistInfo basicMbArtistInfo(String mbid) {
        return new MusicBrainzArtistInfo(
            VALID_MBID,
            ARTIST_NAME,
            List.of(new Relation("wikidata", new Url(WIKIDATA_URI))),
            List.of(new ReleaseGroup(RELEASE_GROUP_ID, ALBUM_TITLE, RELEASE_DATE))
        );
    }

    public static CoverArtResponse basicCoverArtResponse() {
        return new CoverArtResponse(List.of(new Image(IMAGE_URI, true)));
    }

    public static Artist basicArtist(String mbid) {
        return new Artist(
            mbid,
            ARTIST_NAME,
            DESCRIPTION,
            List.of(basicAlbumBuilder().build())
        );
    }

}
