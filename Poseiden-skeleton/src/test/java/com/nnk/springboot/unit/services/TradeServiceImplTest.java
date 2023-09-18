package com.nnk.springboot.unit.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.TradeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TradeServiceImplTest {

    @Mock
    private TradeRepository tradeRepository;
    @InjectMocks
    private TradeServiceImpl tradeServiceUnderTest;

    @Test
    public void getTradesTest() {
        Trade dummyTrade = getDummyTrade();
        List<Trade> dummyTradeList = new ArrayList<>(Arrays.asList(dummyTrade,dummyTrade));
        Mockito.when(tradeRepository.findAll()).thenReturn(dummyTradeList);

        List<Trade> trades = tradeServiceUnderTest.getTrades();

        Assertions.assertThat(trades.size()).isEqualTo(2);
        Assertions.assertThat(trades.get(0)).isEqualTo(dummyTrade);
        Assertions.assertThat(trades.get(1)).isEqualTo(dummyTrade);
    }

    @Test
    public void saveTradeTest() {
        Trade dummyTrade = getDummyTrade();

        tradeServiceUnderTest.saveTrade(dummyTrade);

        Mockito.verify(tradeRepository, Mockito.times(1)).save(dummyTrade);
    }

    @Test
    public void getRatingByIdTest() {
        Trade dummyTrade = getDummyTrade();
        Mockito.when(tradeRepository.findById(1)).thenReturn(Optional.of(dummyTrade));

        Trade trade = tradeServiceUnderTest.getTradeById(1).get();

        Assertions.assertThat(trade).isEqualTo(dummyTrade);
    }

    @Test
    public void deleteRatingTest() {
        Trade dummyTrade = getDummyTrade();

        tradeServiceUnderTest.deleteTrade(dummyTrade);

        Mockito.verify(tradeRepository, Mockito.times(1)).delete(dummyTrade);
    }

    public Trade getDummyTrade() {
        Trade trade = new Trade();
        trade.setAccount("Account");
        trade.setType("Type");
        trade.setBuyQuantity(2.0);
        return trade;
    }
}