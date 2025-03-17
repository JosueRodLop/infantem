import { useState, useEffect } from "react";
import { Text, View, TouchableOpacity, ImageBackground, ScrollView, Image, Alert } from "react-native";
import { Link, useRouter } from "expo-router";
import { getToken } from "../../../utils/jwtStorage";
import { Ionicons } from "@expo/vector-icons"; 

export default function BabyInfo() {
  const gs = require("../../../static/styles/globalStyles");
  const [babies, setBabies] = useState([]);
  const router = useRouter();
  const [jwt, setJwt] = useState<string | null>(null);

  const apiUrl = process.env.EXPO_PUBLIC_API_URL;

  useEffect(() => {
    const getUserToken = async () => {
      const token = await getToken();
      setJwt(token);
    };
    getUserToken();
  }, []);

  useEffect(() => {
    const fetchBabies = async () => {
      try {
        const token = await getToken();
        const response = await fetch(`${apiUrl}/api/v1/baby`, {
          headers: { "Authorization": `Bearer ${token}` },
        });

        if (!response.ok) {
          throw new Error(`Error: ${response.status}`);
        }

        const data = await response.json();
        setBabies(data);
      } catch (error) {
        console.error("Error fetching babies:", error);
      }
    };

    fetchBabies();
  }, []);

  const handleEditBaby = (id: number) => {
    router.push(`/baby/edit?id=${id}`);
  };

  const handleDeleteBaby = async (id: number) => {
    Alert.alert(
      "Eliminar bebé",
      "¿Estás seguro de que quieres eliminar este perfil?",
      [
        { text: "Cancelar", style: "cancel" },
        {
          text: "Eliminar",
          style: "destructive",
          onPress: async () => {
            if (jwt) {
              try {
                const response = await fetch(`${apiUrl}/api/v1/baby/${id}`, {
                  method: "DELETE",
                  headers: {
                    "Authorization": `Bearer ${jwt}`,
                  },
                });

                if (response.ok) {
                  setBabies(babies.filter((baby) => baby.id !== id));
                } else {
                  console.error("Error deleting baby:", response.statusText);
                }
              } catch (error) {
                console.error("Error deleting baby:", error);
              }
            }
          },
        },
      ]
    );
  };

  return (
    <ImageBackground 
      source={require("../../../static/images/Background.png")}  
      style={{ flex: 1, width: "100%", height: "100%" }} 
      imageStyle={{ resizeMode: "cover", opacity: 0.9 }}
    >
      {/* ScrollView permite desplazarse sin que se corte en pantallas pequeñas */}
      <ScrollView contentContainerStyle={{ flexGrow: 1, padding: 20 }}>
        
        {/* 📌 Encabezado */}
        <Text style={{ color: "#1565C0", fontSize: 36, fontWeight: "bold", textAlign: "center", marginBottom: 10 }}>
          Información de los <Text style={{ fontStyle: "italic" }}>bebés</Text>
        </Text>
        
        <Text style={{ color: "#1565C0", textAlign: "center", fontSize: 16, marginBottom: 20 }}>
          Revisa y gestiona la información de tu bebé.
        </Text>

        {/* 📌 Botón de añadir bebé */}
        <View style={{ flexDirection: "row", justifyContent: "center", marginBottom: 20 }}>
          <Link style={[gs.mainButton, { flexDirection: "row", alignItems: "center", padding: 10 }]} href={"/baby/add/"}>
            <Ionicons name="add-circle-outline" size={20} color="white" />
            <Text style={[gs.mainButtonText, { marginLeft: 5 }]}>Añadir bebé</Text>
          </Link>
        </View>

        {/* 📌 Lista de bebés */}
        <Text style={[gs.subHeaderText, { color: "#1565C0",marginBottom: 10 ,fontWeight: "bold"}]}>Mis bebés registrados</Text>

        {babies.length === 0 ? (
          <Text style={{ textAlign: "center", color: "gray", fontSize: 16 }}>
            No hay bebés registrados aún.
          </Text>
        ) : (
          babies.map((baby) => (
            <View key={baby.id} style={[gs.card, { width:"100%",flexDirection: "row", alignItems: "center", padding: 15, marginBottom: 10 }]}>
              
              {/* 📌 Imagen de perfil del bebé */}
              <Image
                source={require("../../../static/images/baby-placeholder.png")} // Imagen por defecto
                style={{ width: 70, height: 70, borderRadius: 50, marginRight: 15 }}
              />

              {/* 📌 Información del bebé */}
              <View style={{ flex: 1 }}>
                <Text style={[gs.cardTitle, { fontSize: 18 }]}>{baby.name}</Text>
                <Text style={gs.cardContent}>📅 Fecha de nacimiento: {baby.birthDate}</Text>
                <Text style={gs.cardContent}>🍼 Preferencia alimentaria: {baby.foodPreference}</Text>
                <Text style={gs.cardContent}>⚖️ Peso: {baby.weight} kg | 📏 Altura: {baby.height} cm</Text>
              </View>

              {/* 📌 Acciones de editar y eliminar */}
              <View style={{ flexDirection: "row", gap: 10 }}>
                <TouchableOpacity onPress={() => handleEditBaby(baby.id)}>
                  <Ionicons name="create-outline" size={24} color="#1565C0" />
                </TouchableOpacity>
                <TouchableOpacity onPress={() => handleDeleteBaby(baby.id)}>
                  <Ionicons name="trash-outline" size={24} color="red" />
                </TouchableOpacity>
              </View>

            </View>
          ))
        )}

      </ScrollView>
    </ImageBackground>
  );
}
