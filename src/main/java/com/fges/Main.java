package com.fges;

import com.fges.commands.CommandFactory;
import com.fges.commands.CommandHandler;
import com.fges.dao.GroceryDAO;
import com.fges.factory.GroceryStorageFactory;
import com.fges.parser.CommandParser;
import com.fges.parser.ParsingResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        CommandParser commandParser = new CommandParser();
        ParsingResult parsingResult = commandParser.parse(args);
        if (parsingResult == null) {
            System.exit(1);
        }

        String commandName = parsingResult.getPositionalArgs().get(0);
        String fileName = parsingResult.getSourceFile();
        String format = parsingResult.getFormat();

        // Affichage des informations pour le débogage
        System.out.println("Commande: " + commandName);
        if (fileName != null) {
            System.out.println("Fichier source: " + fileName);
        }
        System.out.println("Format: " + format);

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

        CommandHandler handler = CommandFactory.getCommandHandler(commandName);
        int result = handler.execute(groceryList, parsingResult);

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
