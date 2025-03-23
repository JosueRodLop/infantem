package com.isppG8.infantem.infantem.dream;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DreamRepository extends JpaRepository<Dream, Long> {

    @Query("SELECT DISTINCT FUNCTION('DATE', d.dateStart) FROM Dream d WHERE d.baby.id = ?1 AND d.dateStart BETWEEN ?2 AND ?3")
    List<Date> findDreamDatesByBabyIdAndDate(Integer babyId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT d FROM Dream d WHERE d.baby.id = ?1 AND d.dateStart BETWEEN ?2 AND ?3")
    List<Dream> findDreamSummaryByBabyIdAndDate(Integer babyId, LocalDateTime start, LocalDateTime end);
}
