package de.javagl.layoutanalyzer.painter;

import java.awt.Graphics2D;

import de.javagl.layoutanalyzer.layout.LayoutObject;

public interface LayoutObjectPainter<T extends LayoutObject> {

  public void paint(T object, Graphics2D graphics);

}
