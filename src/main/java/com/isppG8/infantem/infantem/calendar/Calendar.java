package com.isppG8.infantem.infantem.calendar;

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
public class Calendar {
    private Integer babyId;
    private Map<String, Set<String>> events;

    public Calendar() {
    }

    public Calendar(Integer babyId) {
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
