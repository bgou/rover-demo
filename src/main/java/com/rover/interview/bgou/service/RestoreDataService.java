package com.rover.interview.bgou.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.rover.interview.bgou.model.CsvEntry;
import com.rover.interview.bgou.model.Dog;
import com.rover.interview.bgou.model.Owner;
import com.rover.interview.bgou.model.Review;
import com.rover.interview.bgou.model.Sitter;
import com.rover.interview.bgou.tables.DogRepository;
import com.rover.interview.bgou.tables.OwnerRepository;
import com.rover.interview.bgou.tables.ReviewRepository;
import com.rover.interview.bgou.tables.SitterRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
@Log4j2
public class RestoreDataService {

    @Autowired
    private SitterRepository sitterRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RankingService rankingService;

    private static final String REVIEW_FILENAME = "reviews.csv";

    @Getter(AccessLevel.MODULE)
    private Set<Sitter> sitterSet = new HashSet<>();
    @Getter(AccessLevel.MODULE)
    private Set<Owner> ownerSet = new HashSet<>();
    @Getter(AccessLevel.MODULE)
    private Set<Dog> dogSet = new HashSet<>();
    @Getter(AccessLevel.MODULE)
    private Set<Review> reviewSet = new HashSet<>();

    public void recover() {
        List<CsvEntry> entries = getCsvEntries(REVIEW_FILENAME);
        for (CsvEntry reviewEntry : entries) {
            processReview(reviewEntry);
        }
        writeToDatabase();
        rankingService.calculateAllSitterRanking();
    }

    private void writeToDatabase() {
        sitterRepository.saveAll(sitterSet);
        log.info("Saved {} sitters", sitterSet.size());

        ownerRepository.saveAll(ownerSet);
        log.info("Saved {} owners", ownerSet.size());

        dogRepository.saveAll(dogSet);
        log.info("Saved {} dogs", dogSet.size());

        reviewRepository.saveAll(reviewSet);
        log.info("Saved {} reviews", reviewSet.size());
    }

    private void processReview(@NonNull CsvEntry reviewEntry) {
        Sitter sitter = parseSitter(reviewEntry);
        sitterSet.add(sitter);

        Owner owner = parseOwner(reviewEntry);
        ownerSet.add(owner);

        List<Dog> dogs = parseDog(owner, reviewEntry.getDogs());
        dogSet.addAll(dogs);

        Review review = parseReview(reviewEntry);
        reviewSet.add(review);
    }

    private Review parseReview(CsvEntry reviewEntry) {
        return Review.builder()
                     .rating(reviewEntry.getRating())
                     .dogs(StringUtils.join(reviewEntry.getDogs(), ','))
                     .ownerEmail(reviewEntry.getOwner_email())
                     .sitterEmail(reviewEntry.getSitter_email())
                     .text(reviewEntry.getText())
                     .start_date(reviewEntry.getStart_date())
                     .end_date(reviewEntry.getEnd_date())
                     .build();
    }

    private List<Dog> parseDog(Owner owner, List<String> dogs) {
        List<Dog> result = new LinkedList<>();
        for (String dogName : dogs) {
            result.add(Dog.builder().ownerEmail(owner.getEmail()).name(dogName).build());
        }
        return result;
    }

    private Sitter parseSitter(CsvEntry reviewEntry) {
        Sitter sitter = Sitter.builder()
                              .name(reviewEntry.getSitter())
                              .email(reviewEntry.getSitter_email())
                              .image(reviewEntry.getSitter_image())
                              .phone(reviewEntry.getSitter_phone_number())
                              .build();
        sitter.setScore(Sitter.calculateScore(sitter.getName()));
        return sitter;
    }

    private Owner parseOwner(@NonNull CsvEntry reviewEntry) {
        return Owner.builder()
                    .name(reviewEntry.getOwner())
                    .email(reviewEntry.getOwner_email())
                    .phone(reviewEntry.getOwner_phone_number())
                    .image(reviewEntry.getOwner_image())
                    .build();
    }

    /**
     * Opens the defined file (must be on the resource path, and parse lines to list
     * Package private for tests to invoke
     *
     * @param filename file that's on the class resource path
     * @return List of CsvEntry
     */
    @SuppressWarnings("unchecked")
    List<CsvEntry> getCsvEntries(String filename) {
        InputStreamReader reader = null;
        try {
            InputStream inputStream = new ClassPathResource(filename).getInputStream();
            reader = new InputStreamReader(inputStream);
            CsvToBean<CsvEntry> csvToBean = new CsvToBeanBuilder(reader).withType(CsvEntry.class).build();

            // This returns a list which puts all the data in memory.
            // It needs to be changed if we're dealing with streaming input,
            // or if the data is so large that we can't run this on a single machine
            return csvToBean.parse();

        } catch (IOException ex) {
            log.error(ex);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                log.error("Unable to close input reader");
            }
        }
        return null;
    }
}
