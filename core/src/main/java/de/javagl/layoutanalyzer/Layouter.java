/*
< * LayoutAnalyzer  
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import de.javagl.geom.Points;
import de.javagl.layoutanalyzer.aspects.Aspect;
import de.javagl.layoutanalyzer.objects.LayoutObject;

/**
 * The class computing the positions of the {@link LayoutObject}s in a {@link Layout}, using a
 * simple "physically based simulation".<br>
 * <br>
 * It summarizes several {@link Aspect}s, which provide the {@link AspectData} for the
 * {@link LayoutObject}s. This {@link AspectData} may, for example, contain <i>forces</i>, which
 * affect the acceleration, velocity and position of the {@link LayoutObject}s.<br>
 * <br>
 * The actual work is done in the {@link #performStep()} method: It computes the
 * {@link LayoutAspects}, which summarizes the {@link AspectData} of all {@link Aspect}s. The forces
 * that are stored in these {@link AspectData} objects are accumulated and used for the simulation
 * step.<br>
 * <br>
 * {@link LayouterListener} instances may be attached to a layouter, to be informed whenever a new
 * {@link LayoutAspects} has been computed and a new step has been performed.
 */
public class Layouter<T extends LayoutObject> {
  /**
   * The {@link Layout} summarizing the {@link LayoutObject}s on which this layouter operates
   */
  private final Layout<T> layout;

  /**
   * The {@link Aspect}s which provide the {@link AspectData} (containing the forces) that govern
   * the movement of the {@link LayoutObject}s during the simulation.
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
  private double timeStep;

  /**
   * The current state of the layout regarding forces computed by aspects
   */
  private LayoutAspects currentAspectForces;

  /**
   * The {@link LayouterListener}s that have been attached, and will be informed about the computed
   * {@link LayoutAspects} each time that a simulation step is performed in {@link #performStep()}
   */
  private final List<LayouterListener> layouterDataListeners;

  /**
   * Indicates when the Layout is stable or not
   */
  private boolean isStable;

  /**
   * The first step when the Layout entered a stabel mode
   */
  private int firstStableStep;

  /**
   * If length of velocity Vector is less or equal this value the Layout is considerd stabel
   * {@link #updateVelocities()}
   */
  private static final double EPSILON_STABLE = 1e-1;

  /**
   * List of {@link LayouterExtension} which are invoced after a layout step is performed in
   * {@link #performStep()}
   */
  private final List<LayouterExtension<T>> extensions;

  /**
   * Default constructor
   * 
   * @param layout
   *          The {@link Layout} on which this layouter operates
   */
  public Layouter(Layout<T> layout, List<Aspect> aspects) {
    Objects.requireNonNull(layout, "The layout is null");

    this.layout = layout;
    this.aspects = new ArrayList<Aspect>(aspects);
    this.layouterDataListeners = new CopyOnWriteArrayList<LayouterListener>();

    this.step = 0;
    this.totalTime = 0.0;
    this.timeStep = 0.5;

    currentAspectForces = new LayoutAspects();
    isStable = false;
    firstStableStep = -1;
    extensions = new CopyOnWriteArrayList<LayouterExtension<T>>();
  }

  /**
   * Default constructor
   * 
   * @param layout
   *          The {@link Layout} on which this layouter operates
   */
  public Layouter(Layout<T> layout) {
    this(layout, Collections.emptyList());
  }

  /**
   * Adds an extension to this Layouter, the order the extensions are called is not fix. It is
   * possible to add and remove extension by runtime.
   * 
   * @param extension
   *          the {@link LayouterExtension} which is invoced after {@link #performStep()}
   */
  public void addExtension(LayouterExtension<T> extension) {
    Objects.requireNonNull(extension, "The extension is null");
    extensions.add(extension);
  }
  
  /**
   * @param timeStep sets {@link #timeStep} default is 0.5
   */
  public void setTimeStep(double timeStep) {
    this.timeStep = timeStep;
  }
  
  /**
   * @return return {@link #timeStep}
   */
  public double getTimeStep() {
    return timeStep;
  }

  /**
   * @param extension
   *          removes given extension, possible during runtime
   */
  public void removeExtension(LayouterExtension<T> extension) {
    extensions.remove(extension);
  }

  /**
   * @return whenever or not the Layout {@link #isStable}
   */
  public boolean isStable() {
    return isStable;
  }

  /**
   * Add the given {@link LayouterListener} to be informed about each {@link LayoutAspects} that is
   * computed in {@link #performStep()}
   * 
   * @param layouterDataListener
   *          The listener to add
   */
  public void addLayouterListener(LayouterListener layouterDataListener) {
    Objects.requireNonNull(layouterDataListener, "The listener is null");
    layouterDataListeners.add(layouterDataListener);
  }

  /**
   * Remove the given {@link LayouterListener}
   * 
   * @param layouterDataListener
   *          The listener to remove
   * @see #addLayouterListener(LayouterListener)
   */
  public void removeLayouterListener(LayouterListener layouterDataListener) {
    layouterDataListeners.remove(layouterDataListener);
  }

  /**
   * Add the given {@link Aspect} to be consulted during the computation of the
   * {@link LayoutAspects} the next time that {@link #performStep()} is called. This aspect will be
   * used to compute the {@link AspectData} for the {@link LayoutObject}s, which contains the forces
   * that govern the motion of the {@link LayoutObject}s during the simulation.
   * 
   * @param aspect
   *          The {@link Aspect} to add
   */
  public void addAspect(Aspect aspect) {
    Objects.requireNonNull(aspect, "The aspect is null");
    aspects.add(aspect);
  }

