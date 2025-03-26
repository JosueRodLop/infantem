package com.isppG8.infantem.infantem.baby;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Import;

import com.isppG8.infantem.infantem.InfantemApplication;
import com.isppG8.infantem.infantem.baby.dto.BabyDTO;
import com.isppG8.infantem.infantem.exceptions.ResourceNotFoundException;
import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.user.UserService;

@SpringBootTest(classes = { InfantemApplication.class, BabyService.class, BabyServiceTest.TestConfig.class })
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Import(BabyServiceTest.TestConfig.class)
public class BabyServiceTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserService userService() {
            return org.mockito.Mockito.mock(UserService.class);
        }

    }
    @Autowired
    private BabyRepository babyRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BabyService babyService;

    private User currentUser;
    private Baby testBaby;


    @BeforeEach
    public void setup() {
        currentUser = new User();
        currentUser.setId(1);
        currentUser.setUsername("user1");

        testBaby = new Baby();
        testBaby.setId(1);
        testBaby.setName("Juan");
        currentUser.setBabies(List.of(testBaby));     
        
    }

    @Test
    public void TestGetBabiesByUser() {
        org.mockito.Mockito.when(userService.findCurrentUser()).thenReturn(currentUser);
        List<Baby> babies = babyService.findBabiesByUser();
        assertTrue(babies.size() == 1);

        Baby baby2 = new Baby();
        baby2.setId(2);
        baby2.setName("Pedro");
        currentUser.setBabies(List.of(testBaby, baby2));

        babies = babyService.findBabiesByUser();
        assertTrue(babies.size() == 2);
    }

    @Test
    public void TestUpdateBaby() {
        User u = userService.getUserById(1L);
        //org.mockito.Mockito.when(userService.findCurrentUser()).thenReturn(u);

        System.out.println(u);

        Baby baby = new Baby();
        baby.setId(1);
        baby.setName("pedro");
        baby.setBirthDate(LocalDate.of(2021,12,31));
        baby.setGenre(Genre.MALE);
        baby.setWeight(7.0);
        baby.setHeight(50);
        baby.setCephalicPerimeter(30);
        baby.setFoodPreference("Leche materna");
        u.setBabies(List.of(baby));
        baby.setUsers(List.of(u));

        babyService.updateBaby(1, baby);
        assertTrue(testBaby.getName().equals("Pedro"));
    }
    

    @Test
    public void TestCreateBaby() {
        org.mockito.Mockito.when(userService.findCurrentUser()).thenReturn(currentUser);
        BabyDTO baby = new BabyDTO();
        baby.setId(2);
        baby.setName("Pedro");
        baby.setBirthDate(LocalDate.of(2021,12,31));
        baby.setGenre(Genre.MALE);
        baby.setWeight(7.0);
        baby.setHeight(50);
        baby.setCephalicPerimeter(30);
        baby.setFoodPreference("Leche materna");

        Baby b2 = babyService.createBaby(baby);
        currentUser.setBabies(List.of(b2));
        assertTrue(babyRepository.findById(2).isPresent());
        assertTrue(babyService.findBabiesByUser().contains(b2));
    }

    @Test
    public void TestFindbabyById() {
        org.mockito.Mockito.when(userService.findCurrentUser()).thenReturn(currentUser);
        Baby baby2 = new Baby();
        baby2.setId(2);
        baby2.setName("Pedro");
        baby2.setUsers(List.of(currentUser));
        babyRepository.save(baby2);
        currentUser.setBabies(List.of(baby2));
        
        
        Baby baby = babyService.findById(2);
        assertTrue(baby.getName().equals("Pedro"));
    }

    @Test
    public void TestDeleteBaby() {
        org.mockito.Mockito.when(userService.findCurrentUser()).thenReturn(currentUser);
        
        Baby baby2 = new Baby();
        baby2.setId(2);
        baby2.setName("Pedro");
        baby2.setBirthDate(LocalDate.of(2021,12,31));
        baby2.setGenre(Genre.MALE);
        baby2.setWeight(7.0);
        baby2.setHeight(50);
        baby2.setCephalicPerimeter(30);
        baby2.setFoodPreference("Leche materna");
        baby2.setUsers(List.of(currentUser));
        babyRepository.save(baby2);
        currentUser.setBabies(List.of(baby2));
        
        babyService.deleteBaby(2);
        assertThrows(ResourceNotFoundException.class, () -> babyService.findById(2));
    }

    @Test
    public void TestDeleteBaby_NotFOund() {
        Integer id = 999;
        assertThrows(ResourceNotFoundException.class, () -> babyService.deleteBaby(id));
    }
    
}
