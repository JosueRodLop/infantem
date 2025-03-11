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
					title: "Food",
				}}
			/>
		</Stack>
	);
}

export default StackLayout;

