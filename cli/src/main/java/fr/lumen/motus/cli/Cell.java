package fr.lumen.motus.cli;

import java.awt.*;

public class Cell {
    private final static int BORDER = 5;
    private final String letter;

    public Cell(char letter) {
        this.letter = Character.toString(letter).toUpperCase();
    }

    public void draw(Graphics2D graphics, Rectangle rectangle) {
        drawBackground(graphics, rectangle);
        final Rectangle inner = new Rectangle(rectangle);
        inner.grow(-BORDER / 2, -BORDER / 2);
        drawInner(graphics, inner);
        drawBorder(graphics, rectangle);
        drawLetter(graphics, rectangle);
    }

    protected void drawBackground(Graphics2D graphics, Rectangle rectangle) {
        graphics.setColor(new Color(12, 126, 212));
        graphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    protected void drawInner(Graphics2D graphics, Rectangle rectangle) {
    }

    protected void drawBorder(Graphics2D graphics, Rectangle rectangle) {
        graphics.setColor(new Color(50, 170, 248));
        graphics.setStroke(new BasicStroke(BORDER, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
        graphics.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    protected void drawLetter(Graphics2D graphics, Rectangle rectangle) {
        final Font font = new Font("Arial", Font.BOLD, 45);

        graphics.setColor(Color.WHITE);
        graphics.setFont(font);

        FontMetrics metrics = graphics.getFontMetrics(font);
        int x = rectangle.x + (rectangle.width - metrics.stringWidth(letter)) / 2;
        int y = rectangle.y + ((rectangle.height - metrics.getHeight()) / 2) + metrics.getAscent();
        graphics.drawString(letter, x, y);
    }

    @Override
    public String toString() {
        return letter;
    }
}
