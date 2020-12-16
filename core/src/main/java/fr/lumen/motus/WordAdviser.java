package fr.lumen.motus;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordAdviser {
    private final Solver solver;

    public WordAdviser(Solver solver) {
        this.solver = solver;
    }

    private static double mean(List<String> words, String word) {
        return matchers(words).map(solver(words, word)).mapToLong(WordAdviser::solutionCount).average().getAsDouble();
    }

    private static long solutionCount(Solver solver) {
        return solver.allSolutions().count();
    }

    private static Stream<Matcher> matchers(List<String> words) {
        return words.stream().map(Matcher::new);
    }

    private static Function<Matcher, Solver> solver(List<String> words, String word) {
        char first = word.charAt(0);
        int length = word.length();
        return matcher -> {
            final Solver solver = new Solver(words, length, first);
            solver.addProposition(word, matcher.result(word));
            return solver;
        };
    }

    private double score(List<String> words, String word) {
        return -mean(words, word);
    }

    public Optional<Advise> advise() {
        return advise(-1);
    }

    public Optional<Advise> advise(int limit) {
        final Stream<String> solutions = limit <= 0 ? solver.allSolutions() : solver.allSolutions().limit(limit + 1);
        final List<String> words = solutions.collect(Collectors.toList());
        return advise(words);
    }

    private Optional<Advise> advise(List<String> words) {
        if (words.size() == 1) {
            return Optional.of(words.get(0)).map(Advise::solved);
        } else {
            return words.stream().max(Comparator.comparingDouble(o -> score(words, o))).map(Advise::unsolved);
        }
    }

}
