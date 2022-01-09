package com.apopova.filnk.orderbook_rebuilder.metrics;

import com.codahale.metrics.UniformReservoir;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.dropwizard.metrics.DropwizardHistogramWrapper;
import org.apache.flink.metrics.Histogram;

public class OrderBookEventProcessingTimeMetricsExposingMapFunction extends RichMapFunction<Long, Long> {

  private static final long serialVersionUID = 1L;

  private transient Histogram histogram;

  @Override
  public void open(Configuration config) {
    com.codahale.metrics.Histogram dropwizardHistogram =
        new com.codahale.metrics.Histogram(new UniformReservoir(5000));

    this.histogram =
        getRuntimeContext()
            .getMetricGroup()
            .histogram("my_custom_histogram", new DropwizardHistogramWrapper(dropwizardHistogram));
  }

  @Override
  public Long map(Long value) throws Exception {
    this.histogram.update(value);
    return value;
  }
}
