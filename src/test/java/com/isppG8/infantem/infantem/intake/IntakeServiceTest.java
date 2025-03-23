package com.isppG8.infantem.infantem.intake;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.isppG8.infantem.infantem.baby.Baby;
import com.isppG8.infantem.infantem.baby.BabyService;
import com.isppG8.infantem.infantem.exceptions.ResourceNotFoundException;
import com.isppG8.infantem.infantem.exceptions.ResourceNotOwnedException;
import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.user.UserService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class IntakeServiceTest {

    @Mock
    private IntakeRepository intakeRepository;

    @Mock
    private UserService userService;

    @Mock
    private BabyService babyService;

    @InjectMocks
    private IntakeService intakeService;

    // Simulate that the user with id 1 is the current user.
    private User currentUser;
    // According to data.sql, the baby with id 1 belongs to the user with id 1.
    private Baby testBaby;
    // Create an intake associated with the baby.
    private Intake testIntake;

    @BeforeEach
    public void setUp() {
        // Configure the current user (e.g., "user1" from data.sql with id 1)
        currentUser = new User();
        currentUser.setId(1);
        currentUser.setUsername("user1");

        // Configure a baby that belongs to currentUser (according to data.sql, baby_id=1 is related to user_id=1)
        testBaby = new Baby();
        testBaby.setId(1);
        testBaby.setName("Juan");
        // Simulate the relationship loaded in user_baby
        testBaby.setUsers(new ArrayList<>(Arrays.asList(currentUser)));

        // Configure an intake associated with the baby (e.g., the first record in intake_table)
        testIntake = new Intake();
        testIntake.setId(1L);
        // Use an example date, quantity, and observations according to data.sql
        testIntake.setDate(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        testIntake.setQuantity(200);
        testIntake.setObservations("Breakfast: the baby ate well, no issues.");
        // Other fields can be assigned as per the implementation (recipes, intakeSymptom, etc.)
        testIntake.setBaby(testBaby);
    }

    // Test for getAllIntakes: simulate that only intakes associated with the current user are returned
    @Test
    public void testGetAllIntakes_ByCurrentUser() {
        // Simulate that the current user has two intakes (e.g., the two records in intake_table for baby_id=1)
        Intake secondIntake = new Intake();
        secondIntake.setId(2L);
        secondIntake.setDate(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        secondIntake.setQuantity(150);
        secondIntake.setObservations("Lunch: the baby left some food.");
        secondIntake.setBaby(testBaby);

        List<Intake> intakeList = Arrays.asList(testIntake, secondIntake);
        when(userService.findCurrentUser()).thenReturn(currentUser);
        when(intakeRepository.findAllByUser(currentUser)).thenReturn(intakeList);

        List<Intake> result = intakeService.getAllIntakes();
        assertEquals(intakeList, result);
        verify(userService, times(1)).findCurrentUser();
        verify(intakeRepository, times(1)).findAllByUser(currentUser);
    }

    // Test for getIntakeById success
    @Test
    public void testGetIntakeById_Success() {
        Long id = testIntake.getId();
        when(intakeRepository.findById(id)).thenReturn(Optional.of(testIntake));
        when(userService.findCurrentUser()).thenReturn(currentUser);
        when(babyService.findById(testBaby.getId())).thenReturn(testBaby);

        Intake result = intakeService.getIntakeById(id);
        assertEquals(testIntake, result);
    }

    // Test for getIntakeById when not found (expect exception)
    @Test
    public void testGetIntakeById_NotFound() {
        Long id = 99L;
        when(intakeRepository.findById(id)).thenReturn(Optional.empty());

        // Calling .get() on an empty Optional throws NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> intakeService.getIntakeById(id));
    }

    // Test for getIntakeById when the current user does not own the resource
    @Test
    public void testGetIntakeById_NotOwned() {
        Long id = testIntake.getId();
        when(intakeRepository.findById(id)).thenReturn(Optional.of(testIntake));
        // Simulate a different user who is not the owner (e.g., user2)
        User otherUser = new User();
        otherUser.setId(2);
        otherUser.setUsername("user2");
        when(userService.findCurrentUser()).thenReturn(otherUser);
        when(babyService.findById(testBaby.getId())).thenReturn(testBaby);

        assertThrows(ResourceNotOwnedException.class, () -> intakeService.getIntakeById(id));
    }

    // Test for saveIntake success
    @Test
    public void testSaveIntake_Success() {
        when(userService.findCurrentUser()).thenReturn(currentUser);
        when(babyService.findById(testBaby.getId())).thenReturn(testBaby);
        when(intakeRepository.save(testIntake)).thenReturn(testIntake);

        Intake result = intakeService.saveIntake(testIntake);
        assertEquals(testIntake, result);
    }

    // Test for saveIntake when the user does not own the resource
    @Test
    public void testSaveIntake_NotOwned() {
        // Simulate that the current user is not in the baby's user list
        User otherUser = new User();
        otherUser.setId(2);
        otherUser.setUsername("user2");
        when(userService.findCurrentUser()).thenReturn(otherUser);
        when(babyService.findById(testBaby.getId())).thenReturn(testBaby);

        assertThrows(ResourceNotOwnedException.class, () -> intakeService.saveIntake(testIntake));
    }

    // Test for updateIntake success
    @Test
    public void testUpdateIntake_Success() {
        Long id = testIntake.getId();
        Intake updatedIntake = new Intake();
        updatedIntake.setId(id);
        LocalDateTime newDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        updatedIntake.setDate(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        updatedIntake.setQuantity(250);
        updatedIntake.setObservations("Update: quantity modified.");
        // Assign other fields as necessary
        updatedIntake.setBaby(testBaby);

        when(userService.findCurrentUser()).thenReturn(currentUser);
        when(babyService.findById(testBaby.getId())).thenReturn(testBaby);
        when(intakeRepository.findById(id)).thenReturn(Optional.of(testIntake));
        when(intakeRepository.save(any(Intake.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Intake result = intakeService.updateIntake(id, updatedIntake);
        assertEquals(newDate, result.getDate());
        assertEquals(250, result.getQuantity());
        assertEquals("Update: quantity modified.", result.getObservations());
    }

    // Test for updateIntake when the user does not own the resource
    @Test
    public void testUpdateIntake_NotOwned() {
        Long id = testIntake.getId();
        Intake updatedIntake = new Intake();
        updatedIntake.setId(id);
        updatedIntake.setBaby(testBaby);
        // Simulate a user who is not the owner
        User otherUser = new User();
        otherUser.setId(2);
        otherUser.setUsername("user2");
        when(userService.findCurrentUser()).thenReturn(otherUser);
        when(babyService.findById(testBaby.getId())).thenReturn(testBaby);

        assertThrows(ResourceNotOwnedException.class, () -> intakeService.updateIntake(id, updatedIntake));
    }

    // Test for updateIntake when the intake to update is not found
    @Test
    public void testUpdateIntake_NotFound() {
        Long id = testIntake.getId();
        Intake updatedIntake = new Intake();
        updatedIntake.setId(id);
        updatedIntake.setBaby(testBaby);
        when(userService.findCurrentUser()).thenReturn(currentUser);
        when(babyService.findById(testBaby.getId())).thenReturn(testBaby);
        when(intakeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> intakeService.updateIntake(id, updatedIntake));
    }

    // Test for deleteIntake success
    @Test
    public void testDeleteIntake_Success() {
        Long id = testIntake.getId();
        when(intakeRepository.findById(id)).thenReturn(Optional.of(testIntake));
        when(userService.findCurrentUser()).thenReturn(currentUser);
        when(babyService.findById(testBaby.getId())).thenReturn(testBaby);
        when(intakeRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> intakeService.deleteIntake(id));
        verify(intakeRepository, times(1)).deleteById(id);
    }

    // Test for deleteIntake when the user does not own the resource
    @Test
    public void testDeleteIntake_NotOwned() {
        Long id = testIntake.getId();
        when(intakeRepository.findById(id)).thenReturn(Optional.of(testIntake));
        // Simulate a user who is not the owner
        User otherUser = new User();
        otherUser.setId(2);
        otherUser.setUsername("user2");
        when(userService.findCurrentUser()).thenReturn(otherUser);
        when(babyService.findById(testBaby.getId())).thenReturn(testBaby);

        assertThrows(ResourceNotOwnedException.class, () -> intakeService.deleteIntake(id));
    }

    // Test for deleteIntake when the intake is not found (expect exception when calling .get() on an empty Optional)
    @Test
    public void testDeleteIntake_NotFound() {
        Long id = testIntake.getId();
        when(intakeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> intakeService.deleteIntake(id));
    }
}
