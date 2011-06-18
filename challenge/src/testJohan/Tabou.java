package testJohan;

import gsolution.GSupplyLinkSolution;

/**
 * Classe représentant la structure de la liste Taboue
 * @author johan
 *
 */
public class Tabou {

	//Durée tabou : nombre de tour
	private int duréeTaboue;
	// solution tabou
	private GSupplyLinkSolution sol; 
	/**
	 * Constructeur 
	 */
	public Tabou(GSupplyLinkSolution s,int t){
		this.sol=s;
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
	public GSupplyLinkSolution getSol() {
		return sol;
	}
	
}
