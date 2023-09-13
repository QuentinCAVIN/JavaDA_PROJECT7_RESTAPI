package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {
    @Autowired
    RatingRepository ratingRepository;
    public List<Rating> getRatings(){
        return ratingRepository.findAll();
    }

    public void saveRating (Rating rating){
        ratingRepository.save(rating);
    }

    public Optional<Rating> getRatingById (int id) {
        return ratingRepository.findById(id);
    }

    public void deleteRating(Rating rating) {
        ratingRepository.delete(rating);
    }

}