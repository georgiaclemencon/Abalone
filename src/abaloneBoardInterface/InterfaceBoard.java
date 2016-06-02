package abaloneBoardInterface;


import java.awt.*;
import javax.swing.*;

import abaloneBoard.Board;
import intelligenceArtificielle.*;
import player.Player;

import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

public class InterfaceBoard extends JPanel {

    private boolean IAtriche = false;
    private Board board;
    //MiniMax IA ;
    private MinMaxAbstract IA;
    private ArrayList<int[]> selection;
    private int[] selectDir;

    public InterfaceInformation info;
    private int mode;
    private Player[] players;
    private Main main;
    private boolean IAVoleuse;
    private Thread IAThread;
    private int Coup = 0;



    InterfaceBoard(int mode, int config, int[] poids, int niveauDifficulté, InterfaceInformation info, Main main) {

        //Récupération des informations
        this.mode = mode;
        this.info = info;
        this.main = main;
        //Détermination du nombre de joueur
        // mode = 1 : Joueur contre IA
        // mode = 2 : Joueur contre Joueur
        // mode = 3 : IA contre IA

        players = new Player[2];
        players[0] = new Player(1); //Le joueur 1 et l'humain
        players[1] = new Player(2); //IA


        this.board = new Board(config);    //Mise en place de la board

        if (niveauDifficulté == 0) {
            IA = new IAPureRandom();
        } else if (niveauDifficulté == 1) {
            IA = new IARandom();
        } else if (niveauDifficulté == 2) {
            IA = new MinMax();
        } else if (niveauDifficulté == 3) {
            IA = new MinMaxPlusDeProfondeurALaFin();
        } else if (niveauDifficulté == 4) {
            IA = new MinMaxAlphaBeta();
        } else if (niveauDifficulté == 5) {
            IA = new NegaAlphaBeta();
        } else if (niveauDifficulté == 6) {
            IA = new NegaAlphaBeta();
            IAtriche = true;
        } else if (niveauDifficulté == 7) {
            IA = new NegaAlphaBeta();
            IAtriche = true;
            IAVoleuse = true;
        }


        //Initialisation du Thread
        IAThread = new Thread();

        if (IA != null) {
            IA.setProgressBar(info.progressBar);
            IA.setPoidsHeuristique(poids);
        }

        // Le mode par 0 est le Joueur contre Joueur

        this.selection = new ArrayList<>();    //Mise en place de l'ArrayList pour la sauvegarde de la sélection

        if (mode == 1 || mode == 0) {
            addMouseListener(new MouseAdapter() {        //Permet de détecter les cliques de la souris
                public void mouseClicked(MouseEvent evt) {
                    clickSouris(evt);    //Traitement des cliques en X et Y
                }
            });
        }


        //Mise en place du mode IA vs IA
        if (mode == 2) {

            new Thread(() -> {

                try{

                    while (board.getBilles(1).size() > 8 && board.getBilles(2).size() > 8 ) {
                        //Joueur Blanc joue
                        board.joueurCourant = 1;
                        IA.nextMove(board, players[0].joueur, InterfaceBoard.this);    //Joueur 2 joue
                        board.joueurCourant = players[1].joueur;
                        System.out.println(" => Prochain joueur : " + players[1].joueur);
                        InterfaceBoard.this.update(InterfaceBoard.this.getGraphics());
                        if (IAtriche) { //Dans ce cas l'ia peut rejouer la tricheuse
                            if (randInt(0, 10) >= 5) {

                                System.out.println("/!\\ Oups ! l'IA recommence! /!\\");

                                MinMaxAbstract IAbis = IA;
                                IA = new IAPureRandom();
                                IA.nextMove(board, players[0].joueur, InterfaceBoard.this);
                                board.joueurCourant = players[1].joueur;
                                IA = IAbis;
                            }
                        }
                        if (IAtriche && IAVoleuse) { //Dans ce cas l'ia peut rejouer la tricheuse
                            int result = randInt(0, 10);
                            if (result >= 7) {

                                System.out.println("Oups! Vous avez perdu un pion!  ");

                                ArrayList<int[]> billes = board.getBilles(2);
                                board.set(billes.get(0)[0], billes.get(0)[1], Board.BLANC);


                            } else {
                                System.out.println(result + "/10! Je ne te volerai pas un pion !");
                            }

                        }
                        board.joueurCourant = 2;



                        //Joueur Noir joue
                        board.joueurCourant = 2;
                        IA.nextMove(board, players[1].joueur, InterfaceBoard.this);    //Joueur 2 joue
                        board.joueurCourant = players[0].joueur;
                        System.out.println(" => Prochain joueur : " + players[0].joueur);
                        InterfaceBoard.this.update(InterfaceBoard.this.getGraphics());
                        if (IAtriche) { //Dans ce cas l'ia peut rejouer la tricheuse
                            if (randInt(0, 10) >= 5) {

                                System.out.println("/!\\ Oups ! l'IA recommence! /!\\");

                                MinMaxAbstract IAbis = IA;
                                IA = new IAPureRandom();
                                IA.nextMove(board, players[1].joueur, InterfaceBoard.this);
                                board.joueurCourant = players[0].joueur;
                                IA = IAbis;
                            }
                        }
                        if (IAtriche && IAVoleuse) { //Dans ce cas l'ia peut rejouer la tricheuse
                            int result = randInt(0, 10);
                            if (result >= 7) {

                                System.out.println("Oups! Vous avez perdu un pion!  ");

                                ArrayList<int[]> billes = board.getBilles(1);
                                board.set(billes.get(0)[0], billes.get(0)[1], Board.NOIR);

                            } else {
                                System.out.println(result + "/10! Je ne te volerai pas un pion !");
                            }

                        }
                        board.joueurCourant = 1;
                        Coup += 1;

                        info.setLabelCoup(Coup);
                        info.labelNoir.setText("Joueur Noir : " + board.getBilles(2).size());
                        info.labelBlanc.setText("Joueur Blanc : " + board.getBilles(1).size());
                    }

                }catch (NullPointerException ignored){

                }

            }).start();

        }

    }

