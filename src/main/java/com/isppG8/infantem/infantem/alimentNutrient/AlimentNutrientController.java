package com.isppG8.infantem.infantem.alimentNutrient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
//Añadir la ruta de alimentos cuando se implemente la entidad
@RequestMapping({"/nutrients/{nutrientId}/alimentnutrients"})
public class AlimentNutrientController {
    @Autowired
    private AlimentNutrientService alimentNutrientService;

    @GetMapping
    public List<AlimentNutrient> getAlimentNutrient() {
        return alimentNutrientService.getAllAlimentNutrients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlimentNutrient> getAlimentNutrientById(@PathVariable Long id) {
        return alimentNutrientService.getAlimentNutrientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public AlimentNutrient createAlimentNutrient(@RequestBody AlimentNutrient alimentNutrient) {
        return alimentNutrientService.createAlimentNutrient(alimentNutrient);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AlimentNutrient> updateAlimentNutrient(@PathVariable Long id, @RequestBody AlimentNutrient alimentNutrient) {
        return alimentNutrientService.updateAlimentNutrient(id, alimentNutrient)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlimentNutrient(@PathVariable Long id) {
        if (alimentNutrientService.deleteAlimentNutrient(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    
}
