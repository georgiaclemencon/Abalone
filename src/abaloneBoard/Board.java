package abaloneBoard;


import java.util.ArrayList;


/* *
 *  Cette classe se décompose en 3 parties
 * 	La méthode principale permet de gérer la progression du jeu. (whose turn it is, if the game has ended etc.)
 *  L'interface graphique 
 * 	the GUI hubs currently called print and readMove
 *  La méthode move détermine les mouvements possible.
 * 
 *  Un mouvement est décrit par un tableau :
 *  Si une bille est déplacé le long d'une ligne commune, le tableau à 4 entrées;
 *  [x,y,dx,dy] où (x,y) est le point de commencement et [dx,dy] est le vecteur de déplacement de la ligne
 *  Si les billes forment une ligne et qu'elle est déplacé par le flanc, le tableau à 7 entrées;
 * 	[x,y,ox,oy,dx,dy,l] ou la ligne de longueur l, commence de (x,y) et est déplacé dans le direction de (dx,dy)
 * */

public class Board{

	public final static int INVALIDE = -1;
	private final static int LIBRE = 0;
	public final static int BLANC = 1;
	public final static int NOIR = 2;
	public final int[][] directions = {{-1,-1},{-1,0},{0,-1},{0,1},{1,1},{1,0}};
	private final int[][] orientations = {{1,0},{1,1},{0,1}};

