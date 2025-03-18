import { useState } from "react";
import { View, Text, TouchableOpacity, ActivityIndicator, Alert } from "react-native";
import { Linking } from "react-native";

const PaymentScreen = () => {
  const [loading, setLoading] = useState(false);

  const handleSubscribe = async () => {
    setLoading(true);
    try {
      const response = await fetch(
        "http://localhost:8081/api/v1/subcriptions/start/P-15E846608Y045080FM7MCHII",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
        }
      );
  
      const data = await response.json();
      
      if (!response.ok) {
        throw new Error(`Error: ${data.error || "No se pudo obtener la URL"}`);
      }
  
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