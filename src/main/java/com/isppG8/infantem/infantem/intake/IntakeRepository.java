package com.isppG8.infantem.infantem.intake;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IntakeRepository extends JpaRepository<Intake, Long> {

    @Query("SELECT DISTINCT i.date FROM Intake i WHERE i.baby.id = ?1 AND i.date BETWEEN ?2 AND ?3")
    List<LocalDateTime> getIntakesByBabyIdAndDate(Integer babyId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT i FROM Intake i WHERE i.baby.id = ?1 AND i.date BETWEEN ?2 AND ?3")
    List<Intake> getIntakeSummaryByBabyIdAndDate(Integer babyId, LocalDateTime start, LocalDateTime end);

}
