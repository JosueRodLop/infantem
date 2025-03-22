package com.isppG8.infantem.infantem.disease;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DiseaseRepository extends JpaRepository<Disease, Long> {

    @Query("SELECT DISTINCT CAST(d.startDate AS date) FROM Disease d WHERE d.baby.id = ?1 AND d.startDate BETWEEN ?2 AND ?3")
    List<Date> findDiseaseDatesByBabyIdAndDate(Integer babyId, LocalDate start, LocalDate end);

}
