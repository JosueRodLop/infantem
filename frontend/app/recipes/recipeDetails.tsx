import { useLocalSearchParams } from "expo-router"; 
import { View, Text, TouchableOpacity } from "react-native";
import NavBar from "../../components/NavBar";

export default function RecipeDetails() {

  const { recipeTitle } = useLocalSearchParams();
  const gs = require("../../static/styles/globalStyles");

  const recipes = [
    {
      title: "Sweet Potato Purée",
      description: "Sweet potato purée is a creamy, nutrient-dense dish that is rich in vitamins and minerals, making it perfect for babies 6 months and older. It is packed with beta-carotene, which supports eye health and immune function, and is a great first food option. Its naturally sweet flavor and smooth texture make it easy for babies to eat and digest. You can also add a little cinnamon or nutmeg for extra flavor as they grow older.",
      ingredients: [
        "1 large sweet potato",
        "1/2 cup water or breast milk/formula",
        "Optional: cinnamon or nutmeg for flavor"
      ]
    },
    {
      title: "Zucchini Purée",
      description: "Zucchini purée is a light and easily digestible meal that’s ideal for introducing vegetables to your baby. Full of vitamins and minerals, zucchini helps with hydration due to its high water content, while also providing a good source of fiber. Its mild flavor makes it a great choice for babies new to solid foods, and it pairs well with other vegetables or fruits to create a balanced, wholesome meal for little ones.",
      ingredients: [
        "1 medium zucchini",
        "1/4 cup water or breast milk/formula"
      ]
    },
    {
      title: "Banana and Oatmeal Porridge",
      description: "Banana and oatmeal porridge is a wholesome, energy-boosting breakfast perfect for growing babies and toddlers. The combination of ripe bananas and oats provides a rich source of carbohydrates, fiber, and essential nutrients like potassium and iron. This porridge is not only great for digestion but also helps maintain steady energy levels throughout the morning. You can customize it by adding other ingredients like cinnamon, apple slices, or even some chia seeds for added nutrition.",
      ingredients: [
        "1 ripe banana",
        "1/4 cup rolled oats",
        "1/2 cup water or milk (or as needed)",
        "Optional: cinnamon, apple slices, or chia seeds"
      ]
    },
  ];

  const recipe = recipes.find((r) => r.title === recipeTitle);

  if (!recipe) {
    return (
      <View style={gs.container}>
        <Text>Recipe not found</Text>
      </View>
    );
  }

  return (
    <View style={gs.container}>
      <NavBar />
      <View style={[gs.content, { marginTop: 30 }]}>
        <View style={{ flexDirection: "row", alignItems: "center" }}>
          <Text style={gs.headerText}>{recipe.title}</Text>
          <TouchableOpacity style={[gs.mainButton, { marginLeft: 30 }]}>
            <Text style={gs.mainButtonText}>Add to Favourites</Text>
          </TouchableOpacity>
        </View>
        <Text style={gs.card}>{recipe.description}</Text>

        <View style={{ marginTop: 20 }}>
          <Text style={[gs.cardTitle, { marginBottom: 10 }]}>Ingredients:</Text>
          {recipe.ingredients.map((ingredient, index) => (
            <Text key={index} style={gs.cardContent}>- {ingredient}</Text>
          ))}
        </View>
      </View>
    </View>
  );
}
