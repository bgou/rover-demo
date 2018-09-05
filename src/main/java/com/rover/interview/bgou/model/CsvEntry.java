package com.rover.interview.bgou.model;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CsvEntry {
    @CsvBindByName(required = true)
    private int rating;

    @CsvBindByName(required = true)
    private String sitter_image;

    @CsvBindByName(required = true)
    @CsvDate("yyyy-MM-dd")
    private Date start_date;

    @CsvBindByName(required = true)
    @CsvDate("yyyy-MM-dd")
    private Date end_date;

    @CsvBindByName(required = true)
    private String text;

    @CsvBindByName(required = true)
    private String owner_image;

    @CsvBindAndSplitByName(elementType = String.class, collectionType = List.class, splitOn = "\\|")
    private List<String> dogs;

    @CsvBindByName(required = true)
    private String sitter;

    @CsvBindByName(required = true)
    private String owner;

    @CsvBindByName(required = true)
    private String sitter_phone_number;

    @CsvBindByName(required = true)
    private String sitter_email;

    @CsvBindByName(required = true)
    private String owner_phone_number;

    @CsvBindByName(required = true)
    private String owner_email;
}
