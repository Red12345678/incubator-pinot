/*
 * Copyright (C) 2014-2018 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.linkedin.thirdeye.detection.components;

import com.linkedin.thirdeye.dashboard.resources.v2.BaselineParsingUtils;
import com.linkedin.thirdeye.dataframe.DataFrame;
import com.linkedin.thirdeye.dataframe.util.MetricSlice;
import com.linkedin.thirdeye.datalayer.dto.MergedAnomalyResultDTO;
import com.linkedin.thirdeye.detection.InputDataFetcher;
import com.linkedin.thirdeye.detection.Pattern;
import com.linkedin.thirdeye.detection.annotation.Components;
import com.linkedin.thirdeye.detection.annotation.DetectionTag;
import com.linkedin.thirdeye.detection.spec.AbsoluteChangeRuleAnomalyFilterSpec;
import com.linkedin.thirdeye.detection.spi.components.AnomalyFilter;
import com.linkedin.thirdeye.detection.spi.model.InputDataSpec;
import com.linkedin.thirdeye.rootcause.impl.MetricEntity;
import com.linkedin.thirdeye.rootcause.timeseries.Baseline;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

import static com.linkedin.thirdeye.dataframe.util.DataFrameUtils.*;

/**
 * Absolute change anomaly filter. Check if the anomaly's absolute change compared to baseline is above the threshold.
 * If not, filters the anomaly.
 */
@Components(type = "ABSOLUTE_CHANGE_FILTER", tags = {DetectionTag.RULE_FILTER})
public class AbsoluteChangeRuleAnomalyFilter implements AnomalyFilter<AbsoluteChangeRuleAnomalyFilterSpec> {
  private double threshold;
  private InputDataFetcher dataFetcher;
  private Baseline baseline;
  private Pattern pattern;

  @Override
  public boolean isQualified(MergedAnomalyResultDTO anomaly) {
    MetricEntity me = MetricEntity.fromURN(anomaly.getMetricUrn());
    List<MetricSlice> slices = new ArrayList<>();
    MetricSlice currentSlice =
        MetricSlice.from(me.getId(), anomaly.getStartTime(), anomaly.getEndTime(), me.getFilters());
    slices.add(currentSlice);

    // customize baseline offset
    MetricSlice baselineSlice = null;
    if (baseline != null) {
      baselineSlice = this.baseline.scatter(currentSlice).get(0);
      slices.add(baselineSlice);
    }

    Map<MetricSlice, DataFrame> aggregates =
        this.dataFetcher.fetchData(new InputDataSpec().withAggregateSlices(slices)).getAggregates();

    double currentValue = getValueFromAggregates(currentSlice, aggregates);
    double baselineValue =
        baselineSlice == null ? anomaly.getAvgBaselineVal() : getValueFromAggregates(baselineSlice, aggregates);
    // if inconsistent with up/down, filter the anomaly
    if (!pattern.equals(Pattern.UP_OR_DOWN) && (currentValue < baselineValue && pattern.equals(Pattern.UP)) || (
        currentValue > baselineValue && pattern.equals(Pattern.DOWN))) {
      return false;
    }
    if (Math.abs(currentValue - baselineValue) < this.threshold) {
      return false;
    }
    return true;
  }

  @Override
  public void init(AbsoluteChangeRuleAnomalyFilterSpec spec, InputDataFetcher dataFetcher) {
    this.dataFetcher = dataFetcher;
    this.pattern = Pattern.valueOf(spec.getPattern().toUpperCase());
    // customize baseline offset
    if (StringUtils.isNotBlank(spec.getOffset())) {
      this.baseline = BaselineParsingUtils.parseOffset(spec.getOffset(), spec.getTimezone());
    }
    this.threshold = spec.getThreshold();
  }

  private double getValueFromAggregates(MetricSlice slice, Map<MetricSlice, DataFrame> aggregates) {
    return aggregates.get(slice).getDouble(COL_VALUE, 0);
  }

}
