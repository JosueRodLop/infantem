import { useState, useEffect } from "react";
import { View, Text, TouchableOpacity, ActivityIndicator, Alert } from "react-native";
import { Linking } from "react-native";
import { getToken } from "../../../utils/jwtStorage";

const PaymentScreen = () => {
  const [loading, setLoading] = useState(false);
  const [token, setToken] = useState<string | null>(null);

  useEffect(() => {
    const fetchToken = async () => {
      try {
        const storedToken = await getToken();
        if (!storedToken) {
          Alert.alert("Error", "No se encontró el token de autenticación.");
          return;
        }
        setToken(storedToken);
      } catch (error) {
        console.error("Error obteniendo el token:", error);
        Alert.alert("Error", "No se pudo obtener el token.");
      }
    };
    fetchToken();
  }, []);

  const handleSubscribe = async () => {
    setLoading(true);
    try {
      if (!token) {
        throw new Error("No se encontró el token de autenticación.");
      }
  
      const response = await fetch(
        "http://localhost:8081/api/v1/subcriptions/start/P-15E846608Y045080FM7MCHII",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json; charset=UTF-8",
            "Authorization": `Bearer ${token}`,
          },
        }
      );
  
      const contentType = response.headers.get("content-type");
      const text = await response.text(); // Obtener respuesta como texto
  
      console.log("Respuesta completa del servidor:", text);
      console.log("Content-Type recibido:", contentType);
  
      if (!response.ok) {
        throw new Error(`Error en la respuesta: ${text}`);
      }
  
      if (!contentType || !contentType.includes("application/json")) {
        throw new Error("El servidor no devolvió un JSON válido.");
      }
  
      const data = JSON.parse(text); // Intentar convertir a JSON
  
      if (!data.approvalUrl) {
        throw new Error("No se recibió una URL de aprobación válida.");
      }
  
      console.log("Redirigiendo a:", data.approvalUrl);
      Linking.openURL(data.approvalUrl);
    } catch (error: unknown) {
      if (error instanceof Error) {
        console.error("Error en la suscripción:", error.message);
        Alert.alert("Error", error.message);
      } else {
        console.error("Error desconocido:", error);
        Alert.alert("Error", "Ocurrió un error inesperado.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <View style={{ flex: 1, justifyContent: "center", alignItems: "center" }}>
      <Text style={{ fontSize: 20, marginBottom: 20 }}>Suscripción Premium</Text>
      <TouchableOpacity
        onPress={handleSubscribe}
        style={{
          backgroundColor: "#0070BA",
          padding: 15,
          borderRadius: 10,
        }}
        disabled={loading}
      >
        {loading ? (
          <ActivityIndicator color="white" />
        ) : (
          <Text style={{ color: "white", fontSize: 16 }}>Suscribirse con PayPal</Text>
        )}
      </TouchableOpacity>
    </View>
  );
};

export default PaymentScreen;