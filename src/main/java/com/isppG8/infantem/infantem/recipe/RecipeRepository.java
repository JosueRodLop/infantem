package com.isppG8.infantem.infantem.recipe;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    @Query("SELECT r FROM Recipe r WHERE r.user IS NULL")
    List<Recipe> findAllRecommendedRecipes();

    @Query("SELECT r FROM Recipe r WHERE r.minRecommendedAge <= :age AND r.maxRecommendedAge >= :age AND r.user IS NULL")
    List<Recipe> findRecommendedRecipes(@Param("age") Integer age);

    @Query("SELECT r FROM Recipe r WHERE r.user.id = :userId")
    List<Recipe> findRecipesByUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM Recipe r WHERE r.minRecommendedAge <= :age")
    List<Recipe> findRecipeByMinAge(@Param("age") Integer age);

    @Query("SELECT r FROM Recipe r WHERE r.maxRecommendedAge >= :age")
    List<Recipe> findRecipeByMaxAge(@Param("age") Integer age);

    @Query("SELECT r FROM Recipe r WHERE CAST(FUNCTION('FIND_IN_SET', :ingredient, r.ingredients) AS integer) > 0")
    List<Recipe> findRecipeByIngredient(String ingredient);

    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.foodNutrients fn JOIN fn.nutrient n WHERE n.name = :nutrientName")
    List<Recipe> findRecipeByNutrient(@Param("nutrientName") String nutrientName);

    @Query("SELECT r FROM Recipe r WHERE NOT EXISTS (SELECT 1 FROM r.allergens a WHERE a.name IN :allergens)")
    List<Recipe> findRecipesWithoutAllergen(@Param("allergens") List<String> allergens);

}
