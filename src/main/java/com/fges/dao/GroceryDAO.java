package com.fges.dao;

import com.fges.GroceryItem;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

// Interface pour accéder et manipuler les données de la liste de courses
public interface GroceryDAO {
    List<GroceryItem> readGroceryList(Path filePath) throws IOException;
    void writeGroceryList(List<GroceryItem> groceryList, Path filePath) throws IOException;
}
