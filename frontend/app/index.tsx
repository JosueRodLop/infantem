import { View, Text } from "react-native";
import { Link } from "expo-router";
import NavBar from "../components/NavBar";

export default function Page() {
  const gs = require("../static/styles/globalStyles");

  return (
    <View style={{ flex: 1 }}>
      {}
      <NavBar />

      {}
      <View style={[gs.container, { marginTop: 70 }]}>
        <Text>Hello world, frontend is working. :D</Text>
        <Text>This page is not implemented yet. It has no styles.</Text>

        <Link href="/appStyleSamples" style={gs.linkText}>
          Click here to check a showcase of the styles.
        </Link>

        <Link href="/recipeRecommendations" style={gs.linkText}>
          Click here to check an initial version of the recipe recommendations page.
        </Link>

        <Link href="/food" style={gs.linkText}>
          Click here to check an initial version of the food page.
        </Link>
      </View>
    </View>
  );
}

