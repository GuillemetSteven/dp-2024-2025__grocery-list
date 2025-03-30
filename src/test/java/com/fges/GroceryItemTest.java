package com.fges;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GroceryItemTest {

    @Test
    public void testConstructorAndGetters() {
        // Test du constructeur et des getters
        GroceryItem item = new GroceryItem("Milk", 5);

        assertEquals("Milk", item.getName(), "Le nom doit correspondre à la valeur définie");
        assertEquals(5, item.getQuantity(), "La quantité doit correspondre à la valeur définie");
    }

    @Test
    public void testSetters() {
        // Test des setters
        GroceryItem item = new GroceryItem();
        item.setName("Milk");
        item.setQuantity(3);

        assertEquals("Milk", item.getName(), "Le nom doit être mis à jour");
        assertEquals(3, item.getQuantity(), "La quantité doit être mise à jour");
    }

    @Test
    public void testAddQuantity() {
        // Test de l'ajout de quantité
        GroceryItem item = new GroceryItem("Milk", 2);
        item.addQuantity(3);

        assertEquals(5, item.getQuantity(), "La quantité devrait être augmentée de 3");
    }

    @Test
    public void testDecreaseQuantity() {
        // Test de la diminution de quantité
        GroceryItem item = new GroceryItem("Bread", 10);

        // Diminution normale
        item.decreaseQuantity(4);
        assertEquals(6, item.getQuantity(), "La quantité devrait être diminuée de 4");

        // Vérifier que la quantité ne devient pas négative
        item.decreaseQuantity(10);
        assertEquals(0, item.getQuantity(), "La quantité ne devrait pas être négative");
    }

}