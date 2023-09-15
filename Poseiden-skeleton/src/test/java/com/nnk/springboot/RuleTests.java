package com.nnk.springboot;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;


@SpringBootTest
public class RuleTests {

	@Autowired
	private RuleNameRepository ruleNameRepository;


}
