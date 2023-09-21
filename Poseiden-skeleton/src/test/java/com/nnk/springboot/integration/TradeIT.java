package com.nnk.springboot.integration;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class TradeIT {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        tradeRepository.deleteAll();
        jdbcTemplate.execute("TRUNCATE TABLE trade RESTART IDENTITY;");
        // Le script SQL réinitialise l'incrémentation de l'id dans la table
        // Sans ça les "findById" déconnent

    }

    @DisplayName("validateForm_WithValidTrade_ShouldSaveTradeToDatabase")
    @Test
    @WithMockUser(authorities = "USER")
    public void createOneTrade() throws Exception {
        Trade dummyTrade1 = getDummyTrade1();

        mockMvc.perform(MockMvcRequestBuilders.post("/trade/validate")
                .param("account",dummyTrade1.getAccount())
                .param("type",dummyTrade1.getType())
                .param("buyQuantity",String.valueOf(dummyTrade1.getBuyQuantity())));

        Trade trade = tradeRepository.findById(dummyTrade1.getId()).get();

        Assertions.assertThat(trade.getId()).isNotNull();
        Assertions.assertThat(trade.getAccount()).isEqualTo(dummyTrade1.getAccount());
        Assertions.assertThat(trade.getType()).isEqualTo(dummyTrade1.getType());
        Assertions.assertThat(trade.getBuyQuantity()).isEqualTo(dummyTrade1.getBuyQuantity());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void showTradeHome_WithTradesSaved_ShouldDisplayTradesFromDatabase() throws Exception {
        createTwoTrades();
        Trade dummyTrade1 = getDummyTrade1();
        Trade dummyTrade2 = getDummyTrade2();

        mockMvc.perform(MockMvcRequestBuilders.get("/trade/list"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString(dummyTrade1.getAccount())))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString(dummyTrade2.getAccount())));
    }


    @Test
    @WithMockUser(authorities = "USER")
    public void UpdateTrade_WithValidTrade_ShouldSaveTradeToDatabase() throws Exception {
        createOneTrade();
        Trade trade = tradeRepository.findById(1).get();
        String newAccount = "New account";

        mockMvc.perform(MockMvcRequestBuilders.post("/trade/update/1")
                .param("account",newAccount)
                .param("type",trade.getType())
                .param("buyQuantity",String.valueOf(trade.getBuyQuantity())));

        Trade tradeUpdate = tradeRepository.findById(1).get();
        Assertions.assertThat(trade.getAccount()).isNotEqualTo(newAccount);
        Assertions.assertThat(tradeUpdate.getAccount()).isEqualTo(newAccount);
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void deleteTrade_ShouldDeleteTradeToDatabase() throws Exception {
        createOneTrade();
        Optional<Trade> trade = tradeRepository.findById(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/trade/delete/1"));

        Optional <Trade> tradeDeleted = tradeRepository.findById(1);

        Assertions.assertThat(trade.isPresent()).isTrue();
        Assertions.assertThat(tradeDeleted.isEmpty()).isTrue();
    }

    public void createTwoTrades() throws Exception {
        createOneTrade();
        Trade trade2 = getDummyTrade2();
        tradeRepository.save(trade2);
    }

    public Trade getDummyTrade1() {
        Trade trade = new Trade();
        trade.setId(1);
        trade.setAccount("Account 1");
        trade.setType("Type 1");
        trade.setBuyQuantity(2.0);
        return trade;
    }

    public Trade getDummyTrade2() {
        Trade trade = new Trade();
        trade.setId(2);
        trade.setAccount("Account 2");
        trade.setType("Type 2");
        trade.setBuyQuantity(3.0);
        return trade;
    }
}