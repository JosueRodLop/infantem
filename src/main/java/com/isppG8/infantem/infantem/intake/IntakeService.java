package com.isppG8.infantem.infantem.intake;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isppG8.infantem.infantem.intake.dto.IntakeSummary;

@Service
public class IntakeService {

    @Autowired
    private IntakeRepository intakeRepository;

    public List<Intake> getAllIntakes() {
        return intakeRepository.findAll();
    }

    public Optional<Intake> getIntakeById(Long id) {
        return intakeRepository.findById(id);
    }

    public Intake saveIntake(Intake intake) {
        return intakeRepository.save(intake);
    }

    public void deleteIntake(Long id) {
        intakeRepository.deleteById(id);
    }

    public List<Date> getIntakesByBabyIdAndDate(Integer babyId, LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);
        return intakeRepository.getIntakesByBabyIdAndDate(babyId, startDateTime, endDateTime);
    }

    public List<IntakeSummary> getIntakeSummaryByBabyIdAndDate(Integer babyId, LocalDate day) {
        LocalDateTime start = day.atStartOfDay();
        LocalDateTime end = day.atTime(23, 59, 59);
        List<Intake> intakes = this.intakeRepository.getIntakeSummaryByBabyIdAndDate(babyId, start, end);
        return intakes.stream().map(IntakeSummary::new).toList();
    }
}
