package de.javagl.layoutanalyzer.painter;

import java.awt.Graphics2D;

import de.javagl.layoutanalyzer.Layout;
import de.javagl.layoutanalyzer.objects.LayoutObject;

/**
 * Can paint an {@link LayoutObject} (or a subtype) on an {@link Graphics2D} object.
 * 
 * @param <T>
 *          the exact subtype of {@link LayoutObject}
 */
public interface LayoutObjectPainter<T extends LayoutObject> {

  /**
   * Paints a given {@link Layout} on a canvas
   * 
   * @param object
   *          the object (not null)
   * @param graphics
   *          the canvas (not null)
   */
  public void paint(T object, Graphics2D graphics);

}
