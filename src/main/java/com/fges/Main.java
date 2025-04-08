package com.fges;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import com.fasterxml.jackson.databind.SerializationFeature;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.io.FileWriter;
import java.io.Reader;
import java.util.Objects;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;


public class Main {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);


    public static void main(String[] args) throws IOException {
        System.exit(exec(args));
    }

    private static int calculateTotalForItem(List<GroceryItem> groceryList, String itemName) {
        return groceryList.stream()
                .filter(item -> item.getName().equalsIgnoreCase(itemName))
                .mapToInt(GroceryItem::getQuantity)
                .sum();
    }

    private static List<GroceryItem> readGroceryListFromCsv(Path filePath) throws IOException {
        List<GroceryItem> groceryList = new ArrayList<>();

        // Check if file exists and is not empty
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
            throw new IOException("Invalid format in CSV file: " + e.getMessage());
        }
        return groceryList;
    }


    private static void writeGroceryListToCsv(List<GroceryItem> groceryList, File outputFile) throws IOException {
        // Create new file or overwrite existing
        try (FileWriter writer = new FileWriter(outputFile);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .builder()
                     .setHeader("name", "quantity")
                     .build())) {

            for (GroceryItem item : groceryList) {
                csvPrinter.printRecord(item.getName(), item.getQuantity());
            }
            csvPrinter.flush();
        }
    }


    public static <groceryItem> int exec(String[] args) throws IOException {
        // Setup CLI interface
        Options cliOptions = new Options();
        CommandLineParser parser = new DefaultParser();

        cliOptions.addRequiredOption("s", "source", true, "File containing the grocery list");

        cliOptions.addOption("f", "format", true, "JSON or CSV format");

        cliOptions.addOption("c", "category", true, "category default = default");
        CommandLine cmd;
        try {
            cmd = parser.parse(cliOptions, args);
        } catch (ParseException ex) {
            System.err.println("Fail to parse arguments: " + ex.getMessage());
            return 1;
        }

        String fileName = cmd.getOptionValue("s");
        String cate = cmd.getOptionValue("c");
        System.out.println(cate);

        // Le format par défaut
        String format = "json";
        if (cmd.hasOption("format")) {
            format = cmd.getOptionValue("format").toLowerCase();
            if (!format.equals("json") && !format.equals("csv")) {
                System.err.println("Invalid format. Available format : JSON | CSV");
                return 1;
            }
        }

        List<String> positionalArgs = cmd.getArgList();
        if (positionalArgs.isEmpty()) {
            System.err.println("Missing Command");
            return 1;
        }

        String command = positionalArgs.get(0);
        //System.out.println(command);

        // Load current grocery list state

        Path filePath = Paths.get(fileName);



        List<GroceryItem> groceryList = new ArrayList<>();
        if (Files.exists(filePath)) {
            if (format.equals("csv")) {
                try {
                    // Vérification s'il existe, je l'initialise
                    if (Files.size(filePath) == 0) {
                        writeGroceryListToCsv(new ArrayList<>(), new File(fileName));
                    } else {
                        groceryList = readGroceryListFromCsv(filePath);
                    }
                } catch (Exception e) {
                    System.err.println("Failed to read as CSV: " + e.getMessage());
                }
            } else {
                // Format JSON
                try {
                    if (Files.size(filePath) == 0) {
                        // Initialisation JSON
                        Files.writeString(filePath, "[]");
                        groceryList = new ArrayList<>();
                    } else {
                        String fileContent = Files.readString(filePath).trim();
                        if (!fileContent.startsWith("[") || !fileContent.endsWith("]")) {
                            // Si invalide, initialise
                            Files.writeString(filePath, "[]");
                            groceryList = new ArrayList<>();
                        } else {
                            groceryList = OBJECT_MAPPER.readValue(fileContent,
                                    new TypeReference<List<GroceryItem>>() {});
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Failed to read as JSON: " + e.getMessage());
                }
            }
        } else {
            // Créer au bon format
            if (format.equals("csv")) {
                writeGroceryListToCsv(new ArrayList<>(), new File(fileName));
            } else {
                Files.writeString(filePath, "[]");
            }
        }

        // interpret command
        switch (command) {
            case "add" -> {
                if (positionalArgs.size() < 3) {
                    System.err.println("Missing arguments");
                    return 1;
                }   
                String itemName = positionalArgs.get(1);
                int quantity = Integer.parseInt(positionalArgs.get(2));

                String category = cmd.getOptionValue("c","default");

                groceryList.add(new GroceryItem(itemName, quantity, category));

                var outputFile = new File(fileName);
                if (format.equals("json")) {
                    OBJECT_MAPPER.writeValue(outputFile, groceryList);
                } else {
                    writeGroceryListToCsv(groceryList, outputFile);
                }
                return 0;
            }
            case "list" -> {
                HashMap<String, List<GroceryItem>> allItems = new HashMap<>();
                for (GroceryItem item : groceryList) {
                    allItems.computeIfAbsent(item.getCategory(), k -> new ArrayList<>()).add(item);
                }
                for (String category : new ArrayList<>(allItems.keySet())) {
                    System.out.println("###### " + category + " ######");
                    List<GroceryItem> items = allItems.get(category);
                    for (GroceryItem data : items) {
                        System.out.println(data.getName() + ":" + data.getQuantity());
                    }

                    allItems.remove(category);
                }

                return 0;
            }
            case "remove" -> {
                if (positionalArgs.size() < 2) {
                    System.err.println("Missing arguments");
                    return 1;
                }

                String itemName = positionalArgs.get(1);
                var newGroceryList = groceryList.stream()
                        .filter(item -> !item.getName().equals(itemName))
                        .toList();

                var outputFile = new File(fileName);
                if (format.equals("json")) {
                    OBJECT_MAPPER.writeValue(outputFile, newGroceryList);
                } else {
                    writeGroceryListToCsv(newGroceryList, outputFile);
                }
                return 0;
            }

            case "total" -> {
                if (positionalArgs.size() < 2) {
                    System.err.println("Missing arguments");
                    return 1;
                }

                String itemName = positionalArgs.get(1);
                int total = calculateTotalForItem(groceryList, itemName);

                System.out.println("Total quantity for " + itemName + ": " + total);
                return 0;
            }

            default -> throw new IllegalArgumentException("Unknown command: " + command);
        }
    }
}
