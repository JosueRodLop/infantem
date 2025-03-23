package com.isppG8.infantem.infantem.advertising;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isppG8.infantem.infantem.exceptions.ResourceNotFoundException;

@Service
public class AdvertisementService {

    private AdvertisementRepository advertisementRepository;

    @Autowired
    public AdvertisementService(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    @Transactional(readOnly = true)
    public List<Advertisement> getAllAdvertisements() {
        return this.advertisementRepository.findAllAdvertisements();
    }

    @Transactional(readOnly = true)
    public Advertisement getAdvertisementById(Long advertisementId) {
        Advertisement ad = this.advertisementRepository.findById(advertisementId).orElseThrow(
                () -> new ResourceNotFoundException("Advertisement", "id", advertisementId));
        return ad;
    }

    @Transactional(readOnly = true)
    public List<Advertisement> getAdvertisementByCompanyName(String companyName) {
        return this.advertisementRepository.findAdvertisementsByCompanyName(companyName);
    }

    @Transactional
    public Advertisement updateAdvertisement(Long advertisementId, Advertisement advertisementDetails) {
        Advertisement advertisement = this.advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new ResourceNotFoundException("Advertisement", "id", advertisementId));

        advertisement.setCompanyName(advertisementDetails.getCompanyName());
        advertisement.setTitle(advertisementDetails.getTitle());
        advertisement.setPhotoRoute(advertisementDetails.getPhotoRoute());

        return this.advertisementRepository.save(advertisement);
    }

    @Transactional
    public Advertisement updateAdvertisementMinutes(Long advertisementId) {
        Advertisement advertisement = this.advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new ResourceNotFoundException("Advertisement", "id", advertisementId));

        advertisement.setTimeSeen(advertisement.getTimeSeen() + 1);

        return this.advertisementRepository.save(advertisement);
    }

    @Transactional
    public void deleteAdvertisement(Long advertisementId) {
        Advertisement advertisement = this.advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new ResourceNotFoundException("Advertisement", "id", advertisementId));

        this.advertisementRepository.delete(advertisement);
    }

}
