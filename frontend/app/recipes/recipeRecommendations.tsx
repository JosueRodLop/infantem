import { useState } from "react";
import { Text, View, TouchableOpacity, Image, ScrollView, TextInput, Modal } from "react-native";
import NavBar from "../../components/NavBar";


export default function Page() {
  
    const gs = require('../../static/styles/globalStyles');

    const [searchQuery, setSearchQuery] = useState("");

    return (
      <View style={{ flex: 1 }}>
        {}
        <NavBar />
    
        <ScrollView 
  contentContainerStyle={[gs.container, { paddingTop: 100, paddingBottom: 100 }]} 
>
          <Text style={gs.headerText}>Recipe Suggestions</Text>
          <Text style={gs.bodyText}>
            Find suitable recipes based on your baby's age and preferences
          </Text>
    
          <View style={{ width: '90%' }}>
            <TextInput style={gs.input} placeholder="Search recipes" value={searchQuery} onChangeText={setSearchQuery}/>
    
            <View style={{ flexDirection: 'row', gap: 10, marginVertical: 10, alignSelf: 'flex-start' }}>
              <TouchableOpacity style={gs.mainButton}>
                <Text style={gs.mainButtonText}>Age Group</Text>
              </TouchableOpacity>
              <TouchableOpacity style={gs.mainButton}>
                <Text style={gs.mainButtonText}>Alergies</Text>
              </TouchableOpacity>
              <TouchableOpacity style={gs.mainButton}>
                <Text style={gs.mainButtonText}>Favourites</Text>
              </TouchableOpacity>
              <TouchableOpacity style={gs.mainButton}>
                <Text style={gs.mainButtonText}>Add recipes</Text>
              </TouchableOpacity>
            </View>
          </View>
    
          <Text style={[gs.subHeaderText, { marginTop: 30 }]}>Suggested recipes</Text>
    
          <View style={gs.card}>
            <Text style={gs.cardTitle}>Puré de Batata</Text>
            <Text style={gs.cardContent}>Suave y dulce, ideal para bebés de 6 meses en adelante.</Text>
          </View>
    
          <View style={gs.card}>
            <Text style={gs.cardTitle}>Puré de Calabacín</Text>
            <Text style={gs.cardContent}>Ligero y fácil de digerir, perfecto para introducir vegetales.</Text>
          </View>
    
          <View style={gs.card}>
            <Text style={gs.cardTitle}>Papilla de Plátano y Avena</Text>
            <Text style={gs.cardContent}>Energética y nutritiva, excelente para el desayuno.</Text>
          </View>
          
        </ScrollView>
      </View>
    );
    
}

