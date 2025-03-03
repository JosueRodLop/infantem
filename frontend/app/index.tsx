import { Link } from "expo-router";
import { Text, View } from "react-native";
import Navbar from "./components/Navbar";

export default function Page() {
  return (
    <View> 
      <Navbar />
      <Text>Hello world, frontend is working. :D</Text>
      <Text>This page is not implemented yet. It has not styles.</Text>
      <Link
        href="/appStyleSamples"
        style={{
          color: 'blue', 
          textDecorationLine: 'underline', 
        }}
      >
        Click here to check a showcase of the styles.
      </Link>
      <Link
        href="/alimentos"
        style={{
          color: 'green', 
          textDecorationLine: 'underline', 
        }}
      >
        Registrar Alimentos.
      </Link>
    </View>
  );
}

