package fr.lumen.motus.cli;

import picocli.CommandLine;

public class Utils {
    private Utils() {
    }

    public static int checkIfPositive(CommandLine.Model.CommandSpec spec, String field, int value) {
        if (value <= 0) {
            throw new CommandLine.ParameterException(spec.commandLine(),
                    String.format("Invalid value '%s' for option '--%s': " +
                            "value is not a positive number.", value, field));
        }
        return value;
    }
}
