package com.nnk.springboot.integration;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;

import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class RatingIT {

	@Autowired
	private RatingRepository ratingRepository;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	public void setUp() {
		ratingRepository.deleteAll();
		jdbcTemplate.execute("TRUNCATE TABLE rating RESTART IDENTITY;");
		// Le script SQL réinitialise l'incrémentation de l'id dans la table
		// Sans ça les "findById" déconnent

	}
	@DisplayName("validateForm_WithValidRating_ShouldSaveRatingToDatabase")
	@Test
	@WithMockUser(authorities = "USER")
	public void createOneRating() throws Exception {
		Rating dummyRating1 = getDummyRating1();

		mockMvc.perform(MockMvcRequestBuilders.post("/rating/validate")
						.param("moodysRating",dummyRating1.getMoodysRating())
						.param("sandPRating",dummyRating1.getSandPRating())
						.param("fitchRating",dummyRating1.getFitchRating())
						.param("orderNumber",String.valueOf(dummyRating1.getOrderNumber())));

		Rating rating = ratingRepository.findById(dummyRating1.getId()).get();

		Assertions.assertThat(rating.getId()).isNotNull();
		Assertions.assertThat(rating.getMoodysRating()).isEqualTo(dummyRating1.getMoodysRating());
		Assertions.assertThat(rating.getSandPRating()).isEqualTo(dummyRating1.getSandPRating());
		Assertions.assertThat(rating.getFitchRating()).isEqualTo(dummyRating1.getFitchRating());
		Assertions.assertThat(rating.getOrderNumber()).isEqualTo(dummyRating1.getOrderNumber());
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void showRatingHome_WithRatingsSaved_ShouldDisplayRatingsFromDatabase() throws Exception {
		createTwoRatings();
		Rating dummyRating1 = getDummyRating1();
		Rating dummyRating2 = getDummyRating2();

		mockMvc.perform(MockMvcRequestBuilders.get("/rating/list"))
				.andExpect(MockMvcResultMatchers.content()
						.string(CoreMatchers.containsString(dummyRating1.getMoodysRating())))
						.andExpect(MockMvcResultMatchers.content()
				.string(CoreMatchers.containsString(dummyRating2.getMoodysRating())));
	}


	@Test
	@WithMockUser(authorities = "USER")
	public void UpdateRating_WithValidRating_ShouldSaveRatingToDatabase() throws Exception {
		createOneRating();
		Rating rating = ratingRepository.findById(1).get();

		mockMvc.perform(MockMvcRequestBuilders.post("/rating/update/1")
				.param("moodysRating",rating.getMoodysRating())
				.param("sandPRating",rating.getSandPRating())
				.param("fitchRating",rating.getFitchRating())
				.param("orderNumber", "8"));

		Rating ratingUpdate = ratingRepository.findById(1).get();
		Assertions.assertThat(rating.getOrderNumber()).isNotEqualTo(8);
		Assertions.assertThat(ratingUpdate.getOrderNumber()).isEqualTo(8);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void deleteRating_ShouldDeleteRatingToDatabase() throws Exception {
		createOneRating();
		Optional<Rating> rating = ratingRepository.findById(1);

		mockMvc.perform(MockMvcRequestBuilders.get("/rating/delete/1"));

		Optional <Rating> ratingDeleted = ratingRepository.findById(1);

		Assertions.assertThat(rating.isPresent()).isTrue();
		Assertions.assertThat(ratingDeleted.isEmpty()).isTrue();
	}

	public void createTwoRatings() throws Exception {
		createOneRating();
		Rating rating2 = getDummyRating2();
		ratingRepository.save(rating2);
	}

	public Rating getDummyRating1() {
		Rating rating = new Rating();
		rating.setId(1);
		rating.setMoodysRating("Moodys Rating 1");
		rating.setSandPRating("Sand PRating 1");
		rating.setFitchRating("Fitch Rating 1");
		rating.setOrderNumber(10);
		return rating;
	}

	public Rating getDummyRating2() {
		Rating rating = new Rating();
		rating.setId(2);
		rating.setMoodysRating("Moodys Rating 2");
		rating.setSandPRating("Sand PRating 2");
		rating.setFitchRating("Fitch Rating 2");
		rating.setOrderNumber(9);
		return rating;
	}
}