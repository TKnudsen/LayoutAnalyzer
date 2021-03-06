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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.border.Border;

import de.javagl.geom.ArrowCreator;
import de.javagl.geom.Arrows;
import de.javagl.geom.Points;
import de.javagl.layoutanalyzer.AspectData;
import de.javagl.layoutanalyzer.Layout;
import de.javagl.layoutanalyzer.LayoutAspects;
import de.javagl.layoutanalyzer.aspects.Aspect;
import de.javagl.layoutanalyzer.objects.LayoutObject;
import de.javagl.layoutanalyzer.painter.DefaultLayoutObjectPainter;
import de.javagl.layoutanalyzer.painter.LayoutObjectPainter;
import de.javagl.layoutanalyzer.utils.Colors;

/**
 * A panel that shows a {@link Layout} consisting of {@link LayoutObject}s, and
 * optionally visualizes {@link AspectData}
 */
public class LayoutPanel<T extends LayoutObject> extends JPanel {
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 4678709294951136752L;

	/**
	 * The default font that will be used for the labels of the
	 * {@link LayoutObject}s
	 */
	protected static final Font DEFAULT_FONT = new Font("Sans Serif", Font.PLAIN, 9);

	/**
	 * The {@link Layout} that is shown in this panel
	 * 
	 */
	protected final Layout<T> layout;

	/**
	 * The optional {@link LayoutAspects} that may be shown in this panel
	 */
	protected LayoutAspects layouterData;

	/**
	 * The {@link LayoutObjectPainter} used to draw the {@link LayoutObject}s in
	 * this panel. Defaults to {@link DefaultLayoutObjectPainter}
	 */
	protected LayoutObjectPainter<T> layoutObjectPainter;

	/**
	 * Whether the forces of any given {@link LayoutObject} are painted.
	 */
	private boolean showForces = true;

	/**
	 * Whether the shapes of any given {@link LayoutObject} are painted
	 */
	private boolean showLayoutObjects = true;

	/**
	 * Whether the {@link JPanel#getBorder()} of this panel is used to draw an
	 * outline around the painting area of {@link #paintLayoutObjects(Graphics2D)}
	 */
	private boolean showOutline = false;

	/**
	 * Default constructor
	 * 
	 * @param layout
	 *            The {@link Layout} that should be shown in this panel
	 * 
	 * @param layoutObjectPainter
	 *            The Painter responsible for painting the layout objects in the
	 *            panel
	 */
	public LayoutPanel(Layout<T> layout, LayoutObjectPainter<T> layoutObjectPainter) {
		super();
		Objects.requireNonNull(layout, "The layout is null");
		this.layout = layout;
		setLayoutObjectPainter(layoutObjectPainter);
		setBackground(Color.WHITE);
	}

	/**
	 * Default constructor
	 * 
	 * @param layout
	 *            The {@link Layout} that should be shown in this panel
	 */
	public LayoutPanel(Layout<T> layout) {
		this(layout, new DefaultLayoutObjectPainter<T>());
	}

	/**
	 * @param layoutObjectPainter
	 *            new {@link LayoutObjectPainter} used in subsequent
	 *            {@link #paint(Graphics)}
	 */
	public void setLayoutObjectPainter(LayoutObjectPainter<T> layoutObjectPainter) {
		Objects.requireNonNull(layoutObjectPainter, "Painter is null");
		this.layoutObjectPainter = layoutObjectPainter;
	}

	/**
	 * Set the {@link LayoutAspects} that should be shown in this panel. If this is
	 * <code>null</code>, then no {@link LayoutAspects} will be shown.
	 * 
	 * @param layouterData
	 *            The {@link LayoutAspects}
	 */
	public void setLayouterData(LayoutAspects layouterData) {
		this.layouterData = layouterData;
		repaint();
	}

	/**
	 * Returns an unmodifiable view on the {@link LayoutObject}s
	 * 
	 * @return The {@link LayoutObject}s
	 */
	public List<T> getLayoutObjects() {
		return layout.getLayoutObjects();
	}

