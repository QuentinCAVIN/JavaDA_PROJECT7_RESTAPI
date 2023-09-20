package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.UserDto;
import com.nnk.springboot.mapper.UserMapper;
import com.nnk.springboot.services.UserService;

import jakarta.validation.Valid;
import org.glassfish.jaxb.core.v2.TODO;
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
    public String home(Model model) {

        model.addAttribute("users", UserMapper.convertUserListToUserDtoList(userService.getUsers()));
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUser(Model model) {
        model.addAttribute("user", new UserDto());
        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validate(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        User OptionalUser = userService.getUserByUsername(userDto.getUsername());
        if (OptionalUser != null) {
            result.rejectValue("username", null, "This username is already used");
        }
        if (!result.hasErrors()) {
            User user = UserMapper.convertUserDtoToUser(userDto);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            userService.saveUser(user);
            model.addAttribute("users", UserMapper.convertUserListToUserDtoList(userService.getUsers()));
            return "redirect:/user/list";
        }
        model.addAttribute("user", userDto);
        return "user/add";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        User user = userService.getUserById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        UserDto userDto = UserMapper.convertUserToUserDto(user);
        userDto.setPassword("");//Pour éviter d'afficher le mot de passe au moment de la modif (ajouté au model)
        model.addAttribute("user", user);
        return "user/update";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid @ModelAttribute("user") UserDto userDto, //TODO Poser question à Vincent
                             BindingResult result, Model model) {// Ne peut on pas suprrimer le paramétre id ici voir ci-dessous
        User optionalUser = userService.getUserByUsername(userDto.getUsername());
        User userUpdated = userService.getUserById(id).orElse(null);
        if (optionalUser != null && optionalUser.getUsername() != userUpdated.getUsername()) {
            result.rejectValue("username", null, "This username is already used");
        }
        if (result.hasErrors()) {
            return "user/update";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        userDto.setId(id);// Pourquoi définir l'id ici? elle est déja définis dans le user donnée par showUpdateForm
        userService.saveUser(UserMapper.convertUserDtoToUser(userDto));
        model.addAttribute("users", UserMapper.convertUserListToUserDtoList(userService.getUsers()));
        // Pourquoi ajouter la list des utilisateur au model? redirect:/user/list le fait déja non?
        return "redirect:/user/list";
    }
    //TODO: si modification, mettre à jour les methodes update de chaque controller + les test + le html
    // si pas de modif ajouter le set id dans les methodes update de chaque controller

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        User user = userService.getUserById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userService.deleteUser(user);
        model.addAttribute("users", UserMapper.convertUserListToUserDtoList(userService.getUsers()));
        return "redirect:/user/list";
    }
}