package com.isppG8.infantem.infantem.advertisement;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementRepository extends CrudRepository<Advertisement, Long> {

    @Query("SELECT a FROM Advertisement a")
    public List<Advertisement> findAllAdvertisements();

    @Query("SELECT a FROM Advertisement a WHERE a.companyName = :companyName")
    public List<Advertisement> findAdvertisementsByCompanyName(String companyName);

}
