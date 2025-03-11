package com.isppG8.infantem.infantem.user;

import java.util.ArrayList;
import java.util.List;

import com.isppG8.infantem.infantem.recipe.Recipe;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import com.isppG8.infantem.infantem.auth.Authorities;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_table")
@Getter @Setter
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
    private Authorities authorities;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recipe> recipes = new ArrayList<>();
}
