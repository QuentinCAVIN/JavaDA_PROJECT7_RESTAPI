package com.nnk.springboot.unit.controllers;

import com.nnk.springboot.controllers.UserController;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;
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
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;


    @Test
    @WithMockUser
    public void homeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/list"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("users"));
    }

    @Test
    public void addUserFormTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/add"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/add"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));
    }

    @Test
    public void validateTestWithValidObject() throws Exception {
        User dummyUser = getDummyUser();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/validate")
                        .param("username",dummyUser.getUsername())
                        .param("password",dummyUser.getPassword())
                        .param("fullname",dummyUser.getFullname())
                        .param("role",dummyUser.getRole()))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/user/list"));
        Mockito.verify(userService,Mockito.times(1))
                .saveUser(ArgumentMatchers.any(User.class));
    }

    @Test
    public void validateTestWithWrongObject() throws Exception {
        User dummyUser = getDummyUser();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/validate")
                .param("password","Wrong password"))

                .andExpect(MockMvcResultMatchers.view().name("user/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Username is mandatory")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Invalid Password")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));
    }

    @Test
    public void validateTestWithUserAlreadyRegistered() throws Exception {
        User dummyUser = getDummyUser();
        Mockito.when(userService.getUserByUsername(dummyUser.getUsername())).thenReturn(dummyUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/validate")
                .param("username",dummyUser.getUsername())
                .param("password",dummyUser.getPassword())
                .param("fullname",dummyUser.getFullname())
                .param("role",dummyUser.getRole()))

                .andExpect(MockMvcResultMatchers.view().name("user/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("This username is already used")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));
    }

    @Test
    public void showUpdateFormTest() throws Exception{
        User dummyUser = getDummyUser();
        Mockito.when(userService.getUserById(dummyUser.getId())).thenReturn(Optional.of(dummyUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/update/1"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/update"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));
    }

    @Test
    public void updateUserTestWithValidObject() throws Exception {
        User dummyUser = getDummyUser();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/update/1")
                        .param("username",dummyUser.getUsername())
                        .param("password",dummyUser.getPassword())
                        .param("fullname",dummyUser.getFullname())
                        .param("role",dummyUser.getRole()))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/user/list"));
        Mockito.verify(userService,Mockito.times(1))
                .saveUser(ArgumentMatchers.any(User.class));
    }

    @Test
    public void updateUserTestWithWrongObject() throws Exception {
        User dummyUser = getDummyUser();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/update/1")
                        .param("password","Wrong password"))

                .andExpect(MockMvcResultMatchers.view().name("user/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Username is mandatory")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Invalid Password")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));
    }

    @Test
    public void updateUserTestWithUserAlreadyRegistered() throws Exception {
        User dummyUser = getDummyUser();
        User dummyUserAlreadyRegistered = getDummyUser();
        dummyUserAlreadyRegistered.setUsername("Not dummyUser username");
        Mockito.when(userService.getUserByUsername(dummyUser.getUsername())).thenReturn(dummyUser);
        Mockito.when(userService.getUserById(dummyUser.getId())).thenReturn(Optional.of(dummyUserAlreadyRegistered));

        mockMvc.perform(MockMvcRequestBuilders.post("/user/update/1")
                        .param("username",dummyUser.getUsername())
                        .param("password",dummyUser.getPassword())
                        .param("fullname",dummyUser.getFullname())
                        .param("role",dummyUser.getRole()))

                .andExpect(MockMvcResultMatchers.view().name("user/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("This username is already used")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));
    }

    @Test
    public void deleteUserTest() throws Exception {
        User dummyUser = getDummyUser();
        Mockito.when(userService.getUserById(dummyUser.getId())).thenReturn(Optional.of(dummyUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/delete/1"))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/user/list"));
        Mockito.verify(userService,Mockito.times(1))
                .deleteUser(ArgumentMatchers.any(User.class));
    }

    public User getDummyUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("Username");
        user.setPassword("Password1!");
        user.setFullname("Full name");
        user.setRole("ADMIN");
        return user;
    }
}