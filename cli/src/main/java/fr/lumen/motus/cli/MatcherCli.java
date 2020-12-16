package fr.lumen.motus.cli;


import fr.lumen.motus.Matcher;
import picocli.CommandLine.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Command(name = "match", description = "Compute motus response.")
public class MatcherCli implements Callable<Integer> {
    @Parameters(arity = "0..*", description = "Propositions")
    private final List<String> propositions = List.of();
    private int minRow;
    private int height;
    private int width;
    @Spec
    private Model.CommandSpec spec;
    @Option(names = {"-g", "--guess"}, description = "Word to find.", required = true)
    private String word;
    @Option(names = {"-o", "--output"}, description = "Output png file for result.")
    private File output;

    @Option(names = {"-r", "--row"}, description = "Min number of rows.", defaultValue = "6")
    public void setMinRow(int row) {
        this.minRow = Utils.checkIfPositive(spec, "row", row);
    }

    @Option(names = {"-h", "--height"}, description = "Height of row.", defaultValue = "50")
    public void setHeight(int height) {
        this.height = Utils.checkIfPositive(spec, "height", height);
    }

    @Option(names = {"-w", "--width"}, description = "Width of row.", defaultValue = "450")
    public void setWidth(int width) {
        this.width = Utils.checkIfPositive(spec, "width", width);
    }

    @Override
    public Integer call() {
        final String response = word.trim().toUpperCase();
        final int length = response.length();

        final List<String> propositions = this.propositions.stream().map(String::trim).map(String::toUpperCase).collect(Collectors.toList());
        if (propositions.stream().anyMatch(p -> p.length() != length)) {
            System.err.println("All propositions must have " + length + " characters.");
            return 1;
        }

        final Matcher matcher = new Matcher(response);
        if (Objects.isNull(output)) {
            propositions.stream().map(matcher::result).forEach(System.out::println);
            return 0;
        }
        final List<String> lines = propositions.isEmpty() ? Collections.singletonList(response.charAt(0) + " ".repeat(length - 1)) : propositions;
        final Cell[][] cells = new Cell[Math.max(lines.size(), minRow)][length];
        BufferedImage image = new BufferedImage(width - width % length, cells.length * height, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < cells.length; i++) {
            final String line = i < lines.size() ? lines.get(i) : " ".repeat(length);
            final String result = matcher.result(line);
            for (int j = 0; j < cells[i].length; j++) {
                final Cell cell;
                final char letter = line.charAt(j);
                switch (result.charAt(j)) {
                    case 'R':
                        cell = new RedCell(letter);
                        break;
                    case 'J':
                        cell = new YellowCell(letter);
                        break;
                    default:
                        cell = new Cell(letter);
                }
                cells[i][j] = cell;
            }
        }
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                final Cell cell = cells[i][j];
                final Graphics2D graphics = (Graphics2D) image.getGraphics();
                final int width = this.width / length;
                cell.draw(graphics, new Rectangle(j * width, i * height, width, height));
            }
        }
        try {
            ImageIO.write(image, "png", output);
        } catch (IOException e) {
            System.err.println("Output can't be write because " + e.getMessage());
            return 1;
        }
        return 0;
    }
}
