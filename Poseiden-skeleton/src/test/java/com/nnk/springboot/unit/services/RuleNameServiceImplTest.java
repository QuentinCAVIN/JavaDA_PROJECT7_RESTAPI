package com.nnk.springboot.unit.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.RuleNameServiceImpl;
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
public class RuleNameServiceImplTest {
    @Mock
    private RuleNameRepository ruleNameRepository;
    @InjectMocks
    private RuleNameServiceImpl ruleNameServiceUnderTest;

    @Test
    public void getRuleNamesTest() {
        RuleName dummyRuleName = getDummyRuleName();
        List<RuleName> dummyRuleNameList = new ArrayList<>(Arrays.asList(dummyRuleName,dummyRuleName));
        Mockito.when(ruleNameRepository.findAll()).thenReturn(dummyRuleNameList);

        List<RuleName> ruleNames = ruleNameServiceUnderTest.getRuleNames();

        Assertions.assertThat(ruleNames.size()).isEqualTo(2);
        Assertions.assertThat(ruleNames.get(0)).isEqualTo(dummyRuleName);
        Assertions.assertThat(ruleNames.get(1)).isEqualTo(dummyRuleName);
    }

    @Test
    public void saveRuleNameTest() {
        RuleName ruleName = getDummyRuleName();

        ruleNameServiceUnderTest.saveRuleName(ruleName);

        Mockito.verify(ruleNameRepository, Mockito.times(1)).save(ruleName);
    }

    @Test
    public void getRuleNameByIdTest() {
        RuleName dummyRuleName = getDummyRuleName();
        Mockito.when(ruleNameRepository.findById(1)).thenReturn(Optional.of(dummyRuleName));

        RuleName ruleName = ruleNameServiceUnderTest.getRuleNameById(1).get();

        Assertions.assertThat(ruleName).isEqualTo(dummyRuleName);
    }

    @Test
    public void deleteRuleNameTest() {
        RuleName dummyRuleName = getDummyRuleName();

        ruleNameServiceUnderTest.deleteRuleName(dummyRuleName);

        Mockito.verify(ruleNameRepository, Mockito.times(1)).delete(dummyRuleName);
    }

    public RuleName getDummyRuleName() {
        RuleName ruleName = new RuleName();
        ruleName.setName("Rule Name");
        ruleName.setDescription("Description");
        ruleName.setJson("Json");
        ruleName.setTemplate("Template");
        ruleName.setSqlStr("SQL Str");
        ruleName.setSqlPart("SQL Part");
        return ruleName;
    }
}