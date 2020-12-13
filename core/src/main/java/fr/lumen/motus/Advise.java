package fr.lumen.motus;

import java.util.Objects;

public class Advise {
    private final String word;
    private final boolean solved;

    private Advise(String word, boolean solved) {
        this.solved = solved;
        this.word = word;
    }

    public static Advise solved(String word) {
        return new Advise(word, true);
    }

    public static Advise unsolved(String word) {
        return new Advise(word, false);
    }

    @Override
    public String toString() {
        return "Advise{" +
                "word='" + word + '\'' +
                ", solved=" + solved +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Advise advise = (Advise) o;
        return solved == advise.solved && word.equals(advise.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(solved, word);
    }

    public boolean isSolved() {
        return solved;
    }

    public String getWord() {
        return word;
    }
}
