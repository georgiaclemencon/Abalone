package abaloneBoardInterface;

import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Label;
import java.io.PrintStream;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import util.TextAreaOutputStream;

public class InterfaceInformation extends JPanel {


    private Label labelCalcul;
    private Label labelCoup;
    Label labelNoir;
	Label labelBlanc;
	JProgressBar progressBar = new JProgressBar();
	private InterfaceBoard gui;

	InterfaceInformation(InterfaceBoard gui,String modeJeu, String configJeu, String poids, String niveauDifficulté){
		
		this.gui = gui;

        int board = 8;
        if(niveauDifficulté.equals("")){    //Dans le cas joueur contre joueur
            board -=3;
        }

		GridLayout layout = new GridLayout(board, 2);
		this.setLayout(layout);

        Label labelMode = new Label("Mode : " + modeJeu);
        Label labelEmplacement = new Label("Emplacement : " + configJeu);
        labelCoup = new Label("Nombre de tours : 0");
        labelNoir = new Label("Joueur blanc : 14");
        labelBlanc = new Label("Joueur noir : 14");

        if(niveauDifficulté.equals("")){
            this.add(labelMode);
            this.add(new Label());
            this.add(labelEmplacement);
            this.add(new Label());
            this.add(labelCoup);
            this.add(new Label());
            this.add(labelBlanc);
            this.add(labelNoir);
        }else{

            Label labelDifficulté = new Label("Difficulté : " + niveauDifficulté);

            Label labelPoids = new Label("Poids : " + poids);



            labelCalcul = new Label("IA : Prête ! ");


            //0
            this.add(labelMode);
            this.add(new Label());
            //1
            this.add(labelDifficulté);
            this.add(new Label());
            //2
            this.add(labelEmplacement);
            this.add(new Label());
            //3
            this.add(labelPoids);
            this.add(new Label());
            //4
            this.add(labelCoup);
            this.add(new Label());
            //5
            this.add(labelBlanc);
            this.add(labelNoir);

            //6
            this.add(labelCalcul);
            this.add(progressBar);

        }


        JTextArea ta = new JTextArea();
        TextAreaOutputStream taos = new TextAreaOutputStream( ta, 3 );
        PrintStream ps = new PrintStream( taos );
        System.setOut( ps );
        //System.setErr( ps );

        this.add(ta);

        Button btnRecommencer = new Button("Recommencer");
        this.add(btnRecommencer);
        btnRecommencer.setActionCommand("click");
        btnRecommencer.addActionListener(e -> {
            if(e.getActionCommand().equals("click")){
                InterfaceInformation.this.gui.onRestart();
            }
        });


		
	}



    public void setGUI(InterfaceBoard gui2) {
		this.gui = gui2;
		
	}

    public void setLabelCoup(int i){
        labelCoup.setText("Nombre de tours : "+i);
    }

    public void setIA(String IA) {
        labelCalcul.setText(IA);
    }
}
