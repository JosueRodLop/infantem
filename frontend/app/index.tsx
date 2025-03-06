import { View, Text } from "react-native";
import { Link } from "expo-router";
import NavBar from "../components/NavBar";

export default function Page() {
  const gs = require("../static/styles/globalStyles");

  return (

    <View>
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
        href="/auth/LoginScreen"
        style={{
          color: 'blue',
          textDecorationLine: 'underline',
          marginTop: 20
        }}>
        Go to Login Screen
      </Link>

      <Link
        href="/auth/ProfileScreen"
        style={{
          color: 'blue',
          textDecorationLine: 'underline',
          marginTop: 20
        }}>
        Profile
      </Link>

    </View>
  );
}

