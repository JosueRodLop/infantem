package com.isppG8.infantem.infantem.recipe;

import java.util.ArrayList;
import java.util.List;

import com.isppG8.infantem.infantem.intake.IngredientRecipe;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "recipe_table")
@Getter @Setter
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // <-- Añadido
    private String description; // <-- Añadido
    private String photoRoute; // <-- Añadido

    private Integer minRecommendedAge;
    private Integer maxRecommendedAge;
    private String elaboration;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredientRecipe> ingredients = new ArrayList<>();
}

    
