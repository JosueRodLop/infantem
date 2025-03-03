package com.isppG8.infantem.infantem.bebe;

import java.util.List;

import javax.validation.Valid;

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
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("")
public class BebeController {

    private final BebeService bebeService;

    @Autowired
    public BebeController(BebeService bebeService) {
        this.bebeService = bebeService;
    }
    
    @GetMapping
    public List<Bebe> findAll() {
        return bebeService.findAll();
    }

    @GetMapping("/{id}")
    public Bebe findById(int id) {
        return bebeService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Bebe createBebe(@RequestBody @Valid Bebe bebe) {
        bebeService.save(bebe);
        return bebe;
    }

    @PutMapping("/update/{id}")
    public void updateBebe(@PathVariable("id") Integer id,@RequestBody @Valid Bebe bebe) {
        Bebe bebeToUpdate = bebeService.findById(id);
        if(bebeToUpdate == null) {
            throw new RuntimeException("Bebe not found");
        }
        bebeToUpdate.setNombre(bebe.getNombre());
        bebeToUpdate.setFechaNacimiento(bebe.getFechaNacimiento());
        bebeToUpdate.setSexo(bebe.getSexo());
        bebeToUpdate.setPeso(bebe.getPeso());
        bebeToUpdate.setAltura(bebe.getAltura());
        bebeToUpdate.setPerimetroCefalico(bebe.getPerimetroCefalico());
        bebeToUpdate.setPreferenciaAlimenticia(bebe.getPreferenciaAlimenticia());

        bebeService.save(bebeToUpdate);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBebe(@PathVariable("id") Integer id) {
        bebeService.deleteBebe(id);
    }
}
