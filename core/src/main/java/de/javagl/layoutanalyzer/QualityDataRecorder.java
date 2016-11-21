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

import java.util.Objects;
import java.util.function.DoubleFunction;

import de.javagl.layoutanalyzer.utils.QueueDoubleFunction;

/**
 * A helper class summarizing the minimum- maximum and average quality values
 * from the {@link QualityData} objects that it receives, and offers them as
 * functions that map the range [0,1] to a fixed set of recent quality values.
 */
public class QualityDataRecorder {
	/**
	 * The {@link QueueDoubleFunction} storing the minimum qualities
	 */
	private final QueueDoubleFunction minQualityFunction;

	/**
	 * The {@link QueueDoubleFunction} storing the maximum qualities
	 */
	private final QueueDoubleFunction maxQualityFunction;

	/**
	 * The {@link QueueDoubleFunction} storing the average qualities
	 */
	private final QueueDoubleFunction avgQualityFunction;

	/**
	 * Creates a new instance that stores the specified number of quality
	 * values, and offers them as functions.
	 * 
	 * @param queueSize
	 *            The queue size
	 */
	public QualityDataRecorder(int queueSize) {
		minQualityFunction = new QueueDoubleFunction(queueSize);
		maxQualityFunction = new QueueDoubleFunction(queueSize);
		avgQualityFunction = new QueueDoubleFunction(queueSize);
	}

	/**
	 * Record the minimum, maximum and average quality value from the given
	 * {@link QualityData}
	 * 
	 * @param qualityData
	 *            The {@link QualityData}
	 */
	public void record(QualityData qualityData) {
		Objects.requireNonNull(qualityData, "The qualityData is null");
		minQualityFunction.add(qualityData.getMin());
		maxQualityFunction.add(qualityData.getMax());
		avgQualityFunction.add(qualityData.getAverage());
	}

	/**
	 * Returns the function that maps the range [0,1] to the most recent minimum
	 * quality values that have been recorded. For values outside of this range,
	 * the function will return <code>null</code>
	 * 
	 * @return The function
	 */
	public DoubleFunction<Double> getMinQualityFunction() {
		return minQualityFunction;
	}

	/**
	 * Returns the function that maps the range [0,1] to the most recent maximum
	 * quality values that have been recorded. For values outside of this range,
	 * the function will return <code>null</code>
	 * 
	 * @return The function
	 */
	public DoubleFunction<Double> getMaxQualityFunction() {
		return maxQualityFunction;
	}

	/**
	 * Returns the function that maps the range [0,1] to the most recent average
	 * quality values that have been recorded. For values outside of this range,
	 * the function will return <code>null</code>
	 * 
	 * @return The function
	 */
	public DoubleFunction<Double> getAvgQualityFunction() {
		return avgQualityFunction;
	}

}
