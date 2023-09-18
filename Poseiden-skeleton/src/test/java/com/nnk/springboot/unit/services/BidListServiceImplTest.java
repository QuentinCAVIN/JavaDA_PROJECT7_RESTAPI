package com.nnk.springboot.unit.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.BidListServiceImpl;
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
public class BidListServiceImplTest {

    @Mock
    private BidListRepository bidListRepository;
    @InjectMocks
    private BidListServiceImpl bidListServiceUnderTest;

    @Test
    public void getBidListsTest() {
        BidList dummyBidList = getDummyBidList();
        List<BidList> dummyBidListList = new ArrayList<>(Arrays.asList(dummyBidList,dummyBidList));
        Mockito.when(bidListRepository.findAll()).thenReturn(dummyBidListList);

        List<BidList> bidLists = bidListServiceUnderTest.getBidLists();

        Assertions.assertThat(bidLists.size()).isEqualTo(2);
        Assertions.assertThat(bidLists.get(0)).isEqualTo(dummyBidList);
        Assertions.assertThat(bidLists.get(1)).isEqualTo(dummyBidList);
    }

    @Test
    public void saveBidListTest() {
        BidList dummyBidList = getDummyBidList();

        bidListServiceUnderTest.saveBidList(dummyBidList);

        Mockito.verify(bidListRepository, Mockito.times(1)).save(dummyBidList);
    }

    @Test
    public void getBidListByIdTest() {
        BidList dummyBidList = getDummyBidList();
        Mockito.when(bidListRepository.findById(1)).thenReturn(Optional.of(dummyBidList));

        BidList bidList = bidListServiceUnderTest.getBidListById(1).get();

        Assertions.assertThat(bidList).isEqualTo(dummyBidList);
    }

    @Test
    public void deleteBidListTest() {
        BidList dummyBidList = getDummyBidList();

        bidListServiceUnderTest.deleteBidList(dummyBidList);

        Mockito.verify(bidListRepository, Mockito.times(1)).delete(dummyBidList);
    }

    public BidList getDummyBidList() {
        BidList bidList = new BidList();
        bidList.setAccount("Account");
        bidList.setType("Type");
        bidList.setBidQuantity(2.0);
        return bidList;
    }
}