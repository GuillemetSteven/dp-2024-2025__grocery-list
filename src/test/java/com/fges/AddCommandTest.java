package com.fges;
import com.fges.commands.AddCommand;
import com.fges.parser.ParsingResult;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AddCommandTest {

    @Test
    public void testExecuteAddCommand() {
        // Préparation
        List<GroceryItem> groceryList = new ArrayList<>();
        ParsingResult parsingResult = new ParsingResult();
        parsingResult.setPositionalArgs(List.of("add", "tomates", "3"));
        parsingResult.setCategory("légumes");

        // Exécution de la commande
        AddCommand addCommand = new AddCommand();
        int result = addCommand.execute(groceryList, parsingResult);

        // Vérifications
        assertEquals(0, result, "La commande devrait s'exécuter avec succès");
        assertEquals(1, groceryList.size(), "Un élément devrait être ajouté à la liste");

        GroceryItem addedItem = groceryList.get(0);
        assertEquals("tomates", addedItem.getName(), "Le nom de l'article devrait être 'tomates'");
        assertEquals(3, addedItem.getQuantity(), "La quantité devrait être 3");
        assertEquals("légumes", addedItem.getCategory(), "La catégorie devrait être 'légumes'");
    }

    @Test
    public void testExecuteAddCommandWithInvalidQuantity() {
        // Préparation des données avec une quantité non numérique
        List<GroceryItem> groceryList = new ArrayList<>();
        ParsingResult parsingResult = new ParsingResult();
        parsingResult.setPositionalArgs(List.of("add", "tomates", "trois")); // Quantité non numérique
        parsingResult.setCategory("légumes");

        // Exécution de la commande
        AddCommand addCommand = new AddCommand();
        int result = addCommand.execute(groceryList, parsingResult);

        // Vérifications
        assertEquals(1, result, "La commande devrait échouer avec le code 1");
        assertTrue(groceryList.isEmpty(), "La liste devrait rester vide");
    }
}