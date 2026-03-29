# SAE 2.02 - Exploration Algorithmique (Graphes de dépendances)

## Équipe
- **Membre 1** : [SITTININAE Aditya]
- **Membre 2** : [MOISANT Lény]
- **Membre 3** : [TCHOUANGOU Louis]

## Synthèse du Sprint I : Représentation des graphes
[cite_start]L'objectif de ce sprint était de créer deux structures pour représenter des graphes orientés où les sommets sont des chaînes de caractères (noms de classes/paquetages).

### 1. Matrice d'Adjacence (`GrapheMatrice`)
* [cite_start]**Principe** : Utilise un tableau à deux dimensions (`String[][]`). 
* [cite_start]**Fonctionnement** : On utilise une `HashMap` pour relier chaque nom de sommet à un numéro de ligne/colonne. 
* [cite_start]**Arêtes** : La case contient l'étiquette (ex: "create") ou est vide s'il n'y a pas de lien.

### 2. Liste d'Adjacence (`GrapheListe`)
* [cite_start]**Principe** : Chaque sommet possède une liste de ses voisins. 
* [cite_start]**Fonctionnement** : Utilise une `Map` qui associe un sommet à une liste d'objets contenant la destination et l'étiquette. 
* [cite_start]**Avantage** : Plus économe en mémoire pour les graphes avec peu de liens.

### Tests
Les tests unitaires JUnit se trouvent dans les classes `G1Test` et `G2Test`.
