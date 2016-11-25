/*
 * LayoutAnalyzer  
 *
 * Copyright (c) 2015-2015 Marco Hutter - http://www.javagl.de
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.layoutanalyzer;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import de.javagl.layoutanalyzer.objects.BaseLayoutObject;
import de.javagl.layoutanalyzer.objects.LayoutObject;

/**
 * Methods to initialize {@link Layout}s with sets of {@link LayoutObject}s for testing
 */
public class Layouts {
  /**
   * Initialize the given {@link Layout} with few, simple {@link LayoutObject} s
   * 
   * @param layout
   *          The {@link Layout}
   */
  static void initSimpleTestData(Layout<LayoutObject> layout) {
    int n = 0;
    LayoutObject p0 = new BaseLayoutObject(String.valueOf(n++));
    p0.setPosition(250, 250);
    p0.setShape(new Rectangle2D.Double(-75, -75, 150, 150));

    LayoutObject p1 = new BaseLayoutObject(String.valueOf(n++));
    p1.setPosition(350, 250);
    p1.setShape(new Rectangle2D.Double(-75, -75, 150, 150));

    LayoutObject p2 = new BaseLayoutObject(String.valueOf(n++));
    p2.setPosition(250, 350);
    p2.setShape(new Rectangle2D.Double(-75, -75, 150, 150));

    LayoutObject p3 = new BaseLayoutObject(String.valueOf(n++));
    p3.setPosition(350, 350);
    p3.setShape(new Rectangle2D.Double(-100, -100, 200, 200));

    LayoutObject p4 = new BaseLayoutObject(String.valueOf(n++));
    p4.setPosition(450, 350);
    p4.setShape(new Rectangle2D.Double(-75, -75, 150, 150));

    layout.addLayoutObject(p0);
    layout.addLayoutObject(p1);
    layout.addLayoutObject(p2);
    layout.addLayoutObject(p3);
    layout.addLayoutObject(p4);
  }

  /**
   * Initialize the given {@link Layout} with simple {@link LayoutObject}s
   * 
   * @param layout
   *          The {@link Layout}
   */
  public static void initTestData(Layout<LayoutObject> layout) {
    initTestData(layout, 30, 0.1, 0.9, 0.1, 0.9);
    initTestData(layout, 10, 0.3, 0.6, 0.6, 0.5);
  }

  /**
   * Initialize the given {@link Layout} with {@link LayoutObject}s
   * 
   * @param layout
   *          The {@link Layout}
   * @param numObjects
   *          The number of {@link LayoutObject}s to add
   * @param minX
   *          The minimum x-coordinate for the objects, inclusive
   * @param maxX
   *          The maximum x-coordinate for the objects, exclusive
   * @param minY
   *          The minimum y-coordinate for the objects, inclusive
   * @param maxY
   *          The maximum y-coordinate for the objects, exclusive
   */
  private static void initTestData(Layout<LayoutObject> layout, int numObjects, double minX,
      double maxX, double minY, double maxY) {
    double minSizeX = 0.03;
    double maxSizeX = 0.15;
    double minSizeY = 0.03;
    double maxSizeY = 0.15;
    Random random = new Random(0);
    for (int i = 0; i < numObjects; i++) {
      LayoutObject p = new BaseLayoutObject(String.valueOf(i));
      double x = random(random, minX, maxY);
      double y = random(random, minY, maxY);
      p.setPosition(x, y);
      double sizeX = random(random, minSizeX, maxSizeX);
      double sizeY = random(random, minSizeY, maxSizeY);
      p.setShape(new Rectangle2D.Double(-sizeX * 0.5, -sizeY * 0.5, sizeX, sizeY));
      layout.addLayoutObject(p);
    }
  }

  /**
   * Returns a random number from the given generator, in the specified range
   * 
   * @param random
   *          The random number generator
   * @param min
   *          The minimum value, inclusive
   * @param max
   *          The maximum value, inclusive
   * @return The value
   */
  private static double random(Random random, double min, double max) {
    return min + random.nextDouble() * (max - min);
  }

  /**
   * Private constructor to prevent instantiation
   */
  private Layouts() {
    // Private constructor to prevent instantiation
  }
}
