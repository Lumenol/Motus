package fr.lumen.motus.web;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

class SolveQuery {
    @Length(min = 1, max = 1)
    private String first;
    @Min(1)
    private int length;
    private int depth;
    private List<Proposition> propositions = new ArrayList<>();

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public String toString() {
        return "SolveQuery{" +
                "first=" + first +
                ", length=" + length +
                ", propositions=" + propositions +
                '}';
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<Proposition> getPropositions() {
        return propositions;
    }

    public void setPropositions(List<Proposition> propositions) {
        this.propositions = propositions;
    }

    static class Proposition {
        private String word;
        private String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        @Override
        public String toString() {
            return "Proposition{" +
                    "word='" + word + '\'' +
                    ", result='" + result + '\'' +
                    '}';
        }
    }
}
