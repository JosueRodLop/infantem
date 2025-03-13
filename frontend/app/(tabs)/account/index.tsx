import { useState, useEffect } from "react";
import { ActivityIndicator, Modal, TextInput, Alert } from "react-native";
import { Text, View, TouchableOpacity, ScrollView, Image, FlatList } from "react-native";
import { useNavigation } from "@react-navigation/native";
import { Ionicons } from "@expo/vector-icons";
import { getToken } from '../../../utils/jwtStorage';
import { jwtDecode } from "jwt-decode";

interface User {
  id: number;
  name: string;
  surname: string;
  username: string;
  email: string;
  profilePhotoRoute: string;
}

const avatarOptions = [
  require("../../../assets/avatar/avatar1.png"),
  require("../../../assets/avatar/avatar2.png")
];

export default function Account() {
  const [user, setUser] = useState<User | null>(null);
  const [userId, setUserId] = useState<number | null>(null);
  const [modalVisible, setModalVisible] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const navigation = useNavigation();
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [loading, setLoading] = useState(true);

  const gs = require("../../../static/styles/globalStyles");

  useEffect(() => {
    const checkAuth = async () => {
      const authToken = await getToken();

      if (authToken) {
        console.log("Setting isLoggedIn to true");
        setIsLoggedIn(true);
        try {
          const decodedToken: any = jwtDecode(authToken);
          console.log("Decoded token:", decodedToken);
          const userId = decodedToken.jti;
          setUserId(userId);
        } catch (error) {
          console.error("Error decoding token:", error);
          console.log("Setting isLoggedIn to false");
          setIsLoggedIn(false);
          setUserId(null);
        }
      } else {
        console.log("User is not logged in.");
        console.log("Setting isLoggedIn to false");
        setIsLoggedIn(false);
      }
    };

    checkAuth();
  }, []);

  useEffect(() => {
    console.log("isLoggedIn:", isLoggedIn);
    console.log("userId:", userId);
    if (isLoggedIn && userId) {
      setLoading(true);
      console.log("Fetching user data...");
      fetch(`http://localhost:8081/api/v1/users/${userId}`)
        .then((response) => {
          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }
          console.log("🟠 Respuesta del servidor:", response);
          return response.json();
        })
        .then((data) => {
          const profilePhotoRoute = data.profilePhotoRoute === null ? "" : data.profilePhotoRoute;
          setUser({ ...data, profilePhotoRoute: profilePhotoRoute });
          console.log("🟢 Datos del usuario:", user);
        })
        .catch((error) => {
          console.error("Error fetching recipes:", error);
        })
        .finally(() => setLoading(false));
    }
  }, [isLoggedIn, userId]);

  const handleEditProfile = () => {
    setIsEditing(true);
  };

  const handleSaveChanges = () => {
    const profilePhotoRoute = user.profilePhotoRoute === null ? "" : user.profilePhotoRoute;

    const userData = {
      id: user.id,
      firstName: user.name,
      lastName: user.surname,
      username: user.username,
      email: user.email,
      avatar: profilePhotoRoute
    };

    console.log("🟡 Datos enviados al backend:", userData);

    fetch(`http://localhost:8080/api/v1/users/${userId}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(userData)
    })
      .then(response => {
        console.log("🟠 Respuesta del servidor:", response);
        if (!response.ok) {
          return response.json().then(err => { throw new Error(JSON.stringify(err)); });
        }
        return response.json();
      })
      .then(data => {
        console.log("🟢 Datos actualizados en el backend:", data);
        setUser((prevUser: any) => ({ ...prevUser, ...data }));
        setIsEditing(false);
        Alert.alert("Perfil actualizado", "Los cambios han sido guardados correctamente");
      })
      .catch(error => {
        console.error("🔴 Error al guardar los cambios:", error);
        Alert.alert("Error", `No se pudo guardar los cambios: ${error.message}`);
      });
  };


  const handleLogout = () => {
    console.log("Cerrando sesión");
  };


  const handleAvatarSelection = (avatar: any) => {
    if (user && isEditing) {
      const avatarUri = typeof avatar === "number"
        ? Image.resolveAssetSource(avatar).uri  // Convierte require() a una URI
        : avatar; // Si ya es una URI, úsala directamente

      setUser({ ...user, profilePhotoRoute: avatarUri });
      setModalVisible(false);
    }
  };


  if (!loading && !isLoggedIn && !user) {
    console.log("IsLoggedIn:", isLoggedIn);
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
          <Image source={user.profilePhotoRoute ? { uri: user.profilePhotoRoute } : avatarOptions[0]} style={gs.profileImage} />
        </TouchableOpacity>

        <TextInput
          style={gs.input}
          value={user.name}
          editable={isEditing}
          onChangeText={(text) => setUser({ ...user, name: text })}
        />
        <TextInput
          style={gs.input}
          value={user.surname}
          editable={isEditing}
          onChangeText={(text) => setUser({ ...user, surname: text })}
        />
        <TextInput
          style={gs.input}
          value={user.username}
          editable={isEditing}
          onChangeText={(text) => setUser({ ...user, username: text })}
        />
        <TextInput
          style={gs.input}
          value={user.email}
          editable={isEditing}
          onChangeText={(text) => setUser({ ...user, email: text })}
        />

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
    </View>
  );
}

