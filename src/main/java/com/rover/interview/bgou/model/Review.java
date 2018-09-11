package com.rover.interview.bgou.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "review",
       indexes = {@Index(name = "sitterEmail_idx",
                         columnList = "sitterEmail"), @Index(name = "ownerEmail_idx",
                                                             columnList = "ownerEmail")})
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String text;
    private Date start_date;
    private Date end_date;
    private int rating;
    private String dogs;
    private String ownerEmail;
    private String sitterEmail;
}