    /**
     * Traitement des clicks en X et Y
     *
     * @param evt l'évènement
     */
    private void clickSouris(java.awt.event.MouseEvent evt) {
        if (mode == 2 && !IAThread.isAlive()) return;
        if (board.getBilles(1).size() > 8 && board.getBilles(2).size() > 8 && !IAThread.isAlive()) {    //Si l'on a plus de 8 billes (le 1 et le 2 correspondent aux joueurs)
            getPlayerMove(evt.getX(), evt.getY());
        } else if (board.getBilles(1).size() <= 8 || board.getBilles(2).size() <= 8) {
            JOptionPane.showMessageDialog(this, "Fin de partie");
        }

        this.update(getGraphics());
    }

    /*
     * Permet de récupérer la position du joueur
     */
    private void getPlayerMove(int x, int y) {
        int choixEnY = (y - 35) / 37;
        int choixEnX = (x - (choixEnY % 2) * (20) - Math.abs((choixEnY - 4) / 2) * 40 - 20) / 40 + ((choixEnY > 4) ? (choixEnY - 5) : -1);

        System.out.println("J " + Player.getColor(board.get(choixEnX, choixEnY)) + " x:" + choixEnX + " y: " + choixEnY + " bille : " + (selection.size() + 1));

        int[] point = {choixEnX, choixEnY};

        //Si l'emplacement est différent du joueur courant ou que la sélection est égal à 3 alors on lance le mouvement
        if (board.get(choixEnX, choixEnY) != board.joueurCourant || selection.size() == 3) {

            selectDir = point; //Emplacment du clique, si c'est le mouvement final, ce sera vers cette emplacement que les billes se déplacement
            int[] move = mouvementFinal(); //Calcul du mouvement final

            System.out.println(board.getBilles(1).size());
            info.labelNoir.setText("Joueur Noir : " + board.getBilles(2).size());
            info.labelBlanc.setText("Joueur Blanc : " + board.getBilles(1).size());


            if (board.isMouvementPossible(move, board.joueurCourant)) { //Si le mouvement pour le joueur est possible

                board.move(move, board.joueurCourant);


                //On fait jouer le joueur IA
                if (board.getBilles(1).size() > 8 && board.getBilles(2).size() > 8 && mode != 0) {    //Si l'on a plus de 8 billes (le 1 et le 2 correspondent aux joueurs)

                    IAThread = new Thread(() -> {

                        board.joueurCourant = 2;
                        IA.nextMove(board, players[1].joueur, InterfaceBoard.this);    //Joueur 2 joue
                        board.joueurCourant = players[0].joueur;
                        System.out.println(" => Prochain joueur : " + players[0].joueur);
                        InterfaceBoard.this.update(InterfaceBoard.this.getGraphics());
                        if (IAtriche) { //Dans ce cas l'ia peut rejouer la tricheuse
                            if (randInt(0, 10) >= 5) {

                                System.out.println("/!\\ Oups ! l'IA recommence! /!\\");

                                MinMaxAbstract IAbis = IA;
                                IA = new IAPureRandom();
                                IA.nextMove(board, players[1].joueur, InterfaceBoard.this);
                                board.joueurCourant = players[0].joueur;
                                IA = IAbis;
                                InterfaceBoard.this.update(InterfaceBoard.this.getGraphics());
                            }
                        }
                        if (IAtriche && IAVoleuse) { //Dans ce cas l'ia peut rejouer la tricheuse
                            int result = randInt(0, 10);
                            if (result >= 7) {

                                System.out.println("Oups! Vous avez perdu un pion!  ");

                                ArrayList<int[]> billes = board.getBilles(1);
                                board.set(billes.get(0)[0], billes.get(0)[1], Board.NOIR);
                                InterfaceBoard.this.update(InterfaceBoard.this.getGraphics());

                            } else {
                                System.out.println(result + "/10! Je ne te volerai pas un pion !");
                            }

                        }
                        board.joueurCourant = 1;
                        Coup += 1;
                        info.setLabelCoup(Coup);
                    });
                    IAThread.start();

                } else if (mode == 1) {
                    JOptionPane.showMessageDialog(this, "Fin de partie");
                } else if (board.getBilles(1).size() > 8 && board.getBilles(2).size() > 8 && mode == 0) {
                    if (board.joueurCourant == 1)
                        board.joueurCourant = players[1].joueur;
                    else {
                        board.joueurCourant = players[0].joueur;
                        Coup += 1;
                        info.setLabelCoup(Coup);
                    }

                } else if (mode == 0)
                    JOptionPane.showMessageDialog(this, "Fin de partie");
            }
        } else {
            selection.add(point); //Il connait les points en XY
        }
    }

