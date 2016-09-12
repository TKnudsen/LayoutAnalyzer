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

import java.awt.geom.Point2D;
import java.util.List;

import de.javagl.layoutanalyzer.LayoutData;
import de.javagl.layoutanalyzer.LayoutObject;

/**
 * Methods to create {@link QualityData} data instances
 */
public class QualityDatas {
	/**
	 * Compute a {@link QualityData} from the given {@link LayoutData}, where
	 * the quality values directly correspond to the given minimum and maximum
	 * force lengths: When the length of the
	 * {@link LayoutData#getForce(LayoutObject) force} for a certain
	 * {@link LayoutObject} is smaller than or equal to the given minimum
	 * length, then the quality will be 1.0. When the length of the
	 * {@link LayoutData#getForce(LayoutObject) force} for a certain
	 * {@link LayoutObject} is greater than or equal to the given maximum
	 * length, then the quality will be 0.0.
	 * 
	 * @param layoutData
	 *            The {@link LayoutData}
	 * @param minForceLength
	 *            The minimum force length
	 * @param maxForceLength
	 *            The maximum force length
	 * @return The {@link QualityData}
	 */
	public static QualityData computeFromForceLengths(LayoutData layoutData, double minForceLength, double maxForceLength) {
		QualityData qualityData = new QualityData(layoutData.getLayoutObjects(), layoutData.getWeight());
		List<LayoutObject> layoutObjects = layoutData.getLayoutObjects();
		double invDelta = 1.0 / (maxForceLength - minForceLength);
		for (LayoutObject layoutObject : layoutObjects) {
			Point2D force = layoutData.getForce(layoutObject);
			double x = force.getX();
			double y = force.getY();
			double length = Math.sqrt(x * x + y * y);

			double alpha = (length - minForceLength) * invDelta;
			double quality = Math.max(0.0, Math.min(1.0, 1.0 - alpha));
			qualityData.setQuality(layoutObject, quality);
		}
		return qualityData;
	}

	/**
	 * Private constructor to prevent instantiation
	 */
	private QualityDatas() {
		// Private constructor to prevent instantiation
	}
}