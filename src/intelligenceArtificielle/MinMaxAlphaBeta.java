package intelligenceArtificielle;

import java.util.ArrayList;
import javax.swing.SwingUtilities;

import abaloneBoard.Board;
import abaloneBoardInterface.InterfaceBoard;


public class MinMaxAlphaBeta extends MinMaxAbstract {


    private int noeud = 0;
    private float maxVal;

    /**
     * Récupération du prochain mouvement du joueur
     *
     * @param board  le plateau
     * @param player le joueur
     * @param gui    l'interface
     */
    public void nextMove(Board board, int player, InterfaceBoard gui) {

        progressBar.setValue(0);
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        ArrayList<int[]> moves = board.getMouvements(player);    //Récupération des mouvements possible
        int PROFONDEUR = 2;

        gui.info.setIA("IA : Je cherche ! ");

        progressBar.setMaximum(moves.size());
        if (!(board.getBilles(player).size() == 8)) {
            int meilleurCoup = 0;
            Board nouveauPlateau;
            for (int i = 0; i < moves.size(); i++) {    //Pour tous les coups possibles
                nouveauPlateau = new Board(player, copyArray(board.getBoard()));
                nouveauPlateau.move(moves.get(i), player);    //On simule le coup

                float val = alphabeta(nouveauPlateau, player, PROFONDEUR, alpha, beta); //Min du jeu avec la profondeur
                if (val > maxVal) {
                    meilleurCoup = i;
                    maxVal = val;
                }
                updateUI(moves, i);
                noeud += 1;
            }

            System.out.println("J" + player + " => MeilleurCoup : " + meilleurCoup);
            System.out.println("J" + player + " => Nombre de branches : " + noeud);
            //On réalise réellement le coup
            board.move(moves.get(meilleurCoup), player);
            gui.update(gui.getGraphics());
            gui.info.setIA("IA : Prête ! ");
        }


    }

    private float alphabeta(Board nouveauPlateau, int player, int profondeur, float alpha2, float beta2) {

        //Mouvement de ia
        ArrayList<int[]> moves = nouveauPlateau.getMouvements(2);
        float a = alpha2;
        float b = beta2;
        int joueur;
        if (player == 2)
            joueur = 1;
        else
            joueur = 2;


        if (profondeur == 0 || moves.size() == 0) {
            return fctEvaluation(nouveauPlateau, player);
        }

        Board cp;
        for (int i = 0; i < moves.size(); i++) {
            cp = new Board(player, copyArray(nouveauPlateau.getBoard()));
            float indice = alphabeta(cp, joueur, profondeur - 1, alpha2, beta2);
            if (joueur != 2) {
                if (indice < b) {
                    b = indice;
                }

            } else if (indice > a) {
                a = indice;
            }
            noeud += 1;
        }
        return (joueur != 2) ? b : a;

    }


    private void updateUI(ArrayList<int[]> moves, int i) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(i);
            if (i == moves.size() - 1)
                progressBar.setValue(0);
        });
    }

}
