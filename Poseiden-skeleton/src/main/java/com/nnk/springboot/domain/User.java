package com.nnk.springboot.domain;

import com.nnk.springboot.config.ValidPassword;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;



@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Username is mandatory")
    @Column(unique = true)
    private String username;
    @NotEmpty(message = "Password is mandatory")
    @ValidPassword
    private String password;
    @NotEmpty(message = "FullName is mandatory")
    private String fullname;
    @NotEmpty(message = "Role is mandatory")
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