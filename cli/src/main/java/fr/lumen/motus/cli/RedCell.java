package fr.lumen.motus.cli;

import java.awt.*;

public class RedCell extends Cell {
    public RedCell(char letter) {
        super(letter);
    }

    @Override
    protected void drawInner(Graphics2D graphics, Rectangle rectangle) {
        graphics.setColor(new Color(254, 22, 20));
        graphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
}
