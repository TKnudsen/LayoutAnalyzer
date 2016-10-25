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
package de.javagl.layoutanalyzer.quality;

import java.util.DoubleSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.javagl.layoutanalyzer.Aspect;
import de.javagl.layoutanalyzer.layout.LayoutObject;

/**
 * A class that stores an abstract "quality" value for a set of
 * {@link LayoutObject}s, and allows obtaining simple statistical
 * data about these quality values. Instances of this class are
 * computed by an {@link Aspect}.<br>
 * <br>
 * The quality values in this class are generally assumed to be
 * <i>normalized</i>. This mainly means that the quality will be
 * 1.0 if the layout of an object is "perfect" regarding one
 * {@link Aspect}, and 0.0 if it is as bad as it can be. However,
 * no guarantees are made by this class itself. 
 */
public class QualityData
{
    /**
     * The list of {@link LayoutObject}s for which this data was computed
     */
    private final List<LayoutObject> layoutObjects;
    
    /**
     * The weight which was set for the {@link Aspect} when this data was 
     * computed. 
     * 
     * @see Aspect#setWeight(double)
     */
    private final double weight;
    
    /**
     * The mapping from {@link LayoutObject}s to quality values.
     */
    private final Map<LayoutObject, Double> qualities;
    
    /**
     * Simple statistics for the quality values, computed on demand
     */
    private DoubleSummaryStatistics statistics = null;
    
    /**
     * Default constructor. A reference to the given list will be stored.
     * It should thus be an unmodifiable list, and should not be changed
     * after it has been passed to this constructor.
     * 
     * @param layoutObjects The {@link LayoutObject}s 
     * @param weight The weight that was set in the {@link Aspect}
     */
    QualityData(List<LayoutObject> layoutObjects, double weight)
    {
        this.layoutObjects = layoutObjects;
        this.weight = weight;
        this.qualities = new LinkedHashMap<LayoutObject, Double>();
    }
    
    /**
     * Returns the weight that was set in the {@link Aspect} when this
     * data was computed.
     * 
     * @return The weight
     * @see Aspect#setWeight(double)
     */
    double getWeight()
    {
        return weight;
    }
    
    /**
     * Returns an unmodifiable list containing the {@link LayoutObject}s
     * for which this data was computed.
     * 
     * @return The {@link LayoutObject}s
     */
    List<LayoutObject> getLayoutObjects()
    {
        return layoutObjects;
    }
    
    /**
     * Set the quality for the given {@link LayoutObject}. This should
     * usually be a value in [0,1], with 1.0 meaning a "perfect" quality.
     * 
     * @param layoutObject The {@link LayoutObject}
     * @param quality The quality
     */
    void setQuality(LayoutObject layoutObject, double quality)
    {
        Objects.requireNonNull(layoutObject, "The layoutObject is null");
        qualities.put(layoutObject, quality);
    }
    
    /**
     * Returns the quality for the given {@link LayoutObject}. If no quality
     * value was associated with the given object, then <code>Double.NaN</code>
     * will be returned.
     * 
     * @param layoutObject The {@link LayoutObject}
     * @return The quality for the object
     */
    double getQuality(LayoutObject layoutObject)
    {
        Objects.requireNonNull(layoutObject, "The layoutObject is null");
        Double quality = qualities.get(layoutObject);
        if (quality == null)
        {
            return Double.NaN;
        }
        return quality;
    }
    
    /**
     * Compute the statistics over all quality values
     */
    private void computeStatistics()
    {
        statistics = new DoubleSummaryStatistics();
        for (Double d : qualities.values())
        {
            statistics.accept(d);
        }
    }
    
    /**
     * Returns the minimum quality value that any {@link LayoutObject} has
     * 
     * @return The quality value
     */
    double getMin()
    {
        if (statistics == null)
        {
            computeStatistics();
        }
        return statistics.getMin();
    }
    
    /**
     * Returns the maximum quality value that any {@link LayoutObject} has
     * 
     * @return The quality value
     */
    double getMax()
    {
        if (statistics == null)
        {
            computeStatistics();
        }
        return statistics.getMax();
    }
    
    /**
     * Returns the average quality value that all {@link LayoutObject}s have
     * 
     * @return The quality value
     */
    double getAverage()
    {
        if (statistics == null)
        {
            computeStatistics();
        }
        return statistics.getAverage();
    }
    
    
}
