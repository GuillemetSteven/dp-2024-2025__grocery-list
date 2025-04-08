package com.fges.operations;

import com.fges.GroceryItem;
import com.fges.commands.Command;

import java.util.*;
import java.util.stream.Collectors;

public class GroceryOperation {

    /**
     * Calcule le total des quantités pour un article donné.
     */
    private static int calculateTotalForItem(List<GroceryItem> groceryList, String itemName) {
        return groceryList.stream()
                .filter(item -> item.getName().equalsIgnoreCase(itemName))
                .mapToInt(GroceryItem::getQuantity)
                .sum();
    }

    public static int executeCommand(
            Command command,
            List<GroceryItem> groceryList,
            List<String> positionalArgs,
            String category  // récupéré via le parsing
    ) {
        switch (command) {
            case ADD -> {
                if (positionalArgs.size() < 3) {
                    System.err.println("Arguments manquants pour la commande 'add'.");
                    return 1;
                }
                String itemName = positionalArgs.get(1);
                int quantity;
                try {
                    quantity = Integer.parseInt(positionalArgs.get(2));
                } catch (NumberFormatException e) {
                    System.err.println("La quantité doit être un entier.");
                    return 1;
                }
                // Pour la commande add, la catégorie est celle récupérée (ou "default" si non spécifiée)
                groceryList.add(new GroceryItem(itemName, quantity, category));
                System.out.println("Ajouté : " + itemName + " (quantité: " + quantity + ", catégorie: " + category + ")");
                return 0;
            }
            case LIST -> {
                // Si l'utilisateur a précisé l'option -c,
                // filtrer et afficher uniquement les articles de cette catégorie.
                // Sinon, afficher tous les articles groupés par catégorie.
                if (category != null && !category.equals("default") && category.trim().length() > 0) {
                    System.out.println("###### " + category + " ######");
                    List<GroceryItem> filtered = groceryList.stream()
                            .filter(item -> item.getCategory().equalsIgnoreCase(category))
                            .collect(Collectors.toList());
                    if (filtered.isEmpty()) {
                        System.out.println("Aucun article dans cette catégorie.");
                    } else {
                        filtered.forEach(item -> System.out.println(item.getName() + ": " + item.getQuantity()));
                    }
                } else {
                    // Affichage groupé par catégorie
                    Map<String, List<GroceryItem>> groupedItems = new HashMap<>();
                    for (GroceryItem item : groceryList) {
                        groupedItems.computeIfAbsent(item.getCategory(), k -> new ArrayList<>()).add(item);
                    }
                    groupedItems.forEach((cat, items) -> {
                        System.out.println("###### " + cat + " ######");
                        for (GroceryItem item : items) {
                            System.out.println(item.getName() + ": " + item.getQuantity());
                        }
                    });
                }
                return 0;
            }
            case REMOVE -> {
                if (positionalArgs.size() < 2) {
                    System.err.println("Arguments manquants pour la commande 'remove'.");
                    return 1;
                }
                String itemName = positionalArgs.get(1);
                List<GroceryItem> newList = groceryList.stream()
                        .filter(item -> !item.getName().equalsIgnoreCase(itemName))
                        .collect(Collectors.toList());
                groceryList.clear();
                groceryList.addAll(newList);
                System.out.println("Supprimé : " + itemName);
                return 0;
            }
            case TOTAL -> {
                if (positionalArgs.size() < 2) {
                    System.err.println("Arguments manquants pour la commande 'total'.");
                    return 1;
                }
                String itemName = positionalArgs.get(1);
                int total = calculateTotalForItem(groceryList, itemName);
                System.out.println("Quantité totale pour '" + itemName + "' : " + total);
                return 0;
            }
            default -> {
                System.err.println("Commande inconnue.");
                return 1;
            }
        }
    }
}
