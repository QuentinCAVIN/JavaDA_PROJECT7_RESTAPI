package com.nnk.springboot.unit.controllers;

import com.nnk.springboot.controllers.TradeController;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TradeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TradeControllerTest{
    @Autowired
    MockMvc mockMvc;
    @MockBean
    TradeService tradeService;


    @Test
    @WithMockUser
    public void homeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/trade/list"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("trade/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("trades"));
    }

    @Test
    public void addTradeFormTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/trade/add"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("trade/add"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("trade"));
    }

    @Test
    public void validateTestWithValidObject() throws Exception {
        Trade dummyTrade = getDummyTrade();

        mockMvc.perform(MockMvcRequestBuilders.post("/trade/validate")
                        .param("account",dummyTrade.getAccount())
                        .param("type",dummyTrade.getType())
                        .param("buyQuantity",String.valueOf(dummyTrade.getBuyQuantity())))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/trade/add"));
        Mockito.verify(tradeService,Mockito.times(1))
                .saveTrade(ArgumentMatchers.any(Trade.class));
    }

    @Test
    public void validateTestWithWrongObject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/trade/validate"))

                .andExpect(MockMvcResultMatchers.view().name("/trade/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Account is mandatory")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("trade"));
    }

    @Test
    public void showUpdateFormTest() throws Exception{
        Trade dummyTrade = getDummyTrade();
        Mockito.when(tradeService.getTradeById(dummyTrade.getId())).thenReturn(Optional.of(dummyTrade));

        mockMvc.perform(MockMvcRequestBuilders.get("/trade/update/1"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("trade/update"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("trade"));
    }

    @Test
    public void updateTradeTestWithValidObject() throws Exception {
        Trade dummyTrade = getDummyTrade();

        mockMvc.perform(MockMvcRequestBuilders.post("/trade/update/1")
                        .param("account",dummyTrade.getAccount())
                        .param("type",dummyTrade.getType())
                        .param("buyQuantity",String.valueOf(dummyTrade.getBuyQuantity())))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/trade/list"));
        Mockito.verify(tradeService,Mockito.times(1))
                .saveTrade(ArgumentMatchers.any(Trade.class));
    }

    @Test
    public void updateTradeTestWithWrongObject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/trade/update/1"))

                .andExpect(MockMvcResultMatchers.view().name("trade/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Account is mandatory")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("trade"));
    }

    @Test
    public void deleteTradeTest() throws Exception {
        Trade dummyTrade = getDummyTrade();
        Mockito.when(tradeService.getTradeById(dummyTrade.getId())).thenReturn(Optional.of(dummyTrade));

        mockMvc.perform(MockMvcRequestBuilders.get("/trade/delete/1"))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/trade/list"));
        Mockito.verify(tradeService,Mockito.times(1))
                .deleteTrade(ArgumentMatchers.any(Trade.class));
    }


    public Trade getDummyTrade() {
        Trade trade = new Trade();
        trade.setId(1);
        trade.setAccount("Account");
        trade.setType("Type");
        trade.setBuyQuantity(2.0);
        return trade;
    }
}