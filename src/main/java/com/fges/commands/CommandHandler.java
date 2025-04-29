package com.fges.commands;

import com.fges.GroceryItem;
import com.fges.parser.ParsingResult;
import java.util.List;

public interface CommandHandler {
    int execute(List<GroceryItem> groceryList, ParsingResult parsingResult);
}
