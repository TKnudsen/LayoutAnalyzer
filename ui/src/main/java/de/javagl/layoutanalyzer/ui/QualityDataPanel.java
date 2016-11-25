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

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import de.javagl.layoutanalyzer.QualityDataRecorder;
import de.javagl.layoutanalyzer.aspects.Aspect;
import de.javagl.layoutanalyzer.quality.QualityMeasure;
import de.javagl.layoutanalyzer.utils.Colors;

/**
 * A panel containing basic monitoring components for one {@link QualityMeasure}.
 */
public class QualityDataPanel extends JPanel {
  /**
   * Serial UID
   */
  private static final long serialVersionUID = -5259190132513158779L;
  /**
   * A counter to assign different colors to the {@link QualityMeasure}s
   */
  private static int aspectColorCounter = 0;

  /**
   * Creates a new panel that allows monitoring the given {@link QualityMeasure}, and shows the
   * contents of the given {@link QualityDataRecorder}
   * 
   * @param qualityMeasure
   *          The {@link Aspect}
   * @param qualityDataRecorder
   *          The {@link QualityDataRecorder}
   */
  public QualityDataPanel(QualityMeasure qualityMeasure, QualityDataRecorder qualityDataRecorder) {
    super(new BorderLayout());
    TitledBorder titledBorder = new TitledBorder(qualityMeasure.getName());
    Color color = Colors.getColor(aspectColorCounter++);
    titledBorder.setTitleColor(color);
    setBorder(titledBorder);

    QualityDataRecorderPanel qualityDataRecorderPanel = new QualityDataRecorderPanel(color,
        qualityDataRecorder);
    add(qualityDataRecorderPanel, BorderLayout.CENTER);
  }

}