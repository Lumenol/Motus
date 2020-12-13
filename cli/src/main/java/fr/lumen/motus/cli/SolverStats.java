package fr.lumen.motus.cli;

import fr.lumen.motus.*;
import picocli.CommandLine.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

@Command(name = "stats", description = "Calculate how much the number of proposition needed to find the solution.")
public class SolverStats implements Callable<Integer> {
    @Spec
    private Model.CommandSpec spec;
    @Option(names = {"-v", "--verbose"}, description = "Show more information.")
    private boolean verbose;
    @Mixin
    private SolverMixin mixin;

    @Option(names = {"-n", "--number"}, description = "Number of words to test.", required = true)
    private int number;

    public void setNumber(int number) {
        this.number = Utils.checkIfPositive(spec, "number", number);
    }

    @Override
    public Integer call() throws Exception {
        final List<String> words = getWords();
        final LongSummaryStatistics statistics = words.stream().mapToLong(play(words)).summaryStatistics();

        System.out.println("Count: " + statistics.getCount());
        System.out.println("Min: " + statistics.getMin());
        System.out.println("Average: " + statistics.getAverage());
        System.out.println("Max: " + statistics.getMax());
        return 0;
    }

    private ToLongFunction<String> play(List<String> words) {
        return word -> {
            mixin.info(word);
            final Matcher matcher = new Matcher(word);
            final Solver solver = new Solver(words, word.length(), word.charAt(0));
            final WordAdviser adviser = new WordAdviser(solver);
            int count = 0;
            while (true) {
                count++;
                final Optional<Advise> adviseOpt = mixin.getDepth().map(adviser::advise).orElseGet(adviser::advise);
                final Advise advise = adviseOpt.orElseThrow(() -> new UnsupportedOperationException(String.format("'%s' is unsupported.", word)));
                if (advise.isSolved()) {
                    if (verbose) {
                        mixin.info("");
                    }
                    return count;
                }
                final String proposition = advise.getWord();
                if (verbose) {
                    mixin.info(proposition);
                }
                solver.addProposition(proposition, matcher.result(proposition));
            }
        };
    }

    private List<String> getWords() throws IOException {
        List<String> words = mixin.loadDictionary();
        Collections.shuffle(words);
        return words.stream().filter(w -> w.length() == mixin.getLength()).limit(number).sorted().collect(Collectors.toList());
    }
}
