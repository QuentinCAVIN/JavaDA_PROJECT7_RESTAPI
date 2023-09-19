package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;

import java.util.Optional;


@Controller
public class BidListController  {
    @Autowired
    BidListService bidListService;

    @RequestMapping("/bidList/list")
    public String home(Model model)
    {
        model.addAttribute("bidLists", bidListService.getBidLists());
        return "bidList/list";
    }


    @GetMapping("/bidList/add")
    public String addBidListForm(BidList bidList) {
        return "bidList/add";
    }


    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bidList, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            bidListService.saveBidList(bidList);
            return "redirect:/bidList/add";
        }
        //model.addAttribute(bidList);
        // l'objet BidList est conservé dans le model par default pas besoin de l'ajouter au model
        return "/bidList/add";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<BidList> bidList = bidListService.getBidListById(id);
        model.addAttribute(bidList.get()); //TODO: a vérifier: pas besoin de confirmation car si l'id n'est pas présente
                                           // le endpoint n'est pas visible par l'utilisateur. Copier user sinon
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBidList(@PathVariable("id") Integer id, @Valid BidList bidList,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bidList/update";
            // l'objet BidList est conservé dans le model par default pas besoin de l'ajouter au model
        }
        bidListService.saveBidList(bidList);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBidList(@PathVariable("id") Integer id, Model model) {
        Optional<BidList> bidList = bidListService.getBidListById(id);
        bidListService.deleteBidList(bidList.get());
        return "redirect:/bidList/list";
    }
}