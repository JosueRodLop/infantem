package com.isppG8.infantem.infantem.baby;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.isppG8.infantem.infantem.allergen.Allergen;
import com.isppG8.infantem.infantem.dream.Dream;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
public class Baby {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE	, generator = "entity_seq")
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

    @NotBlank
    private Integer height;

    @NotBlank
    private Integer cephalicPerimeter;

    @NotBlank
    private String foodPreference;

    //Relaciones

    @OneToMany
    private List<Dream> sleep;

    @ManyToMany
    private List<Allergen> allergen;
}


