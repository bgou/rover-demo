package com.rover.interview.bgou.cli;

import com.rover.interview.bgou.service.RestoreDataService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class InitializeDatabase implements CommandLineRunner {

    @Autowired
    RestoreDataService restoreDataService;

    @Override
    public void run(String... args) {
        restoreDataService.recover();
        log.info("Database Initialized");
    }
}
