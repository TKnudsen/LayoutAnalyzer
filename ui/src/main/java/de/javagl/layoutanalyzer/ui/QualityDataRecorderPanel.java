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
package de.javagl.layoutanalyzer.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import de.javagl.layoutanalyzer.QualityDataRecorder;
import de.javagl.layoutanalyzer.utils.Colors;
import de.javagl.viewer.MouseControl;
import de.javagl.viewer.functions.FunctionPanel;

/**
 * A panel showing the contents of a {@link QualityDataRecorder} as a set of plotted functions
 */
public class QualityDataRecorderPanel extends JPanel {
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 4207630607915849318L;

	private final FunctionPanel functionPanel;

	/**
	 * Creates a new panel that plots the functions from the given {@link QualityDataRecorder} with the given base color
	 * 
	 * @param color
	 *            The color
	 * @param qualityDataRecorder
	 *            The {@link QualityDataRecorder}
	 */
	public QualityDataRecorderPanel(Color color, QualityDataRecorder qualityDataRecorder) {
		super(new GridLayout(1, 1));

		functionPanel = new FunctionPanel();
		class SimpleMouseControl extends MouseAdapter implements MouseControl {
			private double zoomingSpeed = 0.15;

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				double d = Math.pow(1 + zoomingSpeed, e.getWheelRotation()) - 1;
				double factorY = 1.0 + d;
				Point2D p = functionPanel.getWorldToScreen().transform(new Point2D.Double(0, 0), null);
				functionPanel.zoom(p.getX(), p.getY(), 1.0, factorY);
			}
		}
		functionPanel.setResizingContents(true);
		functionPanel.setMouseControl(new SimpleMouseControl());
		functionPanel.setDisplayedWorldArea(new Rectangle2D.Double(-0.2, -0.2, 1.2, 1.8));
		functionPanel.addFunctionWithValueLegend(qualityDataRecorder.getMinQualityFunction(), Colors.getColorWithAlpha(color, 0.25), "Min");
		functionPanel.addFunctionWithValueLegend(qualityDataRecorder.getMaxQualityFunction(), Colors.getColorWithAlpha(color, 0.25), "Max");
		functionPanel.addFunctionWithValueLegend(qualityDataRecorder.getAvgQualityFunction(), color, "Avg");
		add(functionPanel);
	}

	public void setDisplayedWorldArea(Rectangle2D area) {
		functionPanel.setDisplayedWorldArea(area);
	}

	public void setFunctionPanelBackground(Color c) {
		functionPanel.setBackground(c);
	}

}