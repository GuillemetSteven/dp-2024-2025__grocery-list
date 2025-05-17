package com.fges.commands;

import com.fges.GroceryItem;
import com.fges.parser.ParsingResult;
import java.util.List;

// Interface définissant le comportement commun à tous les gestionnaires de commandes
public interface CommandHandler {
    int execute(List<GroceryItem> groceryList, ParsingResult parsingResult);
}
