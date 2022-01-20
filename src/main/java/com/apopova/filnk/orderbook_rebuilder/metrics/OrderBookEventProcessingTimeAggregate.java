package com.apopova.filnk.orderbook_rebuilder.metrics;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple2;

public class OrderBookEventProcessingTimeAggregate
    implements AggregateFunction<Instant, List<Long>, Tuple2<Long, Long>> {

  @Override
  public List<Long> createAccumulator() {
    return new ArrayList<>();
  }

  @Override
  public List<Long> add(Instant eventTimestamp, List<Long> accumulator) {
     accumulator.add(ChronoUnit.MILLIS.between(eventTimestamp, Instant.now()));
     return accumulator;
  }

  @Override
  public Tuple2<Long, Long> getResult(List<Long> accumulator) {
    return new Tuple2<>(
        accumulator.stream().reduce(Long::max).orElse(0L),
        accumulator.stream().reduce(Long::min).orElse(0L)
    );
  }

  @Override
  public List<Long> merge(List<Long> a, List<Long> b) {
     a.addAll(b);
     return a;
  }
}
