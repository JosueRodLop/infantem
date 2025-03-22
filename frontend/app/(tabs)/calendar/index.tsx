import React, { useState, useEffect } from "react";
import { View, Text, ImageBackground, ScrollView, ActivityIndicator } from "react-native";
import { Calendar } from "react-native-calendars";
import { getToken } from "../../../utils/jwtStorage"; // Importar la función para obtener el token JWT

const gs = require("../../../static/styles/globalStyles");

const CalendarTab = () => {
  const [selectedDate, setSelectedDate] = useState<string | null>(null);
  const [events, setEvents] = useState<{ [key: string]: string[] }>({});
  const [loading, setLoading] = useState<boolean>(true);
  const [jwt, setJwt] = useState<string | null>(null); // Estado para almacenar el token JWT

  const apiUrl = process.env.EXPO_PUBLIC_API_URL;

  // Manejar la selección de un día en el calendario
  const handleDayPress = (day: { dateString: string }) => {
    setSelectedDate(day.dateString);
  };

  // Obtener los eventos del calendario desde el backend
  const fetchCalendarEvents = async () => {
    try {
      setLoading(true);

      if (!jwt) {
        console.error("No se encontró el token JWT");
        return;
      }

      // Obtener el mes y año actuales
      const currentDate = new Date();
      const month = currentDate.getMonth() + 1; // Los meses en JavaScript comienzan desde 0
      const year = currentDate.getFullYear();

      const response = await fetch(`${apiUrl}/api/v1/calendar?month=${month}&year=${year}`, {
        headers: {
          Authorization: `Bearer ${jwt}`, // Incluir el token JWT en las cabeceras
        },
      });

      if (!response.ok) {
        throw new Error("Error al obtener los eventos del calendario");
      }

      const data = await response.json();

      // Transformar los datos en un formato adecuado para el marcado del calendario
      const transformedEvents: { [key: string]: string[] } = {};
      data.forEach((calendar: any) => {
        Object.keys(calendar.events).forEach((date) => {
          if (!transformedEvents[date]) {
            transformedEvents[date] = [];
          }
          transformedEvents[date] = [...transformedEvents[date], ...calendar.events[date]];
        });
      });

      setEvents(transformedEvents);
    } catch (error) {
      console.error("Error al obtener los eventos del calendario:", error);
    } finally {
      setLoading(false);
    }
  };

  // Obtener el token JWT al cargar el componente
  useEffect(() => {
    const fetchToken = async () => {
      const token = await getToken(); // Obtener el token JWT desde el almacenamiento
      setJwt(token);
    };

    fetchToken();
  }, []);

  // Obtener los eventos del calendario cuando se tenga el token JWT
  useEffect(() => {
    if (jwt) {
      fetchCalendarEvents();
    }
  }, [jwt]);

  // Obtener los eventos del día seleccionado
  useEffect(() => {
    if (selectedDate && jwt) {
      fetchCalendarEvents();
    }
  }, [selectedDate]);

  // Renderizar el componente del calendario
  const renderCalendar = () => (
    <View style={gs.card}>
      <Calendar
        onDayPress={handleDayPress}
        markedDates={Object.keys(events).reduce((acc, date) => {
          acc[date] = {
            marked: true,
            selected: date === selectedDate,
            selectedColor: "#00adf5",
          };
          return acc;
        }, {} as { [key: string]: any })}
        theme={{
          selectedDayBackgroundColor: "#00adf5",
          todayTextColor: "#00adf5",
          arrowColor: "#00adf5",
          textDayFontWeight: "bold",
          textMonthFontWeight: "bold",
          textDayHeaderFontWeight: "bold",
        }}
      />
    </View>
  );

  // Renderizar la información del día seleccionado
  const renderSelectedDateInfo = () => (
    <View style={gs.card}>
      {selectedDate ? (
        events[selectedDate] ? (
          <View>
            <Text style={gs.headerText}>Eventos del día {selectedDate}:</Text>
            {events[selectedDate].map((event, index) => (
              <Text key={index} style={gs.bodyText}>
                - {event}
              </Text>
            ))}
          </View>
        ) : (
          <Text style={gs.bodyText}>No hay eventos para el día seleccionado.</Text>
        )
      ) : (
        <Text style={gs.bodyText}>Selecciona un día para ver información</Text>
      )}
    </View>
  );

  // Renderizar el contenido principal
  const renderContent = () => (
    <ImageBackground
      source={require("../../../static/images/Background.png")}
      style={{ flex: 1, width: "100%", height: "100%" }}
      imageStyle={{ resizeMode: "cover", opacity: 0.9 }}
    >
      <ScrollView contentContainerStyle={{ flexGrow: 1, padding: 20 }}>
        <Text style={gs.headerText}>Calendario</Text>
        <Text style={gs.bodyText}>Selecciona una fecha para ver información</Text>
        {loading ? (
          <ActivityIndicator size="large" color="#00adf5" />
        ) : (
          <>
            {renderCalendar()}
            {renderSelectedDateInfo()}
          </>
        )}
      </ScrollView>
    </ImageBackground>
  );

  return renderContent();
};

export default CalendarTab;



