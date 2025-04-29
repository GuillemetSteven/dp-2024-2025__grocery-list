package com.fges.commands;

import com.fges.GroceryItem;
import com.fges.parser.ParsingResult;

import java.util.List;
import java.util.stream.Collectors;

public class RemoveCommand implements CommandHandler {

    @Override
    public int execute(List<GroceryItem> groceryList, ParsingResult parsingResult) {
        List<String> args = parsingResult.getPositionalArgs();
        if (args.size() < 2) {
            System.err.println("Arguments manquants pour la commande 'remove'.");
            return 1;
        }
        String itemName = args.get(1);
        List<GroceryItem> updatedList = groceryList.stream()
                .filter(item -> !item.getName().equalsIgnoreCase(itemName))
                .collect(Collectors.toList());
        groceryList.clear();
        groceryList.addAll(updatedList);
        System.out.println("Supprimé : " + itemName);
        return 0;
    }
}
