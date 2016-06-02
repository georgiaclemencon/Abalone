package player;

public class Player {

    public int joueur;

	public Player(int joueur){
		this.joueur = joueur;
	}

	public static String getColor(int player){
		String s;
		switch(player){
		case 1:
            s= "Blanc";
			break;
		case 2:
            s= "Noir";
			break;
		default:
			s="plateau";
		}
		return s;
	}
}
