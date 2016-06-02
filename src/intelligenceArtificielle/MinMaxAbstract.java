package intelligenceArtificielle;

import java.util.ArrayList;

import javax.swing.JProgressBar;

import abaloneBoard.Board;
import abaloneBoardInterface.InterfaceBoard;

public abstract class MinMaxAbstract {

    private int[] poids = {1000000,    //Le résultat de la partie
            100,    //Le nombre de billes
            1000,    //Nombre de groupe (plus il y a de groupement, plus c'est safe pour le joueur
            -1000,    //Nombre de groupement énnemie
            4000,    //Attaque possible
            -15, //Attaque possible pour l'ennemie
            2000,    //Emplacement de pions
            -300};    //Emplacement dangeureux de pions
    private final int[][] poidsEmplacement = {
            {0, 0, 0, 0, 0, -1, -1, -1, -1},
            {0, 4, 4, 4, 4, 0, -1, -1, -1},
            {0, 4, 8, 8, 8, 4, 0, -1, -1},
            {0, 4, 8, 12, 12, 8, 4, 0, -1},
            {0, 4, 8, 12, 16, 12, 8, 4, 0},
            {-1, 0, 4, 8, 12, 12, 8, 4, 0},
            {-1, -1, 0, 4, 8, 8, 8, 4, 0},
            {-1, -1, -1, 0, 4, 4, 4, 4, 0},
            {-1, -1, -1, -1, 0, 0, 0, 0, 0}
    };
    JProgressBar progressBar;

    /**
     * Le résultat du nombre de billes du joueur et de l'opposant
     * Le score est forcément très petit ou très grand pour que la fonction d'évaluation évite ou choisi ce mouvement
     */
    private int gameResult(ArrayList<int[]> billes, ArrayList<int[]> billesOpposant) {
        if (billes.size() == 8) {
            return Integer.MIN_VALUE;
        } else if (billesOpposant.size() == 8) {
            return Integer.MAX_VALUE;
        } else {
            return 0;
        }
    }

    /**
     * Retourne le nombre de billes d'un tableau
     *
     * @param billes l'arraylist de billes
     * @return le nombre de bille
     */
    private int nbBilles(ArrayList<int[]> billes) {
        return billes.size();
    }

    /**
     * Retourne le nombre de groupement de billes
     * Dans abalone, plus les billes sont groupés moins on peut se faire attaquer avec
     *
     * @param board  la board courante
     * @param player le joueur
     * @return le score de groupement
     */
    private int groupement(Board board, int player) {
        int g = 0;
        for (int[] billes : board.getBilles(player)) {    //Ensemble des billes du joueurs
            for (int[] d : board.directions) {    //Ensemble des directions possible
                if (board.get(billes[1] + d[1], billes[1] + d[1]) == player) {    //Si une bille + une direction = une nouvelle bille appartenant au joueur
                    g++;    //Alors la bille est groupé
                }
            }
        }
        return g;    //On retourne le nombre de billes qui sont groupés
    }

    /**
     * Détermine le score d'attaque d'un joueur
     *
     * @param board  la board courante
     * @param player le joueur
     * @return la valeur d'ataque du joueur
     */
    private int attaque(Board board, int player) {

        int att = 0;

        for (int[] move : board.getMouvementEnLigne(player)) { //Pour chaque mouvement du joueur
            //Dans le cas de deux billes qui se suivent et que la prochaine bille est celle du joueur adverse, alors on augmente l'attaque
            if (board.get(move[1] + move[3], move[0] + move[2]) == player && board.get(move[1] + 2 * move[3], move[0] + 2 * move[1]) == 3 - player) {
                att++;
            }
            //Dans le cas de trois billes sui se suivent et la présence d'une bille appartenant au joueur adverse alors on augmente l'attaque
            else if (board.get(move[1] + 2 * move[3], move[0] + 2 * move[2]) == player && board.get(move[1] + 3 * move[3], move[0] + 3 * move[2]) == 3 - player) {
                att++;
            }
        }
        return att;
    }


    /**
     * Permet de copier un tableau
     *
     * @param a le tableau à copier
     * @return le nouveau tableau
     */
    int[][] copyArray(int[][] a) {
        int[][] ret = new int[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            System.arraycopy(a[i], 0, ret[i], 0, a[0].length);
        }
        return ret;
    }


    public void nextMove(Board board, int player, InterfaceBoard gui) {
    }

    int fctEvaluation(Board board, int player) {
        int joueurEnnemie;
        if (player == 1)
            joueurEnnemie = 2;
        else
            joueurEnnemie = 1;

        int result = 0;
        ArrayList<int[]> billes = board.getBilles(player); //Les billes de l'IA
        ArrayList<int[]> billesOpposant = board.getBilles(joueurEnnemie);     //Les billes du joueur 1 (Humain)
        result += poids[0] * gameResult(billes, billesOpposant);
        result += poids[1] * nbBilles(billes);
        result += poids[2] * groupement(board, player);
        result += poids[3] * groupement(board, joueurEnnemie);
        result += poids[4] * attaque(board, player);
        result += poids[5] * attaque(board, joueurEnnemie);
        result += poids[6] * emplacement(billes);
        result += poids[7] * emplacementDangeureux(billes, board, player);

        return result;
    }

    /**
     * Emplacement des billes du joueurs sur le plateau
     *
     * @param billes les billes
     * @return le score des billes
     */
    private int emplacement(ArrayList<int[]> billes) {
        int poidsTotalEmplacement = 0;
        for (int[] coordonnées : billes) {
            poidsTotalEmplacement += poidsEmplacement[coordonnées[0]][coordonnées[1]];
        }
        return poidsTotalEmplacement;
    }

    /**
     * Emplamement dangereux sur le plateaux
     *
     * @param billes les billes
     * @param board  le plateau
     * @return le score de danger
     */
    private int emplacementDangeureux(ArrayList<int[]> billes, Board board, int joueur) {
        int emplaementDangeureux = 0;
        for (int[] coordonnées : billes) {
            if (poidsEmplacement[coordonnées[0]][coordonnées[1]] == 0) {    // C'est un emplacement dangeureux
                for (int[] d : board.directions) {    //Ensemble des directions possible
                    if (board.get(coordonnées[1] + d[1], coordonnées[1] + d[1]) == joueur) {    //Si une bille + une direction = une nouvelle bille appartenant au joueur
                        emplaementDangeureux += 1;    //Alors la bille est dangeureuse
                    }
                }
            }
        }
        return emplaementDangeureux;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setPoidsHeuristique(int[] heuristique) {
        this.poids = heuristique;
    }

}
