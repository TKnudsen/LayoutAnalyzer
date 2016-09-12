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

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * An object that takes part in a layout
 */
public class LayoutObject {
	/**
	 * The label of this object
	 */
	private final String label;

	/**
	 * The position
	 */
	private final Point2D position;

	/**
	 * The velocity
	 */
	private final Point2D velocity;

	/**
	 * The acceleration
	 */
	private final Point2D acceleration;

	/**
	 * The force
	 */
	private final Point2D force;

	/**
	 * The mass
	 */
	private double mass;

	/**
	 * The shape
	 */
	private Shape shape;

	/**
	 * Creates a new object with the given label
	 * 
	 * @param label
	 *            The label
	 */
	public LayoutObject(String label) {
		this.label = label;
		this.position = new Point2D.Double();
		this.velocity = new Point2D.Double();
		this.acceleration = new Point2D.Double();
		this.force = new Point2D.Double();
		this.mass = 1.0;
	}

	@Override
	public String toString() {
		String ret = label;
		if (position != null) {
			ret += (", X= " + position.getX());
			ret += (", Y= " + position.getY());
		}
		if (getShape() != null) {
			ret += (", width= " + getShape().getBounds2D().getWidth());
			ret += (", height= " + getShape().getBounds2D().getHeight());
		}
		return ret;
	}

	/**
	 * Set the position of this object to be the same as the given position
	 *
	 * @param position
	 *            The new position
	 */
	protected void setPosition(Point2D position) {
		this.position.setLocation(position);
	}

	/**
	 * Set the position of this object
	 *
	 * @param x
	 *            The x-component of the position
	 * @param y
	 *            The y-component of the position
	 */
	public void setPosition(double x, double y) {
		this.position.setLocation(x, y);
	}

	/**
	 * Returns a copy of the position of this object
	 *
	 * @return The position
	 */
	public Point2D getPosition() {
		return new Point2D.Double(position.getX(), position.getY());
	}

	/**
	 * Returns the x-component of the position of this object
	 *
	 * @return The x-component of the position
	 */
	public double getPositionX() {
		return position.getX();
	}

	/**
	 * Returns the y-component of the position of this object
	 *
	 * @return The y-component of the position
	 */
	public double getPositionY() {
		return position.getY();
	}

	/**
	 * Set the velocity of this object to be the same as the given velocity
	 *
	 * @param velocity
	 *            The new velocity
	 */
	void setVelocity(Point2D velocity) {
		this.velocity.setLocation(velocity);
	}

	/**
	 * Set the velocity of this object
	 *
	 * @param x
	 *            The x-component of the velocity
	 * @param y
	 *            The y-component of the velocity
	 */
	void setVelocity(double x, double y) {
		this.velocity.setLocation(x, y);
	}

	/**
	 * Returns a copy of the velocity of this object
	 *
	 * @return The velocity
	 */
	Point2D getVelocity() {
		return new Point2D.Double(velocity.getX(), velocity.getY());
	}

	/**
	 * Returns the x-component of the velocity of this object
	 *
	 * @return The x-component of the velocity
	 */
	double getVelocityX() {
		return velocity.getX();
	}

	/**
	 * Returns the y-component of the velocity of this object
	 *
	 * @return The y-component of the velocity
	 */
	double getVelocityY() {
		return velocity.getY();
	}

	/**
	 * Set the acceleration of this object to be the same as the given
	 * acceleration
	 *
	 * @param acceleration
	 *            The new acceleration
	 */
	void setAcceleration(Point2D acceleration) {
		this.acceleration.setLocation(acceleration);
	}

	/**
	 * Set the acceleration of this object
	 *
	 * @param x
	 *            The x-component of the acceleration
	 * @param y
	 *            The y-component of the acceleration
	 */
	void setAcceleration(double x, double y) {
		this.acceleration.setLocation(x, y);
	}

	/**
	 * Returns a copy of the acceleration of this object
	 *
	 * @return The acceleration
	 */
	Point2D getAcceleration() {
		return new Point2D.Double(acceleration.getX(), acceleration.getY());
	}

	/**
	 * Returns the x-component of the acceleration of this object
	 *
	 * @return The x-component of the acceleration
	 */
	double getAccelerationX() {
		return acceleration.getX();
	}

	/**
	 * Returns the y-component of the acceleration of this object
	 *
	 * @return The y-component of the acceleration
	 */
	double getAccelerationY() {
		return acceleration.getY();
	}

	/**
	 * Set the force of this object to be the same as the given force
	 *
	 * @param force
	 *            The new force
	 */
	void setForce(Point2D force) {
		this.force.setLocation(force);
	}

	/**
	 * Set the force of this object
	 *
	 * @param x
	 *            The x-component of the force
	 * @param y
	 *            The y-component of the force
	 */
	void setForce(double x, double y) {
		this.force.setLocation(x, y);
	}

	/**
	 * Returns a copy of the force of this object
	 *
	 * @return The force
	 */
	Point2D getForce() {
		return new Point2D.Double(force.getX(), force.getY());
	}

	/**
	 * Returns the x-component of the force of this object
	 *
	 * @return The x-component of the force
	 */
	double getForceX() {
		return force.getX();
	}

	/**
	 * Returns the y-component of the force of this object
	 *
	 * @return The y-component of the force
	 */
	double getForceY() {
		return force.getY();
	}

	/**
	 * Returns the mass of this object
	 * 
	 * @return The mass
	 */
	double getMass() {
		return mass;
	}

	/**
	 * Set the mass of this object
	 * 
	 * @param mass
	 *            The mass
	 */
	void setMass(double mass) {
		this.mass = mass;
	}

	/**
	 * Returns the shape of this object
	 * 
	 * @return The shape
	 */
	public Shape getShape() {
		return shape;
	}

	/**
	 * Set the shape of this object
	 * 
	 * @param shape
	 *            The shape.
	 */
	public void setShape(Shape shape) {
		if (shape == null)
			throw new IllegalArgumentException("Shape of LayoutObject was set null.");
		this.shape = shape;
	}

	/**
	 * Returns the bounds of the {@link #getShape() shape} of this object,
	 * translated based on the current {@link #getPosition() position}
	 * 
	 * @return The shape bounds
	 */
	Rectangle2D getShapeBounds() {
		AffineTransform at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
		Shape paintedShape0 = at.createTransformedShape(getShape());
		return paintedShape0.getBounds2D();
	}

	/**
	 * Returns the label of this object
	 * 
	 * @return The label
	 */
	public String getLabel() {
		return label;
	}
}
