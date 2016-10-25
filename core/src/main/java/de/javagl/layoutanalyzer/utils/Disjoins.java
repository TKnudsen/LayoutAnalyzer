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

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Methods to compute disjoining movements between intervals and rectangles
 */
public class Disjoins {
	/**
	 * Computes the minimum movement that has to be added to the interval 0 so
	 * that the intervals are disjoint
	 * 
	 * @param min0
	 *            The minimum of interval 0
	 * @param max0
	 *            The maximum of interval 0
	 * @param min1
	 *            The minimum of interval 1
	 * @param max1
	 *            The maximum of interval 1
	 * @return The minimum disjoining movement
	 */
	public static double computeMinDisjoinMovement(double min0, double max0, double min1, double max1) {
		if (max0 < min1) {
			return 0;
		}
		if (min0 > max1) {
			return 0;
		}
		double dxL = min1 - max0;
		double dxR = max1 - min0;
		if (Math.abs(dxL) < Math.abs(dxR)) {
			return dxL;
		}
		return dxR;
	}

	/**
	 * Computes the minimum movement that has to be added to rectangle 0 so that
	 * the rectangles are disjoint. The result will be stored in the given
	 * point. If the given point is <code>null</code>, then a new point will be
	 * created and returned.
	 * 
	 * @param rectangle0
	 *            Rectangle 0
	 * @param rectangle1
	 *            Rectangle 1
	 * @param result
	 *            The point that will store the result
	 * @return The result
	 */
	public static Point2D computeMinDisjoinMovement(Rectangle2D rectangle0, Rectangle2D rectangle1, Point2D result) {
		return computeMinDisjoinMovement(rectangle0.getMinX(), rectangle0.getMaxX(), rectangle0.getMinY(), rectangle0.getMaxY(), rectangle1.getMinX(), rectangle1.getMaxX(), rectangle1.getMinY(), rectangle1.getMaxY(), result);
	}

	/**
	 * Computes the minimum movement that has to be added to rectangle 0 so that
	 * the rectangles are disjoint. The result will be stored in the given
	 * point. If the given point is <code>null</code>, then a new point will be
	 * created and returned.
	 * 
	 * @param minX0
	 *            The minimum x-coordinate of rectangle 0
	 * @param maxX0
	 *            The maximum x-coordinate of rectangle 0
	 * @param minY0
	 *            The minimum y-coordinate of rectangle 0
	 * @param maxY0
	 *            The maximum y-coordinate of rectangle 0
	 * @param minX1
	 *            The minimum x-coordinate of rectangle 1
	 * @param maxX1
	 *            The maximum x-coordinate of rectangle 1
	 * @param minY1
	 *            The minimum y-coordinate of rectangle 1
	 * @param maxY1
	 *            The maximum y-coordinate of rectangle 1
	 * @param result
	 *            The point that will store the result
	 * @return The result
	 */
	public static Point2D computeMinDisjoinMovement(double minX0, double maxX0, double minY0, double maxY0, double minX1, double maxX1, double minY1, double maxY1, Point2D result) {
		if (result == null) {
			result = new Point2D.Double();
		}
		double dx = computeMinDisjoinMovement(minX0, maxX0, minX1, maxX1);
		double dy = computeMinDisjoinMovement(minY0, maxY0, minY1, maxY1);
		if (Math.abs(dx) < Math.abs(dy)) {
			result.setLocation(dx, 0);
		} else {
			result.setLocation(0, dy);
		}
		return result;
	}

	/**
	 * Private constructor to prevent instantiation
	 */
	private Disjoins() {
		// Private constructor to prevent instantiation
	}

}
