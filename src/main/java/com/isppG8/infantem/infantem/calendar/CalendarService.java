package com.isppG8.infantem.infantem.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isppG8.infantem.infantem.calendar.dto.CalendarDay;
import com.isppG8.infantem.infantem.calendar.dto.CalendarEvents;
import com.isppG8.infantem.infantem.disease.DiseaseService;
import com.isppG8.infantem.infantem.disease.dto.DiseaseSummary;
import com.isppG8.infantem.infantem.dream.DreamService;
import com.isppG8.infantem.infantem.dream.dto.DreamSummary;
import com.isppG8.infantem.infantem.intake.IntakeService;
import com.isppG8.infantem.infantem.metric.MetricService;
import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.vaccine.VaccineService;

@Service
public class CalendarService {

    private DreamService dreamService;
    private DiseaseService diseaseService;
    private VaccineService vaccineService;
    private IntakeService intakeService;
    private MetricService metricService;

    @Autowired
    public CalendarService(DreamService dreamService, DiseaseService diseaseService, VaccineService vaccineService,
            IntakeService intakeService, MetricService metricService) {
        this.dreamService = dreamService;
        this.diseaseService = diseaseService;
        this.vaccineService = vaccineService;
        this.intakeService = intakeService;
        this.metricService = metricService;

    }

    public List<CalendarEvents> getCalendarByUserId(User user, Date start, Date end) {
        List<Integer> babiesId = user.getBabies().stream().map(baby -> baby.getId()).toList();

        List<CalendarEvents> calendar = new ArrayList<>();

        for (Integer babyId : babiesId) {
            CalendarEvents babyCalendar = new CalendarEvents(babyId);
            // Check dream events
            List<Date> dreamDates = this.dreamService.getDreamsByBabyIdAndDate(babyId, start, end);
            babyCalendar.addDreamEvents(dreamDates);

            // Check diseases events
            List<Date> diseaseDates = this.diseaseService.getDiseasesByBabyIdAndDate(babyId, start, end);
            babyCalendar.addDiseaseEvents(diseaseDates);

            // Check vaccines events
            List<Date> vaccineDates = this.vaccineService.getVaccinesByBabyIdAndDate(babyId, start, end);
            babyCalendar.addVaccineEvents(vaccineDates);

            // Check intake events
            List<Date> intakeDates = this.intakeService.getIntakesByBabyIdAndDate(babyId, start, end);
            babyCalendar.addIntakeEvents(intakeDates);

            // Check metric events
            List<Date> metricDates = this.metricService.getMetricsByUserIdAndDate(babyId, start, end);
            babyCalendar.addMetricEvents(metricDates);

            calendar.add(babyCalendar);
        }
        return calendar;
    }

    public List<CalendarDay> getCalendarDayByUserId(User user, LocalDate day) {
        List<Integer> babiesId = user.getBabies().stream().map(baby -> baby.getId()).toList();
        List<CalendarDay> events = new ArrayList<>();
        for (Integer babyId : babiesId) {
            CalendarDay calendarDay = new CalendarDay(babyId);

            // Get dream summary
            List<DreamSummary> dreamSummary = this.dreamService.getDreamSummaryByBabyIdAndDate(babyId, day);
            calendarDay.setDreams(dreamSummary);

            // Get diseases summary
            List<DiseaseSummary> diseaseSummary = this.diseaseService.getDiseaseSummaryByBabyIdAndDate(babyId, day);
            calendarDay.setDiseases(diseaseSummary);

            events.add(calendarDay);
        }
        return events;
    }
}
