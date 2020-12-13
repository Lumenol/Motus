package fr.lumen.motus.cli;

import picocli.CommandLine;

import static picocli.CommandLine.Command;

@Command(name = "Motus", subcommands = {SolverStats.class, SolverCli.class, MatcherCli.class})
public class Cli {

    public static void main(String[] args) {
        final int exitCode = new CommandLine(new Cli()).execute(args);
        System.exit(exitCode);
    }
}
