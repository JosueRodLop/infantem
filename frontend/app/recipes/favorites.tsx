import { useState, useEffect } from "react";
import { Text, View, ScrollView, TouchableOpacity } from "react-native";
import NavBar from "../../components/NavBar";
import { Recipe } from "../../types/Recipe"; 
import { Link } from "expo-router"; 

export default function FavouriteRecipes() {
  const gs = require("../../static/styles/globalStyles");
  const [recipes, setRecipes] = useState<Recipe[]>([]); 
  const [loading, setLoading] = useState(true); 
  const [isLoggedIn, setIsLoggedIn] = useState(false); 
  const [userId, setUserId] = useState<number | null>(null); 

  useEffect(() => {
    const userToken = localStorage.getItem("userToken"); 
    if (userToken) {
      setIsLoggedIn(true);
      const id = 999; 
      setUserId(id);
    } else {
      setIsLoggedIn(false);
    }
  }, []);

  useEffect(() => {
    if (isLoggedIn && userId) {
      setLoading(true);
      fetch(`http://localhost:8081/api/v1/recipes/favorites/${userId}`)
        .then((response) => {
          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }
          return response.json(); 
        })
        .then((data: Recipe[]) => {
          setRecipes(data); 
        })
        .catch((error) => {
          console.error("Error fetching recipes:", error);
        })
        .finally(() => setLoading(false));
    }
  }, [isLoggedIn, userId]); 

  return (
    <View style={{ flex: 1 }}>
      <NavBar />

      <ScrollView contentContainerStyle={[gs.container, { paddingTop: 100, paddingBottom: 100 }]}>
        <Text style={gs.headerText}>Your Favourite Recipes</Text>

        {!isLoggedIn ? (
          <View style={{ flex: 1, justifyContent: "center", alignItems: "center" }}>
            <Text style={gs.bodyText}>You need to be logged in to see your favourite recipes.</Text>

            <Link href="/auth/LoginScreen" asChild>
              <TouchableOpacity>
                <Text style={gs.mainButton}>Log In</Text>
              </TouchableOpacity>
            </Link>
          </View>
        ) : loading ? (
          
          <Text>Loading recipes...</Text>
        ) : recipes.length === 0 ? (
          <Text>No favourite recipes found.</Text>
        ) : (
          recipes.map((recipe, index) => (
            <Link key={index} href={`/recipes/recipeDetails?recipeTitle=${recipe.title}`}>
                <View style={gs.card}>
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