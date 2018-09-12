package com.rover.interview.bgou.tables;

import com.rover.interview.bgou.model.Owner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface OwnerRepository extends CrudRepository<Owner, Long> {}
