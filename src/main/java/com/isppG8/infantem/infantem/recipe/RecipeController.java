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

      // Obtener recomendaciones de recetas basadas en la edad del bebé
    @GetMapping("/recommendations/{babyId}")
    public ResponseEntity<List<Recipe>> getRecommendedRecipes(@PathVariable Integer babyId) {
        Baby baby = babyRepository.findById(babyId).orElse(null);
        if (baby == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recipeService.getRecommendedRecipes(baby));
    }
        // Buscar recetas por nombre o ingredientes
    @GetMapping("/search")
    public List<Recipe> searchRecipes(@RequestParam String query) {
        return recipeService.searchRecipes(query);
    }
    
    // Guardar receta como favorita
    @PostMapping("/favorites/{userId}/{recipeId}")
    public void saveFavoriteRecipe(@PathVariable Long userId, @PathVariable Long recipeId) {
        recipeService.saveFavoriteRecipe(userId, recipeId);
    }
    
    // Obtener recetas favoritas del usuario
    @GetMapping("/favorites/{userId}")
    public List<Recipe> getFavoriteRecipes(@PathVariable Long userId) {
        return recipeService.getFavoriteRecipes(userId);
    }
}
