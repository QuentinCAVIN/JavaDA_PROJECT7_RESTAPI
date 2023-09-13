package com.nnk.springboot.domain;

/*import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;*/

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @GeneratedValue(strategy= GenerationType.AUTO)
    Integer id;
    @Column(name = "moodys_rating")
    @NotBlank(message = "Moddy's rating is mandatory")
    String moodysRating;
    @Column(name = "sand_p_rating")
    @NotBlank(message = "S&P rating is mandatory")
    String sandPRating;
    @Column(name = "fitch_rating")
    @NotBlank(message = "Fitch Rating is mandatory")
    String fitchRating;
    @Column(name = "order_number")
    @NotBlank(message = "Order number is mandatory")
    Integer orderNumber;
}
