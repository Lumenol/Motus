package fr.lumen.motus.solver;

import fr.lumen.motus.matcher.Matcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("length first [propositions-file]");
        if (args.length < 2) {
            System.err.println("Two arguments are required.");
            return;
        }
        final int length = Integer.parseInt(args[0]);
        final char first = args[1].charAt(0);

        System.out.println("Loading dictionary.");
        final Solver rootSolver = new Solver(words(), length, first);
        if (args.length >= 3) {
            final List<String> propositions = Files.readAllLines(Path.of(args[2]));
            propositions.forEach(s -> {
                final String[] proposition = s.split("\\s", 2);
                rootSolver.addProposition(proposition[0], proposition[1]);
            });
            System.out.println(propositions.size() + " propositions loaded.");
        }

        System.out.println("Searching solution.");
        final List<String> words = rootSolver.allSolutions().collect(Collectors.toList());
        if (words.isEmpty()) {
            System.out.println("No solution.");
        } else if (words.size() == 1) {
            System.out.println("Solution : " + words.get(0));
        } else {
            System.out.println("Computing most discriminant for " + words.size() + " words.");
            final Map<String, Double> counts = words.stream().collect(Collectors.toMap(s -> s, word -> mean(words, word)));
            counts.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(System.out::println);
        }
    }

    private static List<String> words() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Solver.class.getResourceAsStream("/words.txt")))) {
            return reader.lines().collect(Collectors.toList());
        }
    }

    private static double mean(List<String> words, String word) {
        return matchers(words).parallelStream().map(solver(words, word)).mapToLong(solver -> solver.allSolutions().count()).average().getAsDouble();
    }

    private static List<Matcher> matchers(List<String> words) {
        return words.stream().map(Matcher::new).collect(Collectors.toList());
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
}
