package de.javagl.layoutanalyzer.quality;

import java.util.List;

import de.javagl.layoutanalyzer.LayoutAspects;
import de.javagl.layoutanalyzer.QualityData;
import de.javagl.layoutanalyzer.objects.LayoutObject;

/**
 * Interface for one Quality Measure for a given layout. It offers the possibility to compute @link
 * {@link QualityData} for a given list of @link {@link LayoutObject}.
 */
public interface QualityMeasure {
  /**
   * Compute the {@link QualityData} for the given {@link LayoutObject}s.<br>
   * <br>
   * The returned {@link QualityData} will contain a copy of the given list of objects, and map each
   * object to a quality value. This quality value should usually be in the range [0,1], where a
   * quality of 1.0 means that the layout of the object can not be improved.<br>
   * <br>
   * A {@link LayoutAspects} may be passed to this method, which has been computed with
   * {@link #computeLayoutData(List)} before. One has to anticipate that this {@link LayoutAspects}
   * may be <code>null</code>. In this case, the {@link LayoutAspects} may be computed internally, if
   * it is required for the computation of the {@link QualityData}.
   * 
   * @param layoutObjects
   *          The {@link LayoutObject}s
   * @param layoutDataHint
   *          The optional {@link LayoutAspects}
   * @return The {@link QualityData}
   */
  public QualityData computeQualityData(List<? extends LayoutObject> layoutObjects,
      LayoutAspects aspectforces);

  public String getName();
}
