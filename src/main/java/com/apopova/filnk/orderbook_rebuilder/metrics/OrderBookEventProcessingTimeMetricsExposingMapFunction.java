package com.apopova.filnk.orderbook_rebuilder.metrics;

import com.codahale.metrics.UniformReservoir;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Gauge;

public class OrderBookEventProcessingTimeMetricsExposingMapFunction extends RichMapFunction<Tuple2<Long, Long>, Tuple2<Long, Long>> {

    private static final long serialVersionUID = 1L;

    private transient Long min;
    private transient Long max;

    @Override
    public void open(Configuration config) {
        getRuntimeContext()
            .getMetricGroup()
            .gauge("min_gauge", (Gauge<Long>) () -> min);

        getRuntimeContext()
            .getMetricGroup()
            .gauge("max_gauge", (Gauge<Long>) () -> max);
    }

    @Override
    public Tuple2<Long, Long> map(Tuple2<Long, Long> value) throws Exception {
        max = value.f0;
        min = value.f1;
        return value;
    }
}
