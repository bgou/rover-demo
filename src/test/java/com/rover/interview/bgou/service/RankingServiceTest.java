package com.rover.interview.bgou.service;

import com.google.common.collect.Lists;
import com.rover.interview.bgou.model.Review;
import com.rover.interview.bgou.model.Sitter;
import com.rover.interview.bgou.tables.ReviewRepository;
import com.rover.interview.bgou.tables.SitterRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RankingServiceTest {
    @Mock
    private SitterRepository sitterRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    @Spy
    private RankingService testRankingService;
    private Sitter testSitter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        testSitter = Sitter.builder().name("Laura B.").score(2.50f).build();
    }

    @Test
    public void verify_sitterRepo_findAll_isCalled() throws Exception {
        testRankingService.calculateAllSitterRanking();
        verify(sitterRepository).findAll();
    }

    @Test
    public void verify_all_sitters_are_iterated_over() throws Exception {
        Sitter sitter1 = Sitter.builder().name("a").build();
        Sitter sitter2 = Sitter.builder().name("b").build();
        List<Sitter> sitterIterator = Lists.newArrayList(sitter1, sitter2);

        when(sitterRepository.findAll()).thenReturn(sitterIterator);
        testRankingService.calculateAllSitterRanking();

        verify(testRankingService).calculateSitterRanking(eq(sitter1));
        verify(testRankingService).calculateSitterRanking(eq(sitter2));
    }

    @Test
    public void verify_0_reviews_sets_rating_to_0() throws Exception {
        Random r = new Random();
        float randomScore = r.nextFloat();
        testSitter = Sitter.builder().score(randomScore).build();
        try {
            when(reviewRepository.countReviewsBySitterEmail(anyString())).thenReturn(0L);
            when(reviewRepository.sumRatingsBySitterEmail(anyString())).thenReturn(0L);

            testRankingService.calculateSitterRanking(testSitter);

            assertThat(testSitter.getRating(), Matchers.is(0.0F));
        } catch (Exception e) {
            Assert.fail("THere shouldn't be any exception, but caught " + e);
        }
    }

    @Test
    public void verify_rating_is_average_of_stay_ratings() throws Exception {
        Random r = new Random();
        long randomReviewCount = r.nextInt(20);
        if (randomReviewCount == 0) randomReviewCount++;
        long randomReviewRatingSum = randomReviewCount + 1;
        testSitter = Sitter.builder().build();
        try {
            when(reviewRepository.countReviewsBySitterEmail(anyString())).thenReturn(15L);
            when(reviewRepository.sumRatingsBySitterEmail(anyString())).thenReturn(randomReviewRatingSum);

            testRankingService.calculateSitterRanking(testSitter);

            float expected = (float)randomReviewRatingSum / randomReviewCount;
            assertThat(testSitter.getRating(), Matchers.is(expected));
        } catch (Exception e) {
            Assert.fail("THere shouldn't be any exception, but caught " + e);
        }
    }

    @Test
    public void verify_calculate_rating() throws Exception {

        List<Review> reviews = new LinkedList<>();
        float[] expectedResults = {2.50f, 2.75f, 3.00f, 3.25f, 3.50f, 3.75f, 4.00f, 4.25f, 4.50f, 4.75f, 5.00f, 5.00f,
                5.00f};

        long reviewSum = 0;
        for (int i = 0; i < 13; i++) {
//            when(reviewRepository.countReviewsBySitterEmail(anyString())).thenReturn(reviews.size());
            when(reviewRepository.sumRatingsBySitterEmail(anyString())).thenReturn(reviewSum);

            testRankingService.calculateSitterRanking(testSitter);
            float expResult = expectedResults[i];
            assertThat(testSitter.getRating(), Matchers.is(expResult));

            reviews.add(Review.builder().rating(5).build());
            reviewSum += 5;
        }
    }
}