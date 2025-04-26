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

    public static void main(String[] args) {
        // 1. Parsing des arguments
        CommandParser commandParser = new CommandParser();
        ParsingResult parsingResult = commandParser.parse(args);
        if (parsingResult == null) {
            System.exit(1);
        }

        Command command = parsingResult.getCommand();
        String fileName = parsingResult.getSourceFile();
        String format = parsingResult.getFormat();
        String category = parsingResult.getCategory();

        // 2. Sélection de l'implémentation du DAO (pattern Strategy via Factory)
        GroceryDAO dao = GroceryStorageFactory.getGroceryDAO(format);
        Path filePath = Path.of(fileName);

        // Initialisation du fichier s'il n'existe pas
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

        // 3. Lecture de la liste
        List<GroceryItem> groceryList;
        try {
            groceryList = dao.readGroceryList(filePath);
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
            System.exit(1);
            return;
        }

        // 4. Exécution de la commande (logique métier)
        int result = GroceryOperation.executeCommand(
                command,
                groceryList,
                parsingResult.getPositionalArgs(),
                category
        );

        // 5. Sauvegarde si la commande a modifié la liste (ici, on écrit même pour list/total par simplicité)
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
