package com.isppG8.infantem.infantem.intake;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IntakeRepository extends JpaRepository<Intake, Long> {

    @Query("SELECT DISTINCT FUNCTION('DATE', i.date) FROM Intake i WHERE i.baby.id = ?1 AND i.date BETWEEN ?2 AND ?3")
    List<Date> getIntakesByBabyIdAndDate(Integer babyId, LocalDateTime start, LocalDateTime end);

}
