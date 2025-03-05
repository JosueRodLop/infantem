package com.isppG8.infantem.infantem.alimentNutrient;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlimentNutrientService {
    @Autowired
    private AlimentNutrientRepository alimentNutrientRepository;

    public List<AlimentNutrient> getAllAlimentNutrients() {
        return alimentNutrientRepository.findAll();
    }

    public Optional<AlimentNutrient> getAlimentNutrientById(Long id) {
        return alimentNutrientRepository.findById(id);
    }

    public AlimentNutrient createAlimentNutrient(AlimentNutrient AlimentNutrient) {
        return alimentNutrientRepository.save(AlimentNutrient);
    }

    public Optional<AlimentNutrient> updateAlimentNutrient(Long id, AlimentNutrient AlimentNutrientDetails) {
        return alimentNutrientRepository.findById(id).map(AlimentNutrient -> {
            AlimentNutrient.setAmount(AlimentNutrientDetails.getAmount());
            return alimentNutrientRepository.save(AlimentNutrient);
        });
    }

    public boolean deleteAlimentNutrient(Long id) {
        if (alimentNutrientRepository.existsById(id)) {
            alimentNutrientRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
}
