# Jeu de plateau Abalone


# Présentation du jeu

Abalone est un jeu de plateau récent, créé en 1987 par Laurent Levi et Michel Lalet. Il fait maintenant partie des jeux classiques de plateau.
 
Il y a déjà eu plusieurs recherches réalisées sur ce jeu mais comme il reste relativement récent, il est généralement délaisser aux profits d’autres jeux plus anciens. Certains travaux de recherches sont disponibles sur internet comme :

	•	« Constructing an Abalone Game-Playing Agent » (Université de Maastricht - Juin 2005) de Nyree Lemmens présentant un arbre de recherche d’une profondeur de 2.

	•	« A Simple Intelligent Agent for Playing Abalone Game : ABLA » de Ender Ozcan et Berk Hulagu,  à propos d’une intelligence artificielle utilisant l’algorithme AlphaBeta. La profondeur est alors de 3 à 4 avec utilisation de deux fonctions d’évaluations.

	•	« Algorithmic Fun- Abalone » de Oswin Aicholzer, Franz Aurenhammer et Tino Werner qui explique la programmation d’un agent utilisant la recherche alpha-beta avec des fonctions d’évaluations permettant le développement d’un arbre d’une profondeur de 9.

# Règles Basiques

Abalone est un jeu de plateau à 2 personnes. Le plateau est hexagonal avec 61 emplacements. Chaque joueur possède 14 billes. Il y a deux types de billes : les billes blanches et les billes noires. Ce sont les joueurs blancs qui commencent la partie. 
L’objectif du jeu est d’éjecter les billes de l’opposant du plateau, le premier joueur qui réussit à éjecter 6 billes de l’opposant gagne la partie. 

Il y a plusieurs placements possibles au départ, les plus populaires sont :

« La formation Standard » proposée par les concepteurs du jeu.

« La marguerite Belge » surtout utilisée en compétition car cette formation est plus agressive au début du jeu.

Nous avons de notre côté fait le choix d’implémenter d’autres positions pour permettre à notre jeu d’être plus fourni. C’est pourquoi nous avons également retenu la Marguerite Allemande, l’Alien, la Domination, l’Infiltration, le Wall, les Snakes et le « face à face » dont les dispositions sont disponibles en annexe.
Mouvements possibles

A chaque tour, chaque joueur peut faire un mouvement d’une, deux, ou trois billes de sa couleur. 
Plusieurs billes peuvent uniquement être bougées si elles sont connexes aux autres billes et si elles sont en ligne. De plus, toutes les billes doivent être déplacées dans la même direction. Il est possible de déplacer les billes par le côté, le long de la position courante des billes. 

Les mouvements « Sumito » et les situations spéciales

Pour pouvoir pousser les billes de son adversaire, le joueur doit se trouver en position de Sumito, c’est-à-dire en supériorité numérique. 
Une ligne de 3 billes ou plus ne peut jamais être poussée par l’adversaire. Les Sumitos possibles sont donc :
-	Le 3 contre 2,
-	Le 3 contre 1,
-	Le 2 contre 1.
 
Une autre règle importante dans Abalone concerne les situations de « Pac ». Dans ces situations, il est impossible pour les joueurs de réaliser des mouvements de Sumito car le nombre de billes sont équivalent :
-	Une bille contre une autre
-	Deux billes contre deux autres
-	Trois billes contre trois autres 
-	Quatre billes contre trois autres
-	Une bille alliée empêche le Sumito. 

#Les Algorithmes utilisés

Algorithme minimax : 

L’algorithme minimax est un processus de réflexion permettant d’évaluer le meilleur coup à jouer suivant l’environnement du plateau.
Dans la position donnée, le joueur Noir à une série de coups qu’il peut effectuer. Pour chacun d’eux, il s’interroge sur les répliques éventuelles que peut faire le joueur Blanc, qui lui-même analyse pour chacune de ses répliques celles auxquelles peut procéder le joueur Noir, qui à son tour examine à nouveau l’ensemble des coups qu’il peut effectuer suite aux répliques du joueur Blanc. 
Ce processus de réflexion peut continuer et dans le cas où les deux joueurs ont la capacité de mener cette réflexion jusqu’à ce qu’aucun des deux ne puisse plus jouer alors il est facile pour le joueur Noir de décider du coup qu’il doit jouer. Il lui suffit de choisir le coup qui, quelques soit la réplique, le mène à une victoire. 
Cette situation de recherche jusqu’à l’atteinte de positions terminales ne peut exister que lorsqu’elle est mise en œuvre à partir d’une position proche de la fin de la partie. Pour le reste de la partie, il est nécessaire de mettre en œuvre la stratégie minimax en utilisant une profondeur d’arbre prédéfinie et une fonction d’évaluation.

Algorithme NégaMax : 

Il s’agit d’une implémentation de minimax mais en convention Négamax, c’est à dire que plutôt que de tester si on est à un niveau pair ou impair pour savoir si on cherche à maximiser ou à minimiser l’évaluation, on peut inverser le signe des évaluations à chaque niveau, et toujours chercher à maximiser. Ce qui représente un gain de calcul considérable dans le cas d’Abalone.

Algorithme alpha-beta : 

