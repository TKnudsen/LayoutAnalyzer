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

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.javagl.geom.Points;

/**
 * Implementation of an {@link Aspect} that represents a pairwise repulsion
 * between {@link LayoutObject}s.
 */
public class PairwiseRepulsionForce extends AbstractAspect implements Aspect {
	/**
	 * An epsilon for "reasonable" distances between objects
	 */
	private static final double EPSILON = 1e-8;

	/**
	 * A point, used internally for difference computations
	 */
	private final Point2D difference = new Point2D.Double();

	/**
	 * A point, used internally for direction computations
	 */
	private final Point2D direction = new Point2D.Double();

	/**
	 * The desired repulsion distance between each pair of objects
	 */
	private double repulsionDistance = 300.0;

	/**
	 * Creates a new force that tries to keep the given distance between the
	 * {@link LayoutObject}s
	 * 
	 * @param repulsionDistance
	 *            The repulsion distance
	 */
	public PairwiseRepulsionForce(double repulsionDistance) {
		super("PairwiseRepulsionForce");
		setRepulsionDistance(repulsionDistance);
	}

	/**
	 * Set the distance that this class should try to keep between the
	 * {@link LayoutObject}s
	 * 
	 * @param repulsionDistance
	 *            The repulsion distance
	 */
	public void setRepulsionDistance(double repulsionDistance) {
		this.repulsionDistance = repulsionDistance;
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
		Point2D position0 = layoutObject0.getPosition();
		Point2D position1 = layoutObject1.getPosition();
		double distance = position0.distance(position1);
		if (distance < repulsionDistance) {
			if (distance < EPSILON) {
				position1.setLocation(position0.getX() + 1, position0.getY());
				difference.setLocation(1.0, 0.0);
				direction.setLocation(1.0, 0.0);
				distance = 1.0;
			} else {
				Points.sub(position1, position0, difference);
				Points.scale(difference, 1.0 / distance, direction);
			}

			Point2D force0 = layoutData.getForce(layoutObject0);
			Point2D force1 = layoutData.getForce(layoutObject1);
			double factor = (repulsionDistance - distance) * 0.5;
			Points.addScaled(force0, -factor, direction, force0);
			Points.addScaled(force1, factor, direction, force1);
			layoutData.setForce(layoutObject0, force0);
			layoutData.setForce(layoutObject1, force1);
		}
	}

}
