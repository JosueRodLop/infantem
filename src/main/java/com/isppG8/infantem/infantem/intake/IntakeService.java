package com.isppG8.infantem.infantem.intake;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Date> getIntakesByBabyIdAndDate(Integer babyId, Date start, Date end) {
        LocalDateTime startDateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endDateTime = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return intakeRepository.getIntakesByBabyIdAndDate(babyId, startDateTime, endDateTime);
    }
}
