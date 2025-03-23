package com.isppG8.infantem.infantem.intake;

import static org.junit.jupiter.api.Assertions.*;

import com.isppG8.infantem.infantem.InfantemApplication;
import com.isppG8.infantem.infantem.baby.Baby;
import com.isppG8.infantem.infantem.baby.BabyService;
import com.isppG8.infantem.infantem.exceptions.ResourceNotFoundException;
import com.isppG8.infantem.infantem.exceptions.ResourceNotOwnedException;
import com.isppG8.infantem.infantem.recipe.Recipe;
import com.isppG8.infantem.infantem.recipe.RecipeRepository;
import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = { InfantemApplication.class, IntakeService.class, IntakeServiceTest.TestConfig.class })
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Use the configured test database
@Transactional
@Import(IntakeServiceTest.TestConfig.class)
public class IntakeServiceTest {

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

    // Real repository injected
    @Autowired
    private IntakeRepository intakeRepository;

    // Real service (uses the real repository and the other mocked beans)
    @Autowired
    private IntakeService intakeService;

    // Mocked beans defined in TestConfig
    @Autowired
    private UserService userService;
    @Autowired
    private BabyService babyService;

    @Autowired
    private RecipeRepository recipeRepository;

    // Test data
    private User currentUser;
    private Baby testBaby;
    private Intake testIntake;

    @BeforeEach
    public void setUp() {
        // Configure the current user (e.g., "user1" with id 1)
        currentUser = new User();
        currentUser.setId(1);
        currentUser.setUsername("user1");

        // Configure a baby that belongs to the currentUser (according to data.sql, baby with id 1 belongs to user1)
        testBaby = new Baby();
        testBaby.setId(1);
        testBaby.setName("Juan");
        testBaby.setUsers(List.of(currentUser));

        // Persist a Recipe to assign it to the first Intake
        Recipe breakfastRecipe = new Recipe();
        breakfastRecipe.setName("Breakfast Recipe");
        breakfastRecipe = recipeRepository.save(breakfastRecipe);

        // Configure an Intake associated with the baby
        testIntake = new Intake();
        testIntake.setDate(LocalDateTime.now());
        testIntake.setQuantity(200);
        testIntake.setObservations("Breakfast: the baby ate well, no issues.");
        testIntake.setBaby(testBaby);
        // Assign the persisted Recipe to meet the validation (@Size(min=1))
        testIntake.setRecipes(List.of(breakfastRecipe));
        testIntake = intakeRepository.save(testIntake);
    }

    // --- Tests using the real repository instance ---

    // Test for getAllIntakes: persist two records and verify that they are returned
    @Test
    public void testGetAllIntakes_ByCurrentUser() {
        // Persist a Recipe for the second Intake
        Recipe lunchRecipe = new Recipe();
        lunchRecipe.setName("Lunch Recipe");
        lunchRecipe = recipeRepository.save(lunchRecipe);

        // Create and save a second Intake for the same baby
        Intake secondIntake = new Intake();
        secondIntake.setDate(LocalDateTime.now());
        secondIntake.setQuantity(150);
        secondIntake.setObservations("Lunch: the baby left some food.");
        secondIntake.setBaby(testBaby);
        // Assign the persisted Recipe to meet the validation
        secondIntake.setRecipes(List.of(lunchRecipe));
        secondIntake = intakeRepository.save(secondIntake);

        // Stub the mocked services
        org.mockito.Mockito.when(userService.findCurrentUser()).thenReturn(currentUser);
        List<Intake> result = intakeService.getAllIntakes();
        // Expect at least the two inserted records
        assertTrue(result.size() >= 2);
    }

    // Test for getIntakeById success
    @Test
    public void testGetIntakeById_Success() {
        Long id = testIntake.getId();
        org.mockito.Mockito.when(userService.findCurrentUser()).thenReturn(currentUser);
        org.mockito.Mockito.when(babyService.findById(testBaby.getId())).thenReturn(testBaby);

        Intake result = intakeService.getIntakeById(id);
        assertNotNull(result);
        assertEquals(testIntake.getObservations(), result.getObservations());
    }

    // Test for getIntakeById when not found
    @Test
    public void testGetIntakeById_NotFound() {
        Long id = 999L; // Assume this ID does not exist in the DB
        org.mockito.Mockito.when(userService.findCurrentUser()).thenReturn(currentUser);
        // babyService can be left unstubbed in this case

        assertThrows(ResourceNotFoundException.class, () -> intakeService.getIntakeById(id));
    }

    // Test for getIntakeById when the current user does not own the resource
    @Test
    public void testGetIntakeById_NotOwned() {
        Long id = testIntake.getId();
        // Simulate a different user (not the owner)
        User otherUser = new User();
        otherUser.setId(2);
        otherUser.setUsername("user2");
        org.mockito.Mockito.when(userService.findCurrentUser()).thenReturn(otherUser);
        org.mockito.Mockito.when(babyService.findById(testBaby.getId())).thenReturn(testBaby);

        assertThrows(ResourceNotOwnedException.class, () -> intakeService.getIntakeById(id));
    }

