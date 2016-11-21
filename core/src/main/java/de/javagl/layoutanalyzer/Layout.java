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
import java.util.Collections;
import java.util.List;

import de.javagl.layoutanalyzer.objects.LayoutObject;

/**
 * A layout is a collection of {@link LayoutObject}s
 */
public class Layout {
	/**
	 * The {@link LayoutObject}s
	 */
	private List<LayoutObject> layoutObjects;

	/**
	 * Creates a new, empty layout
	 */
	public Layout() {
		this.layoutObjects = new ArrayList<LayoutObject>();
	}

	/**
	 * Returns an unmodifiable view on the {@link LayoutObject}s
	 * 
	 * @return The {@link LayoutObject}s
	 */
	public List<LayoutObject> getLayoutObjects() {
		return Collections.unmodifiableList(layoutObjects);
	}

	/**
	 * Add the given {@link LayoutObject} to this layout
	 * 
	 * @param layoutObject
	 *            The {@link LayoutObject}
	 */
	public void addLayoutObject(LayoutObject layoutObject) {
		layoutObjects.add(layoutObject);
	}

	/**
	 * Remove the given {@link LayoutObject} from this layout
	 * 
	 * @param layoutObject
	 *            The {@link LayoutObject}
	 */
	public boolean removeLayoutObject(LayoutObject layoutObject) {
		if (layoutObjects == null)
			return false;

		return layoutObjects.remove(layoutObject);
	}
	
	/**
	 * Removes all {@link LayoutObject} from this layout
	 */
	public void clear() {
		layoutObjects.clear();
	}

}
