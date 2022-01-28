package se.cygni.mashup.wikipedia;

public record WikipediaResult(String description) {

    private static final WikipediaResult EMPTY = new WikipediaResult(null);

    public static WikipediaResult empty() {
        return EMPTY;
    }
}
