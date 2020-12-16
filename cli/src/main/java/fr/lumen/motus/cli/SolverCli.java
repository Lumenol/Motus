package fr.lumen.motus.cli;

import fr.lumen.motus.*;
import picocli.CommandLine.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;

@Command(name = "solve", description = "Propose word for solve motus.")
public class SolverCli implements Callable<Integer> {
    @Mixin
    private SolverMixin mixin;
    @Option(names = {"-f", "--first"}, description = "First letter of researched word.", required = true)
    private char first;


    @Override
    public Integer call() throws Exception {
        final Solver solver = getSolver();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean loop = true;
        while (loop) {
            mixin.info("Enter proposition.");
            while (true) {
                final String line = reader.readLine();
                if (Objects.isNull(line)) {
                    loop = false;
                    break;
                }
                if (line.isBlank()) {
                    break;
                }
                final String[] proposition = line.toUpperCase().split("\\s", 2);
                if (proposition.length < 2) {
                    System.err.println("Format: word result");
                    if (!mixin.isInteractive()) {
                        return 1;
                    }
                }
                try {
                    solver.addProposition(proposition[0].trim().toUpperCase(), proposition[1].trim().toUpperCase());
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    if (!mixin.isInteractive()) {
                        return 1;
                    }
                }
            }

            final WordAdviser adviser = new WordAdviser(solver);
            mixin.info("Searching solution.");
            final Optional<Advise> adviseOpt = mixin.getDepth().map(adviser::advise).orElseGet(adviser::advise);
            if (adviseOpt.isEmpty()) {
                mixin.info("No solution.");
                if (!mixin.isInteractive()) {
                    System.out.println();
                }
                return 0;
            } else {
                final Advise advise = adviseOpt.get();
                final String word = advise.getWord();

                if (advise.isSolved()) {
                    mixin.info("Solution : " + word);
                    if (!mixin.isInteractive()) {
                        System.out.println(word);
                    }
                    return 0;
                } else {
                    mixin.info("Advice : " + word);
                    if (!mixin.isInteractive()) {
                        System.out.println(word);
                    }
                }
            }
        }
        return 0;
    }

    private Solver getSolver() throws Exception {
        return new Solver(mixin.loadDictionary(), mixin.getLength(), first);
    }

}