  /**
   * Remove the given {@link Aspect}
   * 
   * @param aspect
   *          The {@link Aspect} to remove
   * @see #addAspect(Aspect)
   */
  public void removeAspect(Aspect aspect) {
    aspects.remove(aspect);
  }

  /**
   * Perform a single time step. This will compute the {@link LayoutAspects} containing the
   * {@link AspectData}, which in turn contains the forces that will be applied to the
   * {@link LayoutObject}s. These forces will affect the accelerations, velocities and positions of
   * the {@link LayoutObject} through a simple time integration.
   */
  public void performStep(boolean notify) {
    LayoutAspects layouterData = computeLayouterData();

    applyForces(layouterData);
    updateAccelerations();
    updateVelocities();
    updatePositions();
    updateStable();

    totalTime += timeStep;
    step++;

    synchronized (currentAspectForces) {
      currentAspectForces = layouterData;
    }

    extensions.stream().filter(e -> e.isEnabled()).forEach(e -> e.process(layout));

    if (notify) {
      notifyLayouterDataComputed(layouterData);
    }
  }

  /**
   * see {@link #performStep(boolean)} with notification on
   */
  public void performStep() {
    performStep(true);
  }

  /**
   * @return unmodifiable list of aspects currently in use for the layout
   */
  public List<Aspect> getAspects() {
    return Collections.unmodifiableList(aspects);
  }

  /**
   * @return the current forces in the layout by different aspects
   */
  public LayoutAspects getCurrentAspectForces() {
    return currentAspectForces;
  }

  /**
   * @return current state of the layout
   */
  public Layout<T> getLayout() {
    return layout;
  }

  /**
   * @return number of {@link #performStep()} since start (creation of Layouter)
   */
  public int getStep() {
    return step;
  }

  /**
   * when {@link #isStable()} is true this function returns the step when the layout turned stable
   * 
   * @return step when layout turned stable
   */
  public int getFirstStableStep() {
    return firstStableStep;
  }

  /**
   * Compute the {@link LayoutAspects} for the current time step. This will consult each
   * {@link Aspect} that has been added by calling {@link #addAspect(Aspect)} to compute the
   * {@link AspectData} and {@link QualityData} for the current set of {@link LayoutObject}s. The
   * forces in the {@link AspectData} will govern the motion of the {@link LayoutObject}s for the
   * next time step.
   * 
   * @return The {@link LayoutAspects}
   */
  private LayoutAspects computeLayouterData() {
    LayoutAspects layouterData = new LayoutAspects();
    for (Aspect aspect : aspects) {
      AspectData layoutData = aspect.computeLayoutData(layout.getLayoutObjects());
      layouterData.add(aspect, layoutData);
    }
    return layouterData;
  }

  /**
   * Notify all registered {@link LayouterListener}s that a new {@link LayoutAspects} was computed
   * and a step was performed.
   * 
   * @param layouterData
   *          The {@link LayoutAspects} that was computed
   */
  private void notifyLayouterDataComputed(LayoutAspects layouterData) {
    for (LayouterListener layouterDataListener : layouterDataListeners) {
      layouterDataListener.layouterDataComputed(layouterData);
    }
  }

  /**
   * For each {@link LayoutObject}, compute the sum of all forces that are assigned to the object
   * based on the {@link AspectData}s in the given {@link LayoutAspects}, and assign this
   * accumulated force to the object via {@link LayoutObject#setForce(Point2D)}
   * 
   * @param layouterData
   *          The {@link LayoutAspects}
   */
  private void applyForces(LayoutAspects layouterData) {
    for (LayoutObject layoutObject : layout.getLayoutObjects()) {
      Point2D totalForce = new Point2D.Double();
      for (Aspect aspect : layouterData.getAspects()) {
        AspectData layoutData = layouterData.getLayoutData(aspect);
        double aspectWeight = aspect.getWeight();
        Point2D contributedForce = layoutData.getForce(layoutObject);
        // TODO In Layouter from LayoutOptimizer here was a check if contributedForce is null for
        // the next line
        Points.addScaled(totalForce, aspectWeight, contributedForce, totalForce);
      }
      layoutObject.setForce(totalForce);
    }
  }

  /**
   * Update the {@link LayoutObject#setAcceleration(Point2D) acceleration} of all
   * {@link LayoutObject}s, based on their current {@link LayoutObject#getForce() force} and
   * {@link LayoutObject#getMass() mass}
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
   * Update the {@link LayoutObject#setVelocity(Point2D) velocity} of all {@link LayoutObject}s,
   * based on their current {@link LayoutObject#getAcceleration() acceleration} and the current time
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
   * Update {@link #isStable} and {@link #firstStableStep} and determines if Layout is in a stable
   * state by testing if a velocity exist which exceeds {@link #EPSILON_STABLE}
   */
  private void updateStable() {
    boolean stillMoving = false;
    for (LayoutObject obj : layout.getLayoutObjects()) {
      Point2D velocity = obj.getVelocity();
      double velocityMagnitude = velocity.distance(new Point2D.Double());

      if (velocityMagnitude > EPSILON_STABLE) {
        stillMoving = true;
      }
    }

    if (!stillMoving && isStable == false) {
      firstStableStep = step;
    }
    isStable = !stillMoving;
  }

  /**
   * Update the {@link LayoutObject#setPosition(Point2D) position} of all {@link LayoutObject}s,
   * based on their current {@link LayoutObject#getVelocity() velocity} and the current time step
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
