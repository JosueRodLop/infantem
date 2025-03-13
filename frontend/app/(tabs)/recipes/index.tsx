import { useState, useEffect } from "react";
import { Text, View, TouchableOpacity, TextInput, ScrollView } from "react-native";
import { Link } from "expo-router";
import { Recipe } from "../../../types/Recipe";

export default function Page() {
  const gs = require("../../../static/styles/globalStyles");
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [recommendedRecipes, setRecommendedRecipes] = useState<Recipe[]>([]);
  const [filteredRecommendedRecipes, setFilteredRecommendedRecipes] = useState<Recipe[]>([]);
  const [allRecipes, setAllRecipes] = useState<Recipe[]>([]);
  const [filteredAllRecipes, setFilteredAllRecipes] = useState<Recipe[]>([]);
  const [suggestedRecipes, setSuggestedRecipes] = useState<Recipe[]>([]);
  const [filteredSuggestedRecipes, setFilteredSuggestedRecipes] = useState<Recipe[]>([]);
  const [babyId, setBabyId] = useState<number>(1);

  useEffect(() => {
    fetch("http://localhost:8081/api/v1/recipes")
      .then((response) => {
        return response.text().then((text) => {
          if (!response.ok) {
            throw new Error(`Error: ${response.status} - ${text}`);
          }

          try {
            return JSON.parse(text);
          } catch (error) {
            throw new Error(`Invalid JSON: ${text}`);
          }
        });
      })
      .then((data: Recipe[]) => {
        console.log(data);
        setAllRecipes(data);
        setFilteredAllRecipes(data);
      })
      .catch((error) => {
        console.error("Error fetching all recipes:", error);
      });
  }, []);

  useEffect(() => {
    fetch(`http://localhost:8081/api/v1/recipes/recommendations/${babyId}`)
      .then((response) => {
        if (!response.ok) {
          return response.text().then((text) => {
            throw new Error(`Error: ${response.status} - ${text}`);
          });
        }
        return response.json();
      })
      .then((data: Recipe[]) => {
        setRecommendedRecipes(data);
        setFilteredRecommendedRecipes(data);
      })
      .catch((error) => {
        console.error("Error fetching recommended recipes:", error);
      })
      .finally(() => setLoading(false));
  }, [babyId]);



  const handleSearch = (query: string) => {
    setSearchQuery(query);

    const filteredSuggested = allRecipes.filter((recipe) =>
      recipe.title.toLowerCase().includes(query.toLowerCase())
    );
    setFilteredSuggestedRecipes(filteredSuggested);

    const filteredRecommended = recommendedRecipes.filter((recipe) =>
      recipe.title.toLowerCase().includes(query.toLowerCase())
    );
    setFilteredRecommendedRecipes(filteredRecommended);
  };

  return (
    <View style={{ flex: 1 }}>

      <ScrollView contentContainerStyle={[gs.container, { paddingTop: 100, paddingBottom: 100, flexGrow: 1 }]}>
        <Text style={gs.headerText}>Recipe Suggestions</Text>
        <Text style={gs.bodyText}>
          Find suitable recipes based on your baby's age and preferences
        </Text>

        <View style={{ width: "90%" }}>
          <TextInput
            style={gs.input}
            placeholder="Search recipes..."
            value={searchQuery}
            onChangeText={handleSearch}
          />

          <View style={{ flexDirection: "row", gap: 10, marginVertical: 10, alignSelf: "flex-start" }}>

            <Link href="/recipes/favorites" style={gs.mainButton}>
              <Text style={gs.mainButtonText}>Favourite recipes</Text>
            </Link>
          </View>
        </View>

        <Text style={[gs.subHeaderText, { marginTop: 30 }]}>Suggested recipes</Text>

        {loading ? (
          <Text>Loading recipes...</Text>
        ) : filteredSuggestedRecipes.length === 0 ? (
          <Text>No suggested recipes found.</Text>
        ) : (
          filteredSuggestedRecipes.map((recipe, index) => (
            <Link key={index} href={`/recipes/recipeDetails?recipeTitle=${recipe.title}`}>
              <View style={[gs.card, { marginBottom: 10 }]}>
                <Text style={gs.cardTitle}>{recipe.title}</Text>
                <Text style={gs.cardContent}>{recipe.description}</Text>
              </View>
            </Link>
          ))
        )}

        <Text style={[gs.subHeaderText, { marginTop: 50 }]}>Recommended recipes</Text>

        {loading ? (
          <Text>Loading recommended recipes...</Text>
        ) : allRecipes.length === 0 ? (
          <Text>No recommended recipes found.</Text>
        ) : (
          filteredRecommendedRecipes.map((recipe, index) => (
            <Link key={index} href={`/recipes/recipeDetails?recipeTitle=${recipe.title}`}>
              <View style={[gs.card, { marginBottom: 10 }]}>
                <Text style={gs.cardTitle}>{recipe.title}</Text>
                <Text style={gs.cardContent}>{recipe.description}</Text>
              </View>
            </Link>
          ))
        )}

        <Text style={[gs.subHeaderText, { marginTop: 50 }]}>All Recipes</Text>

        {loading ? (
          <Text>Loading all recipes...</Text>
        ) : allRecipes.length === 0 ? (
          <Text>No recipes found.</Text>
        ) : (
          allRecipes.map((recipe, index) => (
            <Link key={index} href={`/recipes/recipeDetails?recipeTitle=${recipe.title}`}>
              <View style={[gs.card, { marginBottom: 10 }]}>
                <Text style={gs.cardTitle}>{recipe.title}</Text>
                <Text style={gs.cardContent}>{recipe.description}</Text>
              </View>
            </Link>
          ))
        )}
      </ScrollView>
    </View>
  );
}