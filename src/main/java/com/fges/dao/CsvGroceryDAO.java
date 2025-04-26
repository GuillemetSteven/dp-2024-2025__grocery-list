package com.fges.dao;

import com.fges.GroceryItem;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Classe pour gérer les listes de courses en format CSV
public class CsvGroceryDAO implements GroceryDAO {

    @Override
    // Lit une liste de courses depuis un fichier CSV
    public List<GroceryItem> readGroceryList(Path filePath) throws IOException {
        List<GroceryItem> groceryList = new ArrayList<>();
        if (!Files.exists(filePath) || Files.size(filePath) == 0) {
            return groceryList;
        }

        try (Reader reader = Files.newBufferedReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .builder()
                     .setHeader("name", "quantity", "category")
                     .setSkipHeaderRecord(true)
                     .build())) {

            for (CSVRecord record : csvParser) {
                String name = record.get("name");
                int quantity = Integer.parseInt(record.get("quantity"));
                String category = record.get("category");
                groceryList.add(new GroceryItem(name, quantity, category));
            }
        } catch (NumberFormatException e) {
            throw new IOException("Format invalide dans le CSV : " + e.getMessage());
        }
        return groceryList;
    }

    @Override
    // Écrit une liste de courses dans un fichier CSV
    public void writeGroceryList(List<GroceryItem> groceryList, Path filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath.toFile());
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .builder()
                     .setHeader("name", "quantity", "category")
                     .build())) {

            for (GroceryItem item : groceryList) {
                csvPrinter.printRecord(item.getName(), item.getQuantity(), item.getCategory());
            }
            csvPrinter.flush();
        }
    }
}

