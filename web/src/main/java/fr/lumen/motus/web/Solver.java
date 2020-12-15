package fr.lumen.motus.web;

import io.quarkus.amazon.lambda.runtime.AmazonLambdaContext;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class Solver {

    @Funq("solve")
    public Answer solve(@Valid Query query, @Context AmazonLambdaContext context) {
        final Answer answer = new Answer();
        answer.setSolution(true);
        answer.setWord(query.getFirst().repeat(query.getLength()));
        return answer;
    }

    public static class Query {
        @Size(min = 1, max = 1)
        private String first;
        @Min(1)
        private int length;
        private List<Proposition> propositions = new ArrayList<>();

        @Override
        public String toString() {
            return "Query{" +
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

        public static class Proposition {
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

    public static class Answer {
        private boolean solution;
        private String word;

        public boolean isSolution() {
            return solution;
        }

        public void setSolution(boolean solution) {
            this.solution = solution;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }
    }
}
