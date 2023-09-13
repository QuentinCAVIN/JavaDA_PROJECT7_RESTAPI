package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    @NotBlank(message = "Username is mandatory")
    @NotEmpty(message = "Username is mandatory")
    private String username;
    @NotEmpty(message = "Password is mandatory")
    @NotNull(message = "Password is mandatory")
    private String password;
    @NotEmpty(message = "FullName is mandatory")
    private String fullname;
    @NotEmpty(message = "Role is mandatory")
    @NotNull(message = "Role is mandatory")
    private String role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}