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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.javagl.layoutanalyzer.aspects.Aspect;

/**
 * A class summarizing the data that is computed by a {@link Layouter} in a single
 * {@link Layouter#performStep() step}. It combines the {@link AspectData}, which contains the data
 * of the "physics" for the layouted objects, and the {@link QualityData}. Both of them are computed
 * by one {@link Aspect} that is part of the {@link Layouter}.
 */
public class LayoutAspects {
  /**
   * The map from each {@link Aspect} to the {@link AspectData} that it computed
   */
  private final Map<Aspect, AspectData> layoutDatas;

  /**
   * Default constructor
   */
  public LayoutAspects() {
    layoutDatas = new LinkedHashMap<Aspect, AspectData>();
  }

  /**
   * Add the given data to this instance
   * 
   * @param aspect
   *          The {@link Aspect} that computed the data elements
   * @param layoutData
   *          The {@link AspectData} that was computed by the {@link Aspect}
   */
  public void add(Aspect aspect, AspectData layoutData) {
    Objects.requireNonNull(aspect, "The aspect is null");
    Objects.requireNonNull(layoutData, "The layoutData is null");
    layoutDatas.put(aspect, layoutData);
  }

  /**
   * Returns the {@link AspectData} that was computed by the given {@link Aspect}, or
   * <code>null</code> if no such data exists.
   * 
   * @param aspect
   *          The {@link Aspect}
   * @return The {@link AspectData}
   */
  public AspectData getLayoutData(Aspect aspect) {
    return layoutDatas.get(aspect);
  }

  /**
   * Returns an unmodifiable set containing all {@link Aspect}s which have contributed to this data
   * set
   * 
   * @return The {@link Aspect}s
   */
  public Set<Aspect> getAspects() {
    return Collections.unmodifiableSet(layoutDatas.keySet());
  }
}
