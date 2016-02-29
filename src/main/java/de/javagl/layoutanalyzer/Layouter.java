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
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import de.javagl.geom.Points;

/**
 * The class computing the positions of the {@link LayoutObject}s in a
 * {@link Layout}, using a simple "physically based simulation".<br>
 * <br>
 * It summarizes several {@link Aspect}s, which provide the {@link LayoutData}
 * for the {@link LayoutObject}s. This {@link LayoutData} may, for example,
 * contain <i>forces</i>, which affect the acceleration, velocity and position
 * of the {@link LayoutObject}s.<br>
 * <br>
 * The actual work is done in the {@link #performStep()} method: It computes the
 * {@link LayouterData}, which summarizes the {@link LayoutData} of all
 * {@link Aspect}s. The forces that are stored in these {@link LayoutData}
 * objects are accumulated and used for the simulation step.<br>
 * <br>
 * {@link LayouterDataListener} instances may be attached to a layouter, to be
 * informed whenever a new {@link LayouterData} has been computed and a new step
 * has been performed.
 */
public class Layouter {
	/**
	 * The {@link Layout} summarizing the {@link LayoutObject}s on which this
	 * layouter operates
	 */
	private final Layout layout;

	/**
	 * The {@link Aspect}s which provide the {@link LayoutData} (containing the
	 * forces) that govern the movement of the {@link LayoutObject}s during the
	 * simulation.
	 */
	private final List<Aspect> aspects;

	/**
	 * A step counter
	 */
	private int step;

	/**
	 * The total simulation time
	 */
	private double totalTime;

	/**
	 * The size of a simulation time step
	 */
	private final double timeStep;

	/**
	 * The {@link LayouterDataListener}s that have been attached, and will be
	 * informed about the computed {@link LayouterData} each time that a
	 * simulation step is performed in {@link #performStep()}
	 */
	private final List<LayouterDataListener> layouterDataListeners;

	/**
	 * Default constructor
	 * 
	 * @param layout
	 *            The {@link Layout} on which this layouter operates
	 */
	public Layouter(Layout layout) {
		Objects.requireNonNull(layout, "The layout is null");

		this.layout = layout;
		this.aspects = new ArrayList<Aspect>();
		this.layouterDataListeners = new CopyOnWriteArrayList<LayouterDataListener>();

		this.step = 0;
		this.totalTime = 0.0;
		this.timeStep = 0.5;
	}

	/**
	 * Add the given {@link LayouterDataListener} to be informed about each
	 * {@link LayouterData} that is computed in {@link #performStep()}
	 * 
	 * @param layouterDataListener
	 *            The listener to add
	 */
	public void addLayouterDataListener(LayouterDataListener layouterDataListener) {
		Objects.requireNonNull(layouterDataListener, "The listener is null");
		layouterDataListeners.add(layouterDataListener);
	}

	/**
	 * Remove the given {@link LayouterDataListener}
	 * 
	 * @param layouterDataListener
	 *            The listener to remove
	 * @see #addLayouterDataListener(LayouterDataListener)
	 */
	void removeLayouterDataListener(LayouterDataListener layouterDataListener) {
		layouterDataListeners.remove(layouterDataListener);
	}

	/**
	 * Add the given {@link Aspect} to be consulted during the computation of
	 * the {@link LayouterData} the next time that {@link #performStep()} is
	 * called. This aspect will be used to compute the {@link LayoutData} for
	 * the {@link LayoutObject}s, which contains the forces that govern the
	 * motion of the {@link LayoutObject}s during the simulation.
	 * 
	 * @param aspect
	 *            The {@link Aspect} to add
	 */
	public void addAspect(Aspect aspect) {
		Objects.requireNonNull(aspect, "The aspect is null");
		aspects.add(aspect);
	}

	/**
	 * Remove the given {@link Aspect}
	 * 
	 * @param aspect
	 *            The {@link Aspect} to remove
	 * @see #addAspect(Aspect)
	 */
	void removeAspect(Aspect aspect) {
		aspects.remove(aspect);
	}

	/**
	 * Perform a single time step. This will compute the {@link LayouterData}
	 * containing the {@link LayoutData}, which in turn contains the forces that
	 * will be applied to the {@link LayoutObject}s. These forces will affect
	 * the accelerations, velocities and positions of the {@link LayoutObject}
	 * through a simple time integration.
	 */
	void performStep() {
		LayouterData layouterData = computeLayouterData();

		applyForces(layouterData);
		updateAccelerations();
		updateVelocities();
		updatePositions();
		totalTime += timeStep;
		step++;

		notifyLayouterDataComputed(layouterData);
	}