	/**
	 * Add the given {@link LayoutObject} to this layout
	 * 
	 * @param layoutObject
	 *            The {@link LayoutObject}
	 */
	public void addLayoutObject(T layoutObject) {
		layout.addLayoutObject(layoutObject);
		repaint();
	}

	/**
	 * Remove the given {@link LayoutObject} from this layout
	 * 
	 * @param layoutObject
	 *            The {@link LayoutObject}
	 *
	 * @return returnes true on success
	 */
	public boolean removeLayoutObject(T layoutObject) {
		layout.removeLayoutObject(layoutObject);
		repaint();
		return true;
	}

	@Override
	protected void paintComponent(Graphics gr) {
		super.paintComponent(gr);
		Graphics2D g = (Graphics2D) gr;
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int paintingSquareSideLenght = Math.min(getWidth(), getHeight());
		if (showOutline) {
			g.setClip(0, 0, paintingSquareSideLenght, paintingSquareSideLenght);
			paintOutline(g);
		}

		if (showLayoutObjects)
			paintLayoutObjects(g);
		if (showForces)
			paintForces(g);

	}

	/**
	 * Paint the {@link LayoutObject}s of the {@link Layout} into the given
	 * graphics.
	 * 
	 * @param g
	 *            The graphics
	 */
	protected void paintLayoutObjects(Graphics2D g) {

		g.setFont(DEFAULT_FONT);
		double scaleFactor = Math.min(getWidth(), getHeight());
		final AffineTransform scale = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
		final AffineTransform transform = g.getTransform();
		List<T> layoutObjects = layout.getLayoutObjects();
		for (T layoutObject : layoutObjects) {
			Point2D position = layoutObject.getPosition();
			scale.transform(position, position);
			g.translate(position.getX(), position.getY());
			layoutObjectPainter.paint(layoutObject, g);
			g.setTransform(transform);
		}
	}

	/**
	 * Paint the current {@link LayoutAspects}, if it is not <code>null</code>, into
	 * the given graphics
	 * 
	 * @param g
	 *            The graphics
	 */
	protected void paintForces(Graphics2D g) {
		if (layouterData == null) {
			return;
		}
		ArrowCreator arrowCreator = Arrows.create().setAbsoluteHeadLength(15).setAbsoluteHeadWidth(10);

		double scaleFactor = Math.min(getWidth(), getHeight());
		final AffineTransform scale = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);

		List<T> layoutObjects = layout.getLayoutObjects();
		for (LayoutObject layoutObject : layoutObjects) {
			Point2D position = layoutObject.getPosition();
			scale.transform(position, position);
			int n = 0;
			for (Aspect aspect : layouterData.getAspects()) {
				AspectData layoutData = layouterData.getLayoutData(aspect);

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

	/**
	 * Paints this components {@link #getBorder()}, if its not <code>null</code>,
	 * around the painting area of {@link #paintLayoutObjects(Graphics2D)}
	 * 
	 * @param g
	 *            The graphics
	 */
	protected void paintOutline(Graphics2D g) {
		Border border = getBorder();

		if (border == null)
			return;

		int paintingSquareSideLenght = Math.min(getWidth(), getHeight());
		border.paintBorder(this, g, 0, 0, paintingSquareSideLenght, paintingSquareSideLenght);
	}

	public boolean isShowForces() {
		return showForces;
	}

	public void setShowForces(boolean showForces) {
		this.showForces = showForces;
	}

	public void setShowLayoutObjects(boolean showLayoutObjects) {
		this.showLayoutObjects = showLayoutObjects;
	}

	public boolean isShowLayoutObjects() {
		return showLayoutObjects;
	}

	public void setShowOutline(boolean showOutline) {
		this.showOutline = showOutline;
	}

	public boolean isShowOutline() {
		return showOutline;
	}
}
