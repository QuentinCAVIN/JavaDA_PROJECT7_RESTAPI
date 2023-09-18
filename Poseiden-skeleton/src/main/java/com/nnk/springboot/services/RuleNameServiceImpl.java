package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RuleNameServiceImpl implements RuleNameService {
    @Autowired
    RuleNameRepository ruleNameRepository;

    public List<RuleName> getRuleNames() {
        return ruleNameRepository.findAll();
    }

    public void saveRuleName(RuleName ruleName) {
        ruleNameRepository.save(ruleName);
    }

    public Optional<RuleName> getRuleNameById(int id) {
        return ruleNameRepository.findById(id);
    }

    public void deleteRuleName(RuleName ruleName) {
        ruleNameRepository.delete(ruleName);
    }
}