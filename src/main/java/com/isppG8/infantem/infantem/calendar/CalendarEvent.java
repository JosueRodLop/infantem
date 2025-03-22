package com.isppG8.infantem.infantem.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarEvent {
    private Date date;
    private List<String> events;

    public CalendarEvent() {
    }

    public CalendarEvent(Date date) {
        this.date = date;
        this.events = new ArrayList<>();
    }
}
