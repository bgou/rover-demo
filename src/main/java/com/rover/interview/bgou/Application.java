package com.rover.interview.bgou;

import com.rover.interview.bgou.service.RestoreDataService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Log4j2
public class Application {

    @Autowired
    RestoreDataService restoreDataService;

    @RequestMapping("/")
    public String home() {
        restoreDataService.recover();
        return "Hello Docker World";
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
