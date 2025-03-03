package com.isppG8.infantem.infantem.bebe;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BebeService {
    
    private BebeRepository bebeRepository;

    @Autowired
    public BebeService(BebeRepository bebeRepository) {
        this.bebeRepository = bebeRepository;
    }

    @Transactional(readOnly = true)
    public List<Bebe> findAll() {
        return (List<Bebe>) bebeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Bebe findById(int id) {
        return bebeRepository.findById(id).orElse(null);
    }

    public void save(Bebe bebe) {
        bebeRepository.save(bebe);
    }

    public void deleteBebe(int id) {
        bebeRepository.deleteById(id);
    }
}
