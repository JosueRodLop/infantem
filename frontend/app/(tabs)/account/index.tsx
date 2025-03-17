import { useState } from "react";
import { ActivityIndicator, Modal, TextInput, Alert } from "react-native";
import { Text, View, TouchableOpacity, ScrollView, Image, FlatList } from "react-native";
import { useNavigation } from "@react-navigation/native";
import { Ionicons } from "@expo/vector-icons";
import { router } from "expo-router";
import { useAuth } from "../../../context/AuthContext";

const avatarOptions = [
  // There are no avatar images in backend yet.
  require("../../../assets/avatar/avatar1.png"),
  require("../../../assets/avatar/avatar2.png")
];

export default function Account() {
  const [modalVisible, setModalVisible] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const navigation = useNavigation();

  const apiUrl = process.env.EXPO_PUBLIC_API_URL;

  const gs = require("../../../static/styles/globalStyles");

  const { isLoading, user, token, setUser, checkAuth, signOut } = useAuth();


  const handleEditProfile = () => {
    setIsEditing(true);
  };

  const handleSaveChanges = () => {
    if (!user || !token) return;

    const userData = {
      id: user.id,
      name: user.name,
      surname: user.surname,
      username: user.username,
      password: user.password,
      email: user.email,
      profilePhotoRoute: user.profilePhotoRoute
    };

    fetch(`${apiUrl}/api/v1/users/${user.id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      },
      body: JSON.stringify(userData)
    })
      .then(response => {
        if (!response.ok) {
          return response.json().then(err => { throw new Error(JSON.stringify(err)); });
        }
        return response.json();
      })
      .then(data => {
        setUser(data);
        setIsEditing(false);
        Alert.alert("Perfil actualizado", "Los cambios han sido guardados correctamente");
        router.push("/account");
      })
      .catch(error => {
        Alert.alert("Error", `No se pudo guardar los cambios: ${error.message}`);
      });
  };

  const handleLogout = signOut;

  const handleAvatarSelection = (avatar: any) => {
    if (user && isEditing) {
      const avatarUri = typeof avatar === "number"
        ? Image.resolveAssetSource(avatar).uri
        : avatar;

      setUser({ ...user, profilePhotoRoute: avatarUri });
      setModalVisible(false);
    }
  };

  if (isLoading) {
    return (
      <View style={gs.loadingContainer}>
        <ActivityIndicator size="large" color="#00446a" />
        <Text style={gs.loadingText}>Cargando perfil...</Text>
      </View>
    );
  }

  return (
    <View style={{ flex: 1 }}>
      <ScrollView contentContainerStyle={[gs.container, { paddingTop: 100, paddingBottom: 100 }]}>
        <TouchableOpacity style={{ position: 'absolute', top: 20, left: 20, zIndex: 10 }} onPress={() => navigation.goBack()}>
          <Ionicons name="arrow-back" size={24} color="black" />
        </TouchableOpacity>

        <Text style={gs.headerText}>Perfil</Text>
        <Text style={gs.subHeaderText}>Información de usuario</Text>

        <TouchableOpacity style={gs.profileImageContainer} onPress={() => isEditing && setModalVisible(true)} disabled={!isEditing}>
          <Image source={user && user.profilePhotoRoute ? { uri: user.profilePhotoRoute } : avatarOptions[0]} style={gs.profileImage} />
        </TouchableOpacity>

        {user && (
          <>
            <Text style={gs.inputLabel}>Nombre</Text>
            <TextInput
              style={gs.card}
              value={user.name}
              editable={isEditing}
              onChangeText={(text) => setUser({ ...user, name: text })}
            />
            <Text style={gs.inputLabel}>Apellido</Text>
            <TextInput
              style={gs.card}
              value={user.surname}
              editable={isEditing}
              onChangeText={(text) => setUser({ ...user, surname: text })}
            />
            <Text style={gs.inputLabel}>Nombre de Usuario</Text>
            <TextInput
              style={gs.card}
              value={user.username}
              editable={isEditing}
              onChangeText={(text) => setUser({ ...user, username: text })}
            />
            <Text style={gs.inputLabel}>Correo Electrónico</Text>
            <TextInput
              style={gs.card}
              value={user.email}
              editable={isEditing}
              onChangeText={(text) => setUser({ ...user, email: text })}
            />
          </>
        )}

        {isEditing ? (
          <TouchableOpacity style={[gs.mainButton, { marginBottom: 20 }]} onPress={handleSaveChanges}>
            <Text style={gs.mainButtonText}>Guardar Cambios</Text>
          </TouchableOpacity>
        ) : (
          <TouchableOpacity style={[gs.mainButton, { marginBottom: 20 }]} onPress={handleEditProfile}>
            <Text style={gs.mainButtonText}>Editar Perfil</Text>
          </TouchableOpacity>
        )}

        {<TouchableOpacity style={[gs.secondaryButton, { marginTop: 20 }]} onPress={handleLogout}>
          <Text style={gs.secondaryButtonText}>Cerrar Sesión</Text>
        </TouchableOpacity>}

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
    </View>
  );
}