Le problème de Minimax est que les informations ne circulent que dans un seul sens :  des feuilles vers la racine. Il est ainsi nécessaire d’avoir développé chaque feuille de l’arbre de recherche pour pouvoir propager les informations sur les scores des feuilles vers la racine. 
Le principe de l’élagage alpha-beta est d’éviter la génération de feuilles et de parties de l’arbre qui sont inutiles. 
Pour ce faire, cet algorithme repose sur l’idée de la génération de l’arbre selon un processus dit en « profondeur d’abord » où, avant de développer un frère, il convient de faire un nœud. A cette idée vient se greffer la stratégie qui consiste à utiliser l’information en la remontant des feuilles et également en la redescendant vers d’autres feuilles.
Le principe d’alpha-beta est de tenir à jour deux variables α et β qui contiennent respectivement à chaque moment du développement de l’arbre la valeur minimale que le joueur peut espérer obtenir pour le coup à jouer étant donné la position où il se trouve et la valeur maximale. Certains développements de l’arbre sont arrêtés car ils indiquent qu’un des joueurs a l’opportunité de faire des coups qui violent le fait que α est obligatoirement la note la plus basse que le joueur Max sait pouvoir obtenir ou que β est la valeur maximale que le joueur Min autorisera Max à obtenir.

# La fonction d’évaluation

-	Gagner ou Perdre : En fin de partie, elle peut permettre de faire le bon choix pour gagner la partie.  Ce choix peut également éviter à l’intelligence artificielle de perdre. Dans le cas d’une victoire, un résultat fort est généré par cette fonction, pour pouvoir être choisi par la suite. Dans le cas contraire, une valeur négative est émise pour éviter de choisir ce mouvement.

-	Nombre de billes : l’objectif du jeu étant de perdre le moins de billes possibles, une des composantes de la fonction d’évaluation est d’éviter de perdre la bille. Il suffit donc de compter le nombre de billes encore présentes sur le plateau une fois que le joueur précédant a joué son coup. Plus il y a de billes, plus le mouvement est intéressant.

-	Emplacement des billes sur le plateau : Plus un joueur est au centre du plateau, plus il a une position forte. Les billes proches des côtés sont en effet susceptibles de se faire éjecter. Le fait d’occuper le centre du plateau force les billes adverses à rester autour de la position forte au centre. Cette composante de la fonction d’évaluation a pour but de favoriser le placement des billes au centre du plateau.

-	Groupement des billes : C’est également une composante importante de la fonction d’évaluation. Plus les billes sont groupées, plus il est difficile pour un joueur ennemi de les déplacer. De ce fait il faudra toujours une bille de plus à l’adversaire pour attaquer une bille ou deux billes. Le groupement des billes est également important car il permet également d’avoir une position dominante sur l’adversaire et de ce fait, il permet de mieux se défendre contre lui, et/ou de mieux l’attaquer.

-	Groupement des billes ennemies : Le groupement ennemie à l’inverse à un impact négatif sur le joueur, car il lui donne une position dominante sur notre intelligence artificielle. 

-	Attaque de l’opposant : La fonction permet de compter l’ensemble des positions d’attaques donnée par la position des billes sur le plateau. La plus petite valeur pouvant être atteinte étant 0. Dans ce cas, c’est que nous sommes en situation de Pac, ou que l’ensemble des billes du joueur n’a pas de billes ennemies à proximité.  

-	Attaque par l’opposant : Ce composant est identique au précédent mais pour le joueur adverse. 

-	Emplacements dangereux des billes sur le plateau : Ce composant doit permettre de détecter les billes qui sont proches du bord du plateau pour ainsi éviter qu’elles ne soient perdues par le joueur. 

L’ensemble de ces fonctions est associé respectivement à des poids pour rechercher à maximiser ou minimiser l’influence de ces fonctions. 

# Les différents niveaux d’intelligence artificielles

IA Random
C’est l’IA la plus simple. Elle récupère l’ensemble des mouvements du joueur et en choisit 1 au hasard. 

IA Minimax avec Random 
C’est une IA un peu moins simple que la précédente. La profondeur de l’algorithme Minimax est de 4, seulement quand c’est au joueur adverse de jouer, le coup du joueur adverse est généré de façon aléatoire parmi les coups disponibles pour ce dernier.

Minimax classique
Une IA utilisant l’algorithme classique de minimax, la profondeur est de deux pour pouvoir être jouable rapidement (c’est à dire avec un délai d’environ 3-4 secondes).

Minimax avec plus de profondeur
Une IA utilisant l’algorithme classique de minimax, la profondeur est de deux au départ, puis de 4 quand le nombre de billes du joueur est en dessous de 10.

Alpha-beta
Une IA utilisant l’implémentation de l’algorithme minimax avec l’élagage alpha-beta. Le temps d’exécution est semblable à minimax mais le nombre de branches exploré est plus faible. 

NégaAlpha-Beta
Une implémentation d’alpha-beta mais en convention Négamax ce qui permet d’avoir une profondeur maximale de 4 (les temps de calculs peuvent être rallongés suivant la situation du joueur).

IA Tricheuse (peut jouer plusieurs coups)
Dans ce cas là, l’IA a 1 chance sur deux de prendre le tour du joueur suivant en plus du sien. Dans ce cas l’algorithme utilisé pour le tour suivant est un algorithme « random » pour que ce second tour ne soit pas trop long pour le joueur humain.

IA Tricheuse & Voleuse 
Dans ce cas là, l’IA conserve la chance de jouer 2 fois au lieu d’une comme précédemment, mais elle peut également voler un pion du joueur adverse de façon aléatoire (1 « chance » sur 4).