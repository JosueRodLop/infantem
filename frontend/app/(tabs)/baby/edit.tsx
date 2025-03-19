import { useState, useEffect } from "react";
import { Text, View, TextInput, TouchableOpacity,ImageBackground,Image,ScrollView } from "react-native";
import { useRouter } from "expo-router";
import { getToken } from "../../../utils/jwtStorage";
import { useLocalSearchParams } from 'expo-router';

export default function EditBaby() {
  const gs = require("../../../static/styles/globalStyles");
  const router = useRouter();
  const [baby, setBaby] = useState(null);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState(""); // Estado para errores
  const apiUrl = process.env.EXPO_PUBLIC_API_URL;
  const { id } = useLocalSearchParams();

  // Fetch datos iniciales del bebé
  useEffect(() => {
    const fetchBabyData = async () => {
      const jwt = await getToken();
      if (id) {
        try {
          const response = await fetch(`${apiUrl}/api/v1/baby/${id}`, {
            headers: { "Authorization": `Bearer ${jwt}` },
          });
          console.log("Response received:", response);

          const text = await response.text();
          console.log("Response body:", text);

          if (!response.ok) {
            throw new Error(`Error: ${response.status} - ${text}`);
          }

          const data = JSON.parse(text);
          setBaby(data);
        } catch (error) {
          console.error("Error fetching baby data:", error);
          setErrorMessage("Failed to load baby data. Please try again later.");
        } finally {
          setLoading(false);
        }
      } else {
        setLoading(false);
      }
    };

    fetchBabyData();
  }, [id]);

  
  // Función para traducir el error técnico a un mensaje claro
  const parseErrorMessage = (errorMessage: string) => {
    try {
      const errorObj = JSON.parse(errorMessage);
      if (errorObj.message && errorObj.message.includes("java.time.LocalDate")) {
        return "La fecha ingresada no es válida. Usa el formato YYYY-MM-DD.";
      }
      // Puedes agregar más chequeos según otros errores del backend
      return "Error al guardar los datos. Revisa la información ingresada.";
    } catch (e) {
      return "Error inesperado al procesar la respuesta del servidor.";
    }
  };


  // Función para guardar cambios
  const handleSave = async () => {
    setErrorMessage(""); // Limpiar errores anteriores
    const jwt = await getToken();
  
    console.log("JWT for PUT request:", jwt);
  
    if (!jwt) {
      console.error("No JWT token found, aborting request");
      setErrorMessage("Error de autenticación. Por favor, inicia sesión nuevamente.");
      return;
    }
  
    try {
      const response = await fetch(`${apiUrl}/api/v1/baby/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${jwt}`,
        },
        body: JSON.stringify(baby),
      });
  
      if (response.ok) {
        router.push("/baby");
      } else {
        const errorText = await response.text();
        console.error("Error updating baby:", errorText);
        
        // Usar la función para interpretar y mostrar error amigable
        const friendlyMessage = parseErrorMessage(errorText);
        setErrorMessage(friendlyMessage);
      }
    } catch (error) {
      console.error("Error updating baby:", error);
      setErrorMessage("Ocurrió un error inesperado. Por favor, intenta nuevamente.");
    }
  };

  if (loading) {
    return (
      <View style={{ flex: 1 }}>
        <Text>Loading...</Text>
      </View>
    );
  }

  if (!baby) {
    return (
      <View style={{ flex: 1 }}>
        <Text>No baby data found.</Text>
      </View>
    );
  }

  return (
    <ImageBackground
              source={require("../../../static/images/Background.png")}
              style={{ flex: 1, width: "100%", height: "100%", justifyContent: "center" }}
              imageStyle={{ resizeMode: "cover", opacity: 0.9 }}
>      
    <ScrollView contentContainerStyle={[gs.container, { paddingTop: 100, paddingBottom: 100, backgroundColor:"transparent" }]}>
    <Image source={require("../../../static/images/Diaper.png")} style={{ position: 'absolute', top: "20%", right: "10%", width: 120, height: 60,transform: [{ rotate: '-15deg' }] }} />

    <View style={{ flex: 1,width:"100%",marginTop:-140,height:"100%" }}>
      {baby && (
        <View style={[gs.container, { paddingTop: 100, paddingBottom: 100 ,backgroundColor:"transparent",width:"100%"}]}>
          <Text 
          style={[gs.headerText, { color: "#1565C0" }]}
          >Modifica la información de tu</Text>
          <Text 
          style={[gs.headerText, { color: "#1565C0",fontStyle: "italic",marginTop:-10,marginBottom:50 }]}
          >bebé</Text>

          <TextInput
              style={[gs.input, { padding: 12, borderRadius: 8, borderWidth: 1, borderColor: "#1565C0" , opacity:0.8, width:"80%"}]} 
            placeholder="Nombre"
            value={baby.name}
            onChangeText={(text) => setBaby({ ...baby, name: text })}
          />
          <TextInput
              style={[gs.input, { padding: 12, borderRadius: 8, borderWidth: 1, borderColor: "#1565C0" , opacity:0.8, width:"80%"}]} 
              placeholder="Fecha de nacimiento"
            value={baby.birthDate}
            onChangeText={(text) => setBaby({ ...baby, birthDate: text })}
          />
          <TextInput
              style={[gs.input, { padding: 12, borderRadius: 8, borderWidth: 1, borderColor: "#1565C0" , opacity:0.8, width:"80%"}]} 
              placeholder="Género"
            value={baby.genre}
            onChangeText={(text) => setBaby({ ...baby, genre: text })}
          />
          <TextInput
              style={[gs.input, { padding: 12, borderRadius: 8, borderWidth: 1, borderColor: "#1565C0" , opacity:0.8, width:"80%"}]} 
              placeholder="Peso"
            value={baby.weight}
            onChangeText={(text) => setBaby({ ...baby, weight: text })}
          />
          <TextInput
              style={[gs.input, { padding: 12, borderRadius: 8, borderWidth: 1, borderColor: "#1565C0" , opacity:0.8, width:"80%"}]} 
              placeholder="Altura"
            value={baby.height}
            onChangeText={(text) => setBaby({ ...baby, height: text })}
          />
          <TextInput
              style={[gs.input, { padding: 12, borderRadius: 8, borderWidth: 1, borderColor: "#1565C0" , opacity:0.8, width:"80%"}]} 
              placeholder="Perímetro cefálico"
            value={baby.cephalicPerimeter}
            onChangeText={(text) => setBaby({ ...baby, cephalicPerimeter: text })}
          />
          <TextInput
              style={[gs.input, { padding: 12, borderRadius: 8, borderWidth: 1, borderColor: "#1565C0" , opacity:0.8, width:"80%"}]} 
              placeholder="Preferencias alimentarias"
            value={baby.foodPreference}
            onChangeText={(text) => setBaby({ ...baby, foodPreference: text })}
          />

          {/* Botón de Guardar */}
          <TouchableOpacity style={gs.mainButton} onPress={handleSave}>
            <Text style={gs.mainButtonText}>Guardar</Text>
          </TouchableOpacity>

          {/* Mostrar error si existe */}
          {errorMessage !== "" && (
            <Text style={{ color: "red", marginTop: 10, textAlign: "center" }}>
              {errorMessage}
            </Text>
          )}
        </View>
      )}
    </View>
    </ScrollView>
    </ImageBackground>
  );
}
