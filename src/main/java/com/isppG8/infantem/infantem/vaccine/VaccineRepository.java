package com.isppG8.infantem.infantem.vaccine;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VaccineRepository extends JpaRepository<Vaccine, Long> {

    @Query("SELECT DISTINCT FUNCTION('DATE', v.vaccinationDate) FROM Vaccine v WHERE v.baby.id = ?1 AND v.vaccinationDate BETWEEN ?2 AND ?3")
    List<Date> getVaccinesByBabyIdAndDate(Integer babyId, LocalDate start, LocalDate end);

}
