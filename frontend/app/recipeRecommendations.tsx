import { useState } from "react";
import { Text, View, TouchableOpacity, Image, ScrollView, TextInput, Modal } from "react-native";
import NavBar from "../components/NavBar"; 

export default function Page() {
  
    const gs = require('../static/styles/globalStyles');

    const [searchQuery, setSearchQuery] = useState("");

  return (
    <View style={{ flex: 1 }}>
      {}
      <NavBar />
      
      <ScrollView contentContainerStyle={[gs.container, { paddingTop: 80 }]}> 
        <Text style={gs.headerText}>Recomendaciones de Recetas</Text>
        <Text style={gs.bodyText}>
          Encuentra recetas adecuadas según la edad de tu bebé y sus preferencias.
        </Text>
        
        <TextInput
          style={gs.input}
          placeholder="Buscar recetas..."
          value={searchQuery}
          onChangeText={setSearchQuery}
        />
        
        <Text style={gs.subHeaderText}>Recetas sugeridas</Text>
        <View style={gs.card}>
          <Text style={gs.cardTitle}>Puré de Zanahoria</Text>
          <Text style={gs.cardContent}>Ideal para bebés de 6 meses en adelante.</Text>
        </View>
        <View style={gs.card}>
          <Text style={gs.cardTitle}>Compota de Manzana</Text>
          <Text style={gs.cardContent}>Dulce y nutritiva, perfecta para la primera alimentación.</Text>
        </View>
        
        <TouchableOpacity style={gs.mainButton}>
          <Text style={gs.mainButtonText}>Ver más recetas</Text>
        </TouchableOpacity>
      </ScrollView>
    </View>
  );
}

