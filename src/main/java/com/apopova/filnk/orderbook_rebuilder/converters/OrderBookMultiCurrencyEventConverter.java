package com.apopova.filnk.orderbook_rebuilder.converters;

import static com.apopova.filnk.orderbook_rebuilder.model.LevelPriceType.ASK;
import static com.apopova.filnk.orderbook_rebuilder.model.LevelPriceType.BID;

import com.apopova.filnk.orderbook_rebuilder.dto.OrderBookMultiCurrencyEvent;
import com.apopova.filnk.orderbook_rebuilder.dto.OrderBookMultiCurrencyEventPayload;
import com.apopova.filnk.orderbook_rebuilder.model.Currency;
import com.apopova.filnk.orderbook_rebuilder.model.LevelPrice;
import com.apopova.filnk.orderbook_rebuilder.model.OrderBookSingleCurrencyEvent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderBookMultiCurrencyEventConverter {

  public static List<OrderBookSingleCurrencyEvent> splitEventByCurrency(
      OrderBookMultiCurrencyEvent event) {
    Map<Currency, OrderBookMultiCurrencyEventPayload> rawData;

    if (event.getSnapshotData() != null) {
      rawData = event.getSnapshotData();
    } else if (event.getUpdateData() != null) {
      rawData = event.getUpdateData();
    } else {
      throw new IllegalArgumentException(
          "SnapshotData or UpdateData must be filled in OrderBookEvent");
    }

    List<OrderBookSingleCurrencyEvent> results = new ArrayList<>();

    rawData.forEach(
        (currency, payload) -> {
          List<LevelPrice> asks =
              payload.getAsks().stream()
                  .map(levelPrice -> new LevelPrice(ASK, levelPrice.get(0), levelPrice.get(1)))
                  .collect(Collectors.toList());
          List<LevelPrice> bids =
              payload.getBids().stream()
                  .map(levelPrice -> new LevelPrice(BID, levelPrice.get(0), levelPrice.get(1)))
                  .collect(Collectors.toList());

          Instant updateTimestamp = Instant.ofEpochSecond(payload.getTimestamp());

          results.add(
              new OrderBookSingleCurrencyEvent(
                  currency.toString(), asks, bids, updateTimestamp, event.getCreatedAt()));

          results.add(
              new OrderBookSingleCurrencyEvent(
                  currency.toString() + "1", asks, bids, updateTimestamp, event.getCreatedAt()));

          results.add(
              new OrderBookSingleCurrencyEvent(
                  currency.toString() + "2", asks, bids, updateTimestamp, event.getCreatedAt()));

          results.add(
              new OrderBookSingleCurrencyEvent(
                  currency.toString() + "3", asks, bids, updateTimestamp, event.getCreatedAt()));
        });

    return results;
  }
}
