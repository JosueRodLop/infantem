package com.isppG8.infantem.infantem.alergeno;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlergenoService {

    @Autowired
    private AlergenoRepository alergenoRepository;

    public List<Alergeno> getAllAlergenos() {
        return alergenoRepository.findAll();
    }

    public Optional<Alergeno> getAlergenoById(Long id) {
        return alergenoRepository.findById(id);
    }

    public Alergeno createAlergeno(Alergeno alergeno) {
        return alergenoRepository.save(alergeno);
    }

    public Optional<Alergeno> updateAlergeno(Long id, Alergeno alergenoDetails) {
        return alergenoRepository.findById(id).map(alergeno -> {
            alergeno.setNombre(alergenoDetails.getNombre());
            alergeno.setDescripcion(alergenoDetails.getDescripcion());
            return alergenoRepository.save(alergeno);
        });
    }

    public boolean deleteAlergeno(Long id) {
        if (alergenoRepository.existsById(id)) {
            alergenoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

