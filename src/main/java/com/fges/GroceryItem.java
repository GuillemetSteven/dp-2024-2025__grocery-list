package com.fges;

public class GroceryItem {

    private String name;
    private int quantity;

    // Constructeur par défaut
    public GroceryItem() {
    }
    
    public GroceryItem(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
