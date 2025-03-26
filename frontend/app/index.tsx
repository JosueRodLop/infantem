import { Link, Redirect, SplashScreen } from "expo-router";
import { View, Text, Image, Dimensions, ScrollView, ImageBackground } from "react-native";
import { useAuth } from "../context/AuthContext";
import { useFonts } from "expo-font";
import { useEffect, useState } from "react";


export default function Index() {
  const { isAuthenticated } = useAuth();
  const gs = require("../static/styles/globalStyles");
  const { height } = Dimensions.get("window");

  const [isMobile, setIsMobile] = useState<boolean>(Dimensions.get("window").width < 768);

  // Detecta el cambio de tamaño de la ventana
  useEffect(() => {
    const handleResize = () => {
      const { width } = Dimensions.get("window");
      setIsMobile(width < 768); // Establece si la resolución es móvil
    };

    const subscription = Dimensions.addEventListener('change', handleResize);

    return () => {
      subscription?.remove();
    };
  }, []);

  const [fontsLoaded] = useFonts({
    "Loubag-Regular": require("../assets/fonts/Loubag-Regular.ttf"),
    "Loubag-Medium": require("../assets/fonts/Loubag-Medium.ttf"),
    "Loubag-Bold": require("../assets/fonts/Loubag-Bold.ttf"),
    "Loubag-Light": require("../assets/fonts/Loubag-Light.ttf"),
  });

  const features = [
    {
      title: "1. Recomendaciones",
      desc: "Recetas personalizadas para ti y tu bebé según sus necesidades.",
      icon: require("../static/images/Pot.png"),
    },
    {
      title: "2. Detección de Alergias",
      desc: "Identifica ingredientes peligrosos con alertas inteligentes.",
      icon: require("../static/images/Inhaler.png"),
    },
    {
      title: "3. MarketPlace",
      desc: "Compra productos confiables y aprobados en un solo lugar.",
      icon: require("../static/images/Marketplace.png"),
    },
    {
      title: "4. Calendario Inteligente",
      desc: "Organiza hitos, recordatorios y seguimiento del desarrollo.",
      icon: require("../static/images/Calendar.png"),
    },
  ];

  const [currentReviewIndex, setCurrentReviewIndex] = useState(0);  // Estado para rotar las reseñas

  const reviews = [
    {
      name: "María Gómez",
      text: "Me ha ayudado mucho a planificar las comidas de mi bebé, ¡es muy fácil de usar y súper útil!",
      stars: 5,
    },
    {
      name: "Carlos Pérez",
      text: "¡Una herramienta increíble! Las recetas personalizadas son perfectas para las necesidades de mi bebé.",
      stars: 5,
    },
    {
      name: "Lucía Martínez",
      text: "Me encanta que puedo controlar la dieta de mi bebé y las alertas por alergias me han sido muy útiles.",
      stars: 5,
    },
  ];

  useEffect(() => {
    // Rotación de las reseñas cada 3 segundos
    const intervalId = setInterval(() => {
      setCurrentReviewIndex((prevIndex) => (prevIndex + 1) % reviews.length); // Cambiar la reseña
    }, 5000);

    return () => clearInterval(intervalId); // Limpiar el intervalo al desmontar
  }, [reviews.length]);

  // Removed duplicate declarations of width and isMobile

  useEffect(() => {
    if (fontsLoaded) {
      SplashScreen.hideAsync();
    }
  }, [fontsLoaded]);

  if (!fontsLoaded) return null;
  if (isAuthenticated) return <Redirect href="recipes" />;

  const renderStars = (stars: number) => {
    const starArray = Array(stars).fill(true); // Crear un array de estrellas
    return (
      <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 10, marginTop: -2
       }}>
        {starArray.map((_, index) => (
          <Image
            key={index}
            source={require("../static/images/star.png")} // Asegúrate de tener una imagen de estrella
            style={gs.starIcon}
          />
        ))}
      </View>
    );
  };

  return (
    <View style={{ flex: 1, backgroundColor: "#E3F2FD" }}>
      <ScrollView contentContainerStyle={{ padding: 10, paddingBottom: 200 }}>
        <View style={{ width: "110%", height: height, marginHorizontal: -10, marginTop: -10, }}>
          <ImageBackground
            source={isMobile
              ? require("../static/images/Background3.0.png") // Imagen para móvil
              : require("../static/images/Background2.0.png")} // Imagen para web
            style={gs.banner2}
            imageStyle={gs.bannerImage2}
          >
          </ImageBackground>
          <View
            style={{
              flex: 1,
              justifyContent: "center",
              paddingHorizontal: "10%",
              position: "absolute",
              top: isMobile ? 290: 160,
            }}
          >
            <Text
              style={{
                color: "#1565C0",
                fontFamily: "Loubag-Regular",
                fontSize: isMobile ? 30 : 36,
              }}>El mejor cuidado{" "}

            </Text>
            <Text style={{
              color: "#1565C0",
              fontFamily: "Loubag-Bold",
              fontSize: isMobile ? 45 : 50,
              marginBottom: 30,
            }}>para tu bebé</Text>

            <Link
              style={[
                gs.secondaryButton,
                {
                  width: 180,
                  backgroundColor: "#1565C0",
                  padding: 12,
                  borderRadius: 8,
                  borderWidth: 1,
                  borderColor: "#1565C0",
                  alignItems: "center",
                },
              ]}
              href={"/signup"}
            >
              <Text
                style={[
                  gs.secondaryButtonText,
                  { color: "white", fontSize: 16, fontWeight: "bold" },
                ]}
              >
                ¡Empieza ahora!
              </Text>
            </Link>
          </View>
        </View>

        {/* Scroll horizontal de reseñas - Ahora está sobre "¿Cómo te ayudamos?" */}
        <View style={gs.reviewsContainer}>
          <Text style={gs.reviewsTitle}>Lo que dicen nuestros usuarios</Text>
          <View style={gs.reviewsCardContainer}>
            <View style={gs.reviewCard}>
              {/* Nombre y reseña */}
              <View style={gs.reviewHeader}>
                <Text style={gs.reviewName}>{reviews[currentReviewIndex].name}</Text>
              </View>
              {renderStars(reviews[currentReviewIndex].stars)} {/* Mostrar las estrellas debajo del nombre */}
              <Text style={gs.reviewText}>{reviews[currentReviewIndex].text}</Text>
            </View>
          </View>
        </View>

        {/* Sección "¿Cómo te ayudamos?" */}
        <View
          style={{
            backgroundColor: "#BBDEFB",
            padding: 30,
            borderRadius: 20,
            marginHorizontal: 20,
            marginTop: -200,
            marginBottom: 100,
          }}
        >
          <Text
            style={{
              fontSize: 28,
              fontFamily: "Loubag-Bold",
              textAlign: "center",
              color: "#0D47A1",
              marginBottom: 30,
            }}
          >
            ¿Cómo te ayudamos?
          </Text>

          <View
            style={{
              flexDirection: isMobile ? "column" : "row",
              justifyContent: "space-between",
              alignItems: "center",
              gap: 30,
              flexWrap: "wrap",
            }}
          >
            {features.map((feature, index) => (
              <View
                key={index}
                style={{
                  width: isMobile ? "100%" : "23%",
                  alignItems: "center",
                }}
              >
                <Image
                  source={feature.icon}
                  style={{ width: 250, height: 250, marginBottom: 0 }}
                  resizeMode="contain"
                />
                <Text
                  style={{
                    fontSize: 18,
                    fontFamily: "Loubag-Bold",
                    textAlign: "center",
                    marginBottom: 10,
                    color: "#0D47A1",
                  }}
                >
                  {feature.title}
                </Text>
                <Text
                  style={{
                    fontSize: 14,
                    fontFamily: "Loubag-Regular",
                    textAlign: "center",
                    color: "#0D47A1",
                  }}
                >
                  {feature.desc}
                </Text>
              </View>
            ))}
          </View>
        </View>
      </ScrollView>
    </View>
  );
}
