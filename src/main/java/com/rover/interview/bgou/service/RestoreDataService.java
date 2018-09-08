package com.rover.interview.bgou.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.rover.interview.bgou.model.CsvEntry;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
@Log4j2
public class RestoreDataService {

    private static final String REVIEW_FILENAME = "reviews.csv";
    @SuppressWarnings("unchecked")
    public void recover() {
        InputStreamReader reader = null;
        try {
            InputStream inputStream = new ClassPathResource(REVIEW_FILENAME).getInputStream();
            reader = new InputStreamReader(inputStream);
            CsvToBean<CsvEntry> csvToBean = new CsvToBeanBuilder(reader).withType(CsvEntry.class).build();

            for (CsvEntry entry : csvToBean) {
                log.info(entry);
            }

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
    }
}
