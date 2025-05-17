package com.fges;

import com.fges.commands.Command;
import com.fges.parser.CommandParser;
import com.fges.parser.ParsingResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CommandParserTest {

    // Vérifie que la commande 'add' avec ses options est correctement interprétée
    @Test
    public void testParseAddCommand() {
        CommandParser parser = new CommandParser();
        String[] args = {"add", "pain", "2", "-c", "boulangerie", "-s", "courses.json"};

        ParsingResult result = parser.parse(args);

        assertEquals("add", result.getPositionalArgs().get(0), "La commande devrait être 'add'");
        assertEquals("pain", result.getPositionalArgs().get(1), "Le nom de l'article devrait être 'pain'");
        assertEquals("2", result.getPositionalArgs().get(2), "La quantité devrait être '2'");
        assertEquals("courses.json", result.getSourceFile(), "Le fichier source devrait être 'courses.json'");
        assertTrue(result.isCategorySpecified(), "La catégorie doit être spécifiée");
    }

    // Comportement spécifique d'\'info' qui ignore certaines options comme le fichier source
    @Test
    public void testParseInfoCommand() {
        CommandParser parser = new CommandParser();
        String[] args = {"info", "-s", "ignored.json"};

        ParsingResult result = parser.parse(args);

        assertEquals("info", result.getPositionalArgs().get(0), "La commande devrait être 'info'");
        assertNull(result.getSourceFile(), "Le fichier source devrait être ignoré pour 'info'");
        assertEquals("json", result.getFormat(), "Le format pour 'info' devrait être 'json'");
    }
}