package com.fges.parser;

import com.fges.commands.Command;
import org.apache.commons.cli.*;

import java.util.List;

// Classe pour parser les arguments de la ligne de commande

public class CommandParser {

    private final Options cliOptions;

 // Initialise les options de commandes
    public CommandParser() {
        cliOptions = new Options();

        // Option obligatoire pour le fichier source
        cliOptions.addOption("s", "source", true, "Fichier contenant la liste de courses");
        // Option pour définir le format (json ou csv)
        cliOptions.addOption("f", "format", true, "Format JSON ou CSV");
        // Option pour la catégorie
        cliOptions.addOption("c", "category", true, "Catégorie de l'article (par défaut, pour l'add: default)");
    }

// Parse les arguments fournis en ligne de commande
    public ParsingResult parse(String[] args) {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(cliOptions, args);
        } catch (ParseException ex) {
            System.err.println("Erreur lors de l'analyse des arguments : " + ex.getMessage());
            return null;
        }

        List<String> positionalArgs = cmd.getArgList();
        if (positionalArgs.isEmpty()) {
            System.err.println("Commande manquante");
            return null;
        }

        String cmdString = positionalArgs.get(0).toLowerCase();
        Command command;
        switch (cmdString) {
            case "add" -> command = Command.ADD;
            case "list" -> command = Command.LIST;
            case "remove" -> command = Command.REMOVE;
            case "total" -> command = Command.TOTAL;
            case "info"   -> command = Command.INFO;

            default -> command = Command.UNKNOWN;
        }

        ParsingResult result = new ParsingResult();
        result.setCommand(command);
        result.setPositionalArgs(positionalArgs);
        result.setSourceFile(cmd.getOptionValue("s"));
        result.setFormat(cmd.getOptionValue("f", "json"));

        // Si l'utilisateur spécifie explicitement -c, on récupère sa valeur,
        // sinon on laisse la valeur par défaut "default" pour la commande add.
        if (cmd.hasOption("c")) {
            result.setCategory(cmd.getOptionValue("c"));
            result.setCategorySpecified(true);
        } else {
            result.setCategory("default");
            result.setCategorySpecified(false);
        }

        return result;
    }

    public Options getCliOptions() {
        return cliOptions;
    }
}