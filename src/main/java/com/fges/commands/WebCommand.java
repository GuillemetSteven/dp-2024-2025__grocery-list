package com.fges.commands;

import com.fges.GroceryItem;
import com.fges.dao.GroceryDAO;
import com.fges.factory.GroceryStorageFactory;
import com.fges.parser.ParsingResult;
import com.fges.commands.InfoCommand;

import fr.anthonyquere.GroceryShopServer;
import fr.anthonyquere.MyGroceryShop;
import fr.anthonyquere.MyGroceryShop.WebGroceryItem;
import fr.anthonyquere.MyGroceryShop.Runtime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class WebCommand implements CommandHandler {
    @Override
    public int execute(List<GroceryItem> groceryList, ParsingResult parsingResult) {
        List<String> args = parsingResult.getPositionalArgs();
        if (args.size() < 2) {
            System.err.println("Usage: web <port> -s <sourceFile> [-f json|csv]");
            return 1;
        }

        int port;
        try {
            port = Integer.parseInt(args.get(1));
        } catch (NumberFormatException e) {
            System.err.println("Le port doit être un entier.");
            return 1;
        }

        String fileName = parsingResult.getSourceFile();
        // Si le fichier source n'est pas spécifié, utiliser un nom par défaut
        if (fileName == null || fileName.isEmpty()) {
            fileName = "groceries.json"; // Nom par défaut
        }

        // Obtenir le format spécifié
        String format = parsingResult.getFormat();
        System.out.println("Format utilisé: " + format);

        // Changer l'extension du fichier si nécessaire pour correspondre au format
        if (fileName.contains(".")) {
            String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
            fileName = baseName + "." + format;
        } else {
            fileName = fileName + "." + format;
        }

        System.out.println("Utilisation du fichier: " + fileName);

        GroceryDAO dao = GroceryStorageFactory.getGroceryDAO(format);
        Path filePath = Paths.get(fileName);

        try {
            if (!Files.exists(filePath)) {
                // Création du fichier selon le format spécifié
                if ("csv".equalsIgnoreCase(format)) {
                    dao.writeGroceryList(List.of(), filePath);
                } else {
                    Files.writeString(filePath, "[]");
                }
            }

            // Vider la liste actuelle et charger les données depuis le fichier
            groceryList.clear();
            groceryList.addAll(dao.readGroceryList(filePath));
        } catch (IOException e) {
            System.err.println("Erreur fichier : " + e.getMessage());
            return 1;
        }

        InfoCommand infoCmd = new InfoCommand();

        MyGroceryShop shop = new MyGroceryShop() {
            @Override
            public List<WebGroceryItem> getGroceries() {
                return groceryList.stream()
                        .map(i -> new WebGroceryItem(i.getName(), i.getQuantity(), i.getCategory()))
                        .collect(Collectors.toList());
            }

            @Override
            public void addGroceryItem(String name, int qty, String category) {
                groceryList.add(new GroceryItem(name, qty, category));
                try {
                    dao.writeGroceryList(groceryList, filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void removeGroceryItem(String name) {
                groceryList.removeIf(i -> i.getName().equalsIgnoreCase(name));
                try {
                    dao.writeGroceryList(groceryList, filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public Runtime getRuntime() {
                return infoCmd.toWebRuntime();
            }
        };

        // Démarrage du serveur
        GroceryShopServer server = new GroceryShopServer(shop);
        server.start(port);
        System.out.println("Serveur web démarré sur http://localhost:" + port);

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return 0;
    }
}