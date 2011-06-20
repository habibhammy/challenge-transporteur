package tabou;


/**
 * Classe représentant la structure de la liste Taboue
 *
 */
public class MouvementTabou {

	//Durée tabou : nombre de tour
	private int duréeTaboue;

	//Mouvement Tabou
	private Mouvement mouv;
	
	/**
	 * Constructeur 
	 */
	public MouvementTabou(Mouvement m,int t){
		this.mouv=m;
		this.duréeTaboue=t;
	}
	/**
	 * Reduit la durée tabou, return vrai si la durée tabou est = 0
	 * @return
	 */
	public boolean reductionDuréeTabou(){
		duréeTaboue--;
		if (duréeTaboue==0){
			return true;
		}
		return false;
	}
	public Mouvement getMouv() {
		return mouv;
	}
	//test lors de l'ajout à la liste tabou
	public int compareTo(Mouvement mvtTabou,int duréeTaboue) {
		if(this.mouv.getIndiceBatch() == mvtTabou.getIndiceBatch() && this.mouv.getIndiceProcessingSchedule()==mvtTabou.getIndiceProcessingSchedule()){
			//Si le mouvement tabou existe deja , on met à jours la duréeTaboue du mouvement
			this.duréeTaboue=duréeTaboue;
			return 0;
		}
		return 1;
	}
	//Test lors de la creation du voisinage
	public int compareTo(Mouvement mvtTabou) {
		if(this.mouv.getIndiceBatch() == mvtTabou.getIndiceBatch() && this.mouv.getIndiceProcessingSchedule()==mvtTabou.getIndiceProcessingSchedule()){
			return 0;
		}
		return 1;
	}
	
}
