package com.nnk.springboot.integration;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        jdbcTemplate.execute("TRUNCATE TABLE users RESTART IDENTITY;");
        // Le script SQL réinitialise l'incrémentation de l'id dans la table
        // Sans ça les "findById" déconnent

    }
    @DisplayName("validateForm_WithValidUser_ShouldSaveUserToDatabase")
    @Test
    public void createOneUser() throws Exception {
        User dummyUser1 = getDummyUser1();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/validate")
                .param("username",dummyUser1.getUsername())
                .param("password",dummyUser1.getPassword())
                .param("fullname",dummyUser1.getFullname())
                .param("role",dummyUser1.getRole()));

        User user = userRepository.findById(dummyUser1.getId()).get();

        Assertions.assertThat(user.getId()).isNotNull();
        Assertions.assertThat(user.getUsername()).isEqualTo(dummyUser1.getUsername());
        Assertions.assertThat(user.getPassword()).isNotNull();
        Assertions.assertThat(user.getFullname()).isEqualTo(dummyUser1.getFullname());
        Assertions.assertThat(user.getRole()).isEqualTo(dummyUser1.getRole());
    }

    @Test
    public void showUserHome_WithUsersSaved_ShouldDisplayUsersFromDatabase() throws Exception {
        createTwoUsers();
        User dummyUser1 = getDummyUser1();
        User dummyUser2 = getDummyUser2();

        mockMvc.perform(MockMvcRequestBuilders.get("/user/list"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString(dummyUser1.getUsername())))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString(dummyUser2.getUsername())));
    }


    @Test
    public void UpdateUser_WithValidUser_ShouldSaveUserToDatabase() throws Exception {
        createOneUser();
        User user = userRepository.findById(1).get();
        String newUsername = "New Username";

        mockMvc.perform(MockMvcRequestBuilders.post("/user/update/1")
                .param("username",newUsername)
                .param("password",user.getPassword())
                .param("fullname",user.getFullname())
                .param("role",user.getRole()));

        User userUpdate = userRepository.findById(1).get();
        Assertions.assertThat(user.getUsername()).isNotEqualTo(newUsername);
        Assertions.assertThat(userUpdate.getUsername()).isEqualTo(newUsername);
    }

    @Test
    public void deleteUser_ShouldDeleteUserToDatabase() throws Exception {
        createOneUser();
        Optional<User> user = userRepository.findById(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/delete/1"));

        Optional <User> userDeleted = userRepository.findById(1);

        Assertions.assertThat(user.isPresent()).isTrue();
        Assertions.assertThat(userDeleted.isEmpty()).isTrue();
    }

    public void createTwoUsers() throws Exception {
        createOneUser();
        User user2 = getDummyUser2();
        userRepository.save(user2);
    }

    public User getDummyUser1() {
        User user = new User();
        user.setId(1);
        user.setUsername("Username 1");
        user.setPassword("Password 1");
        user.setFullname("Full name 1");
        user.setRole("ADMIN");
        return user;
    }

    public User getDummyUser2() {
        User user = new User();
        user.setId(2);
        user.setUsername("Username 2");
        user.setPassword("Password 2");
        user.setFullname("Full name 2");
        user.setRole("USER");
        return user;
    }
}