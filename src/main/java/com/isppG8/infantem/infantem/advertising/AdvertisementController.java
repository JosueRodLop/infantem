package com.isppG8.infantem.infantem.advertising;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/advertisements")
public class AdvertisementController {

    private AdvertisementService advertisementService;

    @Autowired
    public AdvertisementController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @GetMapping
    public ResponseEntity<List<Advertisement>> getAllAdvertisements() {
        List<Advertisement> advertisements = advertisementService.getAllAdvertisements();
        return ResponseEntity.ok(advertisements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Advertisement> getAdvertisementById(@PathVariable Long id) {
        Advertisement advertisement = advertisementService.getAdvertisementById(id);
        return ResponseEntity.ok(advertisement);
    }

    @GetMapping("/companyName/{companyName}")
    public ResponseEntity<List<Advertisement>> getAdvertisementsByCompanyName(@PathVariable String companyName) {
        List<Advertisement> advertisements = advertisementService.getAdvertisementByCompanyName(companyName);
        return ResponseEntity.ok(advertisements);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Advertisement> updateAdvertisement(@PathVariable Long id,
            @RequestBody Advertisement advertisementDetails) {
        Advertisement updatedAdvertisement = advertisementService.updateAdvertisement(id, advertisementDetails);
        return ResponseEntity.ok(updatedAdvertisement);
    }

    @PutMapping("/minutes/{id}")
    public ResponseEntity<Advertisement> updateAdvertisementMinutes(@PathVariable Long id) {
        Advertisement updatedAdvertisement = advertisementService.updateAdvertisementMinutes(id);
        return ResponseEntity.ok(updatedAdvertisement);
    }

    @PutMapping("/clicks/{id}")
    public ResponseEntity<Advertisement> updateAdvertisementClicks(@PathVariable Long id) {
        Advertisement updatedAdvertisement = advertisementService.updateAdvertisementClicks(id);
        return ResponseEntity.ok(updatedAdvertisement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable Long id) {
        advertisementService.deleteAdvertisement(id);
        return ResponseEntity.ok().build();
    }

}
