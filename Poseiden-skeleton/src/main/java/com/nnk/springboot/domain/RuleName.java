package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rulename")
public class RuleName {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer id;
    @NotEmpty(message = "Name is mandatory")
    String name;
    @NotEmpty(message = "Description is mandatory")
    String description;
    @NotEmpty(message = "Json is mandatory")
    String json;
    @NotEmpty(message = "Template is mandatory")
    String template;
    @NotEmpty(message = "Sql Str is mandatory")
    @Column(name = "sql_str")
    String sqlStr;
    @NotEmpty(message = "Sql Part is mandatory")
    @Column(name = "sql_part")
    String sqlPart;
}