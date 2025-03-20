import { useState, useEffect } from "react";
import { Text, View, TextInput, ScrollView, Image } from "react-native";
import { Link } from "expo-router";
import { Recipe } from "../../../types/Recipe";
import { useAuth } from "../../../context/AuthContext";


const apiUrl = process.env.EXPO_PUBLIC_API_URL;

export default function Page() {
  const [allRecommendedRecipes, setAllRecommendedRecipes] = useState([]);
  const [userRecipes, setUserRecipes] = useState([]);
  const [recommendedRecipes, setRecommendedRecipes] = useState<Recipe[]>([]);
  const [allFilteredRecipes, setAllFilteredRecipes] = useState([]);
  const [userFilteredRecipes, setUserFilteredRecipes] = useState([]);
  const [recommendedFilteredRecipes, setRecommendedFilteredRecipes] = useState<Recipe[]>([]);

  const [searchQuery, setSearchQuery] = useState("");

  const [age, setAge] = useState<number | null>(null);

  const gs = require("../../../static/styles/globalStyles");

  const { user, token } = useAuth();

  useEffect(() => {
    obtainAllRecommendedRecipes();
    obtainUserRecipes();
  }, []);

  const obtainAllRecommendedRecipes = async (): Promise<boolean> => {
    try {
      const response = await fetch(`${apiUrl}/api/v1/recipes/recommended`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      if (response.ok) {
        const recipesData = await response.json();
        setAllRecommendedRecipes(recipesData);
        setAllFilteredRecipes(recipesData);
        return true;
      } else {
        return false;
      }
    } catch (error) {
      console.error('Error fetching recipes: ', error);
      setAllRecommendedRecipes([]);
      setAllFilteredRecipes([]);
      return false;
    }
  };

  const obtainUserRecipes = async (): Promise<boolean> => {
    try {
      let responseReceived = false;
      if (token && user) {
        const response = await fetch(`${apiUrl}/api/v1/recipes/user/${user.id}`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        });
        if (response.ok) {
          const recipesData = await response.json();
          setUserRecipes(recipesData);
          setUserFilteredRecipes(recipesData);
          responseReceived = true;
        }
      }
      return responseReceived ? true : false;
    } catch (error) {
      console.error('Error fetching user recipes: ', error);
      setUserRecipes([]);
      setUserFilteredRecipes([]);
      return false;
    }
  };

  const fetchRecommendedRecipes = async () => {
    if (age === null) return;
    try {
      const response = await fetch(`${apiUrl}/api/v1/recipes/recommended/${age}`, {
        method: "GET",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });
      if (!response.ok) {
        throw new Error("Error al obtener recetas recomendadas");
      }
      const data = await response.json();
      setRecommendedRecipes(data);
      setRecommendedFilteredRecipes(data);
    } catch (error) {
      console.error("Error:", error);
    }
  };

  const handleSearch = (query: string) => {

    setSearchQuery(query);

    const filteredAll = allRecommendedRecipes.filter((recipe: Recipe) =>
      recipe.name.toLowerCase().includes(query.toLowerCase())
    );
    const filteredUser = userRecipes.filter((recipe: Recipe) =>
      recipe.name.toLowerCase().includes(query.toLowerCase())
    );
    const filteredRecommended = recommendedRecipes.filter((recipe: Recipe) =>
      recipe.name.toLowerCase().includes(query.toLowerCase())
    );

    setAllFilteredRecipes(filteredAll);
    setUserFilteredRecipes(filteredUser);
    setRecommendedFilteredRecipes(filteredRecommended);

  };

  return (
    <View style={{ flex: 1 }}>

      <ScrollView contentContainerStyle={[gs.container, { paddingTop: 100, paddingBottom: 100, flexGrow: 1 }]}>
        <Text style={gs.headerText}>Recetas de nuestros expertos en nutrición</Text>
        <Text style={gs.bodyText}>
          Encuentre recetas adecuadas en función de la edad y las preferencias de su bebé
        </Text>

        <View style={{ width: "90%" }}>
          <TextInput
            style={gs.input}
            placeholder="Busca recetas..."
            value={searchQuery}
            onChangeText={handleSearch}
          />

          <View style={{ flexDirection: "row", gap: 10, marginVertical: 10, alignSelf: "flex-start" }}>
            <Link style={gs.mainButton} href={"/recipes/add"}>
              <Text style={gs.mainButtonText}>Añade una receta</Text>
            </Link>
          </View>
        </View>
        <View>
        </View>

        {allFilteredRecipes.length === 0 ? (
          <Text>No se encontraron recetas.</Text>
        ) : (
          allFilteredRecipes.map((recipe: Recipe) => (
            <Link href={`/recipes/detail?recipeId=${recipe.id}`}>
              <View style={[gs.card, { display: 'flex', flexDirection: 'row', gap: 10, marginBottom: 10 }]}>
                <View>
                  <Image
                    source={require('frontend/assets/adaptive-icon.png')}
                    style={{ width: 50, height: 50 }}
                  />
                </View>
                <View>
                  <Text style={gs.cardTitle}>{recipe.name}</Text>
                  <Text style={gs.cardContent}>{recipe.description}</Text>
                </View>
              </View>
            </Link>
          ))
        )

        }

        <Text style={[gs.headerText, { marginTop: 50 }]}>Recetas recomendadas según la edad</Text>

        <TextInput
          style={[gs.input, { width: "25%", marginHorizontal: "auto" }]}
          placeholder="Introduce la edad"
          keyboardType="numeric"
          value={age !== null ? age.toString() : ""}
          onChangeText={(text) => {
            // Convierte el texto en número entero y lo guarda en el estado
            const numericValue = parseInt(text.replace(/[^0-9]/g, ""), 10);
            setAge(isNaN(numericValue) ? null : numericValue);
          }}
          onSubmitEditing={fetchRecommendedRecipes}
          returnKeyType="done"
        />

        {recommendedFilteredRecipes.length === 0 ? (
          <Text>No se encontraron recetas.</Text>
        ) : (
          recommendedFilteredRecipes.map((recipe: Recipe) => (
            <Link href={`/recipes/detail?recipeId=${recipe.id}`}>
              <View style={[gs.card, { display: 'flex', flexDirection: 'row', gap: 10, marginBottom: 10 }]}>
                <View>
                  <Image
                    source={require('frontend/assets/adaptive-icon.png')}
                    style={{ width: 50, height: 50 }}
                  />
                </View>
                <View>
                  <Text style={gs.cardTitle}>{recipe.name}</Text>
                  <Text style={gs.cardContent}>{recipe.description}</Text>
                </View>
              </View>
            </Link>
          ))
        )}

        <Text style={[gs.headerText, { marginTop: 50 }]}>Todas tus recetas</Text>

        {userFilteredRecipes.length === 0 ? (
          <Text>No se encontraron recetas.</Text>
        ) : (
          userFilteredRecipes.map((recipe: Recipe) => (
            <Link href={`/recipes/detail?recipeId=${recipe.id}`}>
              <View style={[gs.card, { display: 'flex', flexDirection: 'row', gap: 10, marginBottom: 10 }]}>
                <View>
                  <Image
                    source={require('frontend/assets/adaptive-icon.png')}
                    style={{ width: 50, height: 50 }}
                  />
                </View>
                <View>
                  <Text style={gs.cardTitle}>{recipe.name}</Text>
                  <Text style={gs.cardContent}>{recipe.description}</Text>
                </View>
              </View>
            </Link>
          ))
        )}
      </ScrollView>
    </View>
  );
}
