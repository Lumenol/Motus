package fr.lumen.motus.cli;

import fr.lumen.motus.DictionaryUtils;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
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
            return DictionaryUtils.getEmbedded();
        } else {
            try (Stream<String> lines = Files.lines(dictionary)) {
                return DictionaryUtils.clean(lines);
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
