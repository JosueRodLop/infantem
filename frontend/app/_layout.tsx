import { Stack } from 'expo-router';

export default function RootLayout() {

  // We must manage the login logic to got an AuthProvider.
  return (
    <Stack
      initialRouteName="login"
      screenOptions={{
        headerTitle: "Infantem",
      }}
    >
      <Stack.Screen
        name="login"
        options={{
          headerShown: false,
          animation: "slide_from_bottom",
        }}
      />
      <Stack.Screen
        name="profile"
        options={{
          headerTitle: "Profile",
          animation: "slide_from_right",
        }}
      />
      <Stack.Screen
        name="(tabs)"
        options={{
          headerShown: false,
          animation: "slide_from_bottom",
        }}
      />
      <Stack.Screen name="+not-found" />
    </Stack>
  );
}

