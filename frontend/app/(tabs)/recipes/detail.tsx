import { useLocalSearchParams } from "expo-router";
import { View, Text, TouchableOpacity } from "react-native";
import { recipes } from "../../../hardcoded_data/recipesData";
import { useEffect, useState } from "react";
import { useAuth } from "../../../context/AuthContext";
import { Recipe } from "../../../types";

export default function RecipeDetails() {

  const apiUrl = process.env.EXPO_PUBLIC_API_URL;

  const { recipeId } = useLocalSearchParams();
  const [recipe, setRecipe] = useState<Recipe | null>(null);

  const gs = require('../../../static/styles/globalStyles');

  const { token } = useAuth();

  useEffect(() => {
    if (recipeId) {
      fetch(`${apiUrl}/api/v1/recipes/${recipeId}`, {
        method: 'GET',
        headers: {
          "Authorization": "Bearer " + token
        }
      })
        .then((response) => {
          if (!response.ok) {
            return response.text().then((text) => {
              throw new Error(`Error: ${response.status} - ${text}`);
            });
          }
          return response.json();
        })
        .then((data: Recipe) => {
          setRecipe(data);
        })
        .catch((error) => {
          console.error("Error fetching recipe:", error);
        });
    } else {
      console.log("No recipe id received")
    }
  })

  if (!recipe) {
    return (
      <View style={gs.container}>
        <Text>Receta no encontrada</Text>
      </View>
    );
  }

  return (
    <View style={[gs.container, { paddingTop: 0, marginTop: 0 }]}>
      <View style={[gs.content, { marginTop: 0 }]}>
        <View style={{ flexDirection: "row", alignItems: "center" }}>
          <Text style={gs.headerText}>{recipe.name}</Text>
          {/* <TouchableOpacity style={[gs.mainButton, { marginLeft: 30 }]}>
            <Text style={gs.mainButtonText}>Añadir a favoritos</Text>
          </TouchableOpacity> */}
        </View>
        <Text style={gs.card}>{recipe.description}</Text>
        <Text style={[gs.cardTitle, { marginBottom: 10 }]}>Edad mínima recomendada:</Text>
        <Text style={gs.card}>{recipe.minRecommendedAge}</Text>
        {recipe.maxRecommendedAge && (
          <>
            <Text style={[gs.cardTitle, { marginBottom: 10 }]}>Edad máxima recomendada:</Text>
            <Text style={gs.card}>{recipe.maxRecommendedAge}</Text>
          </>
        )}
        <Text style={[gs.cardTitle, { marginBottom: 10 }]}>Ingredientes:</Text>
        {recipe.ingredients.split(",").map((ingredient, index) => (
          <Text key={index} style={gs.card}>{ingredient}</Text>
        ))}
        <Text style={[gs.cardTitle, { marginBottom: 10 }]}>Elaboración:</Text>
        <Text style={gs.card}>{recipe.elaboration}</Text>
      </View>
    </View>
  );
}


