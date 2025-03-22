package com.isppG8.infantem.infantem.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isppG8.infantem.infantem.dream.DreamService;
import com.isppG8.infantem.infantem.user.User;

@Service
public class CalendarService {
    
    private DreamService dreamService;

    @Autowired
    public CalendarService(DreamService dreamService) {
        this.dreamService = dreamService;
        
    }

    public List<Calendar> getCalendarByUserId(User user, Date start, Date end) {
        List<Integer> babiesId = user.getBabies().stream().map(baby -> baby.getId()).toList();

        List<Calendar> calendar = new ArrayList<>();
        
        for (Integer babyId : babiesId) {
            Calendar babyCalendar = new Calendar(babyId);
            
            List<Date> dreamDates = this.dreamService.getDreamsByBabyIdAndDate(babyId, start, end);
            babyCalendar.addDreamEvents(dreamDates);
            calendar.add(babyCalendar);
        }
        return calendar;
    }
}
