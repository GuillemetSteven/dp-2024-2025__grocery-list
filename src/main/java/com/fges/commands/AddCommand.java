package com.fges.commands;

import com.fges.GroceryItem;
import com.fges.parser.ParsingResult;
import java.util.List;

public class AddCommand implements CommandHandler {

    @Override
    public int execute(List<GroceryItem> groceryList, ParsingResult parsingResult) {
        List<String> args = parsingResult.getPositionalArgs();
        if (args.size() < 3) {
            System.err.println("Arguments manquants pour la commande 'add'.");
            return 1;
        }
        String itemName = args.get(1);
        int quantity;
        try {
            quantity = Integer.parseInt(args.get(2));
        } catch (NumberFormatException e) {
            System.err.println("La quantité doit être un entier.");
            return 1;
        }
        groceryList.add(new GroceryItem(itemName, quantity, parsingResult.getCategory()));
        System.out.println("Ajouté : " + itemName + " (quantité: " + quantity + ", catégorie: " + parsingResult.getCategory() + ")");
        return 0;
    }
}
