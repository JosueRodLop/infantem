import { useEffect, useState } from "react";
import SubscribeButton from "./SubscribeButton";

const App = () => {
  const [isSubscribed, setIsSubscribed] = useState(false);

  useEffect(() => {
    fetch("http://localhost:8080/api/stripe/subscription-status/test@email.com")
      .then(res => res.text())
      .then(status => {
        if (status === "activo") setIsSubscribed(true);
      });
  }, []);

  return (
    <div>
      <h1>{isSubscribed ? "Tienes una suscripción activa 🎉" : "Suscríbete ahora"}</h1>
      {!isSubscribed && <SubscribeButton />}
    </div>
  );
};

export default App;