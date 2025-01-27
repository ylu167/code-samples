package lab7;

import java.util.List;

public class Artist implements Comparable<Artist> {
    String name;
    List<Nomination> nominations;

    public Artist(String name, List<Nomination> nominations) {
        this.name = name;
        this.nominations = nominations;
    }

    public int getNumNominations() {
        return nominations.size();
    }

    @Override
    public int compareTo(Artist other) {
        // For sorting in decreasing order of nominations, and within same number, ascending order of names (case-insensitive)
        int numNomsDiff = other.getNumNominations() - this.getNumNominations();
        if (numNomsDiff != 0) {
            return numNomsDiff;
        } else {
            return this.name.compareToIgnoreCase(other.name);
        }
    }

    public String toString() {
        return name + ": " + nominations.size();
    }
}
