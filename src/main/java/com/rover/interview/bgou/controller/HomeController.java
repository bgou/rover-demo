package com.rover.interview.bgou.controller;

import com.rover.interview.bgou.service.RestoreDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @Autowired
    RestoreDataService restoreDataService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/init")
    @ResponseBody
    public String initialize() {
        restoreDataService.recover();
        return "Database Initialized";
    }
}
