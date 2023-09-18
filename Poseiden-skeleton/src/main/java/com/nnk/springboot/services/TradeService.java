package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;

import java.util.List;
import java.util.Optional;

public interface TradeService {
    List<Trade> getTrades();

    void saveTrade (Trade trade);

    Optional<Trade> getTradeById (int id);

    void deleteTrade(Trade trade);
}
