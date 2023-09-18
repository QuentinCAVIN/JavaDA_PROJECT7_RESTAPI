package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;

import java.util.List;
import java.util.Optional;

public interface BidListService {
    public List<BidList> getBidLists();
    public void saveBidList (BidList bidList);
    public Optional<BidList> getBidListById (int id);
    public void deleteBidList(BidList bidList);
}