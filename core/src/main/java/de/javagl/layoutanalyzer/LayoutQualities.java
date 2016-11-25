package de.javagl.layoutanalyzer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.javagl.layoutanalyzer.objects.LayoutObject;
import de.javagl.layoutanalyzer.quality.QualityMeasure;

public class LayoutQualities {
  /**
   * The map from each {@link QualityMeasure} to the {@link QualityData} that it computed
   */
  private final Map<QualityMeasure, QualityData> layoutQualities;
  private List<QualityMeasure> qualityMeasures;
  private LayoutAspects forces;
  private List<? extends LayoutObject> layoutObjects;

  /**
   * Default constructor
   */
  public LayoutQualities(List<QualityMeasure> qualityMeasures,
      List<? extends LayoutObject> layoutObjects, LayoutAspects forces) {
    Objects.requireNonNull(layoutObjects, "The Layout objects are NULL");
    Objects.requireNonNull(forces, "The forces object is NULL");
    Objects.requireNonNull(qualityMeasures, "The list of quality measures is NULL");
    this.layoutObjects = layoutObjects;
    this.forces = forces;
    this.qualityMeasures = qualityMeasures;
    layoutQualities = new LinkedHashMap<QualityMeasure, QualityData>();
  }

  public List<QualityMeasure> getQualityMeasures() {
    return qualityMeasures;
  }

  public QualityData getQualityData(QualityMeasure m) {
    if (layoutQualities.containsKey(m)) {
      return layoutQualities.get(m);
    } else {
      QualityData data = m.computeQualityData(layoutObjects, forces);
      layoutQualities.put(m, data);
      return data;
    }
  }
}
