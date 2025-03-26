package com.isppG8.infantem.infantem.vaccine;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.isppG8.infantem.infantem.InfantemApplication;
import com.isppG8.infantem.infantem.baby.Baby;
import com.isppG8.infantem.infantem.baby.BabyService;
import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.user.UserService;


import jakarta.transaction.Transactional;

@SpringBootTest(classes = { InfantemApplication.class, VaccineService.class, VaccineServiceTest.TestConfig.class })
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Import(VaccineServiceTest.TestConfig.class)
public class VaccineServiceTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserService userService() {
            return org.mockito.Mockito.mock(UserService.class);
        }

        @Bean
        public BabyService babyService() {
            return org.mockito.Mockito.mock(BabyService.class);
        }
    }

    @Autowired
    private VaccineService vaccineService;

    @Autowired
    private UserService userService;
    @Autowired
    private BabyService babyService;

    private User currentUser;
    private Baby testBaby;

    @BeforeEach
    public void setUp() {

        currentUser = new User();
        currentUser.setId(1);
        currentUser.setUsername("user1");

        testBaby = new Baby();
        testBaby.setId(1);
        testBaby.setName("Juan");
        testBaby.setUsers(List.of(currentUser));
        currentUser.setBabies(List.of(testBaby));

    }

    @Test
    public void TestFindAll() {
        List<Vaccine> vaccines = vaccineService.getAll();
        assertTrue(vaccines.size() == 5); // 5 vaccines in the database for now
    }

    @Test
    public void TestFindVaccineById() {
        Vaccine vaccine = vaccineService.getById(1L);
        assertTrue(vaccine.getId() == 1);
        assertTrue(vaccine.getType().equals("MMR"));
    }

    /*
     * This test is not working
     * @Test public void TestSaveVaccine() {
     * org.mockito.Mockito.when(userService.findCurrentUser()).thenReturn(currentUser);
     * org.mockito.Mockito.when(babyService.findById(testBaby.getId())).thenReturn(testBaby); Vaccine vaccine = new
     * Vaccine(); vaccine.setId(6L); vaccine.setType("Hepatitis B"); vaccine.setVaccinationDate(LocalDate.now());
     * vaccine.setBaby(testBaby); vaccineService.save(vaccine); List<Vaccine> vaccines = vaccineService.getAll();
     * assertTrue(vaccines.size() == 6); // 6 vaccines in the database now }
     */
    @Test
    public void TestDeleteVaccine() {
        vaccineService.delete(1L);
        List<Vaccine> vaccines = vaccineService.getAll();
        assertTrue(vaccines.size() == 4); // 4 vaccines in the database now
    }
}
