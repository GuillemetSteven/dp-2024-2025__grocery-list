package com.fges;

import com.fges.dao.JsonGroceryDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonGroceryDAOTest {

    private JsonGroceryDAO dao;
    private Path tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        dao = new JsonGroceryDAO();
        tempFile = Files.createTempFile("test_grocery", ".json");
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testWriteAndReadGroceryList() throws IOException {
        // Création d'une liste de courses
        List<GroceryItem> originalList = new ArrayList<>();
        originalList.add(new GroceryItem("pommes", 5, "fruits"));
        originalList.add(new GroceryItem("pain", 2, "boulangerie"));
        originalList.add(new GroceryItem("lait", 1, "produits laitiers"));

        // Écriture dans le fichier
        dao.writeGroceryList(originalList, tempFile);

        // Lecture du fichier
        List<GroceryItem> loadedList = dao.readGroceryList(tempFile);

        // Vérification que les listes sont de la même taille
        assertEquals(originalList.size(), loadedList.size(),
                "La liste chargée devrait avoir la même taille que la liste originale");

        // Vérification des éléments individuels
        for (int i = 0; i < originalList.size(); i++) {
            GroceryItem original = originalList.get(i);
            GroceryItem loaded = loadedList.get(i);

            assertEquals(original.getName(), loaded.getName(),
                    "Les noms des articles devraient correspondre");
            assertEquals(original.getQuantity(), loaded.getQuantity(),
                    "Les quantités des articles devraient correspondre");
            assertEquals(original.getCategory(), loaded.getCategory(),
                    "Les catégories des articles devraient correspondre");
        }
    }

    @Test
    public void testReadFromEmptyFile() throws IOException {
        // Création d'un fichier vide
        Files.writeString(tempFile, "");

        // Lecture du fichier vide
        List<GroceryItem> list = dao.readGroceryList(tempFile);

        // La liste devrait être vide mais pas null
        assertNotNull(list, "La liste ne devrait pas être null");
        assertTrue(list.isEmpty(), "La liste devrait être vide");
    }

    @Test
    public void testReadFromInvalidJson() throws IOException {
        // Création d'un fichier avec un JSON invalide
        Files.writeString(tempFile, "{invalide}");

        // La lecture devrait réinitialiser le fichier et retourner une liste vide
        List<GroceryItem> list = dao.readGroceryList(tempFile);

        assertNotNull(list, "La liste ne devrait pas être null");
        assertTrue(list.isEmpty(), "La liste devrait être vide");

        // Vérification que le fichier a été réinitialisé
        String content = Files.readString(tempFile);
        assertEquals("[]", content.trim(), "Le fichier devrait contenir un tableau JSON vide");
    }
}