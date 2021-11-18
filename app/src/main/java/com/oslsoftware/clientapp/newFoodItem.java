package com.oslsoftware.clientapp;

public class newFoodItem {
    FoodItem item;
    Restaurant makingRestaurant;

    public newFoodItem(FoodItem item, Restaurant makingRestaurant) {
        this.item = item;
        this.makingRestaurant = makingRestaurant;
    }
    public FoodItem getFoodItem()
    {
        return item;
    }
    public newFoodItem(newFoodItem copy)
    {
        this.item = copy.item;
        this.makingRestaurant = copy.makingRestaurant;
    }
}
