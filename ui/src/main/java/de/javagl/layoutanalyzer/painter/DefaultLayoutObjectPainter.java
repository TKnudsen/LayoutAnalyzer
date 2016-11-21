package de.javagl.layoutanalyzer.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import de.javagl.layoutanalyzer.objects.LayoutObject;

/**
 * Paints gray square (32x32 pixel) for every Layout Object 
 */
public class DefaultLayoutObjectPainter<T extends LayoutObject> implements LayoutObjectPainter<T> {

  @Override
  public void paint(LayoutObject object, Graphics2D graphics) {
    Shape shape = object.getShape();

    graphics.setColor(new Color(32, 32, 32, 32));
    graphics.fill(shape);
    graphics.setColor(Color.GRAY);
    graphics.draw(shape);

    String label = object.getLabel();
    Rectangle2D stringBounds = graphics.getFontMetrics().getStringBounds(label, graphics);
    int sx = (int) (-stringBounds.getWidth() * 0.5);
    int sy = (int) (stringBounds.getHeight() * 0.5);
    graphics.drawString(label, sx, sy);
  }

}
