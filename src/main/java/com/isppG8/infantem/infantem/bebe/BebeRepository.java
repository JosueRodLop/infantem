package com.isppG8.infantem.infantem.bebe;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BebeRepository extends CrudRepository<Bebe, Integer> {

}