	/**
	 * Compute the {@link LayouterData} for the current time step. This will
	 * consult each {@link Aspect} that has been added by calling
	 * {@link #addAspect(Aspect)} to compute the {@link LayoutData} and
	 * {@link QualityData} for the current set of {@link LayoutObject}s. The
	 * forces in the {@link LayoutData} will govern the motion of the
	 * {@link LayoutObject}s for the next time step.
	 * 
	 * @return The {@link LayouterData}
	 */
	private LayouterData computeLayouterData() {
		LayouterData layouterData = new LayouterData();
		for (Aspect aspect : aspects) {
			LayoutData layoutData = aspect.computeLayoutData(layout.getLayoutObjects());
			QualityData qualityData = aspect.computeQualityData(layout.getLayoutObjects(), layoutData);
			layouterData.add(aspect, layoutData, qualityData);
		}
		return layouterData;
	}

	/**
	 * Notify all registered {@link LayouterDataListener}s that a new
	 * {@link LayouterData} was computed and a step was performed.
	 * 
	 * @param layouterData
	 *            The {@link LayouterData} that was computed
	 */
	private void notifyLayouterDataComputed(LayouterData layouterData) {
		for (LayouterDataListener layouterDataListener : layouterDataListeners) {
			layouterDataListener.layouterDataComputed(layouterData);
		}
	}

	/**
	 * For each {@link LayoutObject}, compute the sum of all forces that are
	 * assigned to the object based on the {@link LayoutData}s in the given
	 * {@link LayouterData}, and assign this accumulated force to the object via
	 * {@link LayoutObject#setForce(Point2D)}
	 * 
	 * @param layouterData
	 *            The {@link LayouterData}
	 */
	private void applyForces(LayouterData layouterData) {
		for (LayoutObject layoutObject : layout.getLayoutObjects()) {
			Point2D totalForce = new Point2D.Double();
			for (Aspect aspect : layouterData.getAspects()) {
				LayoutData layoutData = layouterData.getLayoutData(aspect);
				double aspectWeight = aspect.getWeight();
				Point2D contributedForce = layoutData.getForce(layoutObject);
				Points.addScaled(totalForce, aspectWeight, contributedForce, totalForce);
			}
			layoutObject.setForce(totalForce);
		}
	}

	/**
	 * Update the {@link LayoutObject#setAcceleration(Point2D) acceleration} of
	 * all {@link LayoutObject}s, based on their current
	 * {@link LayoutObject#getForce() force} and {@link LayoutObject#getMass()
	 * mass}
	 */
	private void updateAccelerations() {
		for (LayoutObject layoutObject : layout.getLayoutObjects()) {
			Point2D acceleration = new Point2D.Double();
			Point2D force = layoutObject.getForce();
			double mass = layoutObject.getMass();
			Points.scale(force, 1.0 / mass, acceleration);
			layoutObject.setAcceleration(acceleration);
			// System.out.println("Acc "+acceleration);
		}
	}

	/**
	 * Update the {@link LayoutObject#setVelocity(Point2D) velocity} of all
	 * {@link LayoutObject}s, based on their current
	 * {@link LayoutObject#getAcceleration() acceleration} and the current time
	 * step size.
	 */
	private void updateVelocities() {
		for (LayoutObject layoutObject : layout.getLayoutObjects()) {
			Point2D velocity = layoutObject.getVelocity();
			Point2D acceleration = layoutObject.getAcceleration();
			Points.scale(acceleration, timeStep, velocity);
			layoutObject.setVelocity(velocity);
			// System.out.println("Vel "+velocity);
		}
	}

	/**
	 * Update the {@link LayoutObject#setPosition(Point2D) position} of all
	 * {@link LayoutObject}s, based on their current
	 * {@link LayoutObject#getVelocity() velocity} and the current time step
	 * size.
	 */
	private void updatePositions() {
		for (LayoutObject layoutObject : layout.getLayoutObjects()) {
			Point2D position = layoutObject.getPosition();
			Point2D velocity = layoutObject.getVelocity();
			Points.addScaled(position, timeStep, velocity, position);
			layoutObject.setPosition(position);
			// System.out.println("Pos "+position);
		}
	}
}
