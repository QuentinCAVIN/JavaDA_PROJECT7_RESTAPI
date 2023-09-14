package com.nnk.springboot.domain;

/*import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;*/

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer id;
    @Column(name = "moodys_rating")
    @NotEmpty(message = "Moddy's rating is mandatory")
    String moodysRating;
    @Column(name = "sand_p_rating")
    @NotEmpty(message = "S&P rating is mandatory")
    String sandPRating;
    @Column(name = "fitch_rating")
    @NotEmpty(message = "Fitch Rating is mandatory")
    String fitchRating;
    @Column(name = "order_number")
    @NotNull(message = "Order number is mandatory")
    Integer orderNumber;
}