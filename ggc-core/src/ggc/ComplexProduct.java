package ggc;

public class ComplexProduct extends Product {
    
    private Recipe _recipe;

    public ComplexProduct(String key, Recipe recipe) {
        super(key);
        _recipe = recipe;
    }

    @Override
    public String showProduct() {
        return super.showProduct() + "|" + _recipe.showRecipe();
    }

    @Override
    public Recipe getRecipe() {
        return _recipe;
    }

    public int getDateModifier() {
        return 3;
    }
}
