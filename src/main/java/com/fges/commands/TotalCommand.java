package com.fges.commands;

import com.fges.GroceryItem;
import com.fges.parser.ParsingResult;

import java.util.List;

// Commande 'total' qui calcule la quantité totale d'un article
public class TotalCommand implements CommandHandler {

    private int calculateTotalForItem(List<GroceryItem> groceryList, String itemName) {
        return groceryList.stream()
                .filter(item -> item.getName().equalsIgnoreCase(itemName))
                .mapToInt(GroceryItem::getQuantity)
                .sum();
    }

    @Override
    public int execute(List<GroceryItem> groceryList, ParsingResult parsingResult) {
        List<String> args = parsingResult.getPositionalArgs();
        if (args.size() < 2) {
            System.err.println("Arguments manquants pour la commande 'total'.");
            return 1;
        }
        String itemName = args.get(1);
        int total = calculateTotalForItem(groceryList, itemName);
        System.out.println("Quantité totale pour '" + itemName + "' : " + total);
        return 0;
    }
}
