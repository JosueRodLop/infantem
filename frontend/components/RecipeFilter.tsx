import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  ScrollView,
  StyleSheet,
  Animated,
} from 'react-native';

const RecipeFilterComponent = ({ filters, setFilters }) => {
  const [ingredient, setIngredient] = useState('');
  const [allergen, setAllergen] = useState('');
  
  const [expanded, setExpanded] = useState(false);
  const animatedHeight = useState(new Animated.Value(0))[0];

  const toggleExpand = () => {
    const toValue = expanded ? 0 : 1;
    Animated.timing(animatedHeight, {
      toValue: toValue,
      duration: 300,
      useNativeDriver: false,
    }).start();
    setExpanded(!expanded);
  };

  const getActiveFiltersCount = () => {
    let count = 0;
    if (filters.searchQuery) count++;
    if (filters.minAge !== undefined) count++;
    if (filters.maxAge !== undefined) count++;
    if (filters.ingredients && filters.ingredients.length > 0) count++;
    if (filters.allergens && filters.allergens.length > 0) count++;
    return count;
  };

  const activeFiltersCount = getActiveFiltersCount();
  const contentHeight = animatedHeight.interpolate({
    inputRange: [0, 1],
    outputRange: [0, 470], 
  });

  const addIngredient = () => {
    if (ingredient.trim() !== '') {
      setFilters({
        ...filters,
        ingredients: [...(filters.ingredients || []), ingredient.trim()],
      });
      setIngredient('');
    }
  };

  const removeIngredient = (index: number) => {
    const updatedIngredients = [...(filters.ingredients || [])];
    updatedIngredients.splice(index, 1);
    setFilters({
      ...filters,
      ingredients: updatedIngredients,
    });
  };

  const addAllergen = () => {
    if (allergen.trim() !== '') {
      setFilters({
        ...filters,
        allergens: [...(filters.allergens || []), allergen.trim()],
      });
      setAllergen('');
    }
  };

  const removeAllergen = (index: number) => {
    const updatedAllergens = [...(filters.allergens || [])];
    updatedAllergens.splice(index, 1);
    setFilters({
      ...filters,
      allergens: updatedAllergens,
    });
  };

  const handleReset = () => {
    setFilters({});
  };

  return (
    <View style={styles.outerContainer}>
      <TouchableOpacity style={styles.toggleButton} onPress={toggleExpand}>
        <Text style={styles.toggleButtonText}>
          Filtros de recetas 
        </Text>
        {activeFiltersCount > 0 && (
          <View style={styles.badge}>
            <Text style={styles.badgeText}>{activeFiltersCount}</Text>
          </View>
        )}
        <Text style={styles.expandIcon}>{expanded ? "▲" : "▼"}</Text>
      </TouchableOpacity>

      <Animated.View style={[styles.animatedContainer, { height: contentHeight }]}>
        {expanded && (
          <ScrollView style={styles.container}>
            <View style={styles.inputContainer}>
              <Text style={styles.label}>Buscar</Text>
              <TextInput
                style={styles.input}
                placeholder="Busca por el nombre de la receta..."
                value={filters.searchQuery || ''}
                onChangeText={(text) => setFilters({ ...filters, searchQuery: text })}
                placeholderTextColor="#A0A0A0"
              />
            </View>

            <View style={styles.rowContainer}>
              <View style={styles.halfInputContainer}>
                <Text style={styles.label}>Edad mínima</Text>
                <TextInput
                  style={styles.input}
                  placeholder="Min"
                  value={filters.minAge?.toString() || ''}
                  onChangeText={(text) => {
                    const minAge = text === '' ? undefined : parseInt(text, 10);
                    setFilters({ ...filters, minAge });
                  }}
                  keyboardType="numeric"
                  placeholderTextColor="#A0A0A0"
                />
              </View>
              <View style={styles.halfInputContainer}>
                <Text style={styles.label}>Edad máxima</Text>
                <TextInput
                  style={styles.input}
                  placeholder="Máx"
                  value={filters.maxAge?.toString() || ''}
                  onChangeText={(text) => {
                    const maxAge = text === '' ? undefined : parseInt(text, 10);
                    setFilters({ ...filters, maxAge });
                  }}
                  keyboardType="numeric"
                  placeholderTextColor="#A0A0A0"
                />
              </View>
            </View>

            <View style={styles.inputContainer}>
              <Text style={styles.label}>Ingredientes</Text>
              <View style={styles.addItemContainer}>
                <TextInput
                  style={styles.addItemInput}
                  placeholder="Añade un ingrediente"
                  value={ingredient}
                  onChangeText={setIngredient}
                  placeholderTextColor="#A0A0A0"
                />
                <TouchableOpacity style={styles.addButton} onPress={addIngredient}>
                  <Text style={styles.addButtonText}>Añadir</Text>
                </TouchableOpacity>
              </View>
              <View style={styles.tagsContainer}>
                {(filters.ingredients || []).map((item, index) => (
                  <TouchableOpacity
                    key={index}
                    style={styles.tag}
                    onPress={() => removeIngredient(index)}
                  >
                    <Text style={styles.tagText}>{item} ×</Text>
                  </TouchableOpacity>
                ))}
              </View>
            </View>

            <View style={styles.inputContainer}>
              <Text style={styles.label}>Alérgenos</Text>
              <View style={styles.addItemContainer}>
                <TextInput
                  style={styles.addItemInput}
                  placeholder="Añade un alérgeno"
                  value={allergen}
                  onChangeText={setAllergen}
                  placeholderTextColor="#A0A0A0"
                />
                <TouchableOpacity style={styles.addButton} onPress={addAllergen}>
                  <Text style={styles.addButtonText}>Añadir</Text>
                </TouchableOpacity>
              </View>
              <View style={styles.tagsContainer}>
                {(filters.allergens || []).map((item, index) => (
                  <TouchableOpacity
                    key={index}
                    style={styles.tag}
                    onPress={() => removeAllergen(index)}
                  >
                    <Text style={styles.tagText}>{item} ×</Text>
                  </TouchableOpacity>
                ))}
              </View>
            </View>

            <View style={styles.buttonContainer}>
              <TouchableOpacity style={styles.resetButton} onPress={handleReset}>
                <Text style={styles.resetButtonText}>Restablecer</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.applyButton} onPress={() => setFilters({ ...filters, page: 0 })}>
                <Text style={styles.applyButtonText}>Aplicar filtros</Text>
              </TouchableOpacity>
            </View>
          </ScrollView>
        )}
      </Animated.View>
    </View>
  );
};

