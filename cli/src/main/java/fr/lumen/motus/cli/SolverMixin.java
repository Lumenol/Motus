package fr.lumen.motus.cli;

import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SolverMixin {
    private Integer depth;
    private int length;
    @Spec
    private CommandSpec spec;
    @Option(names = {"-w", "--words"}, description = "File containing the words to use.")
    private Path dictionary;
    @Option(names = {"-i", "--interactive"})
    private boolean interactive;

    private static List<String> clean(Stream<String> words) {
        return words.map(String::toUpperCase)
                .filter(w -> w.chars().allMatch(Character::isUpperCase))
                .sorted().distinct().collect(Collectors.toList());
    }

    public int getLength() {
        return length;
    }

    @Option(names = {"-l", "--length"}, description = "Length of researched word.", required = true)
    public void setLength(int length) {
        this.length = Utils.checkIfPositive(spec, "length", length);
    }

    public Optional<Integer> getDepth() {
        return Optional.ofNullable(depth);
    }

    @Option(names = {"-d", "--depth"}, description = "Number of solutions taken into account for calculating advice.")
    public void setDepth(int depth) {
        this.depth = Utils.checkIfPositive(spec, "depth", depth);
    }

    public List<String> loadDictionary() throws IOException {
        info("Loading dictionary.");
        if (dictionary == null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(SolverMixin.class.getResourceAsStream("/words.txt")))) {
                return clean(reader.lines());
            }
        } else {
            try (Stream<String> lines = Files.lines(dictionary)) {
                return clean(lines);
            }
        }
    }

    public boolean isInteractive() {
        return interactive;
    }

    public void info(String message) {
        if (isInteractive()) {
            System.out.println(message);
        }
    }
}
