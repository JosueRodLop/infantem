import React from 'react';
import { View, Text } from 'react-native';
import SubscribeButton from "./SubscribeButton";

const PaymentView = () => {
  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Text style={{ fontSize: 20, fontWeight: 'bold' }}>Suscripción Premium</Text>
      <SubscribeButton />
    </View>
  );
};

export default PaymentView;