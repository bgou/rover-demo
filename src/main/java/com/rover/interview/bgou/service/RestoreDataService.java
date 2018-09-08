package com.rover.interview.bgou.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.rover.interview.bgou.model.CsvEntry;
import com.rover.interview.bgou.model.Dog;
import com.rover.interview.bgou.model.Owner;
import com.rover.interview.bgou.model.Sitter;
import com.rover.interview.bgou.tables.DogRepository;
import com.rover.interview.bgou.tables.OwnerRepository;
import com.rover.interview.bgou.tables.SitterRepository;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Component
@Log4j2
public class RestoreDataService {

    @Autowired
    private SitterRepository sitterRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private DogRepository dogRepository;

    private static final String REVIEW_FILENAME = "reviews.csv";
    private Set<Sitter> sitterSet = new HashSet<>();
    private Set<Owner> ownerSet = new HashSet<>();
    private Set<Dog> dogSet = new HashSet<>();

    public void recover() {
        List<CsvEntry> entries = getCsvEntries(REVIEW_FILENAME);
        for (CsvEntry reviewEntry : entries) {
            processReview(reviewEntry);
        }
        writeToDatabase();

        retrieve();
    }

    private void retrieve() {
        sitterRepository.findById(1L).ifPresent(sitter -> {
            log.info("Found {}", sitter);
        });
    }

    private void writeToDatabase() {
        sitterRepository.saveAll(sitterSet);
        log.info("Saved {} sitters", sitterSet.size());

        ownerRepository.saveAll(ownerSet);
        log.info("Saved {} owners", ownerSet.size());

        dogRepository.saveAll(dogSet);
        log.info("Saved {} dogs", dogSet.size());
    }

    private void processReview(@NonNull CsvEntry reviewEntry) {
        Sitter sitter = parseSitter(reviewEntry);
        sitterSet.add(sitter);

        Owner owner = parseOwner(reviewEntry);
        ownerSet.add(owner);

        List<Dog> dogs = parseDog(owner, reviewEntry.getDogs());
        dogSet.addAll(dogs);
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

    @SuppressWarnings("unchecked")
    public List<CsvEntry> getCsvEntries(String filename) {
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
