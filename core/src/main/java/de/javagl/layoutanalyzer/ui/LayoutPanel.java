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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Objects;

import javax.swing.JPanel;

import de.javagl.geom.ArrowCreator;
import de.javagl.geom.Arrows;
import de.javagl.geom.Points;
import de.javagl.layoutanalyzer.Aspect;
import de.javagl.layoutanalyzer.Layout;
import de.javagl.layoutanalyzer.LayoutData;
import de.javagl.layoutanalyzer.LayoutObject;
import de.javagl.layoutanalyzer.LayouterData;

/**
 * A panel that shows a {@link Layout} consisting of {@link LayoutObject}s, and
 * optionally visualizes {@link LayoutData}
 */
public class LayoutPanel extends JPanel {
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 4678709294951136752L;

	/**
	 * The default font that will be used for the labels of the
	 * {@link LayoutObject}s
	 */
	private static final Font DEFAULT_FONT = new Font("Sans Serif", Font.PLAIN, 9);

	/**
	 * The {@link Layout} that is shown in this panel
	 * 
	 */
	private final Layout layout;

	/**
	 * The optional {@link LayouterData} that may be shown in this panel
	 */
	private LayouterData layouterData;

	/**
	 * Whether the forces of any given {@link LayoutObject} are painted.
	 */
	private boolean showForces = true;

	/**
	 * Whether the shapes of any given {@link LayoutObject} are painted
	 */
	private boolean showLayoutObjects = true;

	/**
	 * Default constructor
	 * 
	 * @param layout
	 *            The {@link Layout} that should be shown in this panel
	 */
	public LayoutPanel(Layout layout) {
		Objects.requireNonNull(layout, "The layout is null");
		this.layout = layout;
	}

	/**
	 * Set the {@link LayouterData} that should be shown in this panel. If this
	 * is <code>null</code>, then no {@link LayouterData} will be shown.
	 * 
	 * @param layouterData
	 *            The {@link LayouterData}
	 */
	public void setLayouterData(LayouterData layouterData) {
		this.layouterData = layouterData;
		repaint();
	}

	/**
	 * Returns an unmodifiable view on the {@link LayoutObject}s
	 * 
	 * @return The {@link LayoutObject}s
	 */
	public List<LayoutObject> getLayoutObjects() {
		return layout.getLayoutObjects();
	}

	/**
	 * Add the given {@link LayoutObject} to this layout
	 * 
	 * @param layoutObject
	 *            The {@link LayoutObject}
	 */
	public void addLayoutObject(LayoutObject layoutObject) {
		layout.addLayoutObject(layoutObject);
		repaint();
	}

	/**
	 * Remove the given {@link LayoutObject} from this layout
	 * 
	 * @param layoutObject
	 *            The {@link LayoutObject}
	 */
	public boolean removeLayoutObject(LayoutObject layoutObject) {
		layout.removeLayoutObject(layoutObject);
		repaint();
		return true;
	}

	@Override
	protected void paintComponent(Graphics gr) {
		super.paintComponent(gr);
		Graphics2D g = (Graphics2D) gr;
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (showLayoutObjects )
			paintLayoutObjects(g);
		if (showForces)
			paintLayouterData(g);
	}

	/**
	 * Paint the {@link LayoutObject}s of the {@link Layout} into the given
	 * graphics.
	 * 
	 * @param g
	 *            The graphics
	 */
	private void paintLayoutObjects(Graphics2D g) {
		g.setFont(DEFAULT_FONT);
		FontMetrics fontMetrics = g.getFontMetrics();

		List<LayoutObject> layoutObjects = layout.getLayoutObjects();
		for (LayoutObject layoutObject : layoutObjects) {
			AffineTransform scale = AffineTransform.getScaleInstance(getWidth(), getHeight());
			Point2D position = layoutObject.getPosition();
			scale.transform(position, position);

			Shape shape = layoutObject.getShape();
			AffineTransform at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
			at.scale(getWidth(), getHeight());
			Shape paintedShape = at.createTransformedShape(shape);
			g.setColor(new Color(32, 32, 32, 32));
			g.fill(paintedShape);
			g.setColor(Color.GRAY);
			g.draw(paintedShape);

			String label = layoutObject.getLabel();
			Rectangle2D stringBounds = fontMetrics.getStringBounds(label, g);
			int sx = (int) (position.getX() - stringBounds.getWidth() * 0.5);
			int sy = (int) (position.getY() + stringBounds.getHeight() * 0.5);
			g.drawString(label, sx, sy);
		}
	}

	/**
	 * Paint the current {@link LayouterData}, if it is not <code>null</code>,
	 * into the given graphics
	 * 
	 * @param g
	 *            The graphics
	 */
	private void paintLayouterData(Graphics2D g) {
		if (layouterData == null) {
			return;
		}
		ArrowCreator arrowCreator = Arrows.create().setAbsoluteHeadLength(15).setAbsoluteHeadWidth(10);

		List<LayoutObject> layoutObjects = layout.getLayoutObjects();
		for (LayoutObject layoutObject : layoutObjects) {
			AffineTransform scale = AffineTransform.getScaleInstance(getWidth(), getHeight());
			Point2D position = layoutObject.getPosition();
			scale.transform(position, position);
			
			int n = 0;
			for (Aspect aspect : layouterData.getAspects()) {
				LayoutData layoutData = layouterData.getLayoutData(aspect);

				g.setColor(Colors.getColorWithAlpha(n, 0.25));

				Point2D force = layoutData.getForce(layoutObject);
				scale.transform(force, force);
				Point2D tip = Points.add(position, force, null);
				Shape arrow = arrowCreator.buildShape(position, tip, 1.0);
				g.fill(arrow);

				g.setColor(Colors.getColor(n));
				double aspectWeight = layoutData.getWeight();
				Point2D tipScaled = Points.addScaled(position, aspectWeight, force, null);
				Shape arrowScaled = arrowCreator.buildShape(position, tipScaled, 3.0);
				g.fill(arrowScaled);

				n++;
			}
		}
	}

	public boolean isShowForces() {
		return showForces;
	}

	public void setShowForces(boolean showForces) {
		this.showForces = showForces;
	}
	
	public void setShowLayoutObjects(boolean showLayoutObjects)
	{
		this.showLayoutObjects = showLayoutObjects;
	}
	
	public boolean isShowLayoutObjects()
	{
		return showLayoutObjects;
	}
}
