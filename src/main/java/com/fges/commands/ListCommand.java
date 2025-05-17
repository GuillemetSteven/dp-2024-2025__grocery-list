package com.fges.commands;

import com.fges.GroceryItem;
import com.fges.parser.ParsingResult;

import java.util.*;
import java.util.stream.Collectors;

// Commande 'list' qui affiche les articles par catégorie
public class ListCommand implements CommandHandler {

    @Override
    public int execute(List<GroceryItem> groceryList, ParsingResult parsingResult) {
        String category = parsingResult.getCategory();
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
}
