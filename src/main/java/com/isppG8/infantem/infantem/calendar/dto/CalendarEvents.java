package com.isppG8.infantem.infantem.calendar.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarEvents {
    private Integer babyId;
    private Map<String, Set<String>> events;

    public CalendarEvents() {
    }

    public CalendarEvents(Integer babyId) {
        this.babyId = babyId;
        this.events = new HashMap<>();
    }

    public void addDreamEvents(List<Date> dreamDates) {
        for (Date date : dreamDates) {
            addEvent(date, "Dream");
        }
    }

    public void addDiseaseEvents(List<Date> diseaseDates) {
        for (Date date : diseaseDates) {
            addEvent(date, "Disease");
        }
    }

    public void addVaccineEvents(List<Date> vaccineDates) {
        for (Date date : vaccineDates) {
            addEvent(date, "Vaccine");
        }
    }

    public void addIntakeEvents(List<Date> intakeDates) {
        for (Date date : intakeDates) {
            addEvent(date, "Intake");
        }
    }

    public void addMetricEvents(List<Date> metricDates) {
        for (Date date : metricDates) {
            addEvent(date, "Metric");
        }
    }

    private String parseDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    private void addEvent(Date date, String event) {
        String formattedDate = parseDate(date);
        if (!this.events.containsKey(formattedDate)) {
            Set<String> events = new HashSet<>();
            events.add(event);
            this.events.put(formattedDate, events);
        } else {
            this.events.get(formattedDate).add(event);
        }
    }

}
