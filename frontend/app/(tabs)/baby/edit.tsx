import { useState, useEffect } from "react";
import { Text, View, TextInput, TouchableOpacity } from "react-native";
import { useRouter } from "expo-router";
import { getToken } from "../../../utils/jwtStorage";
import { useLocalSearchParams } from 'expo-router';





export default function EditBaby() {
  const gs = require("../../../static/styles/globalStyles");
  const router = useRouter();
  const [baby, setBaby] = useState(null);

  const [loading, setLoading] = useState(true); // Estado para manejar la carga
  const apiUrl = process.env.EXPO_PUBLIC_API_URL;

  const { id } = useLocalSearchParams();


  useEffect(() => {
    const fetchBabyData = async () => {
      const jwt = await getToken();
      if (id) {
        try {
          const response = await fetch(`${apiUrl}/api/v1/baby/${id}`, {
            headers: { "Authorization": `Bearer ${jwt}` },
          });
          console.log("Response received:", response);

          const text = await response.text();
          console.log("Response body:", text);

          if (!response.ok) {
            throw new Error(`Error: ${response.status} - ${text}`);
          }

          const data = JSON.parse(text);
          console.log("Parsed JSON:", data);
          setBaby(data);
          console.log("Baby data:", baby);
        } catch (error) {
          console.error("Error fetching baby data:", error);
        } finally {
          setLoading(false); // Datos cargados o error, dejar de mostrar el mensaje de carga
        }
      } else {
        setLoading(false); // No hay ID, dejar de mostrar el mensaje de carga
      }
    };

    fetchBabyData();
  }, [id]);

  const handleSave = async () => {
    const jwt = await getToken(); 
  
    console.log("JWT for PUT request:", jwt); 
  
    if (!jwt) {
      console.error("No JWT token found, aborting request");
      return; 
    }
  
    try {
      const response = await fetch(`${apiUrl}/api/v1/baby/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${jwt}`, 
        },
        body: JSON.stringify(baby),
      });
  
      if (response.ok) {
        router.push("/baby"); 
      } else {
        console.error("Error updating baby:", response.statusText);
      }
    } catch (error) {
      console.error("Error updating baby:", error);
    }
  };

  if (loading) {
    return (
      <View style={{ flex: 1 }}>
        
        <Text>Loading...</Text>
      </View>
    );
  }

  if (!baby) {
    return (
      <View style={{ flex: 1 }}>
        
        <Text>No baby data found.</Text>
      </View>
    );
  } 

  return (
    <View style={{ flex: 1 }}>
      {baby && <View style={[gs.container, { paddingTop: 100, paddingBottom: 100 }]}>
          <Text style={gs.headerText}>Edit Baby Information</Text>
          <TextInput
            style={gs.input}
            placeholder="Name"
            value={baby.name}
            onChangeText={(text) => setBaby({ ...baby, name: text })}
          />
          <TextInput
            style={gs.input}
            placeholder="Birth Date"
            value={baby.birthDate}
            onChangeText={(text) => setBaby({ ...baby, birthDate: text })}
          />
          <TextInput
            style={gs.input}
            placeholder="Gender"
            value={baby.genre}
            onChangeText={(text) => setBaby({ ...baby, genre: text })}
          />
          <TextInput
            style={gs.input}
            placeholder="Weight"
            value={baby.weight}
            onChangeText={(text) => setBaby({ ...baby, weight: text })}
          />
          <TextInput
            style={gs.input}
            placeholder="Height"
            value={baby.height}
            onChangeText={(text) => setBaby({ ...baby, height: text })}
          />
          <TextInput
            style={gs.input}
            placeholder="Cephalic Perimeter"
            value={baby.cephalicPerimeter}
            onChangeText={(text) => setBaby({ ...baby, cephalicPerimeter: text })}
          />
          <TextInput
            style={gs.input}
            placeholder="Food Preference"
            value={baby.foodPreference}
            onChangeText={(text) => setBaby({ ...baby, foodPreference: text })}
          />
          <TouchableOpacity style={gs.mainButton} onPress={handleSave}>
            <Text style={gs.mainButtonText}>Save</Text>
          </TouchableOpacity>
        </View>}
        
    </View>
  );
}

