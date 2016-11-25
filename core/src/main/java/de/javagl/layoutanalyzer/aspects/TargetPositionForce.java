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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.javagl.geom.Points;
import de.javagl.layoutanalyzer.AspectData;
import de.javagl.layoutanalyzer.objects.LayoutObject;

/**
 * Implementation of an {@link Aspect} that tries to keep the {@link LayoutObject}s at the positions
 * that they had at construction time.
 */
public class TargetPositionForce extends AbstractAspect implements Aspect {
  /**
   * An epsilon for distance computations
   */
  private static final double EPSILON = 1e-8;

  /**
   * The map storing the target positions of the {@link LayoutObject}s (that is, the positions that
   * they initially had)
   */
  private Map<LayoutObject, Point2D> targetPositions;

  /**
   * Default constructor. This aspect will try to keep the {@link LayoutObject}s at the positions
   * that they had when they have been passed to this constructor.
   * 
   * @param layoutObjects
   *          The {@link LayoutObject}s
   */
  public TargetPositionForce(Iterable<? extends LayoutObject> layoutObjects) {
    super("TargetPositionForce");
    Objects.requireNonNull(layoutObjects, "The layoutObjects are null");
    this.targetPositions = new LinkedHashMap<LayoutObject, Point2D>();
    for (LayoutObject layoutObject : layoutObjects) {
      targetPositions.put(layoutObject, layoutObject.getPosition());
    }
  }

  @Override
  public AspectData computeLayoutData(List<? extends LayoutObject> layoutObjects) {
    Objects.requireNonNull(layoutObjects, "The layoutObjects are null");
    AspectData layoutData = new AspectData(
        Collections.unmodifiableList(new ArrayList<LayoutObject>(layoutObjects)), getWeight());
    Point2D difference = new Point2D.Double();
    Point2D direction = new Point2D.Double();
    for (LayoutObject layoutObject : layoutObjects) {
      if (!targetPositions.containsKey(layoutObject)) {
        // special case relevant when layout is changed and no direct corespondence to target
        // position exists anymore
        layoutData.setForce(layoutObject, new Point2D.Double());
        continue;
      }

      Point2D position0 = layoutObject.getPosition();
      Point2D position1 = targetPositions.get(layoutObject);
      double distance = position0.distance(position1);

      if (distance > EPSILON) {
        Points.sub(position1, position0, difference);
        Points.scale(difference, 1.0 / distance, direction);
        Point2D force0 = layoutData.getForce(layoutObject);
        Points.addScaled(force0, distance, direction, force0);
        layoutData.setForce(layoutObject, force0);
      }
    }
    return layoutData;
  }

  public void setLayoutObjects(Iterable<? extends LayoutObject> layoutObjects) {
    Objects.requireNonNull(layoutObjects, "The layoutObjects are null");
    this.targetPositions = new LinkedHashMap<LayoutObject, Point2D>();
    for (LayoutObject layoutObject : layoutObjects) {
      targetPositions.put(layoutObject, layoutObject.getPosition());
    }
  }

  public void setLayoutObjectPosition(LayoutObject layoutObject, Point2D point2d) {
    targetPositions.put(layoutObject, point2d);
  }
}
