package com.rover.interview.bgou.tables;

import com.rover.interview.bgou.model.Owner;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OwnerRepository extends CrudRepository<Owner, Long> {}
