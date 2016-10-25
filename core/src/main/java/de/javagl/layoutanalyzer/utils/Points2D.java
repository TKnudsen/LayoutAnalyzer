package de.javagl.layoutanalyzer.utils;

import java.awt.geom.Point2D;

/**
 * Some additional operations for {@link Point2D}
 */
public final class Points2D {
  /**
   * Private constructor to prevent instantiation
   */
  private Points2D() {
    // Private constructor to prevent instantiation
  }

  /**
   * Calculates the average of a given collection of points component wise.
   * 
   * @param points
   *          the points to calculate the average form
   * @return the average point (aka. Center of Gravity)
   */
  public static Point2D average(Iterable<? extends Point2D.Double> points) {
    double x = 0;
    double y = 0;
    int count = 0;

    for (Point2D.Double point : points) {
      x += point.x;
      y += point.y;
      count++;
    }

    return new Point2D.Double(x / count, y / count);
  }

  /**
   * Component wise addition of two points
   * 
   * @param point1
   *          first point
   * @param point2
   *          second point
   * @return component wise sum
   */
  public static Point2D add(Point2D point1, Point2D point2) {
    return new Point2D.Double(point1.getX() + point2.getX(), point1.getY() + point2.getY());
  }

  /**
   * Component wise subtraction of two points
   * 
   * @param point1
   *          first point
   * @param point2
   *          second point
   * @return component wise difference
   */
  public static Point2D substract(Point2D point1, Point2D point2) {
    return new Point2D.Double(point1.getX() - point2.getX(), point1.getY() - point2.getY());
  }

}