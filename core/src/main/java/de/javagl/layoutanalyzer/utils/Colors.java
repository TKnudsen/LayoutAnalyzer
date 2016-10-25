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
package de.javagl.layoutanalyzer.utils;

import java.awt.Color;

/**
 * Methods for obtaining a small, simple, predefined set of colors
 */
public class Colors {
  /**
   * The available colors
   */
  private static final Color COLORS[] = { Color.RED, Color.YELLOW.darker(), Color.GREEN.darker(),
      Color.CYAN.darker(), Color.BLUE, Color.MAGENTA };

  /**
   * Returns an unspecified color depending on the given number
   * 
   * @param n
   *          The number
   * @return The color
   */
  public static Color getColor(int n) {
    return COLORS[n % COLORS.length];
  }

  /**
   * Returns the {@link #getColor(int) color} with the given number, but with the given alpha value
   * (which will be clamped to be in [0,1])
   * 
   * @param n
   *          The number
   * @param a
   *          The alpha value
   * @return The color
   */
  public static Color getColorWithAlpha(int n, double a) {
    return getColorWithAlpha(getColor(n), a);
  }

  /**
   * Returns the given color, but with the given alpha value (which will be clamped to be in [0,1])
   * 
   * @param c
   *          The color
   * @param a
   *          The alpha value
   * @return The color
   */
  public static Color getColorWithAlpha(Color c, double a) {
    int ia = Math.min(255, Math.max(0, (int) (a * 255)));
    return new Color(c.getRed(), c.getGreen(), c.getBlue(), ia);
  }

  /**
   * Returns the {@link #getColor(int) color}, but brightened with the given factor
   * 
   * @param n
   *          The number
   * @param factor
   *          The brightening factor
   * @return The new color
   */
  public static Color brighten(int n, float factor) {
    return brighten(getColor(n), factor);
  }

  /**
   * Returns the given color, but brightened with the given factor
   * 
   * @param color
   *          The color
   * @param factor
   *          The brightening factor
   * @return The new color
   */
  public static Color brighten(Color color, float factor) {
    float red = (255 - color.getRed()) * factor + color.getRed();
    float green = (255 - color.getGreen()) * factor + color.getGreen();
    float blue = (255 - color.getBlue()) * factor + color.getBlue();
    return new Color((int) red, (int) green, (int) blue, color.getAlpha());
  }

  /**
   * Private constructor to prevent instantiation
   */
  private Colors() {
    // Private constructor to prevent instantiation
  }
}
