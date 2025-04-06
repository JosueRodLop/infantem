package com.isppG8.infantem.infantem.dream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import com.isppG8.infantem.infantem.baby.Baby;
import com.isppG8.infantem.infantem.baby.BabyRepository;
import com.isppG8.infantem.infantem.dream.dto.DreamDTO;
import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.user.UserService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class DreamControllerTest {

    @Autowired
    private DreamController dreamController;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private DreamService dreamService;

    @MockitoBean
    private BabyRepository babyRepository;

    @MockitoBean
    private DreamRepository dreamRepository;

    @Autowired
    public DreamControllerTest(DreamController dreamController, DreamService dreamService) {
        this.dreamController = dreamController;
        this.dreamService = dreamService;
    }

    @Test
    public void testGetAllDreams() {
        Dream dream = createSampleDream();
        when(dreamService.getAllDreams()).thenReturn(List.of(dream));

        List<DreamDTO> dreams = dreamController.getAllDreams();

        assertEquals(1, dreams.size());
        assertEquals(dream.getDateStart(), dreams.get(0).getDateStart());
    }

    @Test
    public void testGetDreamById() {
        Dream dream = createSampleDream();
        when(dreamService.getDreamById(1L)).thenReturn(dream);

        ResponseEntity<DreamDTO> response = dreamController.getDreamById(1L);

        assertNotNull(response.getBody());
        assertEquals(dream.getDreamType(), response.getBody().getDreamType());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCreateDream() {
        Dream dream = createSampleDream();

        when(dreamService.createDream(any(Dream.class))).thenReturn(dream);

        ResponseEntity<DreamDTO> response = dreamController.createDream(dream);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertNotNull(response.getBody());

        assertEquals(dream.getId(), response.getBody().getId());
        assertEquals(dream.getDateStart(), response.getBody().getDateStart());
    }

    @Test
    public void testUpdateDream() {
        Dream dream = createSampleDream();
        User mockUser = dream.getBaby().getUsers().get(0);
        when(userService.findCurrentUser()).thenReturn(mockUser);
        when(dreamService.updateDream(eq(1L), any(Dream.class))).thenCallRealMethod();

        doReturn(dream).when(dreamService).updateDream(eq(1L), any(Dream.class));

        ResponseEntity<DreamDTO> response = dreamController.updateDream(1L, dream);

        assertNotNull(response.getBody());
        assertEquals(dream.getDateEnd(), response.getBody().getDateEnd());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteDream() {
        doNothing().when(dreamService).deleteDream(1L);

        ResponseEntity<Void> response = dreamController.deleteDream(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testGetAllDreamsEmpty() {
        when(dreamService.getAllDreams()).thenReturn(List.of());

        List<DreamDTO> dreams = dreamController.getAllDreams();

        assertTrue(dreams.isEmpty(), "Dream list should be empty");
    }

    private Dream createSampleDream() {
        Dream dream = new Dream();
        dream.setId(1L);
        dream.setDateStart(LocalDateTime.of(2023, 1, 1, 22, 0));
        dream.setDateEnd(LocalDateTime.of(2023, 1, 2, 6, 0));
        dream.setNumWakeups(2);
        dream.setDreamType(DreamType.DEEP);

        User user = new User();
        user.setId((int) 1L);

        Baby baby = new Baby();
        baby.setId(1);
        baby.setUsers(List.of(user));
        dream.setBaby(baby);

        return dream;
    }

}
