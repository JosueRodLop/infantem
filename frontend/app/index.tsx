import { Link } from "expo-router";
import { View, ActivityIndicator, Text } from "react-native";

export default function Index() {
  const gs = require("../static/styles/globalStyles"); 

  return (
    <View
			style={[gs.container, { justifyContent: "center" }]}
		>
      <Text> This needs the login logic to work properly </Text>
			<ActivityIndicator style={{ padding:20 }} size="large" color="black" />

      <Link href={"/recipes"}>
        <Text style={gs.linkText}>See the recipes!</Text>
      </Link>

		</View>
  );
}

