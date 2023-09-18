package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class TradeServiceImpl implements TradeService{
    @Autowired
    TradeRepository tradeRepository;
    public List<Trade> getTrades(){
        return tradeRepository.findAll();
    }

    public void saveTrade (Trade trade){
        tradeRepository.save(trade);
    }

    public Optional<Trade> getTradeById (int id) {
        return tradeRepository.findById(id);
    }

    public void deleteTrade(Trade trade) {
        tradeRepository.delete(trade);
    }
}