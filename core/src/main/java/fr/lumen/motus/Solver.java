package fr.lumen.motus;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import java.util.*;
import java.util.stream.Stream;

import static java.lang.Character.isUpperCase;

public class Solver {
    private final Model model = new Model("Motus");
    private final IntVar[] word;

    public Solver(List<String> dictionary, int length, char first) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive.");
        }
        if (first < 'A' || 'Z' < first) {
            throw new IllegalArgumentException("First character must be in A and Z.");
        }
        word = model.intVarArray(length, 'A', 'Z');
        eq(0, first).post();
        dictionary.stream().map(this::is).reduce(model::or).ifPresent(Constraint::post);
    }

    private Constraint eq(int index, char value) {
        return word[index].eq(value).decompose();
    }

    private Constraint is(String word) {
        if (this.word.length == word.length()) {
            Constraint[] constraints = new Constraint[this.word.length];
            for (int i = 0; i < this.word.length; i++) {
                constraints[i] = eq(i, word.charAt(i));
            }
            return model.and(constraints);
        }
        return model.falseConstraint();
    }

    public void addProposition(String proposition, String result) {
        if (word.length != proposition.length()) {
            throw new IllegalArgumentException("Proposition length must be " + word.length + ".");
        }
        if (proposition.length() != result.length()) {
            throw new IllegalArgumentException("Proposition and Result must have same length.");
        }
        if (proposition.chars().anyMatch(c -> !isUpperCase(c))) {
            throw new IllegalArgumentException("Proposition characters must be in A and Z.");
        }

        for (int i = 0; i < word.length; i++) {
            final char letter = proposition.charAt(i);
            final char color = result.charAt(i);
            switch (color) {
                case 'R':
                    eq(i, letter).post();
                    break;
                case 'J':
                    ne(i, letter).post();
                    List<Constraint> constraints = new ArrayList<>(word.length);
                    for (int j = 0; j < result.length(); j++) {
                        if (i != j && result.charAt(j) != 'R') {
                            constraints.add(eq(j, letter));
                        }
                    }
                    model.or(constraints.toArray(new Constraint[0])).post();
                    break;
                case 'B':
                    for (int j = 0; j < result.length(); j++) {
                        if (result.charAt(j) != 'R') {
                            ne(j, letter).post();
                        }
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Result must contains only : R,J,B.");
            }
        }
    }

    private Constraint ne(int index, char value) {
        return word[index].ne(value).decompose();
    }

    public Stream<String> allSolutions() {
        model.getSolver().reset();
        return Stream.generate(this::solution).takeWhile(Optional::isPresent).map(Optional::get);
    }

    private Optional<String> solution() {
        return Optional.ofNullable(model.getSolver().findSolution()).map(this::toString);
    }

    private String toString(Solution solution) {
        return Arrays.stream(word).map(solution::getIntVal).reduce(new StringBuilder(word.length), StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }
}

