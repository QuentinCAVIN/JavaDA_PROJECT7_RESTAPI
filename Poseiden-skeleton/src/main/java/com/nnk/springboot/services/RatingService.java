package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface RatingService {
    List<Rating> getRatings();


   void saveRating (Rating rating);


    Optional<Rating> getRatingById (int id);


    void deleteRating(Rating rating);
    }
