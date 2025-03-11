package com.isppG8.infantem.infantem.recipe;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isppG8.infantem.infantem.baby.BabyRepository;
import com.isppG8.infantem.infantem.exceptions.ResourceNotFoundException;

import com.isppG8.infantem.infantem.user.UserRepository;


@Service
public class RecipeService {
    
    private RecipeRepository recipeRepository;


    @Autowired
    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository, BabyRepository babyRepository) {
        this.recipeRepository = recipeRepository;
    }

    //TODO: change age to babyId
    @Transactional(readOnly = true)
    public List<Recipe> getRecommendedRecipes(Integer age) {
        return this.recipeRepository.findRecommendedRecipes(age);
    }

    @Transactional(readOnly = true)
    public List<Recipe> getRecipesByUserId(Long userId) {
        return this.recipeRepository.findRecipesByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Recipe getRecipeById(Long recipeId) {
        return this.recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", recipeId));
    }

    @Transactional
    public Recipe createRecipe(Recipe recipe) {
        return this.recipeRepository.save(recipe);
    }

    @Transactional
    public Recipe updateRecipe(Long recipeId, Recipe recipeDetails) {
        Recipe recipe = this.recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", recipeId));

        recipe.setName(recipeDetails.getName());
        recipe.setDescription(recipeDetails.getDescription());
        recipe.setIngredients(recipeDetails.getIngredients());
        recipe.setMinRecommendedAge(recipeDetails.getMinRecommendedAge());
        recipe.setMaxRecommendedAge(recipeDetails.getMaxRecommendedAge());
        recipe.setElaboration(recipeDetails.getElaboration());

        return this.recipeRepository.save(recipe);
    }

    @Transactional
    public void deleteRecipe(Long recipeId) {
        Recipe recipe = this.recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", recipeId));

        this.recipeRepository.delete(recipe);
    }



}




