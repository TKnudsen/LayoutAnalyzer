package de.javagl.layoutanalyzer.quality;

import java.util.List;
import java.util.Objects;

import de.javagl.layoutanalyzer.AspectData;
import de.javagl.layoutanalyzer.LayoutAspects;
import de.javagl.layoutanalyzer.QualityData;
import de.javagl.layoutanalyzer.aspects.Aspect;
import de.javagl.layoutanalyzer.objects.LayoutObject;
import de.javagl.layoutanalyzer.utils.QualityDatas;

/**
 * Wrapper to derive a {@link QualityMeasure} from an {@link Aspect} by accumulating the force
 * lenght produced by a given aspect.
 */
public class ForceLengthQualityMeasure implements QualityMeasure {

  /**
   * The aspect this quality is computed on
   */
  private Aspect forceAspect;

  private double minForceLength;
  private double maxForceLength;

  /**
   * Default Constructor
   * 
   * @param forceAspect
   *          the @link {@link Aspect} this {@link QualityMeasure} is computed on
   */
  public ForceLengthQualityMeasure(Aspect forceAspect) {
    // XXX TODO Avoid these VERY magic constants ASAP !!!
    this(forceAspect, 0.0, 0.5);
  }

  public ForceLengthQualityMeasure(Aspect forceAspect, double minForceLenght,
      double maxForceLength) {
    Objects.requireNonNull(forceAspect, "Aspect is NULL");
    this.forceAspect = forceAspect;
    this.minForceLength = minForceLenght;
    this.maxForceLength = maxForceLength;
  }

  @Override
  public QualityData computeQualityData(List<? extends LayoutObject> layoutObjects,
      LayoutAspects layoutForcesHint) {
    LayoutAspects forceData = layoutForcesHint;
    if (forceData == null || forceData.getLayoutData(forceAspect) == null) {
      AspectData layoutData = forceAspect.computeLayoutData(layoutObjects);
      forceData = new LayoutAspects();
      forceData.add(forceAspect, layoutData);
    }

    QualityData qualityData = QualityDatas.computeFromForceLengths(
        forceData.getLayoutData(forceAspect), minForceLength, maxForceLength);
    return qualityData;
  }

  @Override
  public String getName() {
    return forceAspect.getName() + " - Force Lenght Quality";
  }

}
