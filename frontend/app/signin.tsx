import { Link, router } from "expo-router";
import { useState } from "react";
import { Text, View, TouchableOpacity, TextInput, Image, ScrollView } from "react-native";
import { storeToken } from "../utils/jwtStorage";
import { useAuth } from "../context/AuthContext";

export default function Signin() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const apiUrl = process.env.EXPO_PUBLIC_API_URL;
  const { isAuthenticated, checkAuth } = useAuth();


  const gs = require("../static/styles/globalStyles");

  const handleSubmit = async () => {
    try {
      const response = await fetch(`${apiUrl}/api/v1/auth/signin`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username: username,
          password: password,
        }),
      });

      if (!response.ok) {
        setErrorMessage("Algo no ha ido bien. Verifica tus credenciales.");
        return;
      }

      const data = await response.json();
      await storeToken(data.token);
      await checkAuth();
      router.replace("/recipes")

    } catch (error) {
      console.error("An error occurred: ", error);
    }
  };

  return (
    <ScrollView contentContainerStyle={{ flexGrow: 1 }} showsVerticalScrollIndicator={false}>
      <View style={[gs.container, { justifyContent: "center", alignItems: "center", backgroundColor: "#E3F2FD", flex: 1, paddingVertical: 40 }]}>
        
        {/* 📌 Imagen de perfil */}
        <Image source={require("../static/images/profile.webp")} style={[{ width: 100, height: 100, borderRadius: 50, marginBottom: 20 }]} />

        {/* 📌 Tarjeta de inicio de sesión */}
        <View style={[gs.card, { maxWidth: 400, width: "90%", padding: 25, borderRadius: 15, backgroundColor: "white", shadowColor: "#000", shadowOpacity: 0.1, shadowRadius: 10, elevation: 5 }]}>
          
          <Text style={{ fontSize: 24, fontWeight: "bold", color: "#1565C0", textAlign: "center", marginBottom: 15 }}>
            Iniciar Sesión
          </Text>

          <Text style={{ fontSize: 15, color: "#1565C0", textAlign: "center", marginBottom: 15 }}>
            Introduce tu cuenta de siempre en Infantem o regístrate si es tu primera vez.
          </Text>

          {/* 📌 Input de usuario */}
          <TextInput
            style={[gs.input, { padding: 12, borderRadius: 8, borderWidth: 1, borderColor: "#1565C0", marginBottom: 10, opacity:0.6 }]}
            placeholder="Nombre de usuario"
            value={username}
            onChangeText={setUsername}
            autoCapitalize="none"
          />

          {/* 📌 Input de contraseña */}
          <TextInput
            style={[gs.input, { padding: 12, borderRadius: 8, borderWidth: 1, borderColor: "#1565C0" , opacity:0.6}]}
            placeholder="Contraseña"
            value={password}
            onChangeText={setPassword}
            secureTextEntry
          />

          {/* 📌 Mensaje de error */}
          {errorMessage ? <Text style={{ color: "red", marginVertical: 10, textAlign: "center" }}>{errorMessage}</Text> : null}

          {/* 📌 Link de registro */}
          <Link href={"/signup"} style={{ marginTop: 10, textAlign: "center" }}>
            <Text style={{ color: "#007AFF", fontSize: 14 }}>¿No tienes cuenta? ¡Regístrate!</Text>
          </Link>

          {/* 📌 Botón de inicio de sesión */}
          <TouchableOpacity 
            style={{ marginTop: 20, backgroundColor: "#1565C0", padding: 14, borderRadius: 8, alignItems: "center", shadowColor: "#000", shadowOpacity: 0.2, shadowRadius: 5, elevation: 3 }}
            onPress={handleSubmit}
          >
            <Text style={{ color: "#FFFFFF", fontSize: 16, fontWeight: "bold" }}>Ingresar</Text>
          </TouchableOpacity>

        </View>

      </View>
    </ScrollView>
  );
}
