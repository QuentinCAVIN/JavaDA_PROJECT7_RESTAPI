package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;

import java.util.List;
import java.util.Optional;

public interface RuleNameService {
    List<RuleName> getRuleNames();

    void saveRuleName(RuleName ruleName);

    Optional<RuleName> getRuleNameById(int id);

    void deleteRuleName(RuleName ruleName);
}
