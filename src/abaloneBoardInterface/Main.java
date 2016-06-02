package abaloneBoardInterface;

import java.awt.*;
import javax.swing.*;


public class Main extends JFrame {

	private int[] poids;
	private int modeJeu;
	private int niveauDifficulté;
	private String[] modes = {"Joueur contre Joueur","Joueur contre Machine","Machine contre Machine"};
	private String[] config = {"Par défaut", "Marguerite belge (compétition)",
			"Marguerite allemande", "Alien", "Domination", "Infiltration"
			, "The Wall", "Snakes", "Face à face"};
	private String[] configHeuristique = {"Equilibre","Attaque","Defense"};

	//On maximise la valeur d'attaque
	private int[] poidsAttaque = {1000000,	//Le résultat de la partie
			100,	//Le nombre de billes
			1000,	//Nombre de groupe (plus il y a de groupement, plus c'est safe pour le joueur
			-1000,	//Nombre de groupement énnemie
			1000,	//Attaque possible
			-200, //Attaque possible pour l'ennemie
			300,	//Emplacement de pions
			-300};	//Emplacement dangeureux de pions

	//On maximise la valeur de défense
	private int[] poidsDefense = {1000000,	//Le résultat de la partie
			100,	//Le nombre de billes
			2000,	//Nombre de groupe (plus il y a de groupement, plus c'est safe pour le joueur
			-2000,	//Nombre de groupement énnemie
			100,	//Attaque possible
			-100, //Attaque possible pour l'ennemie
			2000,	//Emplacement de pions
			-2000};	//Emplacement dangeureux de pions

	//Tentative de jeu équilibré
	private int[] poidsEquilibre = {1000000,	//Le résultat de la partie
			100,	//Le nombre de billes
			2000,	//Nombre de groupe (plus il y a de groupement, plus c'est safe pour le joueur
			-2000,	//Nombre de groupement énnemie
			2000,	//Attaque possible
			-2000, //Attaque possible pour l'ennemie
			2000,	//Emplacement de pions
			-2000};	//Emplacement dangeureux de pions

	private int configJeu;
	private final ImageIcon icon = new ImageIcon("/Users/nicolassalleron/Documents/workspace/Abalone/src/icon.png");
	private String[] nivDifficulté;
	private int niveauHeuristique;

	@SuppressWarnings({"deprecation", "MagicConstant"})
	Main(){

		this.setTitle("Abalone par Nicolas SALLERON et Muzinga TSHILOMBO");
		this.setSize(1000, 400);

		initConfiguration();


		GridLayout layout = new GridLayout(1, 2);
		this.setLayout(layout);
		InterfaceInformation info;
		//Démarrage de l'interface
		if(modeJeu == 1 || modeJeu == 2)
			 info = new InterfaceInformation(null,modes[modeJeu], config[configJeu], configHeuristique[niveauHeuristique], nivDifficulté[niveauDifficulté]);
		else
			info = new InterfaceInformation(null,"Joueur contre joueur", config[configJeu], "", "");

		InterfaceBoard gui = new InterfaceBoard(modeJeu, configJeu, poids, niveauDifficulté, info, this);
		info.setGUI(gui);
		gui.setBorder(BorderFactory.createTitledBorder("Abalone"));
        info.setBorder(BorderFactory.createTitledBorder("Informations"));
		this.add(gui,BorderLayout.CENTER);
		this.add(info,BorderLayout.EAST);
		this.show();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	/**
	 * Initialisation de la configuration
	 */
	private void initConfiguration() {

		//Configuration du jeu
		configJeu = ask(config, "Emplacement de départ ?", "Emplacement");
		System.out.println(""+modeJeu);

		//mode 2 = Machine contre Machine
		//mode 1 = Jouur contre Machine

		//On demande le mode de jeu
		modeJeu = JOptionPane.showOptionDialog(this, "Quel mode de jeu ?", "Mode de jeu",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				icon, modes, modes[1]); //default button title

		if(modeJeu == 1 || modeJeu == 2){	//Dans le cas ou c'est joueur contre machine
			 nivDifficulté = new String[]{"Niveau 0 (Random)",
					 "Niveau 1 (MinMax niveau 4 avec random)",
					 "Niveau 2 (MinMax niveau 2)",
					 "Niveau 2.5 (MinMax niveau 2 puis 4)",
					 "Niveau 3 (AlphaBeta profondeur 2)",
					 "Niveau 4 (NégaAlphaBeta profondeur 4)",
					 "Niveau 5 (IA Tricheuse, avec NégaAlphaBeta profondeur 4)",
					 "Niveau 6 (IA Tricheuse & voleuse, avec NégaAlphaBeta profondeur 4)"};

			niveauDifficulté = ask(nivDifficulté, "Votre niveau de difficulté?","Niveau de difficulté");

			niveauHeuristique = ask(configHeuristique, "L'orientation des poids de l'IA ?", "Poids de l'IA");
			if(niveauHeuristique == 0)
				poids = poidsEquilibre;
			else if(niveauHeuristique == 1)
				poids = poidsAttaque;
			else if (niveauHeuristique == 2)
				poids = poidsDefense;


		}else if (modeJeu == 2){
			niveauDifficulté = 10;	// Dans le cas ou c'est Humain contre humain
		}
	}


	/**
	 * Demande à l'utilisateur et renvoi la valeur
	 */
	private int ask(String[] niv, String message, String title) {

		String result = (String) JOptionPane.showInputDialog(this,
				message,
				title,
				JOptionPane.QUESTION_MESSAGE, 
				icon,
				niv, 
				niv[0]);
		int niveau = 0;
		for(int i = 0;i<niv.length;i++){
			if(niv[i].equals(result)){
				niveau = i;
				break;
			}
		}
		return niveau;
	}


	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(() -> new Main().setVisible(true));
	}
}
