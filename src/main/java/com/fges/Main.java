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
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.Reader;


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

    public static <groceryItem> int exec(String[] args) throws IOException {
        // Setup CLI interface
        Options cliOptions = new Options();
        CommandLineParser parser = new DefaultParser();

        cliOptions.addRequiredOption("s", "source", true, "File containing the grocery list");

        CommandLine cmd;
        try {
            cmd = parser.parse(cliOptions, args);
        } catch (ParseException ex) {
            System.err.println("Fail to parse arguments: " + ex.getMessage());
            return 1;
        }

        String fileName = cmd.getOptionValue("s");

        List<String> positionalArgs = cmd.getArgList();
        if (positionalArgs.isEmpty()) {
            System.err.println("Missing Command");
            return 1;
        }

        String command = positionalArgs.get(0);

        // Load current grocery list state

        Path filePath = Paths.get(fileName);

        String fileContent = "";

        List<GroceryItem> groceryList;

        if (Files.exists(filePath)) {
            fileContent = Files.readString(filePath);

            // Reading JSON
            var parsedList = OBJECT_MAPPER.readValue(fileContent, new TypeReference<List<GroceryItem>>() {
            });
            // Cast the list as an ArrayList to ensure its mutability
            groceryList = new ArrayList<>(parsedList);
        } else {
            groceryList = new ArrayList<>();
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

                groceryList.add(new GroceryItem(itemName, quantity));

                var outputFile = new File(fileName);

                OBJECT_MAPPER.writeValue(outputFile, groceryList);
                return 0;
            }
            case "list" -> {
                for (GroceryItem item : groceryList) {
                    System.out.println(item.getName()+ ": " + item.getQuantity());
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

                OBJECT_MAPPER.writeValue(outputFile, groceryList);
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
