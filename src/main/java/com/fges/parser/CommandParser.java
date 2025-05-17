package com.fges.parser;

import com.fges.commands.Command;
import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Analyse les arguments de la ligne de commande et les convertit en objet ParsingResult
public class CommandParser {

    private final Options cliOptions;

    public CommandParser() {
        cliOptions = new Options();

        // Option obligatoire pour le fichier source
        cliOptions.addOption("s", "source", true, "Fichier contenant la liste de courses");
        // Option pour définir le format (json ou csv)
        cliOptions.addOption("f", "format", true, "Format JSON ou CSV");
        // Option pour la catégorie
        cliOptions.addOption("c", "category", true, "Catégorie de l'article (par défaut, pour l'add: default)");
    }

    public ParsingResult parse(String[] args) {
        // Traitement manuel pour séparer les commandes positionnelles des options
        List<String> positionalArgs = new ArrayList<>();
        List<String> optionArgs = new ArrayList<>();

        // Parcourir tous les arguments
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            // Si c'est une option
            if (arg.startsWith("-")) {
                optionArgs.add(arg);
                // Si l'option a une valeur, ajouter également cette valeur
                if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                    optionArgs.add(args[i + 1]);
                    i++; // Sauter l'argument suivant puisqu'il a déjà été traité
                }
            } else {
                // Sinon, c'est un argument positionnel
                positionalArgs.add(arg);
            }
        }

        // Recréer un tableau d'arguments pour Apache Commons CLI
        String[] optionArgsArray = optionArgs.toArray(new String[0]);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(cliOptions, optionArgsArray);
        } catch (ParseException ex) {
            System.err.println("Erreur lors de l'analyse des arguments : " + ex.getMessage());
            return null;
        }

        if (positionalArgs.isEmpty()) {
            System.err.println("Commande manquante");
            return null;
        }

        String cmdString = positionalArgs.get(0).toLowerCase();
        Command command;
        switch (cmdString) {
            case "add"    -> command = Command.ADD;
            case "list"   -> command = Command.LIST;
            case "remove" -> command = Command.REMOVE;
            case "total"  -> command = Command.TOTAL;
            case "info"   -> command = Command.INFO;
            case "web"    -> command = Command.WEB;
            default       -> command = Command.UNKNOWN;
        }

        // **Cas SPECIAL info : on ignore TOUTES les options**
        if (command == Command.INFO) {
            ParsingResult infoResult = new ParsingResult();
            infoResult.setCommand(Command.INFO);
            infoResult.setPositionalArgs(List.of("info"));
            infoResult.setSourceFile(null);
            infoResult.setFormat("json");
            infoResult.setCategory("default");
            infoResult.setCategorySpecified(false);
            return infoResult;
        }

        // Sinon, parsing "normal" pour add/list/remove/web...
        ParsingResult result = new ParsingResult();
        result.setCommand(command);
        result.setPositionalArgs(positionalArgs);

        // Traitement des options
        result.setSourceFile(cmd.getOptionValue("s"));

        // Traitement explicite du format
        String specifiedFormat = cmd.getOptionValue("f");
        if (specifiedFormat != null) {
            result.setFormat(specifiedFormat.toLowerCase());
        } else {
            result.setFormat("json"); // Valeur par défaut
        }

        if (cmd.hasOption("c")) {
            result.setCategory(cmd.getOptionValue("c"));
            result.setCategorySpecified(true);
        } else {
            result.setCategory("default");
            result.setCategorySpecified(false);
        }
        return result;
    }
}