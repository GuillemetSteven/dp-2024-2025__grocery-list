# TP2 - Implémentation de la catégorisation des articles

## Exigences implémentées

AJout des fonctionnalités suivantes :
- Support de l'option `-c` pour spécifier la catégorie d'un article
- Comportement par défaut affectant les articles sans catégorie spécifiée à "default"
- Affichage des articles groupés par catégorie avec préfixes "#" et suffixes ":"

## Architecture mise en place

 Architecture définie dans le dossier `/schema` :

1. **GroceryItem** - Classe pour les articles avec nom, quantité et maintenant catégorie
2. **CommandLineParser** - Traitement des arguments avec support pour l'option catégorie
3. **GroceryStorageFactory** - Factory pour créer les gestionnaires selon le format
4. **GroceryOperations** - Logique métier des commandes (add, remove, list, total)
5. **Main** - Point d'entrée qui orchestre les interactions entre composants

## Prochaines étapes

- **DAO** - Abstraction de l'accès aux données avec interfaces dédiées
- **Factory** - Création des gestionnaires de stockage selon le format
- **Strategy** - Interchangeabilité des formats de stockage via interfaces communes

On a vu ensemble avec Massi la structure de notre code.
La principale difficulté a été de se mettre d'accord sur la direction à prendre.
On va peut-être revenir en arrière une fois qu'on aura implémenté les différentes fonctionnalités et différents design patterns. 
Mais avec la construction de notre architecture, cela devrait être maintenant facilement modulable.
L'application permet maintenant de catégoriser les articles grâce à l'option -c ou --category.
Les articles sont groupés par catégorie lors de l'affichage.
Le schéma d'architecture se trouve dans le dossier /schema et présente l'interaction entre les différentes classes.