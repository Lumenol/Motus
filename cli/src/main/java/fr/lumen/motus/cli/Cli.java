package fr.lumen.motus.cli;

import io.quarkus.picocli.runtime.annotations.TopCommand;

import static picocli.CommandLine.Command;

@TopCommand
@Command(name = "Motus", subcommands = {SolverStats.class, SolverCli.class, MatcherCli.class})
public class Cli {

}
