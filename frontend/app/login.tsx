import { Link } from "expo-router";
import { useState } from "react";
import { Text, View, TouchableOpacity, TextInput, ScrollView, Image } from "react-native";

export default function LoginScreen() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const gs = require("../static/styles/globalStyles"); // Importando estilos globales

  const handleLogin = () => {
    console.log("Iniciando sesión con:", username, password);
    // Aquí iría la lógica de autenticación con el backend
  };

  return (
    <View style={[gs.container, {justifyContent:"center"}]}>
      <Text style={gs.headerText}>Iniciar Sesión</Text>
      <Image source={require("../static/images/profile.webp")} style={[gs.profileImage, {marginBottom:20}]} />

      <View style={[gs.card, {maxWidth:400}]}>
        <Text style={{fontWeight: "bold"}}>Correo Electrónico</Text>
        <TextInput
          style={gs.input}
          placeholder="user@example.com"
          value={username}
          onChangeText={setUsername}
          keyboardType="email-address"
          autoCapitalize="none"
        />

        <Text style={{paddingTop:10, fontWeight:"bold"}}>Contraseña</Text>
        <TextInput
          style={gs.input}
          placeholder="contraseña1234"
          value={password}
          onChangeText={setPassword}
          secureTextEntry
        />

        <Link href={"/signup"}>
          <Text style={{color: "#007AFF"}}>¿No tienes cuenta? ¡Regístrate!</Text>
        </Link>

        <TouchableOpacity style={[gs.mainButton, {marginTop: 20}]} onPress={handleLogin}>
          <Text style={gs.mainButtonText}>Ingresar</Text>
        </TouchableOpacity>
      </View>


    </View>
  );
}
