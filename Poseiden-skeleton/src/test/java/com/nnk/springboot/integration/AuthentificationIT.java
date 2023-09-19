package com.nnk.springboot.integration;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthentificationIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    @WithAnonymousUser
    @DisplayName("An unauthenticated user should be redirected to the login page")
    public void unauthenticatedUserShouldBeRedirectedToLoginPage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/bidList/list"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("An unregistered user should not log in")
    public void unregistredUserShouldNotLogIn() throws Exception {
        User dummy = getDummyUser();

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", dummy.getUsername())
                        .param("password", dummy.getPassword()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login?error"));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("A registered user can log in")
    public void registeredUserCanLogIn() throws Exception {
        //User registers
        User dummy = getDummyUser();
        registerUserWithForm(dummy);

        //User log in
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", dummy.getUsername())
                        .param("password", dummy.getPassword()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/bidList/list"));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("A user can log out")
    public void userCanLogOut() throws Exception {

        //User registers and log in
        registeredUserCanLogIn();

        //User log out
        mockMvc.perform(MockMvcRequestBuilders.get("/logout"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));

        //User cant connect to his home page
        unauthenticatedUserShouldBeRedirectedToLoginPage();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("A user with ADMIN authentification should acces to an ADMIN page")
    public void accesUrlWithCorrectAuthentificationShouldDisplayView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("ruleName/list"));
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("A user with USER authentification should not acces to an ADMIN page")
    public void accesUrlWithWrongAuthentificationShouldNotDisplayView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/list"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("")));
        // TODO : comment vérifier que la page chargé est bien la page custom

    }

    public void registerUserWithForm(User user) throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/user/validate")
                .param("username", user.getUsername())
                .param("password", user.getPassword())
                .param("fullname", user.getFullname())
                .param("role", user.getRole()));
    }

    public User getDummyUser() {
        User dummy = new User();
        dummy.setId(1);
        dummy.setFullname("Full name");
        dummy.setUsername("Username");
        dummy.setPassword("Password");
        dummy.setRole("USER");
        return dummy;
    }
}