package com.fges;

public class GroceryItem {

    private String name;
    private int quantity;
    private String category;

    // Constructeur par défaut
    public GroceryItem() {
    }

    public GroceryItem(String name, int quantity , String category) {
        this.name = name;
        this.quantity = quantity;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity(){
        return quantity;
    }

    public String getCategory(){
        return category;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Increase the quantity
    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    // Decrease the quantity
    public void decreaseQuantity(int amount) {
        this.quantity = Math.max(0, this.quantity - amount);
    }

    // Display using toString
    @Override
    public String toString() {
        return name + ": " + quantity + ": " + category;
    }
}
