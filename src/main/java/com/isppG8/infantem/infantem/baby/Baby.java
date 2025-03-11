package com.isppG8.infantem.infantem.baby;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.isppG8.infantem.infantem.allergen.Allergen;
import com.isppG8.infantem.infantem.disease.Disease;
import com.isppG8.infantem.infantem.dream.Dream;
import com.isppG8.infantem.infantem.vaccine.Vaccine;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "baby_table")
public class Baby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate birthDate;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @NotBlank
    private Double weight;

    @NotNull
    @Min(0)
    @Max(50)
    private Integer height;

    @NotNull
    @Min(0)
    private Integer cephalicPerimeter;

    @NotBlank
    private String foodPreference;

    //Relaciones

    @OneToMany
    private List<Dream> sleep = new ArrayList<>();

    @ManyToMany(mappedBy = "babies")
    private List<Allergen> allergen = new ArrayList<>();

    @OneToMany(mappedBy = "baby",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Disease> disease = new ArrayList<>();  // Relación con enfermedades

    @OneToMany(mappedBy = "baby", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vaccine> vaccine = new ArrayList<>();  // Relación con vacuna
}


