import { useState, useEffect } from "react";
import { Text, View, TouchableOpacity, ImageBackground, ScrollView, Image, Alert, TextInput } from "react-native";
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

  const [isEditing, setIsEditing] = useState(false);

  const isValidDate = (dateString: string) => {
    const dateRegex = /^\d{4}-\d{2}-\d{2}$/; // Formato YYYY-MM-DD
    if (!dateRegex.test(dateString)) return false;

    const [year, month, day] = dateString.split("-").map(Number);
    const date = new Date(year, month - 1, day);

    return (
      date.getFullYear() === year &&
      date.getMonth() === month - 1 &&
      date.getDate() === day
    );
  };

  const handleBirthDateChange = (text: string) => {
    if (!isValidDate(text)) {
      setBirthDateError("Fecha inválida. Use AAAA-MM-DD y asegúrese de que la fecha existe.");
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
      setIsEditing(false);
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

  const handleEditBaby = (baby: Baby) => {
    setSelectedBaby(baby);
    setIsEditing(true);
  }

  const handleAddBaby = () => {
    setIsEditing(true);
  }

  const handleCancelBaby = () => {
    setSelectedBaby(null);
    setIsEditing(false);
  }

  return (
    <ImageBackground
      source={require("../../../static/images/Background.png")}
      style={{ flex: 1, width: "100%", height: "100%" }}
      imageStyle={{ resizeMode: "cover", opacity: 0.9 }}
    >
      <ScrollView contentContainerStyle={{ flexGrow: 1, padding: 20 }}>
        <Image source={require("../../../static/images/BodySuit.png")} style={{ position: 'absolute', top: "20%", right: "7%", width: 110, height: 110, transform: [{ rotate: '15deg' }] }} />
        <Text style={{ color: "#1565C0", fontSize: 36, fontWeight: "bold", textAlign: "center", marginBottom: 10 }}>
          Información de los <Text style={{ fontStyle: "italic" }}>bebés</Text>
        </Text>

        <Text style={{ color: "#1565C0", textAlign: "center", fontSize: 16, marginBottom: 20 }}>
          Revisa y gestiona la información de tu bebé.
        </Text>

        {/* FORMULARIO */}
        {isEditing && (
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
            <TouchableOpacity style={[gs.mainButton, { backgroundColor: "red", marginTop: 10 }]} onPress={handleCancelBaby}>
              <Text style={gs.mainButtonText}>Cancelar</Text>
            </TouchableOpacity>
          </View>
        )}

        {/* LISTADO DE BEBÉS */}
        <Text style={[gs.subHeaderText, { color: "#1565C0", marginBottom: 10, fontWeight: "bold" }]}>Mis bebés registrados</Text>

        {babies.length === 0 ? (
          <Text style={{ textAlign: "center", color: "gray", fontSize: 16 }}>
            No hay bebés registrados aún.
          </Text>
        ) : (
          babies.map((baby) => (
            <View key={baby.id} style={[gs.card, { width: "100%", flexDirection: "row", alignItems: "center", padding: 15, marginBottom: 10 }]}>

              <Image
                source={require("../../../static/images/baby-placeholder.png")} // Imagen por defecto
                style={{ width: 70, height: 70, borderRadius: 50, marginRight: 15 }}
              />

              <View style={{ flex: 1 }}>
                <Text style={[gs.cardTitle, { fontSize: 18 }]}>{baby.name}</Text>
                <Text style={gs.cardContent}>📅 Fecha de nacimiento: {baby.birthDate}</Text>
                <Text style={gs.cardContent}>🍼 Preferencia: {baby.foodPreference}</Text>
                <Text style={gs.cardContent}>⚖️ Peso: {baby.weight} kg </Text>
                <Text style={gs.cardContent}>📏 Altura: {baby.height} cm</Text>
              </View>

              <TouchableOpacity style={gs.mainButton} onPress={() => handleEditBaby(baby)}>
                <Text style={gs.mainButtonText}>Editar</Text>
              </TouchableOpacity>
              <TouchableOpacity style={[gs.mainButton, { backgroundColor: "red" }]} onPress={() => handleDeleteBaby(baby.id!)}>
                <Text style={gs.mainButtonText}>Eliminar</Text>
              </TouchableOpacity>
            </View>
          )))}
        {!isEditing && (
          <TouchableOpacity style={[gs.mainButton, { backgroundColor: "#1565C0" }]} onPress={handleAddBaby}>
            <Text style={gs.mainButtonText}>{"Añadir un bebé"}</Text>
          </TouchableOpacity>
        )}
      </ScrollView>
    </ImageBackground>
  );
}
