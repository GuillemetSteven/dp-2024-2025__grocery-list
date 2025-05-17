package com.fges.commands;

import com.fges.GroceryItem;
import com.fges.parser.ParsingResult;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

// importe la Runtime imbriquée
import fr.anthonyquere.MyGroceryShop.Runtime;

// Commande 'info' qui affiche les informations système
public class InfoCommand implements CommandHandler {

    @Override
    public int execute(List<GroceryItem> groceryList, ParsingResult parsingResult) {
        String today  = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String osName = System.getProperty("os.name");
        String javaVer= System.getProperty("java.version");

        System.out.println("Today's date:     " + today);
        System.out.println("Operating System: " + osName);
        System.out.println("Java version:     " + javaVer);
        return 0;
    }

    // Pour le web, on crée la Runtime du même coup
    public Runtime toWebRuntime() {
        return new Runtime(
            LocalDate.now(),
            System.getProperty("java.version"),
            System.getProperty("os.name")
        );
    }
}