    /**
     * Génération du mouvement pour le joueur en fonction des billes
     *
     * @return le mouvement du joueur
     */
    private int[] mouvementFinal() {

        int minx = 9;
        int miny = 9;

        //Pour la taille de la sélection
        for (int[] aSelection : selection) {
            if (aSelection[0] < minx) {    //On récupère l'emplacement de la première bille en x et y.
                minx = aSelection[0];
                miny = aSelection[1];
            }
            if (aSelection[1] < miny) {
                miny = aSelection[1];
            }
        }
        int ox = 0;        //Initialisation
        int oy = 0;
        int[] move;
        int[] dir = {selectDir[0] - minx, selectDir[1] - miny};    //Obtention de la direction du nouveau click
        for (int[] d : board.directions) {    //Pour l'ensemble des directions de la board
            int[] nextpoint = {minx + d[0], miny + d[1]};
            for (int[] bille : selection) {    //Pour l'ensemble des points de la sélection
                if (selectDir[0] - bille[0] == d[0] && selectDir[1] - bille[1] == d[1]) {    //Vérification si la direction est correcte pour l'ensemble de la selection
                    dir = d;
                }
                if (bille[0] == nextpoint[0] && bille[1] == nextpoint[1]) {    //Dans le cas des bords
                    ox = d[0];
                    oy = d[1];
                }
            }
        }

        if (dir[0] == ox && dir[1] == oy || dir[0] == -ox && dir[1] == -oy) {
            for (int[] bille : selection) {
                if ((bille[0] == minx - dir[0] * (selection.size() - 1)    //dans le cas des bordures
                        && (bille[1] == miny - dir[1] * (selection.size() - 1)))) {
                    minx = bille[0];
                    miny = bille[1];
                }
            }
            //Mise en place du mouvement avec le point minimum et la direction.
            move = new int[4];
            move[0] = minx;
            move[1] = miny;
            move[2] = dir[0];
            move[3] = dir[1];
        } else {
            //Mise en place du mouvement avec le point minimum, l'origine et la direction ainsi que la taille
            move = new int[7];
            move[0] = minx;
            move[1] = miny;
            move[2] = ox;
            move[3] = oy;
            move[4] = dir[0];
            move[5] = dir[1];
            move[6] = selection.size();
        }
        selection.clear();


        return move;

    }

    /**
     * Affichage des billes
     */
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        g.setColor(new Color(111, 37, 2));

        Polygon background = new Polygon();
        for (int i = 0; i < 6; i++) {
            background.addPoint((int) (210 + 200 * Math.cos(i * 2 * Math.PI / 6) + 25), (int) (170 + 200 * Math.sin(i * 2 * Math.PI / 6) + 25));
        }
        g.drawPolygon(background);

        g.fillPolygon(background);
        g.setColor(Color.WHITE);
        Color color;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        for (int y = 0; y < 9; y++) {
            int cx = (y % 2) * (20) + Math.abs((y - 4) / 2) * 40 + 20;
            int cy = 35 + 37 * y;
            for (int x = 0; x < 9; x++) {
                if (board.get(x, y) != Board.INVALIDE) {


                    color = new Color(255, 255, 255); //Couleur par défaut
                    switch (board.get(x, y)) {
                        case Board.BLANC:
                            if (selection.size() > 0)
                                for (int[] aSelection : selection) {
                                    if ((x == aSelection[0]) && (y == aSelection[1])) {
                                        color = new Color(255, 252, 103);
                                        break;
                                    } else
                                        color = new Color(255, 255, 255);
                                }
                            else
                                color = new Color(255, 255, 255);
                            break;
                        case Board.NOIR:
                            if (selection.size() > 0)
                                for (int[] aSelection : selection) {
                                    if ((x == aSelection[0]) && (y == aSelection[1])) {
                                        color = new Color(77, 77, 78);
                                        break;
                                    } else
                                        color = new Color(1, 1, 1);
                                }
                            else
                                color = new Color(1, 1, 1);
                            break;
                        default:
                            color = new Color(173, 105, 74);
                            break;
                    }
                    cx += 40;
                    g2.setColor(color);
                    Shape cercle = new Ellipse2D.Float(cx, cy, 25, 25);        //forme ovale des cercles
                    //g2.fillOval( cx, cy, 25, 25);	/
                    g2.fill(cercle);

                }
            }
        }
    }

    /**
     * Redémarrage du plateau
     */
    void onRestart() {
        main.setVisible(false);
        main.dispose();
        this.dispatchEvent(new WindowEvent(new Main(), WindowEvent.WINDOW_OPENED));
    }

    private static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}

