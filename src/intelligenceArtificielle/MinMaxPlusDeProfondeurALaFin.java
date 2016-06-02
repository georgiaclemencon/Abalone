package intelligenceArtificielle;


import abaloneBoard.Board;
import abaloneBoardInterface.InterfaceBoard;
import player.Player;

import javax.swing.*;
import java.util.ArrayList;


public class MinMaxPlusDeProfondeurALaFin extends MinMaxAbstract {

    private int noeud = 0;

    public MinMaxPlusDeProfondeurALaFin() {
    }

    /**
     * Récupération du prochain mouvement du joueur
     *
     * @param board  le plateau
     * @param player le joueur
     * @param gui    l'interface pour update
     */

    public void nextMove(Board board, int player, InterfaceBoard gui) {

        ArrayList<int[]> moves = board.getMouvements(player);    //Récupération des mouvements possible

        progressBar.setValue(0);
        progressBar.setMaximum(moves.size());

        float maxVal = Float.MIN_VALUE;
        int meilleurCoup = 0;
        int i = 0;
        Board cp;

        int PROFONDEUR = 2;
        if (board.getBilles(2).size() <= 10)
            PROFONDEUR = 4;

        gui.info.setIA("IA : Je cherche ! ");

        while (i < moves.size()) {    //Pour tous les coups possibles
            cp = new Board(player, copyArray(board.getBoard()));
            cp.move(moves.get(i), player);    //On simule le coup

            float val = min(cp, PROFONDEUR, player); //Min du jeu avec la profondeur
            if (val > maxVal) {
                maxVal = val;
                meilleurCoup = i;
            }
            updateUI(moves, i);
            i++;
            noeud += 1;
        }
        System.out.println("J" + player + " => MeilleurCoup : " + meilleurCoup);
        System.out.println("J" + player + " => Nombre de branche s: " + noeud);
        //On réalise réellement le coup
        board.move(moves.get(meilleurCoup), player);
        gui.update(gui.getGraphics());

        gui.info.setIA("IA : Prête ! ");
    }

    private void updateUI(ArrayList<int[]> moves, int i) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(i);
            if (i == moves.size() - 1)
                progressBar.setValue(0);
        });
    }


    /**
     * Noard Max
     *
     * @param board      le plateau courant
     * @param profondeur la profondeur
     * @param player     le joueur
     * @return le socre pour ce noeud une fois la profondeur maximal atteinte
     */
    private int max(Board board, int profondeur, int player) {
        if (profondeur == 0 || board.getBilles(1).size() == 8 || board.getBilles(2).size() == 8) {
            return fctEvaluation(board, player);
        }

        int maxVal = Integer.MIN_VALUE;
        int val;
        ArrayList<int[]> moves = board.getMouvements(player);
        Board cp;
        for (int[] move : moves) {
            cp = new Board(player, copyArray(board.getBoard()));
            cp.move(move, player);
            val = min(cp, profondeur - 1, player);
            if (val > maxVal) {
                maxVal = val;
            }
            noeud += 1;
        }
        return maxVal;
    }

    /**
     * Noeud Min
     *
     * @param board      le plateau courant
     * @param profondeur la profondeur
     * @param player     le joueur
     * @return le résultat de la fonction une fois la profondeur atteinte
     */
    private int min(Board board, int profondeur, int player) {
        if (profondeur == 0 || board.getBilles(1).size() == 8 || board.getBilles(2).size() == 8) {
            return fctEvaluation(board, player);
        }

        int minVal = Integer.MAX_VALUE;
        int val;
        int joueur;
        if(player == 2){
            joueur =1;
        }else
            joueur = 2;
        ArrayList<int[]> moves = board.getMouvements(joueur);    //On récupère les mouvements du joueur
        Board cp;

        for (int[] move : moves) {
            cp = new Board(joueur, copyArray(board.getBoard()));
            cp.move(move, joueur);                //On simule le coup
            val = max(cp, profondeur - 1, player); //On fait appel au prochain noeud
            if (val < minVal) { //On sauvegarde la meilleur valeur
                minVal = val;
            }
            noeud += 1;
        }
        return minVal;
    }

}
