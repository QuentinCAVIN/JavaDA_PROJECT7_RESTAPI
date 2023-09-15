package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class RuleNameController {
    @Autowired
    RuleNameService ruleNameService;

    @RequestMapping("/ruleName/list")
    public String home(Model model)
    {
        model.addAttribute("ruleNames", ruleNameService.getRuleNames());
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName bid) {
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            ruleNameService.saveRuleName(ruleName);
            return "redirect:/ruleName/add";
        }
        //model.addAttribute(ruleName);
        // l'objet ruleName est conservé dans le model par default pas besoin de l'ajouter au model
        return "/ruleName/add";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<RuleName> ruleName = ruleNameService.getRuleNameById(id);
        model.addAttribute(ruleName.get()); //TODO: a vérifier: pas besoin de confirmation car si l'id n'est pas présente
        // le endpoint n'est pas visible par l'utilisatuer. Copier user sinon
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "ruleName/update";
            // l'objet RuleName est conservé dans le model par default pas besoin de l'ajouter au model
        }
        ruleNameService.saveRuleName(ruleName);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        Optional<RuleName> ruleName = ruleNameService.getRuleNameById(id);
        ruleNameService.deleteRuleName(ruleName.get());
        return "redirect:/ruleName/list";
    }
}
