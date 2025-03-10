package com.isppG8.infantem.infantem.allergen;

import java.util.ArrayList;
import java.util.List;

import com.isppG8.infantem.infantem.baby.Baby;
import com.isppG8.infantem.infantem.recipe.Recipe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Allergen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToMany(mappedBy = "allergens")
    private List<Recipe> recipes = new ArrayList<>();

    
    @JoinTable(
        name = "baby_allergen",
        joinColumns = @JoinColumn(name = "baby_id"),
        inverseJoinColumns = @JoinColumn(name = "allergen_id")
    )
    private List<Baby> babies = new ArrayList<>();

   
    public Allergen() {
    }

    public Allergen(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

