# TP FINAL 

## Difficultés rencontrées

Bonjour Monsieur, j'espère que vous allez bien ! 

### Ce qui a été plus compliqué

Je ne sais pas si cela sera évoqué par beaucoup d'autres, mais l’un de nos plus grands défis a été de réussir à nous réunir pour travailler ensemble.
Massi travaille toute la semaine en dehors des cours, tandis que de mon côté, j’étais en alternance. 
Cela a constitué un vrai challenge pour organiser un travail efficace à deux.

Une autre difficulté que nous avons rencontrée vient du fait que nous avons dû reprendre une base de code existante.
Étant habitués à toujours partir de zéro sur nos projets. C'était un peu déroutant de s’approprier une architecture déjà en place, d’en re-comprendre la logique chaque semaine,
et de l’adapter à nos besoins de façon régulière.

En plus de cela, nous avons eu du mal à comprendre public Runtime getRuntime(), nécessaire pour récupérer les informations dans la commande info.
Cela nous a beaucoup ralenti, car nous ne sommes pas encore très à l’aise avec Java.

Nous avons également rencontré des difficultés dans la gestion des arguments en ligne de commande.
Il n’était pas évident de les interpréter correctement, indépendamment de leur position, tout en respectant le format attendu.


### Par manque de temps

Sur le plan technique, l'amélioration des tests unitaires a représenté une vraie contrainte.
Nous avons eu du mal à savoir exactement quoi tester, comment le faire efficacement, et à évaluer la pertinence de nos choix.

Nous aurions aussi aimé intégrer davantage de fonctionnalités, comme la possibilité de modifier un objet déjà ajouté à la liste de courses.

Enfin, à titre plus personnel (Steven), j’aurais souhaité proposer un design plus moderne pour l’interface web.

## Les design patterns utilisés et pourquoi

### 1. **Command Pattern**

Utilisé pour encapsuler toutes les opérations possibles (`add`, `remove`, `list`, `web`, `etc`.) dans des objets de commande distincts.
Ce pattern permet :
* Une extensibilité facilitée pour l'ajout de nouvelles commandes
* Une séparation claire des responsabilités

### 2. **Factory Pattern**

Implémenté via `GroceryStorageFactory` & `CommandFactory` afin de :

* Masquer la complexité liée à la création des objets
* Gérer la création des objets de persistance (`JsonGroceryDAO`, `CsvGroceryDAO`, `etc`.)
* Centraliser la logique de création
* D'ajouter facilement de nouveaux types de stockage

### 3. **DAO (Data Access Object) Pattern**

Employé pour abstraire et encapsuler l'accès aux données :

* Permet une séparation entre la logique métier et la logique d'accès aux données
* Offre un support pour différents formats de stockage (`JSON`, `CSV`)
* Rend possible l’ajout de nouveaux formats de manière flexible et modulaire


## Questions

### Comment ajouter une nouvelle commande (en théorie) ?

Pour commencer, il faut créer une nouvelle classe qui implémente l'interface `CommandHandler`.  
Ensuite, on définit ce que fait la commande en implémentant la méthode `execute()`.  
Il faut aussi ajouter la commande dans l'enum `Command` pour qu’elle soit reconnue.  
Dans `CommandFactory`, on enregistre la commande dans le bloc statique `commandHandlers`.  
Enfin, il faut penser à ajouter un cas dans `CommandParser` pour que la commande soit bien interprétée.

### Comment ajouter une nouvelle source de données (en théorie) ?

On commence par créer une nouvelle classe qui implémente l’interface `GroceryDAO`.  
Il faut ensuite coder les méthodes `readGroceryList()` et `writeGroceryList()` dans cette classe.  
Pour finir, on modifie `GroceryStorageFactory` pour qu’il puisse reconnaître cette nouvelle source de données.

### Que faut-il changer pour spécifier un magasin pour ajouter des courses ?

Il faut modifier la classe `GroceryItem` pour lui ajouter un attribut `store`.  
Les DAO doivent ensuite être mis à jour pour qu’ils prennent en compte ce nouvel attribut.  
On ajoute une nouvelle option dans la ligne de commande, comme `--store`.  
Il faut aussi mettre à jour `ParsingResult` pour qu’il puisse stocker le nom du magasin.  
Et enfin, on adapte les commandes concernées, surtout `AddCommand`, pour qu’elles utilisent cette information.
