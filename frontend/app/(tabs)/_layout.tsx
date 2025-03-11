import { Tabs } from 'expo-router';
import Ionicons from '@expo/vector-icons/Ionicons';
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
         tabBarIcon: ({ color }) => (
          <Ionicons name='list' color={color} size={24} />
          ),
       }}
       />
        <Tabs.Screen
          name="food"
          options={{
            lazy: true,
            headerShown: false,
            tabBarLabel: "Food",
            tabBarIcon: ({ color }) => (
              <Ionicons name='egg' color={color} size={24} />
            ),
          }}
        />
        <Tabs.Screen
        name="allergens"
        options={{
          lazy: true,
          headerShown: false,
          tabBarLabel: "Allergens",
          tabBarIcon: ({ color }) => (
            <Ionicons name='medical' color={color} size={24} />
          ),
        }}
        />
        <Tabs.Screen
        name="account"
        options={{
          lazy: true,
          headerShown: false,
          tabBarLabel: "Account",
          tabBarIcon: ({ color }) => (
            <Ionicons name='man' color={color} size={24} />
          ),
        }}
        />
      </Tabs>
		);
  // We should return this if the user is not logged in
	// return <Redirect href="/login" />;
}

