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
package de.javagl.layoutanalyzer.objects;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * An object that takes part in a layout
 */
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
