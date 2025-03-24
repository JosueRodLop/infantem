package com.isppG8.infantem.infantem.calendar;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.isppG8.infantem.infantem.baby.Baby;
import com.isppG8.infantem.infantem.calendar.dto.CalendarEvents;
import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.user.UserService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CalendarServiceTest {
    private CalendarService calendarService;

    @Autowired
    public CalendarServiceTest(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @MockitoBean
    private UserService userService;

    static User user1 = new User();

    @BeforeAll
    static void setUp() {
        user1.setId(1);
        user1.setUsername("user1");
        List<Baby> babies = user1.getBabies();
        for (Integer i = 1; i <= 2; i++) {
            Baby baby = new Baby();
            baby.setId(i);
            babies.add(baby);
        }
        user1.setBabies(babies);
    }

    @Test
    public void testGetCalendarByUserId() {
        Mockito.when(userService.findCurrentUser()).thenReturn(user1);

        // Get data for march 2025

        List<CalendarEvents> calendar = calendarService.getCalendarByUserId(LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 3, 31));

        assertTrue(calendar.size() == 2, "The calendar should have data for 2 babies");
    }
}
