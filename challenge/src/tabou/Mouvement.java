package tabou;
/**
* Classe représentant un mouvement de l'algorithme
 */
public class Mouvement {

	private int indiceProcessingSchedule;
	private int indiceBatch;
	public Mouvement(int indiceP,int indiceB){
		this.indiceProcessingSchedule=indiceP;
		this.indiceBatch=indiceB;
	}
	public int getIndiceProcessingSchedule() {
		return indiceProcessingSchedule;
	}
	public int getIndiceBatch() {
		return indiceBatch;
	}
	
	
}
