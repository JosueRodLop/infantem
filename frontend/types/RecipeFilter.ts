export type RecipeFilter = {
  searchQuery?: string;
  maxAge?: number;
  minAge?: number;
  ingredients?: string[];
  allergens?: string[];
}