	private final int[][] test = {
			{2,2,2,2,2,-1,-1,-1,-1},
			{2,1,1,1,1,2,-1,-1,-1},
			{2,1,1,1,1,1,2,-1,-1},
			{2,1,1,0,0,0,0,2,-1},
			{2,2,1,0,0,0,0,0,0},
			{-1,0,0,0,0,0,0,0,0},
			{-1,-1,0,0,0,0,0,0,0},
			{-1,-1,-1,0,0,0,0,0,0},
			{-1,-1,-1,-1,0,0,0,0,0}
	};
	private final int[][] def = {
			{2,2,2,2,2,-1,-1,-1,-1},
			{2,2,2,2,2,2,-1,-1,-1},
			{0,0,2,2,2,0,0,-1,-1},
			{0,0,0,0,0,0,0,0,-1},
			{0,0,0,0,0,0,0,0,0},
			{-1,0,0,0,0,0,0,0,0},
			{-1,-1,0,0,1,1,1,0,0},
			{-1,-1,-1,1,1,1,1,1,1},
			{-1,-1,-1,-1,1,1,1,1,1}
	};
	private final int[][] versionBelge = {
			{1,1,0,2,2,-1,-1,-1,-1},
			{1,1,1,2,2,2,-1,-1,-1},
			{0,1,1,0,2,2,0,-1,-1},
			{0,0,0,0,0,0,0,0,-1},
			{0,0,0,0,0,0,0,0,0},
			{-1,0,0,0,0,0,0,0,0},
			{-1,-1,0,2,2,0,1,1,0},
			{-1,-1,-1,2,2,2,1,1,1},
			{-1,-1,-1,-1,2,2,0,1,1}
	};
	private final int[][] versionAllemande= {
			{0,0,0,0,0,-1,-1,-1,-1},
			{1,1,0,0,2,2,-1,-1,-1},
			{1,1,1,0,2,2,2,-1,-1},
			{0,1,1,0,0,2,2,0,-1},
			{0,0,0,0,0,0,0,0,0},
			{-1,0,2,2,0,0,1,1,0},
			{-1,-1,2,2,2,0,1,1,1},
			{-1,-1,-1,2,2,0,0,1,1},
			{-1,-1,-1,-1,0,0,0,0,0}
	};
	private final int[][] alien= {
			{2,0,2,0,2,-1,-1,-1,-1},
			{0,2,1,1,2,0,-1,-1,-1},
			{0,2,1,2,1,2,0,-1,-1},
			{0,0,0,2,2,0,0,0,-1},
			{0,0,0,0,0,0,0,0,0},
			{-1,0,0,0,1,1,0,0,0},
			{-1,-1,0,1,2,1,2,1,0},
			{-1,-1,-1,0,1,2,2,1,0},
			{-1,-1,-1,-1,1,0,1,0,1}
	};
	private final int[][] domination= {
			{0,0,0,0,0,-1,-1,-1,-1},
			{1,0,0,0,0,2,-1,-1,-1},
			{1,1,0,0,0,2,2,-1,-1},
			{1,1,1,1,0,2,2,2,-1},
			{0,0,0,2,0,2,0,0,0},
			{-1,2,2,2,0,1,1,1,1},
			{-1,-1,2,2,0,0,0,1,1},
			{-1,-1,-1,2,0,0,0,0,1},
			{-1,-1,-1,-1,0,0,0,0,0}
	};
	private final int[][] infiltration= {
			{0,2,1,2,0,-1,-1,-1,-1},
			{0,2,2,2,2,0,-1,-1,-1},
			{0,2,1,2,1,2,0,-1,-1},
			{0,2,0,0,0,0,2,0,-1},
			{0,0,0,0,0,0,0,0,0},
			{-1,0,1,0,0,0,0,0,0},
			{-1,-1,0,1,2,1,2,1,0},
			{-1,-1,-1,0,1,1,1,1,0},
			{-1,-1,-1,-1,0,1,2,1,0}
	};
	private final int[][] wall= {
			{0,0,1,0,0,-1,-1,-1,-1},
			{0,0,0,0,0,0,-1,-1,-1},
			{0,1,1,1,1,1,0,-1,-1},
			{1,1,1,1,1,1,1,1,-1},
			{0,0,0,0,0,0,0,0,0},
			{-1,2,2,2,2,2,2,2,2},
			{-1,-1,0,2,2,2,2,2,0},
			{-1,-1,-1,0,0,0,0,0,0},
			{-1,-1,-1,-1,0,0,2,0,0}
	};
	private final int[][] snakes= {
			{2,2,2,2,2,-1,-1,-1,-1},
			{2,0,0,0,0,0,-1,-1,-1},
			{2,0,0,0,0,0,0,-1,-1},
			{2,0,0,2,2,1,1,0,-1},
			{0,2,0,2,0,1,0,1,0},
			{-1,0,2,2,1,1,0,0,1},
			{-1,-1,0,0,0,0,0,0,1},
			{-1,-1,-1,0,0,0,0,0,1},
			{-1,-1,-1,-1,1,1,1,1,1}
	};
	private final int[][] faceAface= {
			{0,0,0,0,0,-1,-1,-1,-1},
			{0,0,0,0,0,0,-1,-1,-1},
			{1,1,0,0,0,2,2,-1,-1},
			{1,1,1,0,0,2,2,2,-1},
			{1,1,1,1,0,2,2,2,2},
			{-1,1,1,1,0,0,2,2,2},
			{-1,-1,1,1,0,0,0,2,2},
			{-1,-1,-1,0,0,0,0,0,0},
			{-1,-1,-1,-1,0,0,0,0,0}
	};


	public int joueurCourant;	// Le joueur courant
	private int[][] board;	// La board

	// Initialisation de la board par défaut
	public Board(int config){
		initDefault(config);
	}


	// Remise en place de la board par l'IA
	public Board(int cplayer,  int[][] board){
		this.joueurCourant = cplayer;
		setBoard(board);
	}


	// Valeurs par défaut
	private void initDefault(int config) {
		
		switch(config){ //Définition du mode de la board
		case 0:	// Par défaut
			this.board = def;
			break;
		case 1:	//Maguerite Belge (compétition)
			this.board = versionBelge;
			break;
		case 2:	//Marguerite Allemande
			this.board = versionAllemande;
			break;
		case 3: //Alien
			this.board = alien;
			break;
		case 4:
			this.board = domination;
			break;
		case 5:
			this.board = infiltration;
			break;
		case 6:
			this.board = wall;
			break;
		case 7:
			this.board = snakes;
			break;
		case 8:
			this.board = faceAface;
			break;
		default:
			this.board = test;
		}
		this.joueurCourant = 1; //C'est au tour de l'humain (à vérifier)
	}


