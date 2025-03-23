import { useEffect, useState } from "react";
import { Text, View } from "react-native";
import { useAuth } from "../context/AuthContext";

type Ad = {
  id: number;
  brand: string;
  sentence: string;
}

export default function AdBanner({id, brand, sentence}) {
  const gs = require("../static/styles/globalStyles");
  const apiUrl = process.env.EXPO_PUBLIC_API_URL;
  const { token } = useAuth();


  useEffect(() => {

    const interval = setInterval(() => {
      const fetchIncrementMinutes= async () => {
        try {
          const response = await fetch(`${apiUrl}/api/v1/advertisements/${id}/minutes`, {
            method: 'PUT',
            headers: {
              'Authorization': `Bearer ${token}`,
            },
          });

          if (!response.ok) {
              const errorMessage = await response.json();
              throw new Error(errorMessage.error);
          }

          } catch (err) {
            console.error(err)
          }
        }

        fetchIncrementMinutes();

    }, 1000 * 5 ); // time in ms

    return () => clearInterval(interval); 
  }, [])

  return(
      <View style={gs.adBanner}>
        <Text>{brand}</Text>
        <Text>{sentence}</Text>
      </View>
      );

}
