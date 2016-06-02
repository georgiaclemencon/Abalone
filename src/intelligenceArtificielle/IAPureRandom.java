package intelligenceArtificielle;


import abaloneBoard.Board;
import abaloneBoardInterface.InterfaceBoard;

import java.util.ArrayList;
import java.util.Random;


/**
 * IA La plus simple
 */
public class IAPureRandom extends MinMaxAbstract {

    public IAPureRandom() {
    }

    /**
     * Récupération du prochain mouvement du joueur
     *
     * @param board  le plateau
     * @param player le joueur
     * @param gui    l'interface pour update
     */

    public void nextMove(Board board, int player, InterfaceBoard gui) {

        gui.info.setIA("IA : Je cherche ! ");

        ArrayList<int[]> moves = board.getMouvements(player);    //Récupération des mouvements possible
        board.move(moves.get(randInt(0, moves.size() - 1)), player);
        gui.update(gui.getGraphics());
        gui.info.setIA("IA : Prête ! ");
    }

    private static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

}
