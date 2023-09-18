package com.nnk.springboot.unit.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userServiceUnderTest;

    @Test
    public void getUsersTest() {
        User dummyUser = getDummyUser();
        List<User> dummyUserList = new ArrayList<>(Arrays.asList(dummyUser,dummyUser));
        Mockito.when(userRepository.findAll()).thenReturn(dummyUserList);

        List<User> users = userServiceUnderTest.getUsers();

        Assertions.assertThat(users.size()).isEqualTo(2);
        Assertions.assertThat(users.get(0)).isEqualTo(dummyUser);
        Assertions.assertThat(users.get(1)).isEqualTo(dummyUser);
    }

    @Test
    public void saveUserTest() {
        User dummyUser = getDummyUser();

        userServiceUnderTest.saveUser(dummyUser);

        Mockito.verify(userRepository, Mockito.times(1)).save(dummyUser);
    }

    @Test
    public void getUserByIdTest() {
        User dummyUser = getDummyUser();
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(dummyUser));

        User user = userServiceUnderTest.getUserById(1).get();

        Assertions.assertThat(user).isEqualTo(dummyUser);
    }

    @Test
    public void getUserByUsernameTest() {
        User dummyUser = getDummyUser();
        Mockito.when(userRepository.findByUsername(dummyUser.getUsername())).thenReturn(dummyUser);

        User user = userServiceUnderTest.getUserByUsername(dummyUser.getUsername());

        Assertions.assertThat(user).isEqualTo(dummyUser);
    }

    @Test
    public void deleteUserTest() {
        User dummyUser = getDummyUser();

        userServiceUnderTest.deleteUser(dummyUser);

        Mockito.verify(userRepository, Mockito.times(1)).delete(dummyUser);
    }

    public User getDummyUser() {
        User user = new User();
        user.setUsername("Username");
        user.setPassword("Password");
        user.setFullname("Full name");
        user.setRole("ADMIN");
        return user;
    }
}