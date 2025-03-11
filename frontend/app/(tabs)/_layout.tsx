import { Tabs } from 'expo-router';
//import { Redirect } from "expo-router";

export default function TabLayout() {
  return (
     <Tabs>
       <Tabs.Screen
       name="recipes"
       options={{
         headerShown: false,
         lazy: true,
         tabBarLabel: "Recipes",
       }}
       />
        <Tabs.Screen
          name="food"
          options={{
            lazy: true,
            headerShown: false,
            tabBarLabel: "Food",
          }}
        />
        <Tabs.Screen
        name="allergens"
        options={{
          lazy: true,
          headerShown: false,
          tabBarLabel: "Allergens",
        }}
        />
      </Tabs>
		);
  // We should return this if the user is not logged in
	// return <Redirect href="/login" />;
}

