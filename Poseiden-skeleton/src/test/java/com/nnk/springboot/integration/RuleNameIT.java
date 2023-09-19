package com.nnk.springboot.integration;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class RuleNameIT {

    @Autowired
    private RuleNameRepository ruleNameRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        ruleNameRepository.deleteAll();
       jdbcTemplate.execute("TRUNCATE TABLE rulename RESTART IDENTITY;");
        // Le script SQL réinitialise l'incrémentation de l'id dans la table
        // Sans ça les "findById" déconnent

    }

    @DisplayName("validateForm_WithValidRuleName_ShouldSaveRuleNameToDatabase")
    @Test
    @WithMockUser(authorities = "ADMIN")
    public void createOneRuleName() throws Exception {
        RuleName dummyRuleName1 = getDummyRuleName1();

        mockMvc.perform(MockMvcRequestBuilders.post("/ruleName/validate")
                .param("name", dummyRuleName1.getName())
                .param("description", dummyRuleName1.getDescription())
                .param("json", dummyRuleName1.getJson())
                .param("template", dummyRuleName1.getTemplate())
                .param("sqlStr", dummyRuleName1.getSqlStr())
                .param("sqlPart", dummyRuleName1.getSqlPart()));

        RuleName ruleName = ruleNameRepository.findById(dummyRuleName1.getId()).get();

        Assertions.assertThat(ruleName.getId()).isNotNull();
        Assertions.assertThat(ruleName.getName()).isEqualTo(dummyRuleName1.getName());
        Assertions.assertThat(ruleName.getDescription()).isEqualTo(dummyRuleName1.getDescription());
        Assertions.assertThat(ruleName.getJson()).isEqualTo(dummyRuleName1.getJson());
        Assertions.assertThat(ruleName.getTemplate()).isEqualTo(dummyRuleName1.getTemplate());
        Assertions.assertThat(ruleName.getSqlStr()).isEqualTo(dummyRuleName1.getSqlStr());
        Assertions.assertThat(ruleName.getSqlPart()).isEqualTo(dummyRuleName1.getSqlPart());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void showRuleNameHome_WithRuleNamesSaved_ShouldDisplayRuleNamesFromDatabase() throws Exception {
        createTwoRuleNames();
        RuleName dummyRuleName1 = getDummyRuleName1();
        RuleName dummyRuleName2 = getDummyRuleName2();

        mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/list"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString(dummyRuleName1.getName())))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString(dummyRuleName2.getName())));
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    public void UpdateRuleName_WithValidRuleName_ShouldSaveRuleNameToDatabase() throws Exception {
        createOneRuleName();
        RuleName ruleName = ruleNameRepository.findById(1).get();
        String descriptionUpdated = "New Description";

        mockMvc.perform(MockMvcRequestBuilders.post("/ruleName/update/1")
                .param("name", ruleName.getName())
                .param("description", descriptionUpdated)
                .param("json", ruleName.getJson())
                .param("template", ruleName.getTemplate())
                .param("sqlStr", ruleName.getSqlStr())
                .param("sqlPart", ruleName.getSqlPart()));

        RuleName ruleNameUpdate = ruleNameRepository.findById(1).get();
        Assertions.assertThat(ruleName.getDescription()).isNotEqualTo(descriptionUpdated);
        Assertions.assertThat(ruleNameUpdate.getDescription()).isEqualTo(descriptionUpdated);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void deleteRuleName_ShouldDeleteRuleNameToDatabase() throws Exception {
        createOneRuleName();
        Optional<RuleName> ruleName = ruleNameRepository.findById(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/delete/1"));

        Optional<RuleName> ruleNameDeleted = ruleNameRepository.findById(1);

        Assertions.assertThat(ruleName.isPresent()).isTrue();
        Assertions.assertThat(ruleNameDeleted.isEmpty()).isTrue();
    }

    public void createTwoRuleNames() throws Exception {
        createOneRuleName();
        RuleName ruleName2 = getDummyRuleName2();
        ruleNameRepository.save(ruleName2);
    }

    public RuleName getDummyRuleName1() {
        RuleName ruleName = new RuleName();
        ruleName.setId(1);
        ruleName.setName("Rule Name 1");
        ruleName.setDescription("Description 1");
        ruleName.setJson("Json 1");
        ruleName.setTemplate("Template 1");
        ruleName.setSqlStr("SQL Str 1");
        ruleName.setSqlPart("SQL Part 1");
        return ruleName;
    }

    public RuleName getDummyRuleName2() {
        RuleName ruleName = new RuleName();
        ruleName.setId(2);
        ruleName.setName("Rule Name 2");
        ruleName.setDescription("Description 2");
        ruleName.setJson("Json 2");
        ruleName.setTemplate("Template 2");
        ruleName.setSqlStr("SQL Str 2");
        ruleName.setSqlPart("SQL Part 2");
        return ruleName;
    }
}