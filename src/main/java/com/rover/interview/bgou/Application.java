package com.rover.interview.bgou;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.rover.interview.bgou.model.CsvEntry;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@SpringBootApplication
@RestController
@Log4j2
public class Application {

    @RequestMapping("/")
    @SuppressWarnings("unchecked")
    public String home() {
        InputStreamReader reader = null;
        try {
            InputStream inputStream = new ClassPathResource("reviews.csv").getInputStream();
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
        return "Hello Docker World";
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
