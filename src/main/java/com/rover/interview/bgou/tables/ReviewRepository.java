package com.rover.interview.bgou.tables;

import com.rover.interview.bgou.model.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface ReviewRepository extends CrudRepository<Review, Long> {
    int countReviewsBySitterEmail(String email);

    @Query(value = "SELECT sum(rating) as totalRating FROM review WHERE sitter_email = ?1",
           nativeQuery = true)
    long sumRatingsBySitterEmail(String email);
}
