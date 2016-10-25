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
import de.javagl.layoutanalyzer.LayoutData;
import de.javagl.layoutanalyzer.layout.LayoutObject;
import de.javagl.layoutanalyzer.utils.Disjoins;

/**
 * Implementation of an {@link Aspect} that tries to avoid pairwise overlaps
 * between the bounds of the shapes of {@link LayoutObject}s
 */
public class ShapeBoundsRepulsionForce extends AbstractAspect implements Aspect {
	/**
	 * A point, used for internal computations
	 */
	private final Point2D minDisjoinMovement = new Point2D.Double();

	/**
	 * Default constructor
	 */
	public ShapeBoundsRepulsionForce() {
		super("ShapeBoundsRepulsionForce");
	}

	@Override
	public LayoutData computeLayoutData(List<? extends LayoutObject> layoutObjects) {
		Objects.requireNonNull(layoutObjects, "The layoutObjects are null");
		LayoutData layoutData = new LayoutData(Collections.unmodifiableList(new ArrayList<LayoutObject>(layoutObjects)), getWeight());
		for (int i = 0; i < layoutObjects.size(); i++) {
			for (int j = i + 1; j < layoutObjects.size(); j++) {
				LayoutObject layoutObject0 = layoutObjects.get(i);
				LayoutObject layoutObject1 = layoutObjects.get(j);
				computeForce(layoutData, layoutObject0, layoutObject1);
			}
		}
		return layoutData;
	}

	/**
	 * Compute the force that is implied by this aspect, for the given
	 * {@link LayoutObject}s, and store it in the given {@link LayoutData}
	 * 
	 * @param layoutData
	 *            The {@link LayoutData}
	 * @param layoutObject0
	 *            The first {@link LayoutObject}
	 * @param layoutObject1
	 *            The second {@link LayoutObject}
	 */
	private void computeForce(LayoutData layoutData, LayoutObject layoutObject0, LayoutObject layoutObject1) {

		Rectangle2D bounds0 = layoutObject0.getShapeBounds();
		Rectangle2D bounds1 = layoutObject1.getShapeBounds();
		Disjoins.computeMinDisjoinMovement(bounds0, bounds1, minDisjoinMovement);

		Point2D force0 = layoutData.getForce(layoutObject0);
		Point2D force1 = layoutData.getForce(layoutObject1);
		Points.addScaled(force0, 0.5, minDisjoinMovement, force0);
		Points.addScaled(force1, -0.5, minDisjoinMovement, force1);
		layoutData.setForce(layoutObject0, force0);
		layoutData.setForce(layoutObject1, force1);
	}
}
