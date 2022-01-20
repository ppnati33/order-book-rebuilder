package com.apopova.filnk.orderbook_rebuilder.metrics;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class OrderBookEventProcessingTimeWindowFunction
    extends ProcessAllWindowFunction<Tuple2<Long, Long>, Tuple2<Long, Long>, TimeWindow> {

    @Override
    public void process(
        Context context,
        Iterable<Tuple2<Long, Long>> elements,
        Collector<Tuple2<Long, Long>> out
    ) throws Exception {
        Tuple2<Long, Long> maxAndMin = elements.iterator().next();
        out.collect(maxAndMin);
    }
}
