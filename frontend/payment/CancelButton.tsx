const CancelButton = () => {
    const handleCancel = async () => {
      const response = await fetch("http://localhost:8080/api/stripe/cancel-subscription/test@email.com", {
        method: "DELETE",
      });
      const message = await response.text();
      alert(message);
      window.location.reload();
    };
  
    return (
      <button onClick={handleCancel} style={{ backgroundColor: "red", color: "white", padding: "10px" }}>
        Cancelar Suscripción
      </button>
    );
  };
  
  export default CancelButton;