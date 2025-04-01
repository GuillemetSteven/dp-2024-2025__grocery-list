package com.fges;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GroceryItemTest {

    @Test
    public void testConstructorAndGetters() {
        // Utilisation du constructeur à 3 arguments
        GroceryItem item = new GroceryItem("Milk", 5, "Dairy");
        assertEquals("Milk", item.getName(), "Le nom doit correspondre à la valeur définie");
        assertEquals(5, item.getQuantity(), "La quantité doit correspondre à la valeur définie");
        assertEquals("Dairy", item.getCategory(), "La catégorie doit correspondre à la valeur définie");
    }

    @Test
    public void testSetters() {
        // Test des setters pour le nom et la quantité avec le constructeur par défaut.
        GroceryItem item = new GroceryItem();
        item.setName("Milk");
        item.setQuantity(3);
        // La catégorie n'est pas modifiée par les setters, elle reste nulle
        assertEquals("Milk", item.getName(), "Le nom doit être mis à jour");
        assertEquals(3, item.getQuantity(), "La quantité doit être mise à jour");
        assertNull(item.getCategory(), "La catégorie devrait être nulle si non initialisée");
    }

    @Test
    public void testAddQuantity() {
        // Test de l'ajout de quantité
        GroceryItem item = new GroceryItem("Milk", 2, "Dairy");
        item.addQuantity(3);
        assertEquals(5, item.getQuantity(), "La quantité devrait être augmentée de 3");
    }

    @Test
    public void testDecreaseQuantity() {
        // Test de la diminution de quantité
        GroceryItem item = new GroceryItem("Bread", 10, "Bakery");

        // Diminution normale
        item.decreaseQuantity(4);
        assertEquals(6, item.getQuantity(), "La quantité devrait être diminuée de 4");

        // Vérifier que la quantité ne devient pas négative
        item.decreaseQuantity(10);
        assertEquals(0, item.getQuantity(), "La quantité ne devrait pas être négative");
    }

    @Test
    public void testToString() {
        // Vérifier que la représentation en chaîne inclut bien le nom, la quantité et la catégorie
        GroceryItem item = new GroceryItem("Eggs", 12, "Poultry");
        String expected = "Eggs: 12: Poultry";
        assertEquals(expected, item.toString(), "La représentation en chaîne doit être correctement formatée");
    }
}
