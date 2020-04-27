package fr.lumen.motus.matcher;

import java.awt.*;

public class YellowCell extends Cell {
    public YellowCell(char letter) {
        super(letter);
    }


    @Override
    protected void drawInner(Graphics2D graphics, Rectangle rectangle) {
        graphics.setColor(new Color(241, 201, 5));
        graphics.fillOval(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
}
