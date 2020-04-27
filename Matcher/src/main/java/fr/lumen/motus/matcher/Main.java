package fr.lumen.motus.matcher;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private final static int MIN_ROWS = 6;
    private final static int HEIGHT = 50;
    private final static int WIDTH = 450;
    private static final String OUTPUT = "motus.png";

    public static void main(String[] args) {
        System.out.println("response [proposition...]");
        if (args.length < 1) {
            System.err.println("One argument is required.");
            return;
        }
        final String response = args[0].toUpperCase();
        final int length = response.length();
        final List<String> propositions = Arrays.stream(args).skip(1).map(String::toUpperCase).collect(Collectors.toList());
        if (propositions.stream().anyMatch(p -> p.length() != length)) {
            System.err.println("All propositions must have " + length + " characters.");
        }

        final List<String> lines = propositions.isEmpty() ? Collections.singletonList(response.charAt(0) + " ".repeat(length - 1)) : propositions;
        final Cell[][] cells = new Cell[Math.max(lines.size(), MIN_ROWS)][length];
        BufferedImage image = new BufferedImage(WIDTH - WIDTH % length, cells.length * HEIGHT, BufferedImage.TYPE_INT_ARGB);

        final Matcher matcher = new Matcher(response);
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
                final int width = WIDTH / length;
                cell.draw(graphics, new Rectangle(j * width, i * HEIGHT, width, HEIGHT));
            }
        }
        try {
            ImageIO.write(image, "png", new File(OUTPUT));
        } catch (IOException e) {
            System.err.println("Output can't be write because " + e.getMessage());
        }
    }
}
