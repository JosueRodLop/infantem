package com.isppG8.infantem.infantem.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RecipeServiceTest {
    private RecipeService recipeService;

    @Autowired
    public RecipeServiceTest(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    final int HUGE_BABY_AGE = 999;

    @Test
    public void recipeFilterByMinAgeTest() {
        final List<Recipe> allRecipes = recipeService.findAllRecipes();
        final int BABY_AGE = 6;

        List<Recipe> filtered_recipes = recipeService.getRecipeByMinAge(BABY_AGE);
        assertTrue(filtered_recipes.size() < allRecipes.size(),
                "Filtered recipe list size should be less than original list");

        Boolean allRecipesHasCorrectMinAge = filtered_recipes.stream()
                .allMatch(recipe -> BABY_AGE >= recipe.getMinRecommendedAge());

        assertTrue(allRecipesHasCorrectMinAge, "All recipes should has min recommended age less than " + BABY_AGE);

        List<Recipe> emptyFilteredRecipe = recipeService.getRecipeByMinAge(0);
        assertTrue(emptyFilteredRecipe.isEmpty(), "This filtered list should be empty: " + emptyFilteredRecipe);

        List<Recipe> noFilteredRecipes = recipeService.getRecipeByMinAge(HUGE_BABY_AGE);
        assertEquals(allRecipes, noFilteredRecipes, "All recipes should have min age less than" + HUGE_BABY_AGE);
    }

    @Test
    public void recipeFilterByMaxAgeTest() {
        final List<Recipe> allRecipes = recipeService.findAllRecipes();
        final int BABY_AGE = 10;

        List<Recipe> filtered_recipes = recipeService.getRecipeByMaxAge(BABY_AGE);
        assertTrue(filtered_recipes.size() <= allRecipes.size(),
                "Filtered recipe list size should be less than original list, Filtered size: " + filtered_recipes.size()
                        + "Original size: " + allRecipes.size());

        Boolean allRecipesHasCorrectMinAge = filtered_recipes.stream()
                .allMatch(recipe -> BABY_AGE <= recipe.getMaxRecommendedAge());

        assertTrue(allRecipesHasCorrectMinAge, "All recipes should has min recommended age greater than " + BABY_AGE);

        List<Recipe> emptyFilteredRecipe = recipeService.getRecipeByMaxAge(HUGE_BABY_AGE);
        assertTrue(emptyFilteredRecipe.isEmpty(), "This filtered list should be empty: " + emptyFilteredRecipe);

        List<Recipe> noFilteredRecipes = recipeService.getRecipeByMaxAge(0);
        assertEquals(allRecipes, noFilteredRecipes, "All recipes should have min age greater than 0");
    }

    @Test
    public void recipeFilterByNutrientTest() {
        final String EXISTING_NUTRIENT = "zanahoria";
        final String NON_EXISTING_NUTRIENT = "chocolate";

        List<Recipe> recipesWithNutrient = recipeService.getRecipesByNutrient(EXISTING_NUTRIENT);
        assertTrue(recipesWithNutrient.size() > 0, "Recipes containing the nutrient should exist.");

        boolean allRecipesContainNutrient = recipesWithNutrient.stream()
                .allMatch(recipe -> recipe.getIngredients().toLowerCase().contains(EXISTING_NUTRIENT));
        assertTrue(allRecipesContainNutrient, "All returned recipes should contain the nutrient " + EXISTING_NUTRIENT);

        List<Recipe> recipesWithNonExistingNutrient = recipeService.getRecipesByNutrient(NON_EXISTING_NUTRIENT);
        assertTrue(recipesWithNonExistingNutrient.isEmpty(), "No recipes should contain the nutrient " + NON_EXISTING_NUTRIENT);

        List<Recipe> recipesWithNullNutrient = recipeService.getRecipesByNutrient(null);
        assertTrue(recipesWithNullNutrient.isEmpty(), "No recipes should be returned for null nutrient.");

        List<Recipe> recipesWithEmptyNutrient = recipeService.getRecipesByNutrient("");
        assertTrue(recipesWithEmptyNutrient.isEmpty(), "No recipes should be returned for empty nutrient.");
    }

}
