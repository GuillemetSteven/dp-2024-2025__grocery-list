package com.fges.commands;

import com.fges.GroceryItem;
import com.fges.parser.ParsingResult;

import java.util.List;

// Comportement ar défaut pour les commandes non reconnues
public class UnknownCommand implements CommandHandler {

    @Override
    public int execute(List<GroceryItem> groceryList, ParsingResult parsingResult) {
        System.err.println("Commande inconnue.");
        return 1;
    }
}
