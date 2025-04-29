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

public class Main {

    // Méthode principale qui gère l'exécution du programme
    public static void main(String[] args) {
        CommandParser commandParser = new CommandParser();
        ParsingResult parsingResult = commandParser.parse(args);

        if (parsingResult == null) {
            System.exit(1);
        }

        Command command = parsingResult.getCommand();

        // Si la commande est INFO, on exécute et on sort directement
        if (command == Command.INFO) {
            command.execute(null, null, null);
            System.exit(0);
        }

        String fileName = parsingResult.getSourceFile();
        String format = parsingResult.getFormat();
        String category = parsingResult.getCategory();

        GroceryDAO dao = GroceryStorageFactory.getGroceryDAO(format);
        Path filePath = Path.of(fileName);

        try {
            if (!Files.exists(filePath)) {
                if ("csv".equalsIgnoreCase(format)) {
                    dao.writeGroceryList(new ArrayList<>(), filePath);
                } else {
                    Files.writeString(filePath, "[]");
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'initialisation du fichier : " + e.getMessage());
            System.exit(1);
        }

        List<GroceryItem> groceryList;
        try {
            groceryList = dao.readGroceryList(filePath);
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
            System.exit(1);
            return;
        }

        int result = GroceryOperation.executeCommand(
                command,
                groceryList,
                parsingResult.getPositionalArgs(),
                category
        );

        if (command != Command.UNKNOWN && result == 0) {
            try {
                dao.writeGroceryList(groceryList, filePath);
            } catch (IOException e) {
                System.err.println("Erreur lors de l'écriture dans le fichier : " + e.getMessage());
                System.exit(1);
            }
        }

        System.exit(result);
    }
}

