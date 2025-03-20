import { useState } from "react";

const SubscribeButton = () => {
  const [loading, setLoading] = useState(false);

  const handleSubscription = async () => {
    setLoading(true);
    try {
      const response = await fetch("http://localhost:8080/api/stripe/create-checkout-session", {
        method: "POST",
      });
      const url = await response.text();
      window.location.href = url; 
    } catch (error) {
      console.error("Error:", error);
    }
    setLoading(false);
  };

  return (
    <button 
      onClick={handleSubscription} 
      disabled={loading}
      style={{ padding: "10px", fontSize: "16px", backgroundColor: "blue", color: "white" }}
    >
      {loading ? "Cargando..." : "Suscribirse"}
    </button>
  );
};

export default SubscribeButton;
