package com.nnk.springboot.unit.controllers;

import com.nnk.springboot.controllers.RuleNameController;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RuleNameController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RuleNameControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    RuleNameService ruleNameService;


    @Test
    @WithMockUser
    public void homeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/list"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("ruleName/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("ruleNames"));
    }

    @Test
    public void addRuleNameFormTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/add"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("ruleName/add"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("ruleName"));
    }

    @Test
    public void validateTestWithValidObject() throws Exception {
        RuleName dummyRuleName = getDummyRuleName();

        mockMvc.perform(MockMvcRequestBuilders.post("/ruleName/validate")
                .param("name", dummyRuleName.getName())
                .param("description", dummyRuleName.getDescription())
                .param("json", dummyRuleName.getJson())
                .param("template", dummyRuleName.getTemplate())
                .param("sqlStr", dummyRuleName.getSqlStr())
                .param("sqlPart", dummyRuleName.getSqlPart()))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/ruleName/add"));
        Mockito.verify(ruleNameService,Mockito.times(1))
                .saveRuleName(ArgumentMatchers.any(RuleName.class));
    }

    @Test
    public void validateTestWithWrongObject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/ruleName/validate"))

                .andExpect(MockMvcResultMatchers.view().name("/ruleName/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Template is mandatory")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("ruleName"));
    }

    @Test
    public void showUpdateFormTest() throws Exception{
        RuleName dummyRuleName = getDummyRuleName();
        Mockito.when(ruleNameService.getRuleNameById(dummyRuleName.getId())).thenReturn(Optional.of(dummyRuleName));

        mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/update/1"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("ruleName/update"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("ruleName"));
    }

    @Test
    public void updateRuleNameTestWithValidObject() throws Exception {
        RuleName dummyRuleName = getDummyRuleName();
        String descriptionUpdated = "New Description";

        mockMvc.perform(MockMvcRequestBuilders.post("/ruleName/update/1")
                .param("name", dummyRuleName.getName())
                .param("description", descriptionUpdated)
                .param("json", dummyRuleName.getJson())
                .param("template", dummyRuleName.getTemplate())
                .param("sqlStr", dummyRuleName.getSqlStr())
                .param("sqlPart", dummyRuleName.getSqlPart()))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/ruleName/list"));
        Mockito.verify(ruleNameService,Mockito.times(1))
                .saveRuleName(ArgumentMatchers.any(RuleName.class));
    }

    @Test
    public void updateRuleNameTestWithWrongObject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/ruleName/update/1"))

                .andExpect(MockMvcResultMatchers.view().name("ruleName/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Template is mandatory")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("ruleName"));
    }

    @Test
    public void deleteRuleNameTest() throws Exception {
        RuleName dummyRuleName = getDummyRuleName();
        Mockito.when(ruleNameService.getRuleNameById(dummyRuleName.getId())).thenReturn(Optional.of(dummyRuleName));

        mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/delete/1"))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/ruleName/list"));
        Mockito.verify(ruleNameService,Mockito.times(1))
                .deleteRuleName(ArgumentMatchers.any(RuleName.class));
    }


    public RuleName getDummyRuleName() {
        RuleName ruleName = new RuleName();
        ruleName.setId(1);
        ruleName.setName("Rule Name");
        ruleName.setDescription("Description");
        ruleName.setJson("Json");
        ruleName.setTemplate("Template");
        ruleName.setSqlStr("SQL Str");
        ruleName.setSqlPart("SQL Part");
        return ruleName;
    }
}