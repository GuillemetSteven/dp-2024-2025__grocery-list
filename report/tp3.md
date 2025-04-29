# TP3 - Implémentation de la commande info

## Exigences implémentées

Ajout de la commande `info` affichant :
- La date actuelle
- Le nom du système d’exploitation
- La version de Java utilisée

Support du comportement attendu :
- Aucune exigence d'options pour `info` (ni `-s`, `-f`, `-c`)
- Ignore les arguments fournis en trop

## Architecture mise en place

Modification du parser d'arguments pour traiter la commande `info` de manière prioritaire.
Définition de la logique `info` directement dans l'énumération `Command`
Petite factorisation de la méthode `main()` pour limiter les conditions spécifiques

## Choix techniques

On s'est concentré sur la lisibilité en traitant `info` à part des autres commandes
Le parser est modulable et reste flexible pour l’ajout de nouvelles commandes

## Difficultés rencontrées

Le principal défi a été d’adapter la logique du parsing pour supporter `info` sans contrainte sur les options
L'architecture actuelle permet d'ajouter de nouvelles commandes "spéciales" simplement
