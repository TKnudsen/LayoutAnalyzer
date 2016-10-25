/*
 * LayoutAnalyzer  
 *
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
package de.javagl.layoutanalyzer.layout;

import java.awt.Shape;
import java.awt.geom.Point2D;

/**
 * An object that takes part in a layout
 */
public class BaseLayoutObject implements LayoutObject {

	private String label;
	private final Point2D position = new Point2D.Double();
	private final Point2D velocity = new Point2D.Double();
	private final Point2D acceleration = new Point2D.Double();
	private final Point2D force = new Point2D.Double();
	private double mass = 1.0;
	private Shape shape;

	/**
	 * Creates a new object with the given label
	 */
	public BaseLayoutObject() {
		this.label = "";
	}

	/**
	 * Creates a new object with the given label
	 * 
	 * @param label
	 *            The label
	 */
	public BaseLayoutObject(String label) {
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public void setPosition(Point2D position) {
		this.position.setLocation(position);
	}

	public void setPosition(double x, double y) {
		this.position.setLocation(x, y);
	}

	@Override
	public Point2D getPosition() {
		return new Point2D.Double(position.getX(), position.getY());
	}

	@Override
	public double distanceTo(LayoutObject other) {
		return getPosition().distance(other.getPosition());
	}

	@Override
	public void setVelocity(Point2D velocity) {
		this.velocity.setLocation(velocity);
	}

	@Override
	public void setVelocity(double x, double y) {
		this.velocity.setLocation(x, y);
	}

	@Override
	public Point2D getVelocity() {
		return new Point2D.Double(velocity.getX(), velocity.getY());
	}

	@Override
	public void setAcceleration(Point2D acceleration) {
		this.acceleration.setLocation(acceleration);
	}

	@Override
	public void setAcceleration(double x, double y) {
		this.acceleration.setLocation(x, y);
	}

	@Override
	public Point2D getAcceleration() {
		return new Point2D.Double(acceleration.getX(), acceleration.getY());
	}

	@Override
	public void setForce(Point2D force) {
		this.force.setLocation(force);
	}

	@Override
	public void setForce(double x, double y) {
		this.force.setLocation(x, y);
	}

	@Override
	public Point2D getForce() {
		return new Point2D.Double(force.getX(), force.getY());
	}

	@Override
	public double getMass() {
		return mass;
	}

	@Override
	public void setMass(double mass) {
		this.mass = mass;
	}

	@Override
	public Shape getShape() {
		return shape;
	}

	@Override
	public void setShape(Shape shape) {
		this.shape = shape;
	}
}
