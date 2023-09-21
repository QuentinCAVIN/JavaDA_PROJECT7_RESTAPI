package com.nnk.springboot.unit;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.UserDto;
import com.nnk.springboot.mapper.UserMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class UserMapperTest {

    @Test
    public void convertUsertoUserDtoTest() {
        User user = getDummyUser();
        UserDto userDto = UserMapper.convertUserToUserDto(user);
        Assertions.assertThat(user.getId()).isEqualTo(userDto.getId());
        Assertions.assertThat(user.getFullname()).isEqualTo(userDto.getFullname());
        Assertions.assertThat(user.getUsername()).isEqualTo(userDto.getUsername());
        Assertions.assertThat(user.getPassword()).isEqualTo(userDto.getPassword());
        Assertions.assertThat(user.getRole()).isEqualTo(userDto.getRole());
    }

    @Test
    public void convertUserDtotoUserTest() {
        UserDto userDto = getDummyUserDto();
        User user = UserMapper.convertUserDtoToUser(userDto);
        Assertions.assertThat(userDto.getId()).isEqualTo(user.getId());
        Assertions.assertThat(userDto.getFullname()).isEqualTo(user.getFullname());
        Assertions.assertThat(userDto.getUsername()).isEqualTo(user.getUsername());
        Assertions.assertThat(userDto.getPassword()).isEqualTo(user.getPassword());
        Assertions.assertThat(userDto.getRole()).isEqualTo(user.getRole());
    }

    @Test
    public void convertUserListToUserDtoListTest(){
        User user = getDummyUser();
        List<UserDto> usersDto = UserMapper.convertUserListToUserDtoList(Arrays.asList(user,user));
        Assertions.assertThat(usersDto.size()).isEqualTo(2);
       usersDto.forEach(userDto -> {
           Assertions.assertThat(user.getId()).isEqualTo(userDto.getId());
           Assertions.assertThat(user.getFullname()).isEqualTo(userDto.getFullname());
           Assertions.assertThat(user.getUsername()).isEqualTo(userDto.getUsername());
           Assertions.assertThat(user.getPassword()).isEqualTo(userDto.getPassword());
           Assertions.assertThat(user.getRole()).isEqualTo(userDto.getRole());
       });
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
    public UserDto getDummyUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("Username");
        userDto.setPassword("Password1!");
        userDto.setFullname("Full name");
        userDto.setRole("ADMIN");
        return userDto;
    }
}
