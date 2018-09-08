package com.rover.interview.bgou.tables;

import com.rover.interview.bgou.model.Dog;
import org.springframework.data.repository.CrudRepository;

public interface DogRepository extends CrudRepository<Dog, Long> {}
