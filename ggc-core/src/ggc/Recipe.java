package ggc;

import java.io.Serializable;

public class Recipe implements Serializable {

    private String[] _components;
    private int[] _amounts;
    private double _alphaFactor;

    public Recipe(int n_products, double alphaFactor) {
        _components = new String[n_products];
        _amounts = new int[n_products];
        _alphaFactor = alphaFactor;
    }

    public String[] getComponents() {
        return _components;
    }

    public String getComponent(int index) {
        return _components[index];
    }

    public int getAmount(int index) {
        return _amounts[index];
    }

    public double getAlpha() {
        return _alphaFactor;
    }

    public void setComponentAndAmount(int p, String key, int amount) {
        _components[p] = key;
        _amounts[p] = amount;
    }

    public String showRecipe() {
        String s = "";
        for (int i = 0; i < _components.length; i++) {
            s = s + _components[i] + ":" + _amounts[i];
            if (i < _components.length - 1) {
                s += "#";
            }
        }
        return _alphaFactor + "|" + s;
    }
    
}
