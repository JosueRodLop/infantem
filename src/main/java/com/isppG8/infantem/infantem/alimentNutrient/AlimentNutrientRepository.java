package com.isppG8.infantem.infantem.alimentNutrient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlimentNutrientRepository extends JpaRepository<AlimentNutrient, Long> {
}