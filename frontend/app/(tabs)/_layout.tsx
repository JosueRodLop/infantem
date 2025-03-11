import { Tabs } from 'expo-router';
//import { Redirect } from "expo-router";

export default function TabLayout() {
  return (
     <Tabs>
					<Tabs.Screen
						name="allergens"
						options={{
							lazy: true,
							tabBarLabel: "Allergens",
						}}
					/>
          <Tabs.Screen
						name="food"
						options={{
							lazy: true,
							tabBarLabel: "Food",
						}}
					/>
					<Tabs.Screen
						name="recipes"
						options={{
							lazy: true,
							tabBarLabel: "Recipes",
						}}
					/>
				</Tabs>
		);
  // We should return this if the user is not logged in
	// return <Redirect href="/login" />;
}

