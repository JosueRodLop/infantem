import { useState, useEffect } from "react";
import { Text, View, TouchableOpacity, TextInput, ScrollView, Image,ImageBackground } from "react-native";
import { Link } from "expo-router";
import { Recipe } from "../../../types/Recipe";
// import { getToken } from "../../../utils/jwtStorage"
// import { jwtDecode } from "jwt-decode";
import { useAuth } from "../../../context/AuthContext";

// const jwt = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTc0MTkwODQxNSwiaWF0IjoxNzQxODIyMDE1LCJhdXRob3JpdGllcyI6WyJ1c2VyIl19.bf4c5fTej1qEcu03Bt8lTbPIYjw9aVIySOqbtjRNalTZmeUP4Li1-OjFNOAcwfNExThBPyppJxnkhiTS38aUuA'
// console.log(jwt)

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



  useEffect(() => {
    console.log("Updated allRecipes:", allRecipes);
  }, [allRecipes]);

  useEffect(() => {
    console.log("AAAAAAAAAAA")
    console.log("Updated recommendedRecipes:", recommendedRecipes);
  }, [recommendedRecipes]);

  const fetchRecommendedRecipes = async () => {
    if (age === null) return;

    try {
      const response = await fetch(`${apiUrl}/api/v1/recipes/recommended?age=${age}`, {
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
    } catch (error) {
      console.error("Error:", error);
    }
  };

  const handleSearch = (query: string) => {
    setSearchQuery(query);

    const filteredSuggested = allRecipes.filter((recipe: Recipe) =>
      recipe.name.toLowerCase().includes(query.toLowerCase())
    );
    // setFilteredSuggestedRecipes(filteredSuggested);

    const filteredRecommended = recommendedRecipes.filter((recipe: Recipe) =>
      recipe.name.toLowerCase().includes(query.toLowerCase())
    );
    setFilteredRecommendedRecipes(filteredRecommended);
  };

  return (

      <ImageBackground
              source={require("../../../static/images/Background.png")}
              style={{ flex: 1, width: "100%", height: "100%", justifyContent: "center" }}
              imageStyle={{ resizeMode: "cover", opacity: 0.9 }}
      >      
    <ScrollView contentContainerStyle={[gs.container, { paddingTop: 100, paddingBottom: 100, backgroundColor:"transparent" }]}>        
    <Image source={require("../../../static/images/Bottle.png")} style={{ position: 'absolute', top: "0%", right: "70%", width: 150, height: 150,transform: [{ rotate: '15deg' }] }} />

        <Text style={{ color: "#1565C0", fontSize: 36, fontWeight: "bold", textAlign: "center", marginTop: -5 }}>
          Recetas Recomendadas 
        </Text>
        

        <Text style={[gs.bodyText, { color: "#1565C0" }]}>
          ¡Encuentre recetas perfectas para su bebé! 
        </Text>
        <Text style={[gs.bodyText, { color: "#1565C0" }]}>
        Adecuadas en función de su edad y preferencias 
        </Text>

        <View style={{ width: "100%" }}>
        <View style={{ flexDirection: "row", alignItems: "center", borderWidth: 1, borderColor: "#1565C0", borderRadius: 8, opacity: 0.8, paddingHorizontal: 10 }}>
            <TextInput
              style={{ flex: 1, padding: 12 }}
              placeholder="Busca recetas..."
              value={searchQuery}
              onChangeText={setSearchQuery}
            />
              <Image source={require("../../../static/images/MagnifyingGlass.png")} style={{ width: 40, height: 40, opacity: searchQuery.trim() === "" ? 1 : 1 }} />
          </View>

          <View style={{ gap: 10, marginVertical: 10, alignSelf: "flex-start",alignItems: "center",width:"100%" }}>

            <Link href="/recipes/favorites" style={gs.mainButton}>
              <Text style={gs.mainButtonText}>Recetas favoritas</Text>
            </Link>
            <Link style={gs.mainButton} href={"/recipes/add"}>
              <Text style={gs.mainButtonText}>Añade una receta</Text>
            </Link>

          </View>
        </View>


        <Text style={[gs.subHeaderText, { marginTop: 50 }]}>Recetas recomendadas</Text>

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

        {loading ? (
          <Text>Cargando recetas recomendadas...</Text>
        ) : recommendedRecipes.length === 0 ? (
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

        <Text style={[gs.subHeaderText, { marginTop: 50 }]}>Todas tus recetas</Text>

        {loading ? (
          <Text>Cargando recetas recomendadas...</Text>
        ) : allRecipes.length === 0 ? (
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
    </ImageBackground>
  );
}
