import { useState } from "react";
import { Text, View } from "react-native";
import { MarketItem } from "../../../types";
import MarketItemComponent from "../../../components/MarketItem";

// Steps:
// 1. type data 
// 2. hardcode data
// 3. make a beatiful component

export default function Marketplace() {
  const [marketItems, setMarketItems] = useState<MarketItem[]>([]);

  return (
    <View>
      <MarketItemComponent item={{
        title: "Premium Leather Wallet",
        description: "Handcrafted genuine leather wallet with RFID protection",
        shopUrl: "https://example.com/product/premium-wallet",
        imageUrl: "https://backend.example.com/images/wallet.jpg"
      }} />
    </View>
  );
}
