package com.fges;

import com.fges.commands.Command;
import com.fges.dao.GroceryDAO;
import com.fges.factory.GroceryStorageFactory;
import com.fges.operations.GroceryOperation;
import com.fges.parser.CommandParser;
import com.fges.parser.ParsingResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Méthode principale qui gère l'exécution du programme
public class Main {
    public static void main(String[] args) {
        CommandParser commandParser = new CommandParser();
        ParsingResult parsingResult = commandParser.parse(args);
        if (parsingResult == null) {
            System.exit(1);
        }

        Command command = parsingResult.getCommand();
        String fileName = parsingResult.getSourceFile();
        String format = parsingResult.getFormat();
        String category = parsingResult.getCategory();

        List<GroceryItem> groceryList = new ArrayList<>();
        Path filePath = null;
        GroceryDAO dao = null;

        if (fileName != null) {
            dao = GroceryStorageFactory.getGroceryDAO(format);
            filePath = Path.of(fileName);

            try {
                if (!Files.exists(filePath)) {
                    if ("csv".equalsIgnoreCase(format)) {
                        dao.writeGroceryList(new ArrayList<>(), filePath);
                    } else {
                        Files.writeString(filePath, "[]");
                    }
                }

                groceryList = dao.readGroceryList(filePath);
            } catch (IOException e) {
                System.err.println("Erreur fichier : " + e.getMessage());
                System.exit(1);
            }
        }

        int result = GroceryOperation.executeCommand(
            command,
            groceryList,
            parsingResult.getPositionalArgs(),
            category
        );

        if (result == 0 && filePath != null) {
            try {
                dao.writeGroceryList(groceryList, filePath);
            } catch (IOException e) {
                System.err.println("Erreur lors de l'écriture : " + e.getMessage());
                System.exit(1);
            }
        }

        System.exit(result);
    }
}
