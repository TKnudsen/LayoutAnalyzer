package de.javagl.layoutanalyzer;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.javagl.geom.Points;

/**
 * @author Christian Ritter, Jürgen Bernard
 */
public class PairwiseDistanceBasedRepulsionForce extends AbstractAspect {

	/**
	 * A map containing all preferred pairwise distances of the
	 * {@link LayoutObject}s.
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
	 * Creates a new force that tries to keep the given distance between the
	 * {@link LayoutObject}s.
	 *
	 * @param pairwiseDistances
	 *            the pairwise repulsion distances
	 */
	public PairwiseDistanceBasedRepulsionForce(Map<LayoutObject, Map<LayoutObject, Double>> pairwiseDistances) {
		super("PairwiseSpecificDistanceRepulsionForce");
		this.pairwiseDistances = pairwiseDistances;
		this.n = pairwiseDistances.size();
	}

	@Override
	public LayoutData computeLayoutData(List<? extends LayoutObject> layoutObjects) {
		Objects.requireNonNull(layoutObjects, "The layoutObjects are null");
		LayoutData layoutData = new LayoutData(Collections.unmodifiableList(new ArrayList<LayoutObject>(layoutObjects)), getWeight());
		for (int i = 0; i < layoutObjects.size(); i++) {
			LayoutObject layoutObject0 = layoutObjects.get(i);
			for (int j = i + 1; j < layoutObjects.size(); j++) {
				computeForce(layoutData, layoutObject0, layoutObjects.get(j));
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
		Double repulsionDistance = pairwiseDistances.get(layoutObject0).get(layoutObject1);
		if (repulsionDistance == null)
			return;

		// V1
		// if (distance < repulsionDistance) {
		// if (distance > EPSILON) {
		// double d = repulsionDistance - distance;
		// d /= (n - 1.0);
		// d /= distance;
		//
		// Point2D dir = Points.sub(position1, position0, null);
		// Point2D force0 = layoutData.getForce(layoutObject0);
		// Point2D force1 = layoutData.getForce(layoutObject1);
		// Points.addScaled(force0, -d, dir, force0);
		// Points.addScaled(force1, d, dir, force1);
		// layoutData.setForce(layoutObject0, force0);
		// layoutData.setForce(layoutObject1, force1);
		// }
		// }

		// V2
		if (distance < repulsionDistance) {
			// if (distance > EPSILON) {
			double d = repulsionDistance - distance;
			double weightCurrentDistance = calculateWeight(distance);
			double weight = calculateWeight(1 - repulsionDistance);
			d *= Math.pow(weightCurrentDistance, 2);
			d *= Math.pow(weight, 2);
			d /= Math.pow(n, 0.66);

			Point2D dir = Points.sub(position1, position0, null);
			Point2D force0 = layoutData.getForce(layoutObject0);
			Point2D force1 = layoutData.getForce(layoutObject1);
			Points.addScaled(force0, -d, dir, force0);
			Points.addScaled(force1, d, dir, force1);
			layoutData.setForce(layoutObject0, force0);
			layoutData.setForce(layoutObject1, force1);
			// }
		}
	}

	/**
	 * weighting emphasizes objects close to each other. distant objects should
	 * have less attraction.
	 * 
	 * @param distance
	 * @return
	 */
	private double calculateWeight(double distance) {
		double weight = distance;

		weight = Math.min(1.0, weight);
		weight = Math.max(0.0, weight);

		weight = 1 - weight;
		weight = Math.pow(weight, 3);

		return weight;
	}
}
