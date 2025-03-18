package com.isppG8.infantem.infantem.vaccine;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isppG8.infantem.infantem.baby.Baby;
import com.isppG8.infantem.infantem.baby.BabyRepository;
import com.isppG8.infantem.infantem.exceptions.ResourceNotFoundException;
import com.isppG8.infantem.infantem.exceptions.ResourceNotOwnedException;
import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.user.UserService;

@Service
public class VaccineService {

    private final VaccineRepository vaccineRepository;
    private final BabyRepository babyRepository;
    private final UserService userService;

    @Autowired
    public VaccineService(VaccineRepository vaccineRepository, BabyRepository babyRepository, UserService userService) {
        this.vaccineRepository = vaccineRepository;
        this.babyRepository = babyRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<Vaccine> getAll() {
        return this.vaccineRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Vaccine getById(Long id) {
        return this.vaccineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vaccine", "id", id));
    }

    @Transactional
    public Vaccine save(Vaccine vaccine) {
        List<Baby> attachedBabies = resolveBabies(vaccine.getBabies());

        vaccine.setBabies(new ArrayList<>());
        vaccine = vaccineRepository.save(vaccine);

        for (Baby baby : attachedBabies) {
            baby.getVaccines().add(vaccine);
        }

        babyRepository.saveAll(attachedBabies);

        return vaccine;
    }

    @Transactional
    public void delete(Long id) {
        this.vaccineRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vaccine", "id", id));
        this.vaccineRepository.deleteById(id);
    }

    private List<Baby> resolveBabies(List<Baby> babies) {
        User currentUser = userService.findCurrentUser();
        return babies.stream().map(baby -> resolveSingleBaby(baby, currentUser)).toList();
    }

    private Baby resolveSingleBaby(Baby baby, User user) {
        if (baby.getId() == null) {
            throw new IllegalArgumentException("Cannot create a new baby while associating to a vaccine");
        }
        Baby existingBaby = babyRepository.findById(baby.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Baby", "id", baby.getId()));
        checkOwnership(existingBaby, user);
        return existingBaby;
    }

    private void checkOwnership(Baby existingBaby, User user) {
        if (!existingBaby.getUsers().contains(user)) {
            throw new ResourceNotOwnedException(existingBaby);
        }
    }

}
