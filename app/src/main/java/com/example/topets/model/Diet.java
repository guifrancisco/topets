package com.example.topets.model;

import com.example.topets.enums.DietType;

import java.util.Objects;

public class Diet extends Activity{
    private String ingredients;
    private DietType type;

    public Diet(String name, Pet pet, String ingredients, DietType type) {
        super(name, pet);
        this.ingredients = ingredients;
        this.type = type;
    }

    public String getIngredients() {
        return ingredients;
    }

    public DietType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Diet diet = (Diet) o;
        return ingredients.equals(diet.ingredients) && type == diet.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ingredients, type);
    }
}
