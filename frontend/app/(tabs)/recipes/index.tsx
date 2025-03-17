import { useState, useEffect } from "react";
import { Text, View, TextInput, ScrollView, Image } from "react-native";
import { Link } from "expo-router";
import { Recipe } from "../../../types/Recipe";
// import { getToken } from "../../../utils/jwtStorage"
// import { jwtDecode } from "jwt-decode";
import { useAuth } from "../../../context/AuthContext";


const apiUrl = process.env.EXPO_PUBLIC_API_URL;

export default function Page() {
  const gs = require("../../../static/styles/globalStyles");
  const [searchQuery, setSearchQuery] = useState("");
  const [recommendedRecipes, setRecommendedRecipes] = useState<Recipe[]>([]);
  const [filteredRecommendedRecipes, setFilteredRecommendedRecipes] = useState<Recipe[]>([]);
  const [loading, setLoading] = useState(true);
  const [babyId, setBabyId] = useState<number>(1);
  const [allRecipes, setAllRecipes] = useState([]);
  const [age, setAge] = useState<number | null>(null);

  const { isAuthenticated, isLoading, user, token, setUser, checkAuth, signOut } = useAuth();

  useEffect(() => {
    if (token) {
      fetch(`${apiUrl}/api/v1/recipes/recommended?age=${age}`, {
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
        .then((data: Recipe[]) => {
          setRecommendedRecipes(data);
          // setFilteredRecommendedRecipes(data);
        })
        .catch((error) => {
          console.error("Error fetching recommended recipes:", error);
        })
        .finally(() => setLoading(false));
    } else {
      console.log("No jwt token")
    }
  }, [token]);

  useEffect(() => {
    if (token && user) {
      fetch(`${apiUrl}/api/v1/recipes/user/${user.id}`, {
        headers: {
          "Authorization": "Bearer " + token
        }
      })
        .then((response) => {
          console.log("Response received:", response);

          return response.text().then((text) => {
            console.log("Response body:", text);

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
        .then((data) => {
          console.log("Parsed JSON:", data);
          setAllRecipes(data);
        })
        .catch((error) => {
          console.error("Error fetching all recipes:", error);
        });
    } else {
      console.log("No jwt token")
    }
  }, [token]);

  useEffect(() => {
    fetch(`${apiUrl}/api/v1/recipes/recommendations/${babyId}`)
      .then((response) => {
        if (!response.ok) {
          return response.text().then((text) => {
            throw new Error(`Error: ${response.status} - ${text}`);
          });
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
          recommendedRecipes.map((recipe: Recipe) => (

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

          ))
        )}

        <Text style={[gs.headerText, { marginTop: 50 }]}>Todas tus recetas</Text>

        {userFilteredRecipes.length === 0 ? (
          <Text>No se encontraron recetas.</Text>
        ) : (
          allRecipes.map((recipe: Recipe) => (

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

          ))
        )}
      </ScrollView>
    </View>
  );
}
