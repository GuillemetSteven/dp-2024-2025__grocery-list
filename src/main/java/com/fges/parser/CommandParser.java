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
        cliOptions.addOption("s", "source", true, "Fichier contenant la liste de courses");
        cliOptions.addOption("f", "format", true, "Format JSON ou CSV");
        cliOptions.addOption("c", "category", true, "Catégorie de l'article (par défaut, pour l'add: default)");
    }

    // Parse les arguments fournis en ligne de commande
    public ParsingResult parse(String[] args) {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(cliOptions, args, true);
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
            case "info" -> command = Command.INFO;
            default -> command = Command.UNKNOWN;
        }

        ParsingResult result = new ParsingResult();
        result.setCommand(command);
        result.setPositionalArgs(positionalArgs);

        if (command == Command.INFO) {
            // Si la commande est INFO, on ignore toutes les options
            result.setSourceFile(null);
            result.setFormat(null);
            result.setCategory(null);
            return result;
        }

        // Les autres commandes, -s est obligatoire
        if (!cmd.hasOption("s")) {
            System.err.println("Erreur : l'option -s est obligatoire pour cette commande");
            return null;
        }

        result.setSourceFile(cmd.getOptionValue("s"));
        result.setFormat(cmd.getOptionValue("f", "json"));

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

