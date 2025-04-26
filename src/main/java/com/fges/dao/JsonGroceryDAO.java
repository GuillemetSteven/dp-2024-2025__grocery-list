package com.fges.dao;

import com.fges.GroceryItem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Classe pour gérer les listes de courses en format JSON
public class JsonGroceryDAO implements GroceryDAO {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    // Lit une liste de courses depuis un fichier JSON
    public List<GroceryItem> readGroceryList(Path filePath) throws IOException {
        List<GroceryItem> groceryList = new ArrayList<>();
        if (!Files.exists(filePath) || Files.size(filePath) == 0) {
            return groceryList;
        }
        String fileContent = Files.readString(filePath).trim();
        // Vérification d’un JSON de base
        if (!fileContent.startsWith("[") || !fileContent.endsWith("]")) {
            Files.writeString(filePath, "[]");
            return groceryList;
        }
        groceryList = OBJECT_MAPPER.readValue(fileContent, new TypeReference<List<GroceryItem>>() {});
        return groceryList;
    }

    @Override
    // Écrit une liste de courses dans un fichier JSON
    public void writeGroceryList(List<GroceryItem> groceryList, Path filePath) throws IOException {
        String jsonString = OBJECT_MAPPER.writeValueAsString(groceryList);
        Files.writeString(filePath, jsonString);
    }
}
