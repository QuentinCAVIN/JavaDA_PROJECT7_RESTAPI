package com.nnk.springboot.mapper;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.UserDto;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static User convertUserDtoToUser(UserDto userDto) {

        User user = new User();

        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setFullname(userDto.getFullname());
        user.setRole(userDto.getRole());
        user.setPassword(userDto.getPassword());

        return user;
    }

    public static UserDto convertUserToUserDto(User user) {

        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setFullname(user.getFullname());
        userDto.setRole(user.getRole());
        userDto.setPassword(user.getPassword());

        return userDto;
    }

    public static List<UserDto> convertUserListToUserDtoList (List<User> users) {
        List<UserDto> usersDto= new ArrayList<>();

        users.forEach(user -> usersDto.add(convertUserToUserDto(user)));

        return usersDto;
    }
}