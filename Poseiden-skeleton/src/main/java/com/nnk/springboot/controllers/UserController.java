package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/user/list")
    public String home(Model model)
    {
        model.addAttribute("users", userService.getUsers());
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUser(User bid) {
        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {
        if (!result.hasErrors()) { //TODO: @Valid n'a pas d'effet "IllegalArgumentException12005_DebugLabel = Cannot find local variable "IllegalArgumentException12005_DebugLabel""
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            userService.saveUser(user);
            model.addAttribute("users", userService.getUsers());
            return "redirect:/user/list";
        }
        return "user/add";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        User user = userService.getUserById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setPassword("");// TODO c'est quoi ça?
        model.addAttribute("user", user);
        return "user/update";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setId(id);
        userService.saveUser(user);
        model.addAttribute("users", userService.getUsers());
        return "redirect:/user/list";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        User user = userService.getUserById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userService.deleteUser(user);
        model.addAttribute("users", userService.getUsers());
        return "redirect:/user/list";
    }
}
