// Enum représentant toutes les commandes disponibles
package com.fges.commands;

import java.time.LocalDate;
import java.util.List;
import com.fges.GroceryItem;

public enum Command {
    ADD,
    REMOVE,
    LIST,
    TOTAL,
    INFO,
    UNKNOWN;

    // Action spécifique selon la commande
    public int execute(List<GroceryItem> items, List<String> args, String category) {
        if (this == Command.INFO) {
            System.out.println("Today's date: " + LocalDate.now());
            System.out.println("Operating System: " + System.getProperty("os.name"));
            System.out.println("Java version: " + System.getProperty("java.version"));
            return 0;
        }
        return -1; // Pour les autres commandes ailleurs
    }
}



