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
package de.javagl.layoutanalyzer.aspects;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.javagl.geom.Points;
import de.javagl.layoutanalyzer.Aspect;
import de.javagl.layoutanalyzer.LayoutData;
import de.javagl.layoutanalyzer.layout.LayoutObject;

/**
 * Implementation of an {@link Aspect} that tries to avoid overlaps between the
 * bounds of the shapes of {@link LayoutObject}s and the border
 */
public class ShapeBoundsBorderRepulsionForce extends AbstractAspect implements Aspect {
	/**
	 * A point, used for internal movement computations
	 */
	private final Point2D movement = new Point2D.Double();

	/**
	 * The current border
	 */
	private final Rectangle2D border;

	/**
	 * Creates a new instance with the given border
	 * 
	 * @param border
	 *            The border
	 */
	public ShapeBoundsBorderRepulsionForce(Rectangle2D border) {
		super("ShapeBoundsBorderRepulsionForce");
		this.border = new Rectangle2D.Double();
		setBorder(border);
	}

	/**
	 * Set the border which should not be overlapped by the bounds of the shapes
	 * of the {@link LayoutObject}s
	 * 
	 * @param border
	 *            The border
	 */
	public void setBorder(Rectangle2D border) {
		Objects.requireNonNull(border, "The border is null");
		this.border.setRect(border);
	}

	@Override
	public LayoutData computeLayoutData(List<? extends LayoutObject> layoutObjects) {
		Objects.requireNonNull(layoutObjects, "The layoutObjects are null");
		LayoutData layoutData = new LayoutData(Collections.unmodifiableList(new ArrayList<LayoutObject>(layoutObjects)), getWeight());
		for (int i = 0; i < layoutObjects.size(); i++) {
			LayoutObject layoutObject = layoutObjects.get(i);
			computeForce(layoutData, layoutObject);
		}
		return layoutData;
	}

	/**
	 * Compute the force that is implied by the current position and shape of
	 * the given {@link LayoutObject}, and store it in the given
	 * {@link LayoutData}
	 * 
	 * @param layoutData
	 *            The {@link LayoutData}
	 * @param layoutObject
	 *            The {@link LayoutObject}
	 */
	private void computeForce(LayoutData layoutData, LayoutObject layoutObject) {
		Rectangle2D bounds = layoutObject.getShapeBounds();

		double dx = 0;
		double dy = 0;
		double minX = bounds.getMinX();
		double maxX = bounds.getMaxX();
		double minY = bounds.getMinY();
		double maxY = bounds.getMaxY();
		if (minX < border.getMinX()) {
			dx = border.getMinX() - minX;
		}
		if (maxX > border.getMaxX()) {
			dx = -(maxX - border.getMaxX());
		}
		if (minY < border.getMinY()) {
			dy = border.getMinY() - minY;
		}
		if (maxY > border.getMaxY()) {
			dy = -(maxY - border.getMaxY());
		}
		movement.setLocation(dx, dy);
		Point2D force = layoutData.getForce(layoutObject);
		// Points.add(force, movement, force);
		Points.addScaled(force, 0.66, movement, force);
		layoutData.setForce(layoutObject, force);
	}
}
