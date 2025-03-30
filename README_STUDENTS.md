# Fonctionnalités

- Ajouter un article à votre liste de courses
  ```bash
  java -jar ./target/dp-2024-2025__grocery-list-1.0-SNAPSHOT.jar -s shopping_list.json add "Milk" 2
  ```

- Lister tous les articles de votre liste
  ```bash
  java -jar ./target/dp-2024-2025__grocery-list-1.0-SNAPSHOT.jar -s shopping_list.json list
  ```

- Supprimer un article de votre liste
  ```bash
  java -jar ./target/dp-2024-2025__grocery-list-1.0-SNAPSHOT.jar -s shopping_list.json remove "Milk"
  ```

- Connaître la quantité totale d'un article spécifique
  ```bash
  java -jar ./target/dp-2024-2025__grocery-list-1.0-SNAPSHOT.jar -s shopping_list.json total "Milk"
  ```

## Support de formats multiples

L'application prend en charge deux formats de fichiers :

- JSON (par défaut)
  ```bash
  java -jar ./target/dp-2024-2025__grocery-list-1.0-SNAPSHOT.jar -s shopping_list.json add "Milk" 2
  ```

- CSV (en utilisant l'option --format)
  ```bash
  java -jar ./target/dp-2024-2025__grocery-list-1.0-SNAPSHOT.jar -s shopping_list.csv --format csv add "Milk" 2
  ```

Le format par défaut est JSON si l'option `--format` n'est pas spécifiée.