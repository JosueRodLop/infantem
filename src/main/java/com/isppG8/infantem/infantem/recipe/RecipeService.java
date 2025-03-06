package com.isppG8.infantem.infantem.recipe;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.isppG8.infantem.infantem.allergen.Allergen;
import com.isppG8.infantem.infantem.baby.Baby;
import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.user.UserRepository;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository =  userRepository;
    }

    public List<Recipe> getRecommendedRecipes(Baby baby) {
        Integer babyAge = calculateBabyAgeInMonths(baby.getBirthDate());
        List<Long> allergenIds = baby.getAllergen().stream().map(Allergen::getId).collect(Collectors.toList());

        List<Recipe> recipes = recipeRepository.findRecipesByAge(babyAge);
        if (!allergenIds.isEmpty()) {
            recipes = recipes.stream()
                    .filter(recipe -> recipeRepository.findRecipesExcludingAllergens(allergenIds).contains(recipe))
                    .collect(Collectors.toList());
        }

        return recipes;
    }
    public List<Recipe> searchRecipes(String query) {
        return recipeRepository.searchRecipes(query);
    }
    
    public List<Recipe> getRecommendedRecipes(Integer babyAgeMonths) {
        return recipeRepository.findByRecommendedAgeLessThanEqual(babyAgeMonths);
    }
    
    public void saveFavoriteRecipe(Long userId, Long recipeId) {
        User user = userRepository.findById(userId).orElseThrow();
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();
        user.getFavorites().add(recipe);
        userRepository.save(user);
    }
    
    public List<Recipe> getFavoriteRecipes(Long userId) {
        return recipeRepository.findFavoriteRecipes(userId);
    }

    private Integer calculateBabyAgeInMonths(LocalDate birthDate) {
        return (int) java.time.temporal.ChronoUnit.MONTHS.between(birthDate, LocalDate.now());
    }
}

