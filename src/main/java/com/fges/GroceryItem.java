package com.fges;

// Représente un article dans la liste de courses avec son nom, sa quantité et sa catégorie
public class GroceryItem {

    private String name;
    private int quantity;
    private String category;

    // Constructeur par défaut
    public GroceryItem() {
    }

    public GroceryItem(String name, int quantity, String category) {
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

    // Augmente la quantité
    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    // Diminue la quantité en s'assurant qu'elle reste >= 0
    public void decreaseQuantity(int amount) {
        this.quantity = Math.max(0, this.quantity - amount);
    }

    @Override
    public String toString() {
        return name + ": " + quantity + ": " + category;
    }
}
