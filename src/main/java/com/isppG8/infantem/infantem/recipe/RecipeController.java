package com.isppG8.infantem.infantem.recipe;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isppG8.infantem.infantem.baby.Baby;
import com.isppG8.infantem.infantem.baby.BabyRepository;

@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final BabyRepository babyRepository;

    public RecipeController(RecipeService recipeService, BabyRepository babyRepository) {
        this.recipeService = recipeService;
        this.babyRepository = babyRepository;
    }

    // Obtener recetas recomendadas según edad del bebé y alérgenos
    @GetMapping("/recommendations/{babyId}")
    public ResponseEntity<List<Recipe>> getRecommendedRecipes(@PathVariable Integer babyId) {
        Baby baby = babyRepository.findById(babyId).orElse(null);
        if (baby == null) {
            return ResponseEntity.notFound().build();
        }

        List<Recipe> recommendedRecipes = recipeService.getRecommendedRecipes(baby);
        return ResponseEntity.ok(recommendedRecipes);
    }

    // Buscar recetas por nombre
    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipes(@RequestParam String query) {
        return ResponseEntity.ok(recipeService.searchRecipes(query));
    }

    // Guardar receta como favorita
    @PostMapping("/favorites/{userId}/{recipeId}")
    public ResponseEntity<Void> addFavorite(@PathVariable Long userId, @PathVariable Long recipeId) {
        recipeService.saveFavoriteRecipe(userId, recipeId);
        return ResponseEntity.ok().build();
    }

    // Obtener recetas favoritas del usuario
    @GetMapping("/favorites/{userId}")
    public ResponseEntity<List<Recipe>> getFavoriteRecipes(@PathVariable Long userId) {
        return ResponseEntity.ok(recipeService.getFavoriteRecipes(userId));
    }
}
