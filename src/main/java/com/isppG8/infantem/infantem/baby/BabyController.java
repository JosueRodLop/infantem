package com.isppG8.infantem.infantem.baby;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/baby")
public class BabyController {

    private final BabyService babyService;

    @Autowired
    public BabyController(BabyService babyService) {
        this.babyService = babyService;
    }
    
    @GetMapping
    public ResponseEntity<List<Baby>> findAll() {
        List<Baby> babyList =  babyService.findAll();
        return ResponseEntity.ok().body(babyList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Baby> findById(int id) {
        Baby baby = babyService.findById(id);
        return ResponseEntity.ok().body(baby);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Baby> createBaby(@RequestBody @Valid Baby baby) {
        Baby createdBaby = babyService.createBaby(baby);
        return ResponseEntity.status(201).body(createdBaby);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Baby> updateBebe(@PathVariable("id") Integer id,@RequestBody @Valid Baby baby) {
        Baby updatedBaby = babyService.updateBaby(id, baby);
        return ResponseEntity.ok(updatedBaby);
        
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBaby(@PathVariable("id") Integer id) {
        babyService.deleteBaby(id);
    }
}
