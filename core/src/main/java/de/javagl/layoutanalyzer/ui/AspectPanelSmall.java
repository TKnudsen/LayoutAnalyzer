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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.javagl.layoutanalyzer.Aspect;
import de.javagl.layoutanalyzer.quality.QualityData;
import de.javagl.layoutanalyzer.quality.QualityDataRecorder;
import de.javagl.layoutanalyzer.utils.Colors;

/**
 * A panel containing basic control- and monitoring components for one
 * {@link Aspect} and the {@link QualityData} that is computes.
 */
public class AspectPanelSmall extends JPanel {
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 8535490088178983572L;

	/**
	 * A counter to assign different colors to the {@link Aspect}s
	 */
	private static int aspectColorCounter = 0;

	/**
	 * The {@link Aspect} that this panel operates on
	 */
	private final Aspect aspect;

	/**
	 * Creates a new panel that allows controlling the given {@link Aspect}, and
	 * shows the contents of the given {@link QualityDataRecorder}
	 * 
	 * @param aspect
	 *            The {@link Aspect}
	 * @param qualityDataRecorder
	 *            The {@link QualityDataRecorder}
	 */
	public AspectPanelSmall(Aspect aspect) {
		super(new BorderLayout());
		this.aspect = aspect;

		TitledBorder titledBorder = new TitledBorder(aspect.getName());
		Color color = Colors.getColor(aspectColorCounter++);
		titledBorder.setTitleColor(color);
		setBorder(titledBorder);

		JPanel controlPanel = createControlPanel();
		add(controlPanel, BorderLayout.CENTER);
	}

	/**
	 * Create the control panel containing the GUI components for controlling
	 * the {@link Aspect}
	 * 
	 * @return The control panel
	 */
	private JPanel createControlPanel() {
		JPanel controlPanel = new JPanel(new GridLayout(1, 0));
		JSlider slider = new JSlider(0, 100, 0);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				double weight = slider.getValue() / 100.0;
				aspect.setWeight(weight);
			}
		});
		controlPanel.add(slider);
		return controlPanel;
	}
}