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

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleFunction;

/**
 * Implementation of a <code>DoubleFunction</code> that internally maintains
 * a queue of <code>Double</code> values, and offers access to the elements
 * of this queue via the {@link #apply(double)} method. The argument that is
 * passed to this method is assumed to be in [0,1), and will be mapped to
 * the index of the respective queue element. If the resulting index is out
 * of bounds, <code>null</code> will be returned.
 */
final class QueueDoubleFunction implements DoubleFunction<Double>
{
    /**
     * The size of the queue
     */
    private final int size;
    
    /**
     * The values of the queue
     */
    private final List<Double> values;
    
    /**
     * Creates a new function with the given queue size
     * 
     * @param size The size of the queue
     */
    QueueDoubleFunction(int size)
    {
        this.size = size;
        this.values = new ArrayList<Double>();
    }
    
    /**
     * Add the given value to the internal queue, maintaining the maximum
     * size of the queue
     * 
     * @param value The value to add
     */
    void add(double value)
    {
        values.add(value);
        while (values.size() > size)
        {
            values.remove(0);
        }
    }
    
    /**
     * Removes all elements from the internal queue
     */
    void clear()
    {
        values.clear();
    }

    @Override
    public Double apply(double value)
    {
        int index = (int)(value * size);
        if (index < 0 || index >= values.size())
        {
            return null;
        }
        return values.get(index);
    }
}