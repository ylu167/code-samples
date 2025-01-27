package lab7;

public class Nomination {
    String category;
    String title;
    String artist;

    public Nomination(String category, String title, String artist) {
        this.category = category;
        this.title = title;
        this.artist = artist;
    }

    public String toString() {
        return artist + ": " + title + ", " + category;
    }
}

