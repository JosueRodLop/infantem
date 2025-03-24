package com.isppG8.infantem.infantem.calendar;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isppG8.infantem.infantem.calendar.dto.CalendarDay;
import com.isppG8.infantem.infantem.calendar.dto.CalendarEvents;
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
    public List<CalendarEvents> getCalendarByUserId(@RequestParam Integer month, @RequestParam Integer year) {
        User user = userService.findCurrentUser();

        // Calculate start date
        LocalDate start = LocalDate.of(year, month, 1);

        // Calculate end of month
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<CalendarEvents> calendarData = calendarService.getCalendarByUserId(user, start, end);

        return calendarData;
    }

    @GetMapping("/{day}")
    public List<CalendarDay> getCalendarDayByUserId(@PathVariable LocalDate day) {
        User user = userService.findCurrentUser();

        List<CalendarDay> calendarDayData = calendarService.getCalendarDayByUserId(user, day);

        return calendarDayData;
    }

}
