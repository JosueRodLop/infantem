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
            String formattedDate = parseDate(date);
            if (!this.events.containsKey(formattedDate)) {
                Set<String> event = new HashSet<>();
                event.add("Dream");
                this.events.put(formattedDate, event);
            } else {
                this.events.get(formattedDate).add("Dream");
            }
        }
    }

    public void addDiseaseEvents(List<Date> diseaseDates) {
        for (Date date : diseaseDates) {
            String formattedDate = parseDate(date);
            if (!this.events.containsKey(formattedDate)) {
                Set<String> event = new HashSet<>();
                event.add("Disease");
                this.events.put(formattedDate, event);
            } else {
                this.events.get(formattedDate).add("Disease");
            }
        }
    }

    public void addVaccineEvents(List<Date> vaccineDates) {
        for (Date date : vaccineDates) {
            String formattedDate = parseDate(date);
            if (!this.events.containsKey(formattedDate)) {
                Set<String> event = new HashSet<>();
                event.add("Vaccine");
                this.events.put(formattedDate, event);
            } else {
                this.events.get(formattedDate).add("Vaccine");
            }
        }
    }

    private String parseDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

}
