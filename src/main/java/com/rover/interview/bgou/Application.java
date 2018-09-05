package com.rover.interview.bgou;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.bean.CsvToBeanBuilder;
import com.rover.interview.bgou.model.CsvEntry;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
@Log4j2
public class Application {

    @RequestMapping("/")
    public String home() throws Exception{
        File file = new ClassPathResource("reviews.csv").getFile();
        List<CsvEntry> beans = new CsvToBeanBuilder(new FileReader(file))
                .withType(CsvEntry.class).build().parse();
        for (CsvEntry entry : beans) {
            log.info(entry);
        }
//
//        CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(file));
//        Map<String, String> line;
//        while((line = reader.readMap()) != null) {
//            line.forEach((key, val) -> {
//                log.info("{}: {}", key, val);
//            });
//        }
        return "Hello Docker World";
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
