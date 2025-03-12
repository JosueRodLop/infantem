package com.isppG8.infantem.infantem.user;

import java.util.ArrayList;
import java.util.List;

import com.isppG8.infantem.infantem.baby.Baby;
import com.isppG8.infantem.infantem.recipe.Recipe;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.isppG8.infantem.infantem.auth.Authorities;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_table")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String surname;
    private String username;
    private String password;
    private String email;
    private String profilePhotoRoute;

    @ManyToOne
    @JoinColumn(name = "authority_id")
    private Authorities authorities;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Recipe> recipes = new ArrayList<>();

    @ManyToMany(mappedBy = "users")
    List<Baby> babies = new ArrayList<>();
}
