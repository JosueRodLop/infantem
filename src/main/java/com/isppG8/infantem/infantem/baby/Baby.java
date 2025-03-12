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
import com.isppG8.infantem.infantem.intake.Intake;
import com.isppG8.infantem.infantem.milestoneCompleted.MilestoneCompleted;
import com.isppG8.infantem.infantem.nutritionalContribution.NutritionalContribution;
import com.isppG8.infantem.infantem.user.User;
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
import jakarta.persistence.OneToOne;
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

    @OneToOne
    private NutritionalContribution nutritionalContribution;

    @OneToMany(mappedBy = "baby", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dream> sleep = new ArrayList<>();

    @OneToMany(mappedBy = "baby")
    private List<MilestoneCompleted> milestonesCompleted = new ArrayList<>();

    @OneToMany(mappedBy = "baby")
    private List<Intake> intakes = new ArrayList<>();

    @ManyToMany(mappedBy = "babies")
    private List<Allergen> allergen = new ArrayList<>();

    @ManyToMany(mappedBy = "babies",cascade = CascadeType.ALL)
    private List<Disease> disease = new ArrayList<>();  // Relación con enfermedades

    @ManyToMany
    @JoinTable(
        name = "vaccine_baby",
        joinColumns = @JoinColumn(name = "vaccine_id"),
        inverseJoinColumns = @JoinColumn(name = "baby_id")
    )
    private List<Vaccine> vaccines = new ArrayList<>();  // Relación con vacuna

    @ManyToMany
    @JoinTable(
        name = "user_baby",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "baby_id")
    )
    @Size(min = 1, max = 2, message = "A baby should have 1 or 2 users")
    private List<User> users = new ArrayList<>();
}


