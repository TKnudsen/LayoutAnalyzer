package de.javagl.layoutanalyzer.aspects;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.javagl.geom.Points;
import de.javagl.layoutanalyzer.AspectData;
import de.javagl.layoutanalyzer.objects.LayoutObject;

/**
 * @author Christian Ritter, J�rgen Bernard
 */
public class PairwiseDistanceBasedAttractionForce extends AbstractAspect {
  /**
   * A map containing all preferred pairwise distances of the {@link LayoutObject}s.
   */
  private Map<LayoutObject, Map<LayoutObject, Double>> pairwiseDistances;

  /**
   * An epsilon for "reasonable" distances between objects.
   */
  private static final double EPSILON = 1e-8;

  /**
   * The number of {@link LayoutObject}s.
   */
  private int n;

  /**
   * Creates a new force that tries to keep the given distance between the {@link LayoutObject}s.
   *
   * @param pairwiseDistances
   *          the pairwise repulsion distances
   */
  public PairwiseDistanceBasedAttractionForce(
      Map<LayoutObject, Map<LayoutObject, Double>> pairwiseDistances) {
    super("PairwiseSpecificDistanceAttractionForce");
    this.pairwiseDistances = pairwiseDistances;
    this.n = pairwiseDistances.size();
  }

  @Override
  public AspectData computeLayoutData(List<? extends LayoutObject> layoutObjects) {
    Objects.requireNonNull(layoutObjects, "The layoutObjects are null");
    AspectData layoutData = new AspectData(
        Collections.unmodifiableList(new ArrayList<LayoutObject>(layoutObjects)), getWeight());
    for (int i = 0; i < layoutObjects.size(); i++) {
      LayoutObject layoutObject0 = layoutObjects.get(i);
      for (int j = i + 1; j < layoutObjects.size(); j++) {
        computeForce(layoutData, layoutObject0, layoutObjects.get(j));
      }
    }
    return layoutData;
  }

  /**
   * Compute the force that is implied by this aspect, for the given {@link LayoutObject}s, and
   * store it in the given {@link AspectData}
   *
   * @param layoutData
   *          The {@link AspectData}
   * @param layoutObject0
   *          The first {@link LayoutObject}
   * @param layoutObject1
   *          The second {@link LayoutObject}
   */
  private void computeForce(AspectData layoutData, LayoutObject layoutObject0,
      LayoutObject layoutObject1) {
    Point2D position0 = layoutObject0.getPosition();
    Point2D position1 = layoutObject1.getPosition();
    double distance = position0.distance(position1);
    Double attractionDistance = pairwiseDistances.get(layoutObject0).get(layoutObject1);
    if (attractionDistance == null)
      return;

    // ignore distant objects. emphasize
    double weight = calculateWeight(attractionDistance);

    if (distance > attractionDistance) {
      if (distance > EPSILON) {
        if (weight > 0.0) {
          double d = distance - attractionDistance;

          // V1
          // d /= (n - 1.0);
          // d /= distance;

          // V2
          // double weight = calculateWeight(attractionDistance);
          d *= (n * 0.075);
          // d *= weight;

          // V3
          // d *= (1 / d);

          d *= weight;

          Point2D dir = Points.sub(position1, position0, null);
          Point2D force0 = layoutData.getForce(layoutObject0);
          Point2D force1 = layoutData.getForce(layoutObject1);
          Points.addScaled(force0, d, dir, force0);
          Points.addScaled(force1, -d, dir, force1);
          layoutData.setForce(layoutObject0, force0);
          layoutData.setForce(layoutObject1, force1);
        }
      }
    }
  }

  /**
   * weighting emphasizes objects close to each other. distant objects should have less attraction.
   * 
   * @param attractionDistance
   * @return
   */
  private double calculateWeight(double attractionDistance) {
    double weight = attractionDistance;

    weight = Math.min(1.0, weight);
    weight = Math.max(0.0, weight);
    weight = 1 - weight;
    weight = Math.pow(weight, 2.0);

    return weight;
  }
}
