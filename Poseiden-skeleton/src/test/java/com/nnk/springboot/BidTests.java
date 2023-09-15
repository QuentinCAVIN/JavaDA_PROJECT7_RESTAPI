package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;


@SpringBootTest
public class BidTests {

	@Autowired
	private BidListRepository bidListRepository;

	@Test
	public void bidListTest() {
		BidList bid = new BidList(/*"Account Test", "Type Test", 10d*/);
		bid.setAccount("Account Test");
		bid.setType("Type Test");
		bid.setBidQuantity(10d); // TODO: Verifier que le 10d correspond bien a BidQuantity


		// Save
		bid = bidListRepository.save(bid);
		Assertions.assertNotNull(bid.getBidListId());
		Assertions.assertEquals(bid.getBidQuantity(), 10d, 10d);


		// Update
		bid.setBidQuantity(20d);
		bid = bidListRepository.save(bid);
		Assertions.assertEquals(bid.getBidQuantity(), 20d, 20d);

		// Find
		List<BidList> listResult = bidListRepository.findAll();
		Assertions.assertTrue(listResult.size() > 0);

		// Delete
		Integer id = bid.getBidListId();
		bidListRepository.delete(bid);
		Optional<BidList> bidList = bidListRepository.findById(id);
		Assertions.assertFalse(bidList.isPresent());
	}
}
