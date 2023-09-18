package com.nnk.springboot.unit.controllers;

import com.nnk.springboot.controllers.BidListController;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
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
@WebMvcTest(controllers = BidListController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BidListControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    BidListService bidListService;


    @Test
    @WithMockUser
    public void homeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/bidList/list"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bidList/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("bidLists"));
    }

    @Test
    public void addBidListFormTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/bidList/add"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bidList/add"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("bidList"));
    }

    @Test
    public void validateTestWithValidObject() throws Exception {
        BidList dummyBidList = getDummyBidList();

        mockMvc.perform(MockMvcRequestBuilders.post("/bidList/validate")
                        .param("account",dummyBidList.getAccount())
                        .param("type",dummyBidList.getType())
                        .param("bidQuantity",String.valueOf(dummyBidList.getBidQuantity())))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/bidList/add"));
        Mockito.verify(bidListService,Mockito.times(1))
                .saveBidList(ArgumentMatchers.any(BidList.class));
    }

    @Test
    public void validateTestWithWrongObject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/bidList/validate"))

                .andExpect(MockMvcResultMatchers.view().name("/bidList/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Account is mandatory")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Type is mandatory")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("bidList"));
    }

    @Test
    public void showUpdateFormTest() throws Exception{
        BidList dummyBidList = getDummyBidList();
        Mockito.when(bidListService.getBidListById(dummyBidList.getId())).thenReturn(Optional.of(dummyBidList));

        mockMvc.perform(MockMvcRequestBuilders.get("/bidList/update/1"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bidList/update"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("bidList"));
    }

    @Test
    public void updateBidListTestWithValidObject() throws Exception {
        BidList dummyBidList = getDummyBidList();

        mockMvc.perform(MockMvcRequestBuilders.post("/bidList/update/1")
                        .param("account",dummyBidList.getAccount())
                        .param("type",dummyBidList.getType())
                        .param("bidQuantity",String.valueOf(dummyBidList.getBidQuantity())))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/bidList/list"));
        Mockito.verify(bidListService,Mockito.times(1))
                .saveBidList(ArgumentMatchers.any(BidList.class));
    }

    @Test
    public void updateBidListTestWithWrongObject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/bidList/update/1"))

                .andExpect(MockMvcResultMatchers.view().name("bidList/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                .string(CoreMatchers.containsString("Account is mandatory")))
                .andExpect(MockMvcResultMatchers.content()
                .string(CoreMatchers.containsString("Type is mandatory")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("bidList"));
    }

    @Test
    public void deleteBidListTest() throws Exception {
        BidList dummyBidList = getDummyBidList();
        Mockito.when(bidListService.getBidListById(dummyBidList.getId())).thenReturn(Optional.of(dummyBidList));

        mockMvc.perform(MockMvcRequestBuilders.get("/bidList/delete/1"))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/bidList/list"));
        Mockito.verify(bidListService,Mockito.times(1))
                .deleteBidList(ArgumentMatchers.any(BidList.class));
    }

    public BidList getDummyBidList() {
        BidList bidList = new BidList();
        bidList.setId(1);
        bidList.setAccount("Account");
        bidList.setType("Type");
        bidList.setBidQuantity(2.0);
        return bidList;
    }
}