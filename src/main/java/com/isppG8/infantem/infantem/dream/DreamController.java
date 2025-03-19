package com.isppG8.infantem.infantem.dream;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/dream")
public class DreamController {

    private DreamService dreamService;

    @Autowired
    public DreamController(DreamService dreamService) {
        this.dreamService = dreamService;
    }

    @GetMapping
    public List<Dream> getAllDreams() {
        return dreamService.getAllDreams();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dream> getDreamById(@PathVariable Long id) {
        Optional<Dream> dream = dreamService.getDreamById(id);
        return dream.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Dream> createDream(@Valid @RequestBody Dream dream) {
        return ResponseEntity.ok(dreamService.createDream(dream));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dream> updateDream(@PathVariable Long id, @Valid @RequestBody Dream dreamDetails) {
        Dream updatedDream = dreamService.updateDream(id, dreamDetails);
        return ResponseEntity.ok(updatedDream);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDream(@PathVariable Long id) {
        boolean deleted = dreamService.deleteDream(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
