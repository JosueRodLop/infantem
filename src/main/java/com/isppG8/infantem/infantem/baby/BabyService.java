package com.isppG8.infantem.infantem.baby;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isppG8.infantem.infantem.exceptions.ResourceNotFoundException;

@Service
public class BabyService {
    
    private final BabyRepository babyRepository;

    @Autowired
    public BabyService(BabyRepository babyRepository) {
        this.babyRepository = babyRepository;
    }

    @Transactional(readOnly = true)
    public List<Baby> findAll() {
        return (List<Baby>) babyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Baby findById(int id) {
        return babyRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Baby", "id", id)
            );
    }

    @Transactional
    public Baby createBaby(Baby baby) {
        return babyRepository.save(baby);
    }

    @Transactional
    public Baby updateBaby(int id, Baby updatedBaby) {
        Baby existingBaby = babyRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Baby", "id", id)
        );
        existingBaby.setName(updatedBaby.getName());
        existingBaby.setBirthDate(updatedBaby.getBirthDate());
        existingBaby.setGenre(updatedBaby.getGenre());
        existingBaby.setWeight(updatedBaby.getWeight());
        existingBaby.setHeight(updatedBaby.getHeight());
        existingBaby.setCephalicPerimeter(updatedBaby.getCephalicPerimeter());
        existingBaby.setFoodPreference(updatedBaby.getFoodPreference());
        existingBaby.setSleep(updatedBaby.getSleep());
        existingBaby.setAllergen(updatedBaby.getAllergen());
        existingBaby.setDisease(updatedBaby.getDisease());
        existingBaby.setVaccine(updatedBaby.getVaccine());
        return babyRepository.save(existingBaby);
    }

    @Transactional
    public void deleteBaby(int id) {
        if (!babyRepository.existsById(id)) {
             throw new ResourceNotFoundException("Baby", "id", id);
        }
        babyRepository.deleteById(id);
    }
}
