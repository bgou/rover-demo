package com.rover.interview.bgou.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Data
@Builder
@Entity
@Table(name = "owner",
       indexes = {
        @Index(name = "email_idx", columnList = "email", unique = true),
        @Index(name = "name_idx", columnList = "name")}
)
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String image;
}
