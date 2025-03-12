import { useState, useEffect } from "react";
import { Text, View, TextInput, TouchableOpacity} from "react-native";
import { useRouter} from "expo-router";
import NavBar from "../../components/NavBar";

export default function EditBaby() {
  const gs = require("../../static/styles/globalStyles");
  const router = useRouter();
  const [baby, setBaby] = useState(null);
  const [id, setId] = useState<string | null>(null);

  useEffect(() => {
    // Obtener la ID del bebé desde la URL
    const query = new URLSearchParams(window.location.search);
    const babyId = query.get('id');
    setId(babyId);
  }, []);

  useEffect(() => {
    if (id) {
      fetch(`http://localhost:8080/baby/${id}`)
        .then((response) => response.json())
        .then((data) => setBaby(data))
        .catch((error) => console.error("Error fetching baby data:", error));
    }
  }, [id]);

  const handleSave = () => {
    fetch(`http://localhost:8080/baby/update/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(baby),
    })
      .then((response) => {
        if (response.ok) {
          router.push("/baby/babyInfo");
        } else {
          console.error("Error updating baby:", response.statusText);
        }
      })
      .catch((error) => console.error("Error updating baby:", error));
  };

  if (!baby) {
    return (
      <View style={{ flex: 1 }}>
        <NavBar />
        <Text>Loading...</Text>
      </View>
    );
  }

  return (
    <View style={{ flex: 1 }}>
      <NavBar />
      <View style={[gs.container, { paddingTop: 100, paddingBottom: 100 }]}>
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
      </View>
    </View>
  );
}

