import { useState, useEffect } from "react";
import { Text, View, TextInput, TouchableOpacity,ImageBackground,ScrollView ,Image} from "react-native";
import { useRouter } from "expo-router";
import { getToken } from "../../../utils/jwtStorage";
import { Picker } from "@react-native-picker/picker";

export default function AddBaby() {
  const gs = require("../../../static/styles/globalStyles");
  const router = useRouter();

  const [name, setName] = useState<string>("");
  const [birthDate, setBirthDate] = useState<string>("");
  const [genre, setGenre] = useState<string>("MALE");
  const [weight, setWeight] = useState<number | null>(null);
  const [height, setHeight] = useState<number | null>(null);
  const [cephalicPerimeter, setCephalicPerimeter] = useState<number | null>(null);
  const [foodPreference, setFoodPreference] = useState<string>("");

  const [loading, setLoading] = useState<boolean>(true);
  const [jwt, setJwt] = useState<string | null>(null);

  const [errorMessage, setErrorMessage] = useState<string>(""); // Para mostrar errores

  const apiUrl = process.env.EXPO_PUBLIC_API_URL;

  // Obtener token al montar
  useEffect(() => {
    const getUserToken = async () => {
      const token = await getToken();
      setJwt(token);
      setLoading(false);
    };
    getUserToken();
  }, []);

  // Función para traducir errores del backend
  const parseErrorMessage = (errorMessage: string) => {
    try {
      const errorObj = JSON.parse(errorMessage);
      if (errorObj.message && errorObj.message.includes("java.time.LocalDate")) {
        return "La fecha ingresada no es válida. Usa el formato YYYY-MM-DD.";
      }
      return "Error al guardar los datos. Revisa la información ingresada.";
    } catch (e) {
      return "Error inesperado al procesar la respuesta del servidor.";
    }
  };

  // Guardar bebé
  const handleSave = async () => {
    setErrorMessage(""); // Limpiar errores previos
    if (jwt) {
      try {
        const response = await fetch(`${apiUrl}/api/v1/baby`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${jwt}`,
          },
          body: JSON.stringify({
            name,
            birthDate,
            genre,
            weight,
            height,
            cephalicPerimeter,
            foodPreference,
          }),
        });

        if (response.ok) {
          router.push("/baby"); // Redirigir al listado
        } else {
          const errorText = await response.text();
          console.error("Error creating baby:", errorText);
          const friendlyMessage = parseErrorMessage(errorText);
          setErrorMessage(friendlyMessage); // Mostrar error amigable
        }
      } catch (error) {
        console.error("Error creating baby:", error);
        setErrorMessage("Ocurrió un error inesperado. Por favor, intenta nuevamente.");
      }
    } else {
      setErrorMessage("Error de autenticación. Por favor, inicia sesión nuevamente.");
    }
  };

  if (loading) {
    return (
      <View style={[gs.container, { paddingTop: 100 }]}>
        <Text>Cargando...</Text>
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
        <Image source={require("../../../static/images/Diaper.png")} style={{ position: 'absolute', top: "20%", right: "0%", width: 150, height: 80,transform: [{ rotate: '-15deg' }] }} />

        <Text style={[gs.headerText, { color: "#1565C0" }]}>Añade a tu </Text>
        <Text style={[gs.headerText, { color: "#1565C0", fontStyle: "italic"}]}>bebé </Text>


      <TextInput
              style={[gs.input, { padding: 12, borderRadius: 8, borderWidth: 1, borderColor: "#1565C0" , opacity:0.8, width:"80%"}]} 
              placeholder="Nombre"
        value={name}
        onChangeText={setName}
      />
      <TextInput
              style={[gs.input, { padding: 12, borderRadius: 8, borderWidth: 1, borderColor: "#1565C0" , opacity:0.8, width:"80%"}]} 
              placeholder="Fecha de nacimiento: YYYY-MM-DD"
        value={birthDate}
        onChangeText={setBirthDate}
      />
      <Picker
        selectedValue={genre}
        style={[gs.input, { padding: 12, borderRadius: 8, borderWidth: 1, borderColor: "#1565C0" , opacity:0.8, width:"80%",height:50}]} 
        onValueChange={(itemValue) => setGenre(itemValue)}
      >
        <Picker.Item label="Niño" value="MALE" />
        <Picker.Item label="Niña" value="FEMALE" />
        <Picker.Item label="Otro" value="OTHER" />
      </Picker>
     
      <TextInput
        style={[gs.input, { padding: 12, borderRadius: 8, borderWidth: 1, borderColor: "#1565C0" , opacity:0.8, width:"80%"}]} 
        placeholder="Peso (kg)"
        value={weight?.toString() || ""}
        keyboardType="numeric"
        onChangeText={(text) => setWeight(text ? parseFloat(text) : null)}
      />
      <TextInput
        style={[gs.input, { padding: 12, borderRadius: 8, borderWidth: 1, borderColor: "#1565C0" , opacity:0.8, width:"80%"}]} 
        placeholder="Altura (cm)"
        value={height?.toString() || ""}
        keyboardType="numeric"
        onChangeText={(text) => setHeight(text ? parseInt(text) : null)}
      />
      <TextInput
        style={[gs.input, { padding: 12, borderRadius: 8, borderWidth: 1, borderColor: "#1565C0" , opacity:0.8, width:"80%"}]} 
        placeholder="Perímetro cefálico (cm)"
        value={cephalicPerimeter?.toString() || ""}
        keyboardType="numeric"
        onChangeText={(text) => setCephalicPerimeter(text ? parseInt(text) : null)}
      />
      <TextInput
        style={[gs.input, { padding: 12, borderRadius: 8, borderWidth: 1, borderColor: "#1565C0" , opacity:0.8, width:"80%"}]} 
        placeholder="Preferencias "
        value={foodPreference}
        onChangeText={setFoodPreference}
      />

      {/* Mostrar error si existe */}
      {errorMessage !== "" && (
        <View style={{ backgroundColor: "#ffdddd", padding: 10, borderRadius: 8, marginTop: 15 }}>
          <Text style={{ color: "#cc0000", textAlign: "center" }}>{errorMessage}</Text>
        </View>
      )}

      <TouchableOpacity style={gs.mainButton} onPress={handleSave}>
        <Text style={gs.mainButtonText}>Guardar</Text>
      </TouchableOpacity>
    </ScrollView>
        </ImageBackground>
  );
}
