package com.isppG8.infantem.infantem.alergeno;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alergenos")
public class AlergenoController {

    @Autowired
    private AlergenoService alergenoService;

    @GetMapping
    public List<Alergeno> getAllAlergenos() {
        return alergenoService.getAllAlergenos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alergeno> getAlergenoById(@PathVariable Long id) {
        Optional<Alergeno> alergeno = alergenoService.getAlergenoById(id);
        return alergeno.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Alergeno createAlergeno(@RequestBody Alergeno alergeno) {
        return alergenoService.createAlergeno(alergeno);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alergeno> updateAlergeno(@PathVariable Long id, @RequestBody Alergeno alergenoDetails) {
        Optional<Alergeno> updatedAlergeno = alergenoService.updateAlergeno(id, alergenoDetails);
        return updatedAlergeno.map(ResponseEntity::ok)
                              .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlergeno(@PathVariable Long id) {
        return alergenoService.deleteAlergeno(id) ? ResponseEntity.noContent().build()
                                                  : ResponseEntity.notFound().build();
    }
}

