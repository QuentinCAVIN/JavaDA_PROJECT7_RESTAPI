package com.nnk.springboot.integration;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class BidListIT {

    @Autowired
    private BidListRepository bidListRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        bidListRepository.deleteAll();
        jdbcTemplate.execute("TRUNCATE TABLE bidlist RESTART IDENTITY;");
        // Le script SQL réinitialise l'incrémentation de l'id dans la table
        // Sans ça les "findById" déconnent

    }
    @DisplayName("validateForm_WithValidBidList_ShouldSaveBidListToDatabase")
    @Test
    public void createOneBidList() throws Exception {
        BidList dummyBidList1 = getDummyBidList1();

        mockMvc.perform(MockMvcRequestBuilders.post("/bidList/validate")
                .param("account",dummyBidList1.getAccount())
                .param("type",dummyBidList1.getType())
                .param("bidQuantity",String.valueOf(dummyBidList1.getBidQuantity())));

        BidList bidList = bidListRepository.findById(dummyBidList1.getId()).get();

        Assertions.assertThat(bidList.getId()).isNotNull();
        Assertions.assertThat(bidList.getAccount()).isEqualTo(dummyBidList1.getAccount());
        Assertions.assertThat(bidList.getType()).isEqualTo(dummyBidList1.getType());
        Assertions.assertThat(bidList.getBidQuantity()).isEqualTo(dummyBidList1.getBidQuantity());
    }

    @Test
    public void showBidListHome_WithBidListsSaved_ShouldDisplayBidListsFromDatabase() throws Exception {
        createTwoBidLists();
        BidList dummyBidList1 = getDummyBidList1();
        BidList dummyBidList2 = getDummyBidList2();

        mockMvc.perform(MockMvcRequestBuilders.get("/bidList/list"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString(dummyBidList1.getAccount())))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString(dummyBidList2.getAccount())));
    }


    @Test
    public void UpdateBidList_WithValidBidList_ShouldSaveBidListToDatabase() throws Exception {
        createOneBidList();
        BidList bidList = bidListRepository.findById(1).get();
        String newAccount = "new Account";

        mockMvc.perform(MockMvcRequestBuilders.post("/bidList/update/1")
                .param("account",newAccount)
                .param("type",bidList.getType())
                .param("bidQuantity",String.valueOf(bidList.getBidQuantity())));

        BidList bidListUpdate = bidListRepository.findById(1).get();
        Assertions.assertThat(bidList.getAccount()).isNotEqualTo(newAccount);
        Assertions.assertThat(bidListUpdate.getAccount()).isEqualTo(newAccount);
    }

    @Test
    public void deleteBidList_ShouldDeleteBidListToDatabase() throws Exception {
        createOneBidList();
        Optional<BidList> bidList = bidListRepository.findById(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/bidList/delete/1"));

        Optional <BidList> bidListDeleted = bidListRepository.findById(1);

        Assertions.assertThat(bidList.isPresent()).isTrue();
        Assertions.assertThat(bidListDeleted.isEmpty()).isTrue();
    }

    public void createTwoBidLists() throws Exception {
        createOneBidList();
        BidList bidList2 = getDummyBidList2();
        bidListRepository.save(bidList2);
    }

    public BidList getDummyBidList1() {
        BidList bidList = new BidList();
        bidList.setId(1);
        bidList.setAccount("Account 1");
        bidList.setType("Type 1");
        bidList.setBidQuantity(2.0);
        return bidList;
    }

    public BidList getDummyBidList2() {
        BidList bidList = new BidList();
        bidList.setId(2);
        bidList.setAccount("Account 2");
        bidList.setType("Type 2");
        bidList.setBidQuantity(3.0);
        return bidList;
    }
}