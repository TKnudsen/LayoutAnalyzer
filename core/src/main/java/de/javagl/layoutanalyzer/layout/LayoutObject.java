package de.javagl.layoutanalyzer.layout;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public interface LayoutObject {

  /**
   * Returns the label of this object
   * 
   * @return The label
   */
  public String getLabel();

  /**
   * Set the label of this object
   *
   * @param label
   *          The new label
   */
  public void setLabel(String label);

  /**
   * Set the position of this object to be the same as the given position
   *
   * @param position
   *          The new position
   */
  public void setPosition(Point2D position);

  /**
   * Set the position of this object
   *
   * @param x
   *          The x-component of the position
   * @param y
   *          The y-component of the position
   */
  public void setPosition(double x, double y);

  /**
   * Returns a copy of the position of this object
   *
   * @return The position
   */
  public Point2D getPosition();

  /**
   * Returns the 2Dim-distance of this object to the other object
   * 
   * @param other
   *          The other object
   * @return The 2Dim-distance
   */
  public double distanceTo(LayoutObject other);

  /**
   * Set the velocity of this object to be the same as the given velocity
   *
   * @param velocity
   *          The new velocity
   */
  public void setVelocity(Point2D velocity);

  /**
   * Set the velocity of this object
   *
   * @param x
   *          The x-component of the velocity
   * @param y
   *          The y-component of the velocity
   */
  public void setVelocity(double x, double y);

  /**
   * Returns a copy of the velocity of this object
   *
   * @return The velocity
   */
  public Point2D getVelocity();

  /**
   * Set the acceleration of this object to be the same as the given acceleration
   *
   * @param acceleration
   *          The new acceleration
   */
  public void setAcceleration(Point2D acceleration);

  /**
   * Set the acceleration of this object
   *
   * @param x
   *          The x-component of the acceleration
   * @param y
   *          The y-component of the acceleration
   */
  public void setAcceleration(double x, double y);

  /**
   * Returns a copy of the acceleration of this object
   *
   * @return The acceleration
   */
  public Point2D getAcceleration();

  /**
   * Set the force of this object to be the same as the given force
   *
   * @param force
   *          The new force
   */
  public void setForce(Point2D force);

  /**
   * Set the force of this object
   *
   * @param x
   *          The x-component of the force
   * @param y
   *          The y-component of the force
   */
  public void setForce(double x, double y);

  /**
   * Returns a copy of the force of this object
   *
   * @return The force
   */
  public Point2D getForce();

  /**
   * Returns the mass of this object
   * 
   * @return The mass
   */
  public double getMass();

  /**
   * Set the mass of this object
   * 
   * @param mass
   *          The mass
   */
  public void setMass(double mass);

  /**
   * Returns the shape of this object
   * 
   * @return The shape
   */
  public Shape getShape();

  /**
   * Set the shape of this object
   * 
   * @param shape
   *          The shape.
   */
  public void setShape(Shape shape);

  /**
   * Returns the bounds of the {@link #getShape() shape} of this object, translated based on the
   * current {@link #getPosition() position}
   * 
   * @return The shape bounds
   */
  public default Rectangle2D getShapeBounds() {
    Point2D position = getPosition();
    AffineTransform at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
    Shape paintedShape0 = at.createTransformedShape(getShape());
    return paintedShape0.getBounds2D();
  }
}