	/**
	 * Place la valaur V en XY;
	 * @param x en x
	 * @param y en y
	 * @param v la nouvelle valeur
	 */
	public void set(int x, int y, int v){
		try {
			if(this.get(x, y) != INVALIDE){
				board[y][x] = v;}
		} catch (Exception ignored) {
		}
	}
	/**
	 * Récupère la valeur d'un XY du tableau
	 * @param x en x
	 * @param y en y
	 * @return la valeur
	 */
	public int get(int x, int y) {
		if(0 <= x && x <= 8 && 0 <= y && y <= 8 ){
			return this.board[y][x];
		}
		else{
			return INVALIDE;
		}
	}
	// Débug, affichage du tableau;
	@SuppressWarnings("unused")
	public void print(){
		System.out.println(this.joueurCourant);
		for(int y = 0; y < this.board.length; y++){
			System.out.print(new String(new char[Math.abs(y-4)]).replace("\0", " "));
			for(int x = 0; x < this.board[y].length; x++){
				if(board[y][x]!=-1){System.out.print(board[y][x] + " ");}
			}
			System.out.println();
		}
	}
	// Retourne les billes d'un joueur
	public ArrayList<int[]> getBilles(int player){
		ArrayList<int[]> ret = new ArrayList<>();
		for(int y = 0; y < board.length; y++){
			for(int x = 0; x < board[y].length; x++){
				if(board[y][x] == player){
					int[] pair = {x,y};
					ret.add(pair);
				}
			}
		}
		return ret;
	}

	// Retourne l'ensemble des mouvement d'un tableau
	public ArrayList<int[]> getMouvements(int player){
		ArrayList<int[]> moves = new ArrayList<>();
		moves.addAll(this.getMouvementEnLigne(player));
		moves.addAll(this.getMouvementParLeFlanc(player));
		return moves;
	}

	// Retourne les mouvement possible par le flanc
	private ArrayList<int[]> getMouvementParLeFlanc(int player) {
		ArrayList<int[]> moves = new ArrayList<>();
		for(int[] billes : getBilles(player)){			// Pour chaque bille du joueur
			for(int[] d : directions){
				for(int[] or: orientations){

					if(isPossibleParLeFlanc(billes[0], billes[1], or[0], or[1], d[0], d[1] , 2, player)){
						int[] move = {billes[0], billes[1], or[0], or[1], d[0], d[1], 2};
						moves.add(move);
					}
					if(isPossibleParLeFlanc(billes[0], billes[1], or[0], or[1], d[0], d[1] , 3, player)){
						int[] move = {billes[0], billes[1], or[0], or[1], d[0], d[1], 3};
						moves.add(move);
					}
				}
			}
		}
		return moves;
	}

	/*
	 * Recherche sur le mouvement du flanc est possible
	 */
	private boolean isPossibleParLeFlanc(int x, int y, int ox, int oy, int dx, int dy, int length, int p) {
		boolean isDir = false;
		for(int[] d : directions){
			if(d[0] == dx && d[1] == dy){
				isDir = true;
			}
		}

		for(int i = 0; i< length;i++){
			int f = this.get(x + i*ox, y + i*oy);
			int off = this.get(x + i*ox + dx, y + i*oy + dy);
			if(f != p || off != LIBRE){
				return false;
			}
		}
		return isDir;
	}


