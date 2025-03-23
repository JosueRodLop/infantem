import { View, Text, TouchableOpacity, ScrollView } from "react-native";
import { useState } from "react";
import { useRouter } from "expo-router";

export default function Subscription() {
    const gs = require("../../../static/styles/globalStyles");
    const [isStripeUser, setIsStripeUser] = useState<boolean | null>(null);
    const router = useRouter();

    const handleResponse = (response: boolean) => {
        setIsStripeUser(response);
        if (response) {
        router.push("/account/premiumplan");
        }
    };

    return (
        <View style={{ flex: 1 }}>
        <ScrollView contentContainerStyle={[gs.container, { paddingTop: 100, paddingBottom: 100 }]}>
            <Text style={gs.headerText}>¿Eres usuario de Stripe?</Text>

            <TouchableOpacity onPress={() => handleResponse(true)} style={[gs.mainButton, { margin: 10 }]}>
            <Text style={gs.mainButtonText}>Sí</Text>
            </TouchableOpacity>

            <TouchableOpacity onPress={() => handleResponse(false)} style={[gs.secondaryButton, { margin: 10 }]}>
            <Text style={gs.secondaryButtonText}>No</Text>
            </TouchableOpacity>

            {isStripeUser === false && (
            <Text style={[gs.subheaderText, { marginTop: 20 }]}>No eres usuario de Stripe.</Text>
            )}
        </ScrollView>
        </View>
    );
}
