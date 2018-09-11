package com.rover.interview.bgou.service;

import com.rover.interview.bgou.model.Sitter;
import com.rover.interview.bgou.model.Sum;
import com.rover.interview.bgou.tables.DogRepository;
import com.rover.interview.bgou.tables.OwnerRepository;
import com.rover.interview.bgou.tables.ReviewRepository;
import com.rover.interview.bgou.tables.SitterRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class RankingService {
    @Autowired
    private SitterRepository sitterRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public void calculateSitterRating() {
        // Ratings Score is the average of their stay ratings.
        for (Sitter sitter : sitterRepository.findAll()) {
            String sitterEmail = sitter.getEmail();
            long reviewCount = reviewRepository.countReviewsBySitterEmail(sitterEmail);
            long totalRating = reviewRepository.sumRatingsBySitterEmail(sitterEmail);
            log.info("{} has {} reviews, total rating: {}", sitterEmail, reviewCount, totalRating);
            float rating = (float)totalRating / (float)reviewCount;
            log.info("Updating sitter {}'s rating to {}", sitterEmail, rating);
            sitter.setRating(rating);
            sitterRepository.save(sitter);
        }
    }
}
