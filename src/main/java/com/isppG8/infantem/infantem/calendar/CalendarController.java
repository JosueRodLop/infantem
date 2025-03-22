package com.isppG8.infantem.infantem.calendar;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.user.UserService;

@RestController
@RequestMapping("/api/v1/calendar")
public class CalendarController {

    private CalendarService calendarService;
    private UserService userService;

    @Autowired
    public CalendarController(CalendarService calendarService, UserService userService) {
        this.calendarService = calendarService;
        this.userService = userService;
    }

    @GetMapping
    public List<Calendar> getCalendarByUserId(@RequestParam Integer month, @RequestParam Integer year) {
        User user = userService.findCurrentUser();

        // Calculate start date
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.YEAR, year);
        calendar.set(java.util.Calendar.MONTH, month - 1);
        calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
        Date start = calendar.getTime();
    
        // Calculate end of month
        calendar.set(java.util.Calendar.DAY_OF_MONTH, calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));
        Date end = calendar.getTime();

        List<Calendar> calendarData = calendarService.getCalendarByUserId(user, start, end);
        
        return calendarData;
    }
    
}
