package com.isppG8.infantem.infantem.recipe;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT r FROM Recipe r WHERE :age BETWEEN r.minRecommendedAge AND r.maxRecommendedAge")
    List<Recipe> findRecipesByAge(Integer age);

    @Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE i.food.id NOT IN :allergenIds")
    List<Recipe> findRecipesExcludingAllergens(List<Long> allergenIds);

    @Query("SELECT r FROM Recipe r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Recipe> searchRecipes(@Param("query") String query);
    
    @Query("SELECT r FROM Recipe r JOIN r.favoritedBy u WHERE u.id = :userId")
    List<Recipe> findFavoriteRecipes(@Param("userId") Long userId);
    
}
