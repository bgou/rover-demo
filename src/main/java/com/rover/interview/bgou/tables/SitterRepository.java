package com.rover.interview.bgou.tables;

import com.rover.interview.bgou.model.Sitter;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface SitterRepository extends PagingAndSortingRepository<Sitter, Long> {}
