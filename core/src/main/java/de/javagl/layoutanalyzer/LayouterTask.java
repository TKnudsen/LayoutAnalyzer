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

import java.util.logging.Logger;

import de.javagl.swing.tasks.runner.Task;

/**
 * Implementation of a {@link Task} that is run in an own thread, and repeatedly calls the
 * {@link Layouter#performStep()} method.
 */
public class LayouterTask implements Task {
  /**
   * The logger used in this class
   */
  private static final Logger logger = Logger.getLogger(LayouterTask.class.getName());

  /**
   * The {@link Layouter} that is run in this task
   */
  private final Layouter<?> layouter;

  /**
   * A callback that will be called after each step
   */
  private final Runnable stepCallback;

  /**
   * The delay between two layout steps
   */
  private long stepDelayMs = 10;

  /**
   * Creates a new task for running the given {@link Layouter}.
   * 
   * @param layouter
   *          The {@link Layouter} to run
   * @param stepCallback
   *          An (optional) callback that will be called after each step
   */
  public LayouterTask(Layouter<?> layouter, Runnable stepCallback) {
    this.layouter = layouter;
    this.stepCallback = stepCallback;
  }

  @Override
  public void started() {
    // Nothing to do here
  }

  @Override
  public void run() {
    layouter.performStep();
    if (stepCallback != null) {
      try {
        stepCallback.run();
      } catch (RuntimeException e) {
        logger.warning("Ignoring " + e);
      }
    }
    try {
      Thread.sleep(stepDelayMs);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  @Override
  public boolean isDone() {
    // Like writing such an application:
    return false;
  }

  @Override
  public void finished(boolean completed, Throwable t) {
    // Nothing to do here
  }

}
