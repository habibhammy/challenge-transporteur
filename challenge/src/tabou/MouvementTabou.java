package tabou;

import gsolution.GSupplyLinkSolution;

/**
 * Classe représentant la structure de la liste Taboue
 * @author johan
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
	 * Reduit la durée tabou, return vrai si la durée tabou est = à0
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
	public int compareTo(Mouvement mvtTabou,int duréeTaboue) {
		// TODO Auto-generated method stub
		if(this.mouv.getIndiceBatch() == mvtTabou.getIndiceBatch() && this.mouv.getIndiceProcessingSchedule()==mvtTabou.getIndiceProcessingSchedule()){
			this.duréeTaboue=duréeTaboue;
			return 0;
		}
		return 1;
	}
	public int compareTo(Mouvement mvtTabou) {
		// TODO Auto-generated method stub
		if(this.mouv.getIndiceBatch() == mvtTabou.getIndiceBatch() && this.mouv.getIndiceProcessingSchedule()==mvtTabou.getIndiceProcessingSchedule()){
			return 0;
		}
		return 1;
	}
	
}
