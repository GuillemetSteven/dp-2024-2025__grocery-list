package com.fges.commands;

import java.util.HashMap;
import java.util.Map;

// Crée et renvoie le gestionnaire de commande selon le nom de commande
public class CommandFactory {

    private static final Map<String, CommandHandler> commandHandlers = new HashMap<>();

    static {
        commandHandlers.put("add", new AddCommand());
        commandHandlers.put("list", new ListCommand());
        commandHandlers.put("remove", new RemoveCommand());
        commandHandlers.put("total", new TotalCommand());
        commandHandlers.put("info", new InfoCommand());
        commandHandlers.put("web", new WebCommand());

    }

    public static CommandHandler getCommandHandler(String commandName) {
        return commandHandlers.getOrDefault(commandName.toLowerCase(), new UnknownCommand());
    }
}
