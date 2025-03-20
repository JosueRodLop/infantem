import { useState, useEffect } from "react";
import { Text, View, TextInput, ScrollView, Image, ImageBackground } from "react-native";
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

    <ImageBackground
      source={require("../../../static/images/Background.png")}
      style={{ flex: 1, width: "100%", height: "100%", justifyContent: "center" }}
      imageStyle={{ resizeMode: "cover", opacity: 0.9 }}
    >
      <ScrollView contentContainerStyle={[gs.container, { paddingTop: 100, paddingBottom: 100, backgroundColor: "transparent" }]}>
        <Image source={require("../../../static/images/Bottle.png")} style={{ position: 'absolute', top: "0%", right: "70%", width: 150, height: 150, transform: [{ rotate: '15deg' }] }} />

        <Text style={{ color: "#1565C0", fontSize: 36, fontWeight: "bold", textAlign: "center", marginTop: -5 }}>
          Recetas Recomendadas Por Nuestros Expertos
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

          <View style={{ gap: 10, marginVertical: 10, alignSelf: "flex-start", alignItems: "center", width: "100%" }}>
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
    </ImageBackground>
  );
}
