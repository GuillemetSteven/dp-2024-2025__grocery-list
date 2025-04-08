package com.fges.factory;

import com.fges.dao.CsvGroceryDAO;
import com.fges.dao.JsonGroceryDAO;
import com.fges.dao.GroceryDAO;

public class GroceryStorageFactory {

    /**
     * Renvoie l'instance de GroceryDAO selon le format.
     *
     * @param format "csv" ou "json"
     * @return l’implémentation de GroceryDAO correspondante
     */
    public static GroceryDAO getGroceryDAO(String format) {
        if ("csv".equalsIgnoreCase(format)) {
            return new CsvGroceryDAO();
        } else {
            return new JsonGroceryDAO();
        }
    }
}
