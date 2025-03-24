import React, { useState, useEffect } from "react";
import { Text, View, TextInput, TouchableOpacity } from "react-native";
import { useRouter } from "expo-router";
import { getToken } from "../../../utils/jwtStorage";
import { jwtDecode } from "jwt-decode";

export default function Subscription() {
    const gs = require("../../../static/styles/globalStyles");
    const router = useRouter();
    const [email, setEmail] = useState("");
    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [last4, setLast4] = useState("");
    const [customerId, setCustomerId] = useState("");
    const [cardNumber, setCardNumber] = useState("");
    const [expMonth, setExpMonth] = useState("");
    const [expYear, setExpYear] = useState("");
    const [cvc, setCvc] = useState("");
    const [paymentMethodId, setPaymentMethodId] = useState("");
    const [customerData, setCustomerData] = useState(null);
    const [isStripeUser, setIsStripeUser] = useState<boolean | null>(null);
    const [jwt, setJwt] = useState<string | "">("");
    const apiUrl = process.env.EXPO_PUBLIC_API_URL;
    const [accepted, setAccepted] = useState(false);
    const [priceId, setPriceId] = useState("price_1R4hyZRD1fD8EiuBaXzXdw9p");
    const [userId, setUserId] = useState<number | null>(null);


    useEffect(() => {
        const getUserToken = async () => {
            const token = await getToken();
            setJwt(token);
        };
        getUserToken();
    }, []);

    useEffect(() => {
        if (!jwt) return; // Evita ejecutar el efecto si jwt es null o undefined
        try {
            const decodedToken: any = jwtDecode(jwt);
            setUserId(decodedToken.jti);
        } catch (error) {
            console.error("Error al decodificar el token:", error);
        }
    }, [jwt]);

    const handleResponse = async () => {
        if (jwt && accepted) {
        try {
            const endpoint = `${apiUrl}/api/v1/subscriptions/customers?email=${email}&lasts4=${last4}`;
            const response = await fetch(endpoint, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${jwt}`,
            }
            });
            if (response.ok) {
                const data = await response.json();
                setCustomerData(data);
            } else {
            console.error("Error en la suscripción:", response.statusText);
            }
        } catch (error) {
            console.error("Error en la suscripción:", error);
        }
        }
    };

    const handleResponseCard = async () => {
        if (jwt && accepted) {
            try {
                const endpoint = `${apiUrl}/api/v1/subscriptions/create-customer?email=${email}&name=${name}&description=${description}`;
                const responseUser = await fetch(endpoint, {
                    method:"POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${jwt}`,
                    },
                });
                if (responseUser.ok) {
                    const data = await responseUser.json();
                    const customerId = data.customerId;
                    setCustomerId(customerId);
                    const endpoint = `${apiUrl}/api/v1/subscriptions/create-payment-method?`;
                    const responseCard = await fetch(endpoint, {
                        method:"POST",
                        headers: {
                            "Content-Type": "application/json",
                            Authorization: `Bearer ${jwt}`,
                        },
                        body: JSON.stringify({ cardNumber, expMonth, expYear, cvc, customerId }),
                    });
                    if (responseCard.ok) {
                        const data = await responseCard.json();
                        const paymentMethodId = data.paymentMethodId;
                        setPaymentMethodId(paymentMethodId);
                        console.log("PaymentMethodId:", paymentMethodId);
                        const endpoint = `${apiUrl}/api/v1/subscriptions/create?userId=${userId}&customerId=${customerId}&priceId=${priceId}&paymentMethodId=${paymentMethodId}`;
                        const response = await fetch(endpoint, {
                            method:"POST",
                            headers: {
                                "Content-Type": "application/json",
                                Authorization: `Bearer ${jwt}`,
                            },
                        });
                        if (response.ok) {
                            router.push("/account/premiumplan");
                        } else {
                            console.error("Error en la suscripción:", response.statusText);
                        }
                        router.push("/account/premiumplan");
                    } else {
                        console.error("Error en la suscripción:", responseCard.statusText);
                    }
                } else {
                    console.error("Error en la suscripción:", responseUser.statusText);
                }
            } catch (error) {
                console.error("Error en la suscripción:", error);
            }
        }
    };

    const handleResponseSubscription = async () => {
        if (jwt && accepted) {
            try {
                const endpoint = `${apiUrl}/api/v1/subscriptions/create?userId=${userId}&customerId=${customerData.id}&priceId=${priceId}&paymentMethodId=${customerData.paymentMethod.id}`;
                const response = await fetch(endpoint, {
                    method:"POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${jwt}`,
                    },
                });
                if (response.ok) {
                    router.push("/account/premiumplan");
                } else {
                    console.error("Error en la suscripción:", response.statusText);
                }
            } catch (error) {
                console.error("Error en la suscripción:", error);
            }
        }
    };

    return (
    <View style={[gs.container, { paddingTop: 100, paddingBottom: 100 }]}>
        <Text style={gs.headerText}>¿Eres usuario de Stripe?</Text>
        <TouchableOpacity onPress={() => setIsStripeUser(true)} style={[gs.mainButton, { margin: 10 }]}>
            <Text style={gs.mainButtonText}>Sí</Text>
        </TouchableOpacity>
        <TouchableOpacity onPress={() => setIsStripeUser(false)} style={[gs.secondaryButton, { margin: 10 }]}>
            <Text style={gs.secondaryButtonText}>No</Text>
        </TouchableOpacity>

        {isStripeUser !== null && customerData == null && customerId == "" &&(
        <>
            {isStripeUser ? (
            <>
                <TextInput
                    style={gs.input}
                    placeholder="Correo asociado a Stripe"
                    value={email}
                    onChangeText={setEmail}
                />
                <TextInput
                    style={gs.input}
                    placeholder="Últimos 4 dígitos de la tarjeta"
                    value={last4}
                    keyboardType="numeric"
                    onChangeText={setLast4}
                />
            </>
            ) : (
            <>
                <TextInput
                    style={gs.input}
                    placeholder="Nombre"
                    value={name}
                    onChangeText={setName}
                />
                <TextInput
                    style={gs.input}
                    placeholder="Email"
                    value={email}
                    onChangeText={setEmail}
                />
                <TextInput
                    style={gs.input}
                    placeholder="Descripción"
                    value={description}
                    onChangeText={setDescription}
                />
            </>
            )}
            <View style={{ flexDirection: "row", alignItems: "center", marginVertical: 10 }}>
                <TouchableOpacity onPress={() => setAccepted(!accepted)} style={{ flexDirection: "row", alignItems: "center", marginVertical: 10 }}>
                    <View style={{
                        width: 20,
                        height: 20,
                        borderWidth: 2,
                        borderColor: "black",
                        justifyContent: "center",
                        alignItems: "center",
                        marginRight: 10,
                        backgroundColor: accepted ? "black" : "white",
                    }}>
                        {accepted && <Text style={{ color: "white" }}>✔</Text>}
                    </View>
                    <Text>Acepto el uso de mis datos</Text>
                </TouchableOpacity>
            </View>
            <TouchableOpacity
                style={[gs.mainButton, { opacity: accepted ? 1 : 0.5 }]}
                onPress={isStripeUser ? handleResponse : () => setCustomerId("0") }
                disabled={!accepted}
            >
                <Text style={gs.mainButtonText}>Confirmar</Text>
            </TouchableOpacity>
        </>
        )}

        {customerData !== null && (
            <>
            <View>
                <Text style={gs.text}>Datos a usar para la suscripción:</Text>
                <Text style={gs.text}>Correo: {customerData?.customerData.id || "Nada"}</Text>
                <Text style={gs.text}>Correo: {customerData?.paymentMethod?.id || "Nada"}</Text>
                <Text style={gs.text}>Correo: {customerData?.customerData.email || "Nada"}</Text>
                <Text style={gs.text}>Tarjeta: {customerData?.customerData.paymentMethod.card.brand || "Nada"} **** {customerData?.customerData.paymentMethod.card.last4 || "nada"}</Text>
            </View>
            <View style={{ flexDirection: "row", alignItems: "center", marginVertical: 10 }}>
                <TouchableOpacity onPress={() => setAccepted(!accepted)} style={{ flexDirection: "row", alignItems: "center", marginVertical: 10 }}>
                    <View style={{
                        width: 20,
                        height: 20,
                        borderWidth: 2,
                        borderColor: "black",
                        justifyContent: "center",
                        alignItems: "center",
                        marginRight: 10,
                        backgroundColor: accepted ? "black" : "white",
                    }}>
                        {accepted && <Text style={{ color: "white" }}>✔</Text>}
                    </View>
                    <Text style={gs.text}>Acepto el uso de mis datos para la suscripción</Text>
                </TouchableOpacity>
            </View>
            <TouchableOpacity
                style={[gs.mainButton, { opacity: accepted ? 1 : 0.5 }]}
                onPress={handleResponseSubscription}
                disabled={!accepted}
            >
                <Text style={gs.mainButtonText}>Confirmar suscripción</Text>
            </TouchableOpacity>
            </>
        )}
        {customerId !== "" && (
            <View>
                <Text style={gs.text}>Ingrese los datos de la trajeta que quieres asociar a la cuenta creada anteriormente</Text>
                <>
                    <TextInput
                        style={gs.input}
                        placeholder="Número de tarjeta"
                        value={cardNumber}
                        onChangeText={setCardNumber}
                    />
                    <TextInput
                        style={gs.input}
                        placeholder="Mes de expiración"
                        value={expMonth}
                        keyboardType="numeric"
                        onChangeText={setExpMonth}
                    />
                    <TextInput
                        style={gs.input}
                        placeholder="Año de expiración"
                        value={expYear}
                        keyboardType="numeric"
                        onChangeText={setExpYear}
                    />
                    
                    <TextInput
                        style={gs.input}
                        placeholder="CVC"
                        value={cvc}
                        onChangeText={setCvc}
                    />
                </>
                <View style={{ flexDirection: "row", alignItems: "center", marginVertical: 10 }}>
                    <TouchableOpacity onPress={() => setAccepted(!accepted)} style={{ flexDirection: "row", alignItems: "center", marginVertical: 10 }}>
                        <View style={{
                            width: 20,
                            height: 20,
                            borderWidth: 2,
                            borderColor: "black",
                            justifyContent: "center",
                            alignItems: "center",
                            marginRight: 10,
                            backgroundColor: accepted ? "black" : "white",
                        }}>
                            {accepted && <Text style={{ color: "white" }}>✔</Text>}
                        </View>
                        <Text>Acepto el uso de mis datos</Text>
                    </TouchableOpacity>
                </View>
                <TouchableOpacity
                    style={[gs.mainButton, { opacity: accepted ? 1 : 0.5 }]}
                    onPress={handleResponseCard}
                    disabled={!accepted}
                >
                    <Text style={gs.mainButtonText}>Confirmar</Text>
                </TouchableOpacity>
            </View>
        )}
    </View>
    );

}
