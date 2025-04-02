import React, { useEffect, useState } from 'react';
import { ScrollView, View } from 'react-native';
import { marketItems as initialMarketItems } from '../../../hardcoded_data/marketItems'; // adjust the path as needed
import { MarketItem } from '../../../types';
import MarketItemComponent from '../../../components/MarketItem';

export default function Marketplace() {
  const [marketItems, setMarketItems] = useState<MarketItem[]>([]);
  const gs = require("../../../static/styles/globalStyles");

  useEffect(() => {
    // Populate the state with the imported market items
    setMarketItems(initialMarketItems);
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
