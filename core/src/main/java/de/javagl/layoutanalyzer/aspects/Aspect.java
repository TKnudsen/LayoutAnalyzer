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

import java.util.List;

import de.javagl.layoutanalyzer.AspectListener;
import de.javagl.layoutanalyzer.AspectData;
import de.javagl.layoutanalyzer.objects.LayoutObject;

/**
 * Interface for one "aspect" of a layout. It offers methods to compute the {@link AspectData} for a
 * list of {@link LayoutObject}s.
 */
public interface Aspect {
  /**
   * A short, human-readable name for this aspect, suitable to be displayed in a GUI component
   * 
   * @return The name of this aspect
   */
  String getName();

  /**
   * Returns the relative weight of this aspect, as a value in [0,1]
   * 
   * @return The weight of this aspect
   */
  double getWeight();

  /**
   * Set the relative weight of this aspect, as a value in [0,1]. Values outside of this range will
   * be clamped.
   * 
   * @param weight
   *          The weight of this aspect
   */
  void setWeight(double weight);

  /**
   * Compute the {@link AspectData} for the given {@link LayoutObject}s.<br>
   * <br>
   * The returned {@link AspectData} will contain a copy of the given list of objects, and map each
   * object to data elements that may be relevant for the layout (currently, this only is the force
   * that has to be applied to the object in order to "improve the layout")
   * 
   * @param layoutObjects
   *          The {@link LayoutObject}s
   * @return The {@link AspectData}
   */
  AspectData computeLayoutData(List<? extends LayoutObject> layoutObjects);

  /**
   * Add a Listener to this aspect
   * 
   * @param listener
   *          the listener
   */
  public void addListener(AspectListener listener);

  /**
   * removes a Listener to this aspect
   * 
   * @param listener
   *          the listener
   */
  public void removeListener(AspectListener listener);
}