	/**
	 * Recherche les mouvements possible pour un joueur
	 * @param player le joueur
	 * @return Un ArrayList contenant des tableaux d'entiers qui retournent les mouvements des billes en ligne
	 */
	public ArrayList<int[]> getMouvementEnLigne(int player) {
		ArrayList<int[]> moves = new ArrayList<>();	// List accumulant tout les mouvements possible
		for(int[] bille : getBilles(player)){			// Pour chaque bille du joueur
			for(int[] d : directions){			//Pour chaque direction de la bille
				
				//Vérification si le mouvement est possible en ligne
				//Pour le x y de la bille et la direction x y du joueur
				if(isMouvementPossibleEnLigne(bille[0], bille[1],d[0],d[1], player)){ 
					//Si c'est le cas, le mouvement est possible
					int[] move = {bille[0], bille[1],  d[0], d[1]};
					moves.add(move);
				}
			}
		}
		return moves;
	}
	/**
	 * Vérifie si un mouvement est possible en ligne pour un joueur
	 * @param x emplacement de départ
	 * @param y emplacement de départ
	 * @param dx direction en x
	 * @param dy direction en y
	 * @param p longueur
	 * @return vrai ou faux selon le mouvement
	 */
	private boolean isMouvementPossibleEnLigne(int x, int y, int dx, int dy, int p) {
		if(this.get(x, y) != p){return false;}
		int[] d = {dx, dy};
		if(!isDir(d)){return false;}
		int f1 = this.get(x + dx, y + dy);
		if(f1 == LIBRE){	// si le champs est vide, on ajoute le mouvement
			return true;
		}
		else if(f1==INVALIDE){return false;}	// si le prochain champs est invalide on retourne faux;
		else if(f1 == p){		// si le prochaine champs est occupé par la bille du même joueur
			int f2 = this.get(x + 2*dx, y + 2*dy);

			// Deux dans la même ligne
			if(f2 == LIBRE){		// Si la prochaine champs est libre, on ajoute la position
				return true;
			}
			else if(f2==INVALIDE){return false;}	// si le prochain champs est invalide on retourne faux;
			else if(f2!=p){		// si le prochaine champs est occupé par la bille du même joueur
				int f3 = this.get(x + 3*dx, y + 3*dy);
				if(f3 ==LIBRE || f3 ==INVALIDE){
					return true;
				}
			}
			else{
				// Trois dans la même ligne

				int f3 = this.get(x + 3*dx, y + 3*dy);
				int f4 = this.get(x + 4*dx, y + 4*dy);
				int f5 = this.get(x + 5*dx, y + 5*dy);
				if(f3 == INVALIDE || f3 == p){
					return false;
				}
				else if (f3 == LIBRE){	// Dans le cas ou l'emplacement est libre
					return true;
				}
				else{
					if(f4 == LIBRE || f4 == INVALIDE){	
						return true;
					}
					else if(f4 == p){return false;}	// Dans le cas ou la 4ème bille est celle du même joueur
					else{
						if(f5 == INVALIDE || f5 == LIBRE){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	private boolean isDir(int[] d) {
		for(int[] di:directions){
			if(di[0] == d[0] && di[1] == d[1]){return true;}
		}
		return false;
	}
	// Application du mouvement décrit par m
	/**
	 * Applique le mouvement de m
	 * @param m, le mouvement d'une bille avec sa position avant / après
	 */
	public void  move(int[] m, int player){
		int cx = m[0];
		int cy = m[1];
		int tmp = 0;
		int tmpn;

		if(m.length == 4){				//le long du ligne {x,y,dx,dy}
			do {
				tmpn = get(cx, cy);
				set(cx, cy, tmp);
				cx += m[2];
				cy += m[3];
				tmp = tmpn;

			} while (tmp!= LIBRE && tmp != INVALIDE);
		}
		else{						// sur le flanc
			for(int i = 0; i< m[6];i++){
				this.set(m[0] + i*m[2], m[1] + i*m[3], LIBRE);
				this.set(m[0] + i*m[2] + m[4], m[1] + i*m[3] + m[5], player);
			}
		}

	}

	public int[][] getBoard() {
		return board;
	}
	private void setBoard(int[][] board) {
		this.board = board;
	}

	/**
	 * Vérification si c'est un mouvement possible pour le joueur
	 * @param m le mouvement
	 * @param cPlayer le joueur
	 * @return vrai ou faux
	 */
	public boolean isMouvementPossible(int[] m, int cPlayer) {
		if (m.length == 4) {
			return isMouvementPossibleEnLigne(m[0], m[1], m[2], m[3], cPlayer);
		}
		return m.length == 7 && isPossibleParLeFlanc(m[0], m[1], m[2], m[3], m[4], m[5], m[6], cPlayer);
	}
}