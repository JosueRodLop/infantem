import { Stack } from "expo-router";

function StackLayout() {
	return (
		<Stack
			screenOptions={{
				headerTitleStyle: {
					fontWeight: "normal",
					fontSize: 25,
					fontFamily: "sans-serif",
				},
			}}
		>
			<Stack.Screen
				name="index"
				options={{
					title: "Recipes",
				}}
			/>
      <Stack.Screen
				name="detail"
				options={{
					title: "Detail",
				}}
			/>
      <Stack.Screen
				name="add"
				options={{
					title: "Add a recipe",
				}}
			/>

		</Stack>
	);
}

export default StackLayout;

