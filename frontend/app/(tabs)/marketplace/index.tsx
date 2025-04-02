import React, { useEffect, useState } from 'react';
import { ScrollView, View } from 'react-native';
import { MarketItem } from '../../../types';
import MarketItemComponent from '../../../components/MarketItem';
import { useAuth } from '../../../context/AuthContext';

export default function Marketplace() {
  const apiUrl = process.env.EXPO_PUBLIC_API_URL;
  const gs = require("../../../static/styles/globalStyles");
  const { token } = useAuth();

  const [marketItems, setMarketItems] = useState<MarketItem[]>([]);

  useEffect(() => {
    const fetchMarketItems = async (): Promise<boolean> => {
      try {
        const response = await fetch(`${apiUrl}/api/v1/products`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        });

        if (!response.ok) {
          throw new Error("Error fetching subscription");
        } 

        const data = await response.json();
        setMarketItems(data.content);
        return true;

      } catch (error) {
        console.error('Error fetching recipes: ', error);
        return false;
      }
    };

    fetchMarketItems();
  }, []);

  return (
    <View style={gs.container}>
      <ScrollView>
        {marketItems.map((item, index) => (
          <MarketItemComponent key={index} item={item} />
        ))}
      </ScrollView>
    </View>
  );
}
