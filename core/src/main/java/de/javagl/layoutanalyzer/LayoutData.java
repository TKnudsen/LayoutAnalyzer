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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.javagl.layoutanalyzer.layout.LayoutObject;

/**
 * The data that is computed by an {@link Aspect}, for a given set of
 * {@link LayoutObject}s, and which affects the layout process.<br>
 * <br>
 * Currently, this only consists of <i>forces</i> that may be applied to the
 * layout objects during the simulation in the {@link Layouter}.
 */
public class LayoutData {
	/**
	 * The list of {@link LayoutObject}s for which this data was computed
	 */
	private final List<LayoutObject> layoutObjects;

	/**
	 * The weight which was set for the {@link Aspect} when this data was
	 * computed.
	 * 
	 * @see Aspect#setWeight(double)
	 */
	private final double weight;

	/**
	 * The mapping from {@link LayoutObject}s to forces.
	 */
	private final Map<LayoutObject, Point2D> forces;

	/**
	 * Default constructor. A reference to the given list will be stored. It
	 * should thus be an unmodifiable list, and should not be changed after it
	 * has been passed to this constructor.
	 * 
	 * @param layoutObjects
	 *            The {@link LayoutObject}s
	 * @param weight
	 *            The weight that was set in the {@link Aspect}
	 */
	public LayoutData(List<LayoutObject> layoutObjects, double weight) {
		Objects.requireNonNull(layoutObjects, "The layoutObjects are null");
		this.layoutObjects = layoutObjects;
		this.weight = weight;
		this.forces = new LinkedHashMap<LayoutObject, Point2D>();
	}

	/**
	 * Returns the weight that was set in the {@link Aspect} when this data was
	 * computed.
	 * 
	 * @return The weight
	 * @see Aspect#setWeight(double)
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Returns an unmodifiable list containing the {@link LayoutObject}s for
	 * which this data was computed.
	 * 
	 * @return The {@link LayoutObject}s
	 */
	public List<LayoutObject> getLayoutObjects() {
		return layoutObjects;
	}

	/**
	 * Set the force for the given {@link LayoutObject} to be a copy of the
	 * given force.
	 * 
	 * @param layoutObject
	 *            The {@link LayoutObject}
	 * @param force
	 *            The force.
	 */
	public void setForce(LayoutObject layoutObject, Point2D force) {
		Objects.requireNonNull(layoutObject, "The layoutObject is null");
		Objects.requireNonNull(force, "The force is null");
		forces.put(layoutObject, new Point2D.Double(force.getX(), force.getY()));
	}

	/**
	 * Returns a copy of the force that was set for the given
	 * {@link LayoutObject}. If no force was associated with the given object,
	 * then a new point (0,0) will be returned.
	 * 
	 * @param layoutObject
	 *            The {@link LayoutObject}
	 * @return The force
	 */
	public Point2D getForce(LayoutObject layoutObject) {
		Objects.requireNonNull(layoutObject, "The layoutObject is null");
		Point2D force = forces.get(layoutObject);
		if (force == null) {
			return new Point2D.Double();
		}
		return new Point2D.Double(force.getX(), force.getY());
	}
}