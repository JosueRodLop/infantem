import { Link, Redirect, SplashScreen } from "expo-router";
import { View, Text, Image, Dimensions, ScrollView } from "react-native";
import { useAuth } from "../context/AuthContext";
import { useFonts } from "expo-font";
import { useEffect } from "react";

export default function Index() {
  const { isAuthenticated } = useAuth();
  const gs = require("../static/styles/globalStyles");
  const { height } = Dimensions.get("window");

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

  const { width } = Dimensions.get("window");
  const isMobile = width < 768;

  useEffect(() => {
    if (fontsLoaded) {
      SplashScreen.hideAsync();
    }
  }, [fontsLoaded]);

  if (!fontsLoaded) return null;
  if (isAuthenticated) return <Redirect href="recipes" />;

  return (
    <View style={{ flex: 1, backgroundColor: "#E3F2FD" }}>
      <ScrollView contentContainerStyle={{ padding: 10, paddingBottom: 200 }}>
      <View style={{ width: "110%", height: height  }}>
        <Image
          source={require("../static/images/Background.png")}
          style={{
            width: "100%",
            height: "100%",
            resizeMode: "cover",
          }}
        />
        <View
            style={{
              flex: 1,
              justifyContent: "center",
              paddingHorizontal:"10%",
              position: "absolute",
              top: 160,}}
          >
            <Text
              style={{
                color: "#1565C0",
                fontFamily: "Loubag-Regular",
                fontSize: 36,
              }}
            >
              El mejor cuidado{" "}
              
            </Text>
            <Text style={{
                color: "#1565C0",
                fontFamily: "Loubag-Bold",
                fontSize: 50,
                marginBottom: 30,}}>para tu bebé</Text>

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
        
       
{/*---------------------------------------------------------------------------------------------------------------------------- */}
        <View
      style={{
        backgroundColor: "#BBDEFB",
        padding: 30,
        borderRadius: 20,
        marginHorizontal: 20,
        marginTop: 60,
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
              style={{ width: 250, height: 250, marginBottom: 0}}
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
