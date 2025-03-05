import { useState, useEffect } from "react";
import { ActivityIndicator, Modal, TextInput, Alert } from "react-native";
import { Text, View, TouchableOpacity, ScrollView, Image, FlatList } from "react-native";
import { useNavigation } from "@react-navigation/native";
import { Ionicons } from "@expo/vector-icons";

interface User {
  nombre: string;
  apellidos: string;
  nombreUsuario: string;
  email: string;
  rutaFotoPerfil: any;
}

const avatarOptions = [
  require("../../assets/avatar/avatar1.png"),
  require("../../assets/avatar/avatar2.png")
];

export default function ProfileScreen() {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const [modalVisible, setModalVisible] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const navigation = useNavigation();

  const gs = require("../../static/styles/globalStyles"); // Importando estilos globales

  useEffect(() => {
    fetch("http://localhost:8080/usuarios/1") // Se usa el ID del usuario en la URL
      .then(response => response.json())
      .then((data: User) => {
        if (data) {
          setUser(data);
        }
        setLoading(false);
      })
      .catch(error => {
        console.error("Error al cargar el usuario:", error);
        setLoading(false);
      });
  }, []);

  const handleEditProfile = () => {
    setIsEditing(true);
  };

  const handleSaveChanges = () => {
    if (!user) return;
    
    fetch("http://localhost:8080/usuarios/1", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(user)
    })
      .then(response => {
        if (!response.ok) {
          throw new Error("Error en la actualización del perfil");
        }
        return response.json();
      })
      .then(data => {
        if (data) {
          setUser(data);
          setIsEditing(false);
          Alert.alert("Perfil actualizado", "Los cambios han sido guardados correctamente");
        }
      })
      .catch(error => {
        console.error("Error al guardar los cambios:", error);
        Alert.alert("Error", "No se pudo guardar los cambios");
      });
  };

  const handleLogout = () => {
    console.log("Cerrando sesión");
  };

  const handleAvatarSelection = (avatar: any) => {
    if (user && isEditing) {
      setUser({ ...user, rutaFotoPerfil: avatar });
      setModalVisible(false);
    }
  };

  if (!user) {
    return <Text>Cargando perfil...</Text>;
  }
  if (loading) {
    return (
      <View style={gs.loadingContainer}>
        <ActivityIndicator size="large" color="#00446a" />
        <Text style={gs.loadingText}>Cargando perfil...</Text>
      </View>
    );
  }

  return (
    <ScrollView contentContainerStyle={gs.container}>
      <TouchableOpacity style={{ position: 'absolute', top: 20, left: 20, zIndex: 10 }} onPress={() => navigation.goBack()}>
        <Ionicons name="arrow-back" size={24} color="black" />
      </TouchableOpacity>
      
      <Text style={gs.headerText}>Perfil</Text>
      <Text style={gs.subHeaderText}>Información de usuario</Text>

      <TouchableOpacity style={gs.profileImageContainer} onPress={() => isEditing && setModalVisible(true)} disabled={!isEditing}>
        <Image source={user.rutaFotoPerfil || require("../../assets/avatar/avatar1.png")} style={gs.profileImage} />
      </TouchableOpacity>

      <TextInput style={gs.input} value={user.nombre} editable={isEditing} onChangeText={text => setUser({ ...user, nombre: text })} />
      <TextInput style={gs.input} value={user.apellidos} editable={isEditing} onChangeText={text => setUser({ ...user, apellidos: text })} />
      <TextInput style={gs.input} value={user.nombreUsuario} editable={isEditing} onChangeText={text => setUser({ ...user, nombreUsuario: text })} />
      <TextInput style={gs.input} value={user.email} editable={isEditing} onChangeText={text => setUser({ ...user, email: text })} />

      {isEditing ? (
        <TouchableOpacity style={[gs.mainButton, { marginBottom: 20 }]} onPress={handleSaveChanges}>
          <Text style={gs.mainButtonText}>Guardar Cambios</Text>
        </TouchableOpacity>
      ) : (
        <TouchableOpacity style={[gs.mainButton, { marginBottom: 20 }]} onPress={handleEditProfile}>
          <Text style={gs.mainButtonText}>Editar Perfil</Text>
        </TouchableOpacity>
      )}

      <TouchableOpacity style={[gs.secondaryButton, { marginTop: 20 }]} onPress={handleLogout}>
        <Text style={gs.secondaryButtonText}>Cerrar Sesión</Text>
      </TouchableOpacity>

      <Modal visible={modalVisible} animationType="fade" transparent={true}>
        <View style={gs.modalOverlay}>
          <View style={gs.modalContent}>
            <Text style={gs.modalTitle}>Selecciona tu avatar</Text>
            <FlatList
              data={avatarOptions}
              keyExtractor={(item, index) => index.toString()}
              numColumns={2}
              renderItem={({ item }) => (
                <TouchableOpacity onPress={() => handleAvatarSelection(item)}>
                  <Image source={item} style={gs.avatarOption} />
                </TouchableOpacity>
              )}
            />
            <TouchableOpacity style={gs.closeModalButton} onPress={() => setModalVisible(false)}>
              <Text style={gs.closeModalButtonText}>Cerrar</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
    </ScrollView>
  );
}
