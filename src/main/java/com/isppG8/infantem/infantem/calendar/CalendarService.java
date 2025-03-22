package com.isppG8.infantem.infantem.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isppG8.infantem.infantem.disease.DiseaseService;
import com.isppG8.infantem.infantem.dream.DreamService;
import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.vaccine.VaccineService;

@Service
public class CalendarService {

    private DreamService dreamService;
    private DiseaseService diseaseService;
    private VaccineService vaccineService;

    @Autowired
    public CalendarService(DreamService dreamService, DiseaseService diseaseService, VaccineService vaccineService) {
        this.dreamService = dreamService;
        this.diseaseService = diseaseService;
        this.vaccineService = vaccineService;

    }

    public List<Calendar> getCalendarByUserId(User user, Date start, Date end) {
        List<Integer> babiesId = user.getBabies().stream().map(baby -> baby.getId()).toList();

        List<Calendar> calendar = new ArrayList<>();

        for (Integer babyId : babiesId) {
            Calendar babyCalendar = new Calendar(babyId);
            // Check dream events
            List<Date> dreamDates = this.dreamService.getDreamsByBabyIdAndDate(babyId, start, end);
            babyCalendar.addDreamEvents(dreamDates);

            // Check diseases events
            List<Date> diseaseDates = this.diseaseService.getDiseasesByBabyIdAndDate(babyId, start, end);
            babyCalendar.addDiseaseEvents(diseaseDates);

            // Check vaccines events
            List<Date> vaccineDates = this.vaccineService.getVaccinesByBabyIdAndDate(babyId, start, end);
            babyCalendar.addVaccineEvents(vaccineDates);
            calendar.add(babyCalendar);
        }
        return calendar;
    }
}
