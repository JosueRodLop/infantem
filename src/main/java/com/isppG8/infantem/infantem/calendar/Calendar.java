package com.isppG8.infantem.infantem.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Calendar {
    private Integer babyId;
    private Map<Date, List<String>> events;

    public Calendar() {
    }

    public Calendar(Integer babyId) {
        this.babyId = babyId;
        this.events = new HashMap<>();
    }
    

    public void addDreamEvents(List<Date> dreamDates) {
        for (Date date : dreamDates) {
            if (!this.events.containsKey(date)) {
                List<String> event = new ArrayList<>();
                event.add("Dream");
                this.events.put(date, event);
            } else {
                this.events.get(date).add("Dream");
            }
        }
    }
}