package com.nnk.springboot.unit.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.RatingServiceImpl;
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
public class RatingServiceImplTest {

    @Mock
    private RatingRepository ratingRepository;
    @InjectMocks
    private RatingServiceImpl ratingServiceUnderTest;

    @Test
    public void getRatingsTest() {
        Rating dummyRating = getDummyRating();
        List<Rating> dummyRatingList = new ArrayList<>(Arrays.asList(dummyRating,dummyRating));
        Mockito.when(ratingRepository.findAll()).thenReturn(dummyRatingList);

        List<Rating> ratings = ratingServiceUnderTest.getRatings();

        Assertions.assertThat(ratings.size()).isEqualTo(2);
        Assertions.assertThat(ratings.get(0)).isEqualTo(dummyRating);
        Assertions.assertThat(ratings.get(1)).isEqualTo(dummyRating);
    }

    @Test
    public void saveRatingTest() {
        Rating dummyRating = getDummyRating();

        ratingServiceUnderTest.saveRating(dummyRating);

        Mockito.verify(ratingRepository, Mockito.times(1)).save(dummyRating);
    }

    @Test
    public void getRatingByIdTest() {
        Rating dummyRating = getDummyRating();
        Mockito.when(ratingRepository.findById(1)).thenReturn(Optional.of(dummyRating));

        Rating rating = ratingServiceUnderTest.getRatingById(1).get();

        Assertions.assertThat(rating).isEqualTo(dummyRating);
    }

    @Test
    public void deleteRatingTest() {
        Rating dummyRating = getDummyRating();

        ratingServiceUnderTest.deleteRating(dummyRating);

        Mockito.verify(ratingRepository, Mockito.times(1)).delete(dummyRating);
    }

    public Rating getDummyRating() {
        Rating rating = new Rating();
        rating.setMoodysRating("Moodys Rating");
        rating.setSandPRating("Sand PRating");
        rating.setFitchRating("Fitch Rating");
        rating.setOrderNumber(10);
        return rating;
    }
}