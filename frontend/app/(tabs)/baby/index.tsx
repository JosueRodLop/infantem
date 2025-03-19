import { useState, useEffect } from "react";
import { Text, View, TouchableOpacity, ScrollView, TextInput } from "react-native";
import { getToken } from "../../../utils/jwtStorage";
import { Picker } from "@react-native-picker/picker";

const apiUrl = process.env.EXPO_PUBLIC_API_URL;

interface Baby {
  id?: number;
  name: string;
  birthDate: string;
  genre?: string;
  weight?: number;
  height?: number;
  cephalicPerimeter?: number;
  foodPreference?: string;
}

export default function BabyInfo() {
  const gs = require("../../../static/styles/globalStyles");
  const [babies, setBabies] = useState<Baby[]>([]);
  const [jwt, setJwt] = useState<string | null>(null);
  const [selectedBaby, setSelectedBaby] = useState<Baby | null>(null);
  const [birthDateError, setBirthDateError] = useState<string | null>(null);

  const handleBirthDateChange = (text: string) => {
    const dateRegex = /^\d{4}-\d{2}-\d{2}$/; // Formato YYYY-MM-DD
    if (!dateRegex.test(text)) {
      setBirthDateError("Formato incorrecto. Use AAAA-MM-DD");
    } else {
      setBirthDateError(null);
    }
    setSelectedBaby((prev) => ({ ...prev!, birthDate: text }));
  };
  

  useEffect(() => {
    const getUserToken = async () => {
      const token = await getToken();
      setJwt(token);
    };
    getUserToken();
  }, []);

  useEffect(() => {
    if (!jwt) return;

    const fetchBabies = async () => {
      try {
        const response = await fetch(`${apiUrl}/api/v1/baby`, {
          headers: { Authorization: `Bearer ${jwt}` },
        });
        if (!response.ok) throw new Error("Error fetching babies");
        const data: Baby[] = await response.json();
        setBabies(data);
      } catch (error) {
        console.error(error);
      }
    };
    fetchBabies();
  }, [jwt]);

  const handleSaveBaby = async () => {
    if (!jwt) return;

    const method = selectedBaby?.id ? "PUT" : "POST";
    const url = selectedBaby?.id ? `${apiUrl}/api/v1/baby/${selectedBaby.id}` : `${apiUrl}/api/v1/baby`;

    try {
      const response = await fetch(url, {
        method,
        headers: {
          "Authorization": `Bearer ${jwt}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(selectedBaby),
      });
      if (!response.ok) throw new Error("Error saving baby");
      
      const updatedBaby = await response.json();
      setBabies((prev) =>
        selectedBaby?.id ? prev.map((b) => (b.id === updatedBaby.id ? updatedBaby : b)) : [...prev, updatedBaby]
      );
      setSelectedBaby(null);
    } catch (error) {
      console.error(error);
    }
  };

  const handleDeleteBaby = async (id: number) => {
    if (!jwt) return;

    try {
      const response = await fetch(`${apiUrl}/api/v1/baby/${id}`, {
        method: "DELETE",
        headers: {
          "Authorization": `Bearer ${jwt}`,
        },
      });

      if (!response.ok) throw new Error("Error deleting baby");

      setBabies((prev) => prev.filter((baby) => baby.id !== id));
    } catch (error) {
      console.error(error);
    }
  };

  const handleDecimalInput = (text: string, field: keyof Baby) => {
    if (/^\d*\.?\d*$/.test(text)) {
      setSelectedBaby((prev) => ({
        ...prev!,
        [field]: text ? parseFloat(text) : undefined,
      }));
    }
  };

  return (
    <View style={{ flex: 1 }}>
      <ScrollView contentContainerStyle={[gs.container, { paddingTop: 100, paddingBottom: 100 }]}>
        <Text style={gs.headerText}>Información de los bebés</Text>

        {/* FORMULARIO */}
        <View style={[gs.card, { padding: 20 }]}>
          <Text style={gs.cardTitle}>{selectedBaby?.id ? "Editar bebé" : "Añadir bebé"}</Text>
          <TextInput
            style={gs.input}
            placeholder="Nombre"
            value={selectedBaby?.name || ""}
            onChangeText={(text) => setSelectedBaby({ ...selectedBaby!, name: text })} 
          />
          <TextInput
            style={gs.input}
            placeholder="Fecha de nacimiento (AAAA-MM-DD)"
            value={selectedBaby?.birthDate || ""}
            onChangeText={handleBirthDateChange}
          />
          {birthDateError && <Text style={{ color: "red" }}>{birthDateError}</Text>}

          {/* Campo para el género */}
          <Picker
            selectedValue={selectedBaby?.genre || "OTHER"} 
            style={gs.input}
            onValueChange={(itemValue) => setSelectedBaby({ ...selectedBaby!, genre: itemValue })}
          >
            <Picker.Item label="Niño" value="MALE" />
            <Picker.Item label="Niña" value="FEMALE" />
            <Picker.Item label="Otro" value="OTHER" />
          </Picker>

          <TextInput
            style={gs.input}
            placeholder="Peso (kg)"
            keyboardType="decimal-pad"
            value={selectedBaby?.weight?.toString() || ""}
            onChangeText={(text) => handleDecimalInput(text, "weight")} 
          />

          <TextInput
            style={gs.input}
            placeholder="Altura (cm)"
            keyboardType="decimal-pad" 
            value={selectedBaby?.height?.toString() || ""}
            onChangeText={(text) => handleDecimalInput(text, "height")} 
          />

          <TextInput
            style={gs.input}
            placeholder="Perímetro cefálico (cm)"
            keyboardType="decimal-pad" 
            value={selectedBaby?.cephalicPerimeter?.toString() || ""}
            onChangeText={(text) => handleDecimalInput(text, "cephalicPerimeter")} 
          />

          <TextInput
            style={gs.input}
            placeholder="Preferencias alimentarias"
            value={selectedBaby?.foodPreference || ""}
            onChangeText={(text) => setSelectedBaby({ ...selectedBaby!, foodPreference: text })}
          />

          <TouchableOpacity style={gs.mainButton} onPress={handleSaveBaby}>
            <Text style={gs.mainButtonText}>{selectedBaby?.id ? "Actualizar" : "Guardar"}</Text>
          </TouchableOpacity>
        </View>

        {/* LISTADO DE BEBÉS */}
        <Text style={[gs.subHeaderText, { marginTop: 30 }]}>Mis bebés registrados</Text>
        {babies.map((baby) => (
          <View key={baby.id} style={[gs.card, { maxWidth: 500, alignSelf: "center" }]}>
            <Text style={gs.cardTitle}>{baby.name}</Text>
            <Text style={gs.cardContent}>Fecha de nacimiento: {baby.birthDate}</Text>
            <Text style={gs.cardContent}>Género: {baby.genre === "MALE" ? "Niño" : baby.genre === "FEMALE" ? "Niña" : "Otro"}</Text>
            <Text style={gs.cardContent}>Peso: {baby.weight} kg</Text>
            <Text style={gs.cardContent}>Altura: {baby.height} cm</Text>
            <Text style={gs.cardContent}>Perímetro cefálico: {baby.cephalicPerimeter} cm</Text>
            <Text style={gs.cardContent}>Preferencias alimentarias: {baby.foodPreference}</Text>
            
            <TouchableOpacity style={gs.mainButton} onPress={() => setSelectedBaby(baby)}>
              <Text style={gs.mainButtonText}>Editar</Text>
            </TouchableOpacity>
            <TouchableOpacity style={[gs.mainButton, { backgroundColor: "red" }]} onPress={() => handleDeleteBaby(baby.id!)}>
              <Text style={gs.mainButtonText}>Eliminar</Text>
            </TouchableOpacity>
          </View>
        ))}
      </ScrollView>
    </View>
  );
}
