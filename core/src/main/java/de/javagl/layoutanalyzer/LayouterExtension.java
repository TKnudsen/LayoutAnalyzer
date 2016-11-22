package de.javagl.layoutanalyzer;

import de.javagl.layoutanalyzer.objects.LayoutObject;

/**
 * Implements additional behavior for an Layout
 * 
 * @param <T> subtype of {@link LayoutObject}
 */
public interface LayouterExtension<T extends LayoutObject> {

	public void process(Layout<T> layoutData);

	public void setEnabled(boolean enabled);

	public boolean isEnabled();

}
