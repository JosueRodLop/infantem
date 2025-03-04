import React from "react";
import { View, Image, TouchableOpacity, Text } from "react-native";
import { useRouter, Link } from "expo-router";

const NavBar: React.FC = () => {
  const router = useRouter(); 
  const gs = require('../static/styles/globalStyles');

  return (
    <View style={gs.navBar}>
      {}
      <TouchableOpacity onPress={() => router.push("/")}>
        <Image source={require("../static/images/profile.webp")} style={gs.navBarImage} />
      </TouchableOpacity>

      <Link href="/profile" asChild>
        <TouchableOpacity>
          <Text style={gs.navText}>Perfil</Text>
        </TouchableOpacity>
      </Link>
    </View>
  );
};

export default NavBar;