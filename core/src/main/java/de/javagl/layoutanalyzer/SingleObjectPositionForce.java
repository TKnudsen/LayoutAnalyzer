package de.javagl.layoutanalyzer;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.javagl.geom.Points;

/**
 * @author Christian Ritter
 */
public class SingleObjectPositionForce extends AbstractAspect {

    /**
     * An epsilon for "reasonable" distances between objects.
     */
    private static final double EPSILON = 1e-8;

    /**
     * The only {@link LayoutObject} that is influenced by this force.
     */
    private LayoutObject singleObject;

    /**
     * The position this force is pointing to.
     */
    private Point2D targetPosition;

    public SingleObjectPositionForce(LayoutObject obj, Point2D position) {
        super("SingleObjectPositionForce");
        this.singleObject = obj;
        this.targetPosition = position;
    }

    @Override
    public LayoutData computeLayoutData(List<? extends LayoutObject> layoutObjects) {
        Objects.requireNonNull(layoutObjects, "The layoutObjects are null");
        LayoutData layoutData = new LayoutData(Collections.unmodifiableList(new ArrayList<LayoutObject>(layoutObjects)),
                getWeight());
        for (int i = 0; i < layoutObjects.size(); i++) {
            LayoutObject lo = layoutObjects.get(i);
            Point2D position = lo.getPosition();
            if (lo.equals(singleObject)) {
                double distanceToTarget = position.distance(targetPosition);
                if (distanceToTarget > EPSILON) {
                    Point2D dir = Points.sub(targetPosition, position, null);
                    Point2D force0 = layoutData.getForce(lo);
                    Points.add(force0, dir, force0);
                    layoutData.setForce(lo, force0);
                } else {
                    layoutData.setForce(lo, new Point2D.Double(0.0, 0.0));
                }
            } else {
                layoutData.setForce(lo, new Point2D.Double(0.0, 0.0));
            }
        }
        return layoutData;
    }
}
