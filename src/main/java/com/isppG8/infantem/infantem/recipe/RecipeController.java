package com.isppG8.infantem.infantem.recipe;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    // Obtener recetas recomendadas según edad del bebé y alérgenos
    @GetMapping("/recommendations/{babyId}")
    public ResponseEntity<List<Recipe>> getRecommendedRecipes(@PathVariable Integer babyId) {
        List<Recipe> recommendedRecipes = recipeService.getRecommendedRecipes(babyId);
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
