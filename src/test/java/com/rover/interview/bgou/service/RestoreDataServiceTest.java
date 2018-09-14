package com.rover.interview.bgou.service;

import com.google.common.collect.Lists;
import com.rover.interview.bgou.model.CsvEntry;
import com.rover.interview.bgou.model.Dog;
import com.rover.interview.bgou.model.Owner;
import com.rover.interview.bgou.model.Sitter;
import com.rover.interview.bgou.tables.DogRepository;
import com.rover.interview.bgou.tables.OwnerRepository;
import com.rover.interview.bgou.tables.ReviewRepository;
import com.rover.interview.bgou.tables.SitterRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

public class RestoreDataServiceTest {

    @Mock
    private SitterRepository sitterRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private DogRepository dogRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private RankingService rankingService;

    @InjectMocks
    @Spy
    private RestoreDataService testRestoreDataService;
    private final CsvEntry testReviewEntry = new CsvEntry();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void verify_csv_parsing_returns_CsvEntry_list() throws Exception {
        List<CsvEntry> result = testRestoreDataService.getCsvEntries("test_reviews.csv");
        assertThat(result, Matchers.notNullValue());
        assertThat(result.size(), Matchers.is(1));
    }

    @Test
    public void verify_invalid_filename_returns_null() throws Exception {
        List<CsvEntry> result = testRestoreDataService.getCsvEntries("invalid.csv");
        assertThat(result, Matchers.nullValue());
    }

    @Test
    public void verify_recover_calls_write_to_database() throws Exception {
        List<CsvEntry> mockEntries = new ArrayList<>();
        Mockito.doReturn(mockEntries).when(testRestoreDataService).getCsvEntries(anyString());

        testRestoreDataService.recover();

        verify(sitterRepository).saveAll(anySet());
        verify(ownerRepository).saveAll(anySet());
        verify(dogRepository).saveAll(anySet());
        verify(reviewRepository).saveAll(anySet());
    }

    @Test
    public void verify_recover_calls_rankingService_calculateAllSitterRanking() throws Exception {
        List<CsvEntry> mockEntries = new ArrayList<>();
        Mockito.doReturn(mockEntries).when(testRestoreDataService).getCsvEntries(anyString());

        testRestoreDataService.recover();

        verify(rankingService).calculateAllSitterRanking();
    }

    private void initializeTestCsvEntry() {
        testReviewEntry.setRating(5);
        testReviewEntry.setStart_date(Date.valueOf("2013-02-26"));
        testReviewEntry.setEnd_date(Date.valueOf("2013-04-08"));
        testReviewEntry.setText("description");

        testReviewEntry.setOwner("Shelli K.");
        testReviewEntry.setOwner_phone_number("+15817557107");
        testReviewEntry.setOwner_email("user2555@verizon.net");
        testReviewEntry.setOwner_image("https://images.dog.ceo/breeds/hound-ibizan/n02091244_327.jpg");
        testReviewEntry.setDogs(Lists.newArrayList("Pinot", "Grigio"));

        testReviewEntry.setSitter("Lauren B.");
        testReviewEntry.setSitter_image("https://images.dog.ceo/breeds/dalmatian/cooper2.jpg");
        testReviewEntry.setSitter_phone_number("+12546478758");
        testReviewEntry.setSitter_email("user4739@gmail.com");
    }

    @Test
    public void verify_processReview_extracts_sitter_info() throws Exception {
        initializeTestCsvEntry();

        List<CsvEntry> mockEntries = Lists.newArrayList(testReviewEntry);
        Mockito.doReturn(mockEntries).when(testRestoreDataService).getCsvEntries(anyString());

        testRestoreDataService.recover();

        Set<Sitter> sitterSet = testRestoreDataService.getSitterSet();
        assertThat(sitterSet.size(), Matchers.is(1));
        Sitter actual = sitterSet.iterator().next();

        assertThat(actual.getName(), Matchers.is(testReviewEntry.getSitter()));
        assertThat(actual.getImage(), Matchers.is(testReviewEntry.getSitter_image()));
        assertThat(actual.getPhone(), Matchers.is(testReviewEntry.getSitter_phone_number()));
        assertThat(actual.getEmail(), Matchers.is(testReviewEntry.getSitter_email()));
    }

    @Test
    public void verify_processReview_extracts_owner_info() throws Exception {
        initializeTestCsvEntry();

        List<CsvEntry> mockEntries = Lists.newArrayList(testReviewEntry);
        Mockito.doReturn(mockEntries).when(testRestoreDataService).getCsvEntries(anyString());

        testRestoreDataService.recover();

        Set<Owner> ownerSet = testRestoreDataService.getOwnerSet();
        assertThat(ownerSet.size(), Matchers.is(1));
        Owner actual = ownerSet.iterator().next();

        assertThat(actual.getName(), Matchers.is(testReviewEntry.getOwner()));
        assertThat(actual.getImage(), Matchers.is(testReviewEntry.getOwner_image()));
        assertThat(actual.getPhone(), Matchers.is(testReviewEntry.getOwner_phone_number()));
        assertThat(actual.getEmail(), Matchers.is(testReviewEntry.getOwner_email()));
    }

    @Test
    public void verify_processReview_extracts_dog_info() throws Exception {
        initializeTestCsvEntry();

        List<CsvEntry> mockEntries = Lists.newArrayList(testReviewEntry);
        Mockito.doReturn(mockEntries).when(testRestoreDataService).getCsvEntries(anyString());

        testRestoreDataService.recover();

        Set<Dog> dogSet = testRestoreDataService.getDogSet();
        assertThat(dogSet.size(), Matchers.is(2));
        dogSet.forEach(actual -> {
            assertThat(actual.getName(), Matchers.isIn(testReviewEntry.getDogs()));
            assertThat(actual.getOwnerEmail(), Matchers.is(testReviewEntry.getOwner_email()));
        });
    }
}