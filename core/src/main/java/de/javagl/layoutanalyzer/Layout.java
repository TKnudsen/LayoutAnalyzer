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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.javagl.layoutanalyzer.objects.LayoutObject;

/**
 * A layout is a collection of {@link LayoutObject}s and there bounding box
 */
public class Layout<T extends LayoutObject> {
	/**
	 * The {@link LayoutObject}s
	 */
	private final List<T> layoutObjects;
	/**
	 * Creates a new, empty layout
	 */
	public Layout() {
		this.layoutObjects = new ArrayList<T>(); // CopyOnWriteArrayList ???
	}

	/**
	 * Returns an unmodifiable view on the {@link LayoutObject}s
	 * 
	 * @return The {@link LayoutObject}s
	 */
	public List<T> getLayoutObjects() {
		return Collections.unmodifiableList(layoutObjects);
	}

	/**
	 * Add the given {@link LayoutObject} to this layout
	 * 
	 * @param layoutObject
	 *            The {@link LayoutObject}
	 */
	public synchronized void addLayoutObject(T object) {
		layoutObjects.add(object);
	}


  /**
   * Remove the given {@link LayoutObject} from this layout
   * 
   * @param layoutObject
   *            The {@link LayoutObject}
   */
	public synchronized void removeLayoutObject(T object) {
		layoutObjects.remove(object);
	}

	public synchronized void addAll(Collection<T> objects) {
		layoutObjects.addAll(objects);
	}

	public synchronized void removeAll(Collection<T> objects) {
		layoutObjects.removeAll(objects);
	}
	
	 /**
   * Removes all {@link LayoutObject} from this layout
   */
  public synchronized void clear() {
    layoutObjects.clear();
  }
}
