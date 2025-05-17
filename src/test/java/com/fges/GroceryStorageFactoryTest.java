package com.fges;

import com.fges.dao.CsvGroceryDAO;
import com.fges.dao.GroceryDAO;
import com.fges.dao.JsonGroceryDAO;
import com.fges.factory.GroceryStorageFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GroceryStorageFactoryTest {

    @Test
    public void testGetGroceryDAO() {
        // Test pour le format CSV
        GroceryDAO csvDAO = GroceryStorageFactory.getGroceryDAO("csv");
        assertTrue(csvDAO instanceof CsvGroceryDAO,
                "Pour le format 'csv', devrait retourner une instance de CsvGroceryDAO");

        // Test pour le format JSON
        GroceryDAO jsonDAO = GroceryStorageFactory.getGroceryDAO("json");
        assertTrue(jsonDAO instanceof JsonGroceryDAO,
                "Pour le format 'json', devrait retourner une instance de JsonGroceryDAO");

        // Test pour un format non reconnu (devrait retourner JsonGroceryDAO par défaut)
        GroceryDAO defaultDAO = GroceryStorageFactory.getGroceryDAO("format_inconnu");
        assertTrue(defaultDAO instanceof JsonGroceryDAO,
                "Pour un format non reconnu, devrait retourner une instance de JsonGroceryDAO");

        // Test pour l'insensibilité à la casse
        GroceryDAO csvDAOUpperCase = GroceryStorageFactory.getGroceryDAO("CSV");
        assertTrue(csvDAOUpperCase instanceof CsvGroceryDAO,
                "Devrait être insensible à la casse");
    }
}