const styles = StyleSheet.create({
  outerContainer: {
    backgroundColor: 'white',
    borderRadius: 8,
    overflow: 'hidden',
    marginBottom: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
    marginHorizontal: "5%",
    marginVertical: 20,
  },
  animatedContainer: {
    overflow: 'hidden',
  },
  toggleButton: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: 16,
    backgroundColor: "#1565C0",
  },
  toggleButtonText: {
    fontSize: 16,
    fontWeight: 'bold',
    color: 'white',
  },
  expandIcon: {
    color: 'white',
    fontSize: 16,
  },
  badge: {
    backgroundColor: 'white',
    borderRadius: 10,
    width: 20,
    height: 20,
    justifyContent: 'center',
    alignItems: 'center',
    marginLeft: 8,
  },
  badgeText: {
    color: "#1565C0",
    fontSize: 12,
    fontWeight: 'bold',
  },
  container: {
    padding: 16,
    backgroundColor: 'white',
  },
  title: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 16,
    color: "#1565C0",
  },
  inputContainer: {
    marginBottom: 16,
  },
  rowContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 16,
  },
  halfInputContainer: {
    width: '48%',
  },
  label: {
    fontSize: 16,
    marginBottom: 8,
    color: "#1565C0",
    fontWeight: '500',
  },
  input: {
    height: 40,
    borderWidth: 1,
    borderColor: '#D0D0D0',
    borderRadius: 8,
    paddingHorizontal: 12,
    backgroundColor: 'white',
    color: '#333333',
  },
  addItemContainer: {
    flexDirection: 'row',
    marginBottom: 8,
  },
  addItemInput: {
    flex: 1,
    height: 40,
    borderWidth: 1,
    borderColor: '#D0D0D0',
    borderRadius: 8,
    paddingHorizontal: 12,
    backgroundColor: 'white',
    marginRight: 8,
    color: '#333333',
  },
  addButton: {
    backgroundColor: "#1565C0",
    borderRadius: 8,
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: 16,
  },
  addButtonText: {
    color: 'white',
    fontWeight: '500',
  },
  tagsContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
  },
  tag: {
    backgroundColor: '#e6f0ff', 
    borderRadius: 16,
    paddingVertical: 6,
    paddingHorizontal: 12,
    margin: 4,
    borderWidth: 1,
    borderColor: '#0052cc',
  },
  tagText: {
    color: '1565CO',
  },
  buttonContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 16,
    marginBottom: 8,
  },
  resetButton: {
    width: '30%',
    height: 44,
    borderRadius: 8,
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: "#1565C0",
  },
  resetButtonText: {
    color: "#1565C0",
    fontWeight: '600',
  },
  applyButton: {
    width: '65%',
    height: 44,
    backgroundColor: "#1565C0",
    borderRadius: 8,
    justifyContent: 'center',
    alignItems: 'center',
  },
  applyButtonText: {
    color: 'white',
    fontWeight: '600',
  },
});

export default RecipeFilterComponent;
