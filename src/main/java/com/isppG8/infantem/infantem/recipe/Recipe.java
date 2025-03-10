package com.isppG8.infantem.infantem.recipe;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.isppG8.infantem.infantem.allergen.Allergen;
import com.isppG8.infantem.infantem.foodNutrient.FoodNutrient;
import com.isppG8.infantem.infantem.intake.Intake;
import com.isppG8.infantem.infantem.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "recipe_table")
@Getter @Setter
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String photo_route;

    @Column(nullable = true)
    private String ingredients;

    @Min(0)
    private Integer minRecommendedAge;
    
    @Min(0)
    @Max(36)
    private Integer maxRecommendedAge;
    
    @Column(nullable = true)
    private String elaboration;

    // Recipes made by nutritionists are not associated with any user
    @ManyToOne(optional = true)
    private User user;

    @ManyToMany(mappedBy = "recipes")
    private List<Intake> intakes = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "intake_recipe",
        joinColumns = @JoinColumn(name = "intake_id"),
        inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private List<Allergen> allergens = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodNutrient> alimentoNutrientes = new ArrayList<>();
}

    
