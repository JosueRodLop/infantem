import { useState } from "react";
import { Text, View, TextInput, Picker, TouchableOpacity } from "react-native";
import Navbar from "../components/Navbar";

export default function Page() {
  const [foodName, setFoodName] = useState("");
  const [foodType, setFoodType] = useState("");

  const gs = require('../../static/styles/globalStyles');

  return (
    <View>
        
        <View style={gs.container}>
        
            <Text style={gs.headerText}>Registro de Alimento</Text>

            <Text style={gs.subHeaderText}>Nombre del Alimento</Text>
            <TextInput
                style={gs.input}        
                placeholder="Introduce el nombre del alimento"
                value={foodName}
                onChangeText={setFoodName}
            />

            <Text style={gs.subHeaderText}>Tipo de Alimento</Text>
            <Picker
                selectedValue={foodType}
                style={gs.input}
                onValueChange={(itemValue) => setFoodType(itemValue)}
            >
                <Picker.Item label="Fruta" value="fruta" />
                <Picker.Item label="Verdura" value="verdura" />
                <Picker.Item label="Proteína" value="proteina" />
                <Picker.Item label="Carbohidrato" value="carbohidrato" />
            </Picker>

            <TouchableOpacity style={gs.mainButton}>
                <Text style={gs.mainButtonText}>Guardar</Text>
            </TouchableOpacity>

            <TouchableOpacity style={gs.secondaryButton} >
            <Text style={gs.secondaryButtonText} onPress={() => {window.location.href = "/";}}>Inicio</Text>
            </TouchableOpacity>
        </View>
    </View>
  );
}