    // Test for saveIntake success using the real repository
    @Test
    public void testSaveIntake_Success() {
        // Create a new Intake without an ID (to simulate creation)
        Intake newIntake = new Intake();
        newIntake.setDate(LocalDateTime.now());
        newIntake.setQuantity(180);
        newIntake.setObservations("Snack: new intake record.");
        newIntake.setBaby(testBaby);

        // Persist a Recipe to assign it to the new Intake
        Recipe snackRecipe = new Recipe();
        snackRecipe.setName("Snack Recipe"); // Assign a valid name to meet @NotNull
        snackRecipe = recipeRepository.save(snackRecipe);
        newIntake.setRecipes(List.of(snackRecipe));

        org.mockito.Mockito.when(userService.findCurrentUser()).thenReturn(currentUser);
        org.mockito.Mockito.when(babyService.findById(testBaby.getId())).thenReturn(testBaby);

        Intake saved = intakeService.saveIntake(newIntake);
        assertNotNull(saved.getId());
        assertEquals("Snack: new intake record.", saved.getObservations());
    }

    // Test for saveIntake when the current user does not own the resource
    @Test
    public void testSaveIntake_NotOwned() {
        Intake newIntake = new Intake();
        newIntake.setDate(LocalDateTime.now());
        newIntake.setQuantity(180);
        newIntake.setObservations("Snack: new intake record.");
        newIntake.setBaby(testBaby);

        User otherUser = new User();
        otherUser.setId(2);
        otherUser.setUsername("user2");
        org.mockito.Mockito.when(userService.findCurrentUser()).thenReturn(otherUser);
        org.mockito.Mockito.when(babyService.findById(testBaby.getId())).thenReturn(testBaby);

        assertThrows(ResourceNotOwnedException.class, () -> intakeService.saveIntake(newIntake));
    }

    // Test for updateIntake success
    @Test
    public void testUpdateIntake_Success() {
        Long id = testIntake.getId();
        // Create an object with the new data
        Intake updatedIntake = new Intake();
        updatedIntake.setDate(LocalDateTime.now());
        updatedIntake.setQuantity(250);
        updatedIntake.setObservations("Update: quantity modified.");
        updatedIntake.setBaby(testBaby);

        org.mockito.Mockito.when(userService.findCurrentUser()).thenReturn(currentUser);
        org.mockito.Mockito.when(babyService.findById(testBaby.getId())).thenReturn(testBaby);

        Intake result = intakeService.updateIntake(id, updatedIntake);
        assertEquals(250, result.getQuantity());
        assertEquals("Update: quantity modified.", result.getObservations());
    }

    // Test for updateIntake when the current user does not own the resource
    @Test
    public void testUpdateIntake_NotOwned() {
        Long id = testIntake.getId();
        Intake updatedIntake = new Intake();
        updatedIntake.setBaby(testBaby);

        User otherUser = new User();
        otherUser.setId(2);
        otherUser.setUsername("user2");
        org.mockito.Mockito.when(userService.findCurrentUser()).thenReturn(otherUser);
        org.mockito.Mockito.when(babyService.findById(testBaby.getId())).thenReturn(testBaby);

        assertThrows(ResourceNotOwnedException.class, () -> intakeService.updateIntake(id, updatedIntake));
    }

    // Test for updateIntake when the intake to update is not found
    @Test
    public void testUpdateIntake_NotFound() {
        Long id = 999L; // Non-existent ID
        Intake updatedIntake = new Intake();
        updatedIntake.setBaby(testBaby);

        org.mockito.Mockito.when(userService.findCurrentUser()).thenReturn(currentUser);
        org.mockito.Mockito.when(babyService.findById(testBaby.getId())).thenReturn(testBaby);

        assertThrows(ResourceNotFoundException.class, () -> intakeService.updateIntake(id, updatedIntake));
    }

    // Test for deleteIntake success
    @Test
    public void testDeleteIntake_Success() {
        Long id = testIntake.getId();
        org.mockito.Mockito.when(userService.findCurrentUser()).thenReturn(currentUser);
        org.mockito.Mockito.when(babyService.findById(testBaby.getId())).thenReturn(testBaby);

        // Assume that testIntake exists in the DB (already saved in setUp)
        // Execute delete
        assertDoesNotThrow(() -> intakeService.deleteIntake(id));
        // Verify that it is no longer in the DB
        Optional<Intake> deleted = intakeRepository.findById(id);
        assertTrue(deleted.isEmpty());
    }

    // Test for deleteIntake when the current user does not own the resource
    @Test
    public void testDeleteIntake_NotOwned() {
        Long id = testIntake.getId();
        User otherUser = new User();
        otherUser.setId(2);
        otherUser.setUsername("user2");
        org.mockito.Mockito.when(userService.findCurrentUser()).thenReturn(otherUser);
        org.mockito.Mockito.when(babyService.findById(testBaby.getId())).thenReturn(testBaby);

        assertThrows(ResourceNotOwnedException.class, () -> intakeService.deleteIntake(id));
    }

    // Test for deleteIntake when the intake is not found
    @Test
    public void testDeleteIntake_NotFound() {
        Long id = 999L; // Non-existent ID
        assertThrows(ResourceNotFoundException.class, () -> intakeService.deleteIntake(id));
    }
}
