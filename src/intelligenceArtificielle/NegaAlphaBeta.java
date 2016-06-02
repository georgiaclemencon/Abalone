package intelligenceArtificielle;


import abaloneBoard.Board;
import abaloneBoardInterface.InterfaceBoard;
import javax.swing.*;
import java.util.ArrayList;


@SuppressWarnings("ALL")
public class NegaAlphaBeta extends MinMaxAbstract {

    private int noeud = 0;
    private int MAXDEPTH = 4;

    public NegaAlphaBeta() {
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
        gui.info.setIA("IA : Je cherche ! ");
        while (i < moves.size()) {    //Pour tous les coups possibles
            cp = new Board(player, copyArray(board.getBoard()));
            cp.move(moves.get(i), player);    //On simule le coup

            //float val = NegaAlphaBeta(cp,0,2,Integer.MIN_VALUE, Integer.MAX_VALUE);
            int val = NegaAlphaBeta(cp, player, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (val > maxVal) {
                maxVal = val;
                meilleurCoup = i;
            }
            updateUI(moves, i);
            i++;
            noeud += 1;
        }
        System.out.println("J" + player + " => MeilleurCoup : " + meilleurCoup);
        System.out.println("J" + player + " => Nombre de branches : " + noeud);
        //On réalise réellement le coup
        board.move(moves.get(meilleurCoup), player);
        gui.update(gui.getGraphics());
        gui.info.setIA("IA : Prête ! ");
    }

    /**
     * Recherche en profondeur 4 avec NegaAlphaBeta
     *
     * @param board      la board initiale
     * @param player     le joueur
     * @param profondeur la profondeur
     * @param alpha      l'alpha
     * @param beta       le beta
     */
    private int NegaAlphaBeta(Board board, int player, int profondeur, int alpha, int beta) {
        int valeur, Best, i;
        Board cp;

        if (profondeur == MAXDEPTH || board.getBilles(player).size() == 8)
            return fctEvaluation(board, player);                            //Evaluation
        ArrayList<int[]> moves = board.getMouvements(player);

        Best = -Integer.MAX_VALUE;

        int joueur;
        if (player == 2)
            joueur = 1;
        else
            joueur = 2;
        for (i = 0; i < moves.size(); i++) {
            cp = new Board(player, copyArray(board.getBoard()));
            cp.move(moves.get(i), player);
            valeur = -NegaAlphaBeta(cp, joueur, profondeur + 1, -beta, -alpha);    //Recherche
            if (valeur > Best) {
                Best = valeur;
                if (Best > alpha) {
                    alpha = Best;
                    if (alpha > beta) return Best;    //Coupure Alpha
                }
            }
            noeud += 1;
        }
        return Best;
    }

    private void updateUI(ArrayList<int[]> moves, int i) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(i);
            if (i == moves.size() - 1)
                progressBar.setValue(0);
        });
    }
}
