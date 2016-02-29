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

import java.util.List;


/**
 * Abstract base implementation of an {@link Aspect}
 */
abstract class AbstractAspect implements Aspect
{
    /**
     * The name of this aspect. 
     */
    private final String name;
    
    /**
     * The weight of this aspect
     */
    private double weight;
    
    /**
     * Default constructor
     * 
     * @param name The name of this aspect
     */
    protected AbstractAspect(String name)
    {
        this.name = name;
        this.weight = 1.0;
    }

    @Override
    public final String getName()
    {
        return name;
    }

    @Override
    public final double getWeight()
    {
        return weight;
    }

    @Override
    public final void setWeight(double weight)
    {
        this.weight = Math.min(1.0, Math.max(0.0, weight));
    }
    
    @Override
    public QualityData computeQualityData(
        List<? extends LayoutObject> layoutObjects, LayoutData layoutDataHint)
    {
        LayoutData layoutData = layoutDataHint;
        if (layoutData == null)
        {
            layoutData = computeLayoutData(layoutObjects);
        }
        // XXX TODO Avoid these VERY magic constants ASAP !!!
        QualityData qualityData =
            QualityDatas.computeFromForceLengths(layoutData, 0.0,  500.0);
        return qualityData;
    }
    
}
