import { useCallback, useEffect, useState } from "react";
import { Linking, Text, TouchableOpacity, View } from "react-native";
import { useAuth } from "../context/AuthContext";
import { useFocusEffect } from "expo-router";
import { Ad } from "../types";

export default function AdBanner({id, brand, sentence, link}: Ad) {
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

  const handlePress = async () => {
    try {
      const supported = await Linking.canOpenURL(link);
      
      if (supported) {
        await fetchClicked();
        await Linking.openURL(link);
      } else {
        console.warn(`Cannot open URL: ${link}`);
      }
    } catch (error) {
      console.error('An error occurred', error);
    }
  };
  
    const fetchClicked = async () => {
      try {
      const response = await fetch(`${apiUrl}/api/v1/advertisements/clicks/${id}`, {
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


  return(
    <TouchableOpacity
      style={gs.bannerContainer} 
      onPress={handlePress}
      activeOpacity={0.7} 
    >
      <Text style={gs.brandText}>{brand}</Text>
      <Text style={gs.sentenceText}>{sentence}</Text>
    </TouchableOpacity>
      );
}
