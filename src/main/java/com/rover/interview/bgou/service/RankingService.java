package com.rover.interview.bgou.service;

import com.rover.interview.bgou.model.Sitter;
import com.rover.interview.bgou.tables.ReviewRepository;
import com.rover.interview.bgou.tables.SitterRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RankingService {
    private static final int RANKING_REVIEW_COUNT_THRESHOLD = 10;

    @Autowired
    private SitterRepository sitterRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public void calculateAllSitterRanking() {
        for (Sitter sitter : sitterRepository.findAll()) {
            calculateSitterRanking(sitter);
        }
    }

    public void calculateSitterRanking(Sitter sitter) {
        String sitterEmail = sitter.getEmail();

        // Ratings Score is the average of their stay ratings.
        long reviewCount = reviewRepository.countReviewsBySitterEmail(sitterEmail);
        long totalRating = reviewRepository.sumRatingsBySitterEmail(sitterEmail);
        log.info("{} has {} reviews, total rating: {}", sitterEmail, reviewCount, totalRating);

        float rating = (float) totalRating / (float) reviewCount;
        log.info("Updating sitter {}'s rating to {}", sitterEmail, rating);
        sitter.setRating(rating);

        // The Overall Sitter Rank is a weighted average of the Sitter Score and Ratings Score,
        // weighted by the number of stays.
        // When a sitter has no stays, their Overall Sitter Rank is equal to the Sitter Score.
        // When a sitter has 10 or more stays, their Overall Sitter Rank is equal to the Ratings Score.

        if (reviewCount < RANKING_REVIEW_COUNT_THRESHOLD) {
            float reviewRatingsPercentage = (float) reviewCount / RANKING_REVIEW_COUNT_THRESHOLD;
            float sitterScorePercentage = 1.0f - reviewRatingsPercentage;
            log.info("Ranking sitterScore vs reviewRating: {}/{}", sitterScorePercentage, reviewRatingsPercentage);
            sitter.setRank(
                    sitterScorePercentage * sitter.getScore() + reviewRatingsPercentage * sitter.getRating());
        } else {
            // review count > RANKING_REVIEW_COUNT_THRESHOLD
            log.info("More than 10 reviews for {}, using rating {}", sitterEmail, sitter.getRating());
            sitter.setRank(sitter.getRating());
        }

        log.info("Final ranking for {} is {}", sitterEmail, sitter.getRank());

        // updating sitter info
        sitterRepository.save(sitter);
    }
}
