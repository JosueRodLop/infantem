package com.isppG8.infantem.infantem.recipe;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.isppG8.infantem.infantem.allergen.Allergen;
import com.isppG8.infantem.infantem.baby.Baby;
import com.isppG8.infantem.infantem.baby.BabyRepository;
import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.user.UserRepository;


@Service
/*
 * TODO:
 * Add delete favourites recipes functionality
 * Make all bbdd interactions transactional
 * 
 */

public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BabyRepository babyRepository;


    
    public List<Recipe> getRecommendedRecipes(Integer babyId) {
        //TODO: Add exception handler when baby is not found
        Baby baby = babyRepository.findById(babyId).orElse(null);

        //TODO: Change baby age into years instead of months
        Integer babyAge = calculateBabyAgeInMonths(baby.getBirthDate());

        //TODO: Change this search into a repository query
        List<Long> allergenIds = baby.getAllergen().stream()
            .map(Allergen::getId)
            .collect(Collectors.toList());

        List<Recipe> recipes = recipeRepository.findRecipesByAge(babyAge);


        if (!allergenIds.isEmpty()) {
            //TODO: Change this search into a repository query
            recipes = recipes.stream()
                .filter(recipe -> recipeRepository.findRecipesExcludingAllergens(allergenIds).contains(recipe))
                .collect(Collectors.toList());
        }

        return recipes;
    }

    public List<Recipe> searchRecipes(String query) {
        return recipeRepository.searchRecipes(query);
    }

    public void saveFavoriteRecipe(Long userId, Long recipeId) throws ResponseStatusException{
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Recipe recipe = recipeRepository.findById(recipeId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        user.getFavorites().add(recipe);
        userRepository.save(user);
    }


    public List<Recipe> getFavoriteRecipes(Long userId) throws ResponseStatusException{
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return user.getFavorites();
    }



    //TODO
    public void deleteRecipeFromFavourites(Integer recipeId,Integer userId){

    }


    //TODO: Change age into years 
    private Integer calculateBabyAgeInMonths(LocalDate birthDate) {
        return (int) ChronoUnit.MONTHS.between(birthDate, LocalDate.now());
    }
}




