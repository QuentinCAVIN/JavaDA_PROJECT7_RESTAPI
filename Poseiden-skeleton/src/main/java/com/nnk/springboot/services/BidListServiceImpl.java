package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BidListServiceImpl implements BidListService {
    @Autowired
    BidListRepository bidListRepository;
    public List<BidList> getBidLists(){
        return bidListRepository.findAll();
    }

    public void saveBidList (BidList bidList){
        bidListRepository.save(bidList);
    }

    public Optional<BidList> getBidListById (int id) {
        return bidListRepository.findById(id);
    }

    public void deleteBidList(BidList bidList) {
        bidListRepository.delete(bidList);
    }
}
