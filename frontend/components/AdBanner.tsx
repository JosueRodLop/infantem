import { useCallback, useEffect, useState } from "react";
import { Text, View } from "react-native";
import { useAuth } from "../context/AuthContext";
import { useFocusEffect } from "expo-router";
import { Ad } from "../types";

export default function AdBanner({id, brand, sentence}: Ad) {
  const gs = require("../static/styles/globalStyles");
  const apiUrl = process.env.EXPO_PUBLIC_API_URL;
  const { token } = useAuth();


  useFocusEffect(
    useCallback(() => {
      const fetchStartViewing = async () => {
        try {
          const response = await fetch(`${apiUrl}/api/v1/advertisements/start-viewing/${id}`, {
            method: 'POST',
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

      fetchStartViewing();

      // This is returned when the component is unfocused (equivalent to unmounted for react native) 
      return () => {
        const fetchStopViewing = async () => {
        try {
        const response = await fetch(`${apiUrl}/api/v1/advertisements/stop-viewing/${id}`, {
          method: 'POST',
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
      fetchStopViewing();

      };
    }, [])
  );

  return(
    <View style={gs.bannerContainer}>
      <Text style={gs.brandText}>{brand}</Text>
      <Text style={gs.sentenceText}>{sentence}</Text>
    </View>
      );
}
