import React from 'react';
import { View, Text, TouchableOpacity, Image } from 'react-native';

const Navbar = () => {
  const gs = require('../../static/styles/globalStyles');

  return (
    <View style={gs.navbar}>
        
        <Image source={require("../../static/images/profile.webp")} style={gs.navbarImage} />

      <TouchableOpacity onPress={() => { window.location.href = "/"; }}>
        <Text style={gs.navbarText}>Inicio</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={() => { window.location.href = "/alimentos"; }}>
        <Text style={gs.navbarText}>Alimentos</Text>
      </TouchableOpacity>
    </View>
  );
};

export default Navbar;