package com.apopova.filnk.orderbook_rebuilder;

import com.apopova.filnk.orderbook_rebuilder.metrics.OrderBookEventProcessingTimeMetricsExposingMapFunction;
import com.apopova.filnk.orderbook_rebuilder.metrics.OrderBookEventProcessingTimeAggregate;
import com.apopova.filnk.orderbook_rebuilder.metrics.OrderBookEventProcessingTimeWindowFunction;
import com.apopova.filnk.orderbook_rebuilder.model.OrderBookSingleCurrencyEvent;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.DiscardingSink;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.OutputTag;

import java.time.Instant;

public class StreamingJob {

    private static final long CHECKPOINTING_INTERVAL_MS = 80000;

    private final ParameterTool parameters;

    public static void main(String[] args) throws Exception {
        new StreamingJob(ParameterTool.fromArgs(args)).run();
    }

    private StreamingJob(ParameterTool parameters) {
        this.parameters = parameters;
    }

    private void run() throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(parameters.getLong("checkpointing_interval_ms", CHECKPOINTING_INTERVAL_MS));

        final OutputTag<Instant> eventsTimestampOutput = new OutputTag<>("side-output") {
        };

        DataStream<OrderBookSingleCurrencyEvent> orderBookEvents = env.addSource(new OrderBookEventSource())
            .name(OrderBookEventSource.class.getSimpleName());

        SingleOutputStreamOperator<OrderBookSingleCurrencyEvent> processed =
            orderBookEvents
                .keyBy(new OrderBookSingleCurrencyEventKeySelector())
                .process(new OrderBookEventProcessFunction())
                .name(OrderBookEventProcessFunction.class.getSimpleName());

        processed
            .getSideOutput(eventsTimestampOutput)
            .windowAll(TumblingProcessingTimeWindows.of(Time.seconds(20)))
            .aggregate(new OrderBookEventProcessingTimeAggregate(), new OrderBookEventProcessingTimeWindowFunction())
            .name("avg-processing-time-aggregator")
            .map(new OrderBookEventProcessingTimeMetricsExposingMapFunction())
            .name(OrderBookEventProcessingTimeMetricsExposingMapFunction.class.getSimpleName())
            .addSink(new DiscardingSink<>())
            .name(DiscardingSink.class.getSimpleName());

        env.execute(StreamingJob.class.getSimpleName());
    }
}
