package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
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
public class RatingController {
    @Autowired
    RatingService ratingService;

    @RequestMapping("/rating/list")
    public String home(Model model)
    {
        model.addAttribute("ratings", ratingService.getRatings());
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating) {// TODO ça marche ça?
       /* Rating rating = new Rating();
        model.addAttribute("rating",rating);*/
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return Rating list
        // TODO : @Valid n'a pas d'effet "IllegalArgumentException12005_DebugLabel = Cannot find local variable "IllegalArgumentException12005_DebugLabel""
        if (!result.hasErrors()) {
            ratingService.saveRating(rating);
            return "redirect:/rating/add";
        }
        model.addAttribute(rating);
        return "/rating/add";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<Rating> rating = ratingService.getRatingById(id);
        model.addAttribute(rating.get()); //TODO: a vérifier: pas besoin de confirmation car si l'id n'est pas présente
                                          // le endpoint n'est pas visible par l'utilisatuer
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                             BindingResult result, Model model) {
        if (!result.hasErrors()) {
            ratingService.saveRating(rating);
            model.addAttribute(rating);
            model.addAttribute("ratings", ratingService.getRatings());
            return "redirect:/rating/list"; //TODO Structure copié sur User mais vérifier la différence entre "/" et "redirect:/"
                                            // il me semble que c'est mal utilisé ici
                                            // pas vérifiable pour le moment vu que le @Valid ne fonctionne pas
        }
        return "/rating/list";
        // TODO: check required fields, if valid call service to update Rating and return Rating list
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        Optional<Rating> rating = ratingService.getRatingById(id);
        ratingService.deleteRating(rating.get());
        return "redirect:/rating/list";
    }
}