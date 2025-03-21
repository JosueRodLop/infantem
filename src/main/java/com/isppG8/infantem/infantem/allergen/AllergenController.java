package com.isppG8.infantem.infantem.allergen;

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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/allergens")
public class AllergenController {

    @Autowired
    private AllergenService allergenService;

    @GetMapping
    public List<Allergen> getAllAllergens() {
        return allergenService.getAllAllergens();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Allergen> getAllergenById(@PathVariable Long id) {
        Optional<Allergen> allergen = allergenService.getAllergenById(id);
        return allergen.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Allergen createAllergen(@RequestBody Allergen allergen) {
        return allergenService.createAllergen(allergen);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Allergen> updateAllergen(@PathVariable Long id, @RequestBody Allergen allergenDetails) {
        Optional<Allergen> updatedAllergen = allergenService.updateAllergen(id, allergenDetails);
        return updatedAllergen.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAllergen(@PathVariable Long id) {
        return allergenService.deleteAllergen(id) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
