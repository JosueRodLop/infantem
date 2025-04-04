import { useState, useEffect, useRef } from "react";
import { Text, View, TextInput, ScrollView, Image, Dimensions, TouchableOpacity } from "react-native";
import { Link, useRouter } from "expo-router";
import { Recipe } from "../../../types/Recipe";
import { useAuth } from "../../../context/AuthContext";

const apiUrl = process.env.EXPO_PUBLIC_API_URL;

interface ScrollEvent {
  nativeEvent: {
    contentOffset: {
      x: number;
      y: number;
    };
  };
}

export default function Page() {
  const gs = require("../../../static/styles/globalStyles");
  const { token } = useAuth();
  const router = useRouter();
  const scrollRef = useRef<ScrollView>(null);
  const screenWidth = Dimensions.get('window').width;
  const cardWidth = screenWidth < 500 ? 170 : 250;

  const [userRecipes, setUserRecipes] = useState<Recipe[]>([]);
  const [recommendedRecipes, setRecommendedRecipes] = useState<Recipe[]>([]);

  const [activeIndex, setActiveIndex] = useState(0);
  const [searchQuery, setSearchQuery] = useState("");


  useEffect(() => {
    fetchRecommendedRecipes();
    fetchUserRecipes();
  }, []);

  const fetchRecommendedRecipes= async (): Promise<boolean> => {
    try {
      const response = await fetch(`${apiUrl}/api/v1/recipes/recommended`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      if (!response.ok)
        throw new Error("Error fetching recipes");

      const recipesData = await response.json();
      setRecommendedRecipes(recipesData.content);
      return true;

    } catch (error) {
      console.error('Error fetching recipes: ', error);
      return false;
    }
  };

  const fetchUserRecipes = async (): Promise<boolean> => {
    try {
      const response = await fetch(`${apiUrl}/api/v1/recipes`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      if (!response.ok)
        throw new Error("Error fetching recipes");

      const recipesData = await response.json();
      setUserRecipes(recipesData.content);
      return true

    } catch (error) {
      console.error('Error fetching user recipes: ', error);
      return false;
    }
  };

  const handleSearch = (query: string) => {
    // TODO: Add this
  }


  const handleScroll = (event: ScrollEvent) => {
    const slideIndex = Math.round(event.nativeEvent.contentOffset.x / screenWidth);
    setActiveIndex(slideIndex);
  };
  const goToSlide = (index: number) => {
    scrollRef.current?.scrollTo({ x: index * screenWidth, animated: true });
    setActiveIndex(index);
  };

  return (

    <View style={{ flex: 1, backgroundColor: "#E3F2FD" }}>
      <ScrollView contentContainerStyle={{ padding: 0, paddingBottom: 0 }}>
        <View
          style={{
            width: "90%",
            backgroundColor: "rgba(255, 255, 255, 0.8)",
            borderRadius: 10,
            padding: 20,
            flexDirection: "row",
            justifyContent: "space-between",
            alignItems: "center",
            marginTop: 30,
            marginBottom: 10,
            marginLeft: "5%",
          }}
        >
          <View style={{ flex: 1 }}>
            <View style={{ flexDirection: "row", alignItems: "center", marginBottom: 8 }}>
              <Text style={{ fontSize: 28, fontWeight: "bold", color: "#1565C0", marginRight: 10 }}>
                Recetas
              </Text>

            </View>
            <Text style={{ fontSize: 16, color: "#1565C0" }}>
              Elige entre nuestra variedad de recetas
            </Text>
          </View>
          <View style={{ flex: 1, alignItems: "flex-end" }}>
            <View style={{ flexDirection: "row", width: "100%", backgroundColor: "white", alignItems: "center", borderWidth: 1, borderColor: "#1565C0", borderRadius: 8, opacity: 0.8, paddingHorizontal: 10 }}>
              <TextInput
                style={{ flex: 1, padding: 12 }}
                placeholder="Busca recetas... (Actualmente no disponible)"
                value={searchQuery}
                onChangeText={(text) => {
                  setSearchQuery(text);
                }}
              /* onSubmitEditing={() => {
                handleSearch(searchQuery);
              }} */
              />
              <Image source={require("../../../static/images/MagnifyingGlass.png")} style={{ width: 40, height: 40, opacity: searchQuery.trim() === "" ? 1 : 1 }} />
            </View>
          </View>
        </View>

        <View
          style={{
            width: "90%",
            flexDirection: "row",
            alignItems: "center",
            marginLeft: "5%",
            marginTop: 40,
            marginBottom: 10,
          }}
        >
          <Text style={{ fontSize: 24, fontWeight: "bold", color: "#1565C0", marginRight: 10 }}>
            Recetas recomendadas por nuestros expertos
          </Text>
          <View
            style={{
              flex: 1,
              height: 2,
              backgroundColor: "#1565C0",
              opacity: 0.6,
            }}
          />
        </View>



        {recommendedRecipes.length === 0 ? (
          <Text>No se encontraron recetas.</Text>
        ) : (
          <View
            style={{ flexDirection: 'row', justifyContent: 'center', alignItems: 'center', marginTop: 20, width: "100%" }}
          >
            <ScrollView
              ref={scrollRef}
              horizontal
              pagingEnabled
              showsHorizontalScrollIndicator={false}
              onScroll={handleScroll}
              scrollEventThrottle={16}
              style={{ paddingVertical: 50, marginLeft: 0, marginTop: 0 }}
            >
              <View style={{ flexDirection: 'row', flexWrap: 'wrap', justifyContent: 'center', gap: 20, paddingHorizontal: 20 }}>

                {recommendedRecipes.map((recipe, index) => (
                  <TouchableOpacity key={index} onPress={() => router.push(`/recipes/detail?recipeId=${recipe.id}`)}>
                    <View
                      key={index}
                      style={{
                        width: cardWidth,
                        backgroundColor: "#fff",
                        borderRadius: 10,
                        overflow: "hidden",
                        shadowColor: "#000",
                        shadowOpacity: 0.1,
                        shadowRadius: 6,
                        elevation: 3,
                        marginBottom: 30,
                      }}>
                      <Image
                        source={require("frontend/assets/adaptive-icon.png")}
                        style={{ width: "100%", height: 150 }}
                        resizeMode="cover"
                      />
                      {/* Contenido */}
                      <View style={{ padding: 12 }}>
                        <Text style={{ fontWeight: "bold", fontSize: 16, marginBottom: 4 }}>
                          {recipe.name}
                        </Text>
                      </View>
                    </View>
                  </TouchableOpacity>
                ))}
              </View>
            </ScrollView>
            {/* Flecha Izq */}
            <TouchableOpacity
              onPress={() => goToSlide(activeIndex - 1)}
              style={{ position: 'absolute', right: 0, width: "60%", top: '90%', zIndex: 1 }}
            >
              <Image
                source={require('../../../static/images/left-arrow.png')}
                style={{ width: 50, height: 50 }}
              />
            </TouchableOpacity>

            {/* Flecha derecha */}
            <TouchableOpacity
              onPress={() => goToSlide(activeIndex + 1)}
              style={{ position: 'absolute', right: "-55%", width: "100%", top: '90%', zIndex: 1, marginBottom: 20 }}
            >
              <Image
                source={require('../../../static/images/rigth-arrow.png')}
                style={{ width: 50, height: 50 }}
              />
            </TouchableOpacity>


          </View>
        )}

        <View style={{ width: "100%", alignItems: "center", justifyContent: "center", }}>

          <View
            style={{
              width: "90%",
              flexDirection: "row",
              alignItems: "center",
              marginLeft: "5%",
              marginTop: 40,
              marginBottom: 10,
            }}
          >
            <Text style={{ fontSize: 24, fontWeight: "bold", color: "#1565C0", marginRight: 10 }}>
              Todas tus recetas
            </Text>
            <View
              style={{
                flex: 1,
                height: 2,
                backgroundColor: "#1565C0",
                opacity: 0.6,
              }}
            />
          </View>
          <View style={{ gap: 10, marginVertical: 20, alignSelf: "flex-start", alignItems: "center", width: "100%" }}>
            <Link style={[gs.mainButton, { backgroundColor: "#1565C0" }]} href={"/recipes/add"}>
              <Text style={gs.mainButtonText}>Añade una receta</Text>
            </Link>
          </View>

          {userRecipes.length === 0 ? (
            <Text style={{ color: "#1565C0" }}>No se encontraron recetas 😥 </Text>
          ) : (
            <View style={{ flexDirection: 'row', flexWrap: 'wrap', justifyContent: 'center', gap: 20, paddingHorizontal: 10, marginTop: 20 }}>
              {
                userRecipes.map((recipe, index) => (
                  <TouchableOpacity key={index} onPress={() => router.push(`/recipes/detail?recipeId=${recipe.id}`)}>
                    <View
                      key={index}
                      style={{
                        width: cardWidth,
                        backgroundColor: "#fff",
                        borderRadius: 10,
                        overflow: "hidden",
                        shadowColor: "#000",
                        shadowOpacity: 0.1,
                        shadowRadius: 6,
                        elevation: 3,
                        marginBottom: 30,
                      }}>
                      <Image
                        source={require("frontend/assets/adaptive-icon.png")}
                        style={{ width: "100%", height: 150 }}
                        resizeMode="cover"
                      />
                      {/* Contenido */}
                      <View style={{ padding: 12 }}>
                        <Text style={{ fontWeight: "bold", fontSize: 16, marginBottom: 4 }}>
                          {recipe.name}
                        </Text>
                      </View>
                    </View>
                  </TouchableOpacity>
                ))}
            </View>
          )}
        </View>
      </ScrollView>
    </View>
  );
}
