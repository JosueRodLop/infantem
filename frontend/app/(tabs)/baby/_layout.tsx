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
					title: "Mis bebés",
				}}
			/>

      		<Stack.Screen
				name="form"
				options={{
					title: "Añadir o editar un bebé",
				}}
			/>

		</Stack>
	);
}

export default StackLayout;
