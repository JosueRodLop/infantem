import { useState, useEffect } from "react";
import { View, Text, TouchableOpacity, ActivityIndicator, Alert } from "react-native";
import { useRouter } from "expo-router";
import { getToken } from "../../../utils/jwtStorage";

export default function PremiumPlan() {
    const router = useRouter();
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

    const handleSubscribe = () => { 
        console.log("Navegando a /account/subscription");
        router.push('/account/subscription') 
    };

    return (
        <View style={{ flex: 1, justifyContent: "center", alignItems: "center", padding: 20, backgroundColor: "#f4f4f4" }}>
            <View style={{ backgroundColor: "#fff", padding: 20, borderRadius: 10, shadowColor: "#000", shadowOpacity: 0.2, shadowRadius: 5 }}>
                <Text style={{ fontSize: 22, fontWeight: "bold", marginBottom: 10 }}>Hazte Premium</Text>
                <Text style={{ fontSize: 16, marginBottom: 10 }}>Disfruta de beneficios exclusivos:</Text>
                <Text style={{ marginBottom: 5 }}>🔹 Acceso ilimitado a contenido premium</Text>
                <Text style={{ marginBottom: 5 }}>🔹 Métricas avanzadas y reportes</Text>
                <Text style={{ marginBottom: 5 }}>🔹 Soporte prioritario</Text>
                <Text style={{ marginBottom: 5 }}>🔹 Contenido sin anuncios</Text>
                <Text style={{ marginBottom: 15, color: "#ff4500", fontWeight: "bold" }}>¡Todo esto por solo 9,99€/mes!</Text>

                <TouchableOpacity
                    onPress={handleSubscribe}
                    style={{ backgroundColor: "#0070BA", padding: 10, borderRadius: 5, alignItems: "center" }}
                    disabled={loading}
                >
                    {loading ? (
                        <ActivityIndicator color="white" />
                    ) : (
                        <Text style={{ color: "white", fontWeight: "bold" }}>Suscribirse ahora</Text>
                    )}
                </TouchableOpacity>
            </View>
        </View>
    );
};