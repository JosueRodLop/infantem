
// I KNOW THIS MUST BE ERASED AND THE ADD AND THE INDEX MUST BE THE SAME COMPONENTE/SCREEN
// THIS IS JUST TEMPORARY FOR THE FIRST SPRING
// TODO: CHANGE THIS.

import { useState, useEffect } from "react";
import { Text, View, TextInput, TouchableOpacity } from "react-native";
import { useRouter } from "expo-router";
import { getToken } from "../../../utils/jwtStorage";
import { Picker } from "@react-native-picker/picker";

export default function AddBaby() {
  const gs = require("../../../static/styles/globalStyles");
  const router = useRouter();

  const [name, setName] = useState<string>("");
  const [birthDate, setBirthDate] = useState<string>("");
  const [genre, setGenre] = useState<string>("male"); 
  const [weight, setWeight] = useState<number | null>(null);
  const [height, setHeight] = useState<number | null>(null);
  const [cephalicPerimeter, setCephalicPerimeter] = useState<number | null>(null);
  const [foodPreference, setFoodPreference] = useState<string>("");

  const [loading, setLoading] = useState<boolean>(true); // Estado para manejar la carga
  const [jwt, setJwt] = useState<string | null>(null);

  const apiUrl = process.env.EXPO_PUBLIC_API_URL;


  useEffect(() => {
    const getUserToken = async () => {
      const token = await getToken();
      setJwt(token);
    };
    getUserToken();
  },[]) 

  const handleSave = async () => {
    if (jwt) {
      try {
        const response = await fetch(`${apiUrl}/api/v1/baby`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${jwt}`, 
          },
          body: JSON.stringify({
            name: name,
            birthDate: birthDate,
            genre: genre,
            weight: weight,
            height: height,
            cephalicPerimeter: cephalicPerimeter,
            foodPreference: foodPreference,
          }),
        });
    
        if (response.ok) {
          router.push("/baby"); 
        } else {
          console.error("Error updating baby:", response.statusText);
        }
      } catch (error) {
        console.error("Error updating baby:", error);
      }
    }
  };


  return (
  <View style={[gs.container, { paddingTop: 100, paddingBottom: 100 }]}>
      <Text style={gs.headerText}>Add a baby</Text>
      <TextInput
        style={gs.input}
        placeholder="Name"
        value={name}
        onChangeText={setName}
      />
      <TextInput
        style={gs.input}
        placeholder="2023-01-01"
        value={birthDate}
        onChangeText={setBirthDate}
      />
      <Picker
        selectedValue={genre}
        style={[gs.input, { height: 50 }]}
        onValueChange={(itemValue) => setGenre(itemValue)}
      >
        <Picker.Item label="Male" value="MALE" />
        <Picker.Item label="Female" value="FEMALE" />
        <Picker.Item label="Other" value="OTHER" />
      </Picker>
      <TextInput
        style={gs.input}
        placeholder="Weight"
        value={weight?.toString()}
        keyboardType="numeric"
        onChangeText={(text) => {
          const newValue = parseFloat(text) || 0;
          setWeight(newValue);
        }}
      />
      <TextInput
        style={gs.input}
        placeholder="Height"
        value={height?.toString()}
        keyboardType="numeric"
        onChangeText={(text) => {
          const newValue = parseInt(text) || 0;
          setHeight(newValue);
        }}
      />
      <TextInput
        style={gs.input}
        placeholder="Cephalic Perimeter"
        value={cephalicPerimeter?.toString()}
        keyboardType="numeric"
        onChangeText={(text) => {
          const newValue = parseInt(text) || 0;
          setCephalicPerimeter(newValue);
        }}
      />
      <TextInput
        style={gs.input}
        placeholder="Food Preference"
        value={foodPreference}
        onChangeText={setFoodPreference}
      />
      <TouchableOpacity style={gs.mainButton} onPress={handleSave}>
        <Text style={gs.mainButtonText}>Save</Text>
      </TouchableOpacity>
    </View>
  );
}


