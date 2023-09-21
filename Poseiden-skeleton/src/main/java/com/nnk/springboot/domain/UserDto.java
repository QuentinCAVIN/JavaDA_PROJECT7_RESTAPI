package com.nnk.springboot.domain;

import com.nnk.springboot.config.ValidPassword;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Integer id;
    @NotEmpty(message = "Username is mandatory")
    private String username;
    @NotEmpty(message = "Password is mandatory")
    @ValidPassword
    private String password;
    @NotEmpty(message = "FullName is mandatory")
    private String fullname;
    @NotEmpty(message = "Role is mandatory")
    private String role;
}