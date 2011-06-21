package tabou;


import java.util.ArrayList;
import gproblem.GSupplyLinkProblem;
import gsolution.GSupplyLinkSolution;
import gsolver.GSolver;

public class MySolverTabou extends GSolver {
	//Meilleur voisins
	private GSupplyLinkSolution meilleurCandidats;
	//Liste de mouvement tabou
	private ArrayList<MouvementTabou> listeTabou = new ArrayList<MouvementTabou>();
	//Nombre d'iteration
	private int iteration = 0 ;
	//Nombre d'iteration sans amélioration 
	private int iterationSansAmelio=0;
	//Durée taboue
	private int dureeTaboue;
	public MySolverTabou() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MySolverTabou(GSupplyLinkProblem problem, GSupplyLinkSolution bestSolution) {
		super(problem, bestSolution);
		// TODO Auto-generated constructor stub
	}

	public MySolverTabou(GSupplyLinkProblem problem) {
		super(problem);
		// TODO Auto-generated constructor stub
	}
	public MySolverTabou(GSupplyLinkProblem problem, int tps) {
		super(problem);
		this.setSolvingTime(tps);
		// TODO Auto-generated constructor stub
	}


	/**
	 * Fonction qui trouve une solution initiale qui minimise le nombre de batch
	 * @return la solution 
	 */

	public GSupplyLinkSolution initMinimizeBatch2(){
		GSupplyLinkSolution sol = new GSupplyLinkSolution(problem);
		boolean fin=false;
		double sommeVolume=0;
		int nbrbatch = -1;
		boolean added=false;
		//Tableau  des valeurs qu'il reste à ajouter
		double indice[]=new double[problem.getN()];
		// Tableau de la solution finale ><'
		double finaltab[]=new double[problem.getN()];
		//initialisation du tableau indice
		for(int i=0;i<problem.getN();++i){
			indice[i]=problem.getJobData()[i].getSize();
		}
		int nbBatchAjouté=0;
		// indice des jobs qui vont constituer le prochain batch
		ArrayList<Integer> indiceBatch = new ArrayList<Integer>();
		while(!fin){
			//Retourne l'index du plus petit volume, si il n'y a plus de job a ajouter , retourne -1
			int index=indexMaxiSize(indice);

			if(index != -1){
				sommeVolume+=problem.getJobData()[index].getSize();
				if(sommeVolume > problem.getTransporter(0).getCapacity()){
					sommeVolume-=problem.getJobData()[index].getSize();
					added=true;
				}else{
					indiceBatch.add(index);
					indice[index]=-1;
				}
				if(sommeVolume < problem.getTransporter(0).getCapacity()){
					for(int i=0;i<indice.length;i++){
						if(indice[i]!=-1){
							if( sommeVolume + problem.getJobData()[i].getSize()<=problem.getTransporter(0).getCapacity() ){
								if (i==indexMaxiSize(indice,sommeVolume)){
								sommeVolume+=problem.getJobData()[i].getSize();
								indiceBatch.add(i);
								indice[i]=-1;
								while(existeMini(indice,sommeVolume)){
									for(int j=0;j<indice.length;j++){
										if(indice[j]!=-1){
											if( sommeVolume + problem.getJobData()[j].getSize()<=problem.getTransporter(0).getCapacity() ){
												if (j==indexMaxiSize(indice,sommeVolume)){
													sommeVolume+=problem.getJobData()[j].getSize();
													indiceBatch.add(j);
													indice[j]=-1;
													added=true;
												}
											}
										}
									}
								}
								added=true;
								}
							}
						}
					}
				}else{
					added=true;
				}
			}else{
				added=true;
			}
			if(added){
				while(!indiceBatch.isEmpty()){
					finaltab[indiceBatch.get(indiceBatch.size()-1)]=nbrbatch;
					sommeVolume=0;
					indice[indiceBatch.get(indiceBatch.size()-1)]=-1;
					indiceBatch.remove(indiceBatch.size()-1);
					nbBatchAjouté++;
				}
				nbrbatch--;
				added=false;
			}
			if(nbBatchAjouté>=problem.getN()){
				fin=true;
			}
		}
		for (int i = 0; i < problem.getN(); i++) {
			finaltab[i]=Math.abs(finaltab[i]);
		}

		for (int i = 0; i < problem.getN(); i++) {
			sol.getProcessingSchedule()[i].setBatchIndice((int)finaltab[i]);
		}
		sol.setNbrBatch(Math.abs(nbrbatch)-1);
		sol.evaluate();
		return sol;
	}


	//Méthode qui retourne l'index de la valeur maximum du tableau différente de -1
	// si il n'y en a pas , retourne -1
	public boolean existeMini(double indice[],double sommeVolume){
		for(int i=0;i<problem.getN();++i){
			if(indice[i] != -1){
				if( (problem.getJobData()[i].getSize()) <= problem.getTransporter(0).getCapacity()-sommeVolume){
					return true;
				}
			}
		}
		return false;
	}
	
	//Retourne l'indice du job qui a une taille maximum dans le tableau indice
	public int indexMaxiSize(double indice[]){
		int index=-1;
		double maximum=0;
		for(int i=0;i<problem.getN();++i){
			if(indice[i] != -1){
				if( (problem.getJobData()[i].getSize()) >= maximum){
					index=i;
					maximum=indice[i];
				}
			}
		}
		return index;
	}
	//Retourne l'indice du job qui a une taille maximum < sommeVolume dans le tableau indice
	public int indexMaxiSize(double indice[],double sommeVolume){
		int index=-1;
		double maximum=0;
		for(int i=0;i<problem.getN();++i){
			if(indice[i] != -1){
				if( (problem.getJobData()[i].getSize()) >= maximum && (problem.getJobData()[i].getSize()) <= (problem.getTransporter(0).getCapacity()-sommeVolume)  ){
					index=i;
					maximum=indice[i];
				}
			}
		}
		return index;
	}
	protected void solve() {
		System.out.close();
		GSupplyLinkSolution sol;
		// Creation d'une solution initiale valide qui minimize le nombre de batch
		sol=initMinimizeBatch2();
		bestSolution=sol.clone();
		int nbrBatch=sol.getNbrBatch();
		if(problem.getN()>=50){
			dureeTaboue=10;
		}else{
			dureeTaboue=5;
		}
		// Mouvement est Deja dans la liste taboue = faux
		boolean estDejaTaboue=false;
		// Temps sans amelioration de la bestSolution
		long tempsSansAmelio=0;
		//Temps maximum sans amelioration
		long maxTpsSansAmelio=500;
		//Temps pour une itération
		long tempsIteration=0;
		// Mouvement tabou
		Mouvement mvtTabou;
		// boolean  oui si le batch à amelioré la bestSolution 
		boolean batchSansAmelio=false;
		int test=1;
		while (true) {
			//Creation de la liste de voisin de la derniere solution 
			mvtTabou=creerListeCandidats(sol);
			//*********************************MAJ liste tabou
			// test si le mouvement de la meilleur solution trouvée est deja dans la liste tabou
			for(int i =0;i <listeTabou.size();++i){
				if(listeTabou.get(i).compareTo(mvtTabou,dureeTaboue) == 0){
					estDejaTaboue=true;
				}
			}
			//on ajoute dans la liste que les mvt qui n'y sont pas deja
			if(!estDejaTaboue){
				listeTabou.add(new MouvementTabou(mvtTabou,dureeTaboue));
			}
			estDejaTaboue=false;
			//la solution initiale devient le clone du meilleur voisin
			sol=meilleurCandidats.clone();
			// Evaluation of the newly built solution 
			if (sol.getEvaluation()>=0) {
				if (bestSolution==null || sol.getEvaluation()<bestSolution.getEvaluation()) { 
					bestSolution = sol ;
					iterationSansAmelio=0;
					tempsIteration+=tempsSansAmelio;
					tempsSansAmelio=0;
					batchSansAmelio=false;
				}
			}
			iteration ++  ;
			iterationSansAmelio++;
			tempsSansAmelio=this.getElapsedTime()-tempsIteration;

			//Si on dépasse un certain nombre d'iteration ou un temps sans amelioration, on change de nombre de batch
			if (iterationSansAmelio > 250 || tempsSansAmelio > maxTpsSansAmelio ){			
				if(batchSansAmelio==true){
					maxTpsSansAmelio=4000;
					test=-1;
				}
				if(test==1){
					nbrBatch++;
				}
				batchSansAmelio=true;
				sol.setNbrBatch(nbrBatch);
				iterationSansAmelio=0;
				tempsIteration+=tempsSansAmelio;
				tempsSansAmelio=0;
			}
			// Reduction de la durée tabou des mvt tabou de 1
			for(int i =0;i <listeTabou.size();++i){
				if(listeTabou.get(i).reductionDuréeTabou()){
					listeTabou.remove(i);
				}
			}
		}
	}
	/**
	 * Met à jours la variable meilleur solution 
	 * retourne le mouvement tabou
	 * @param sol
	 */
	public Mouvement creerListeCandidats(GSupplyLinkSolution sol){
		double meilleurVal=2000000000;
		double eval=0;
		Mouvement mvtTabou=null;
		Mouvement testTabou=null;
		//Initialisation de temp au valeur de sol
		GSupplyLinkSolution temp=sol.clone();
		GSupplyLinkSolution best=sol.clone();
		int batch;
		int indiceSheldule;
		int nbrBatch=sol.getNbrBatch();
		boolean estTaboue=false;
		int min;
		//Creation des voisins
		for(int j=0;j<400;j++){
			temp=sol.clone();
			batch= (int) (rand.nextDouble()*nbrBatch)+1 ;
			indiceSheldule= (int) (rand.nextDouble()*sol.getProcessingSchedule().length) ;
			testTabou=new Mouvement(indiceSheldule, batch);
			for(int i =0;i <listeTabou.size();++i){
				//on test si le mouvement crée est tabou
				if(listeTabou.get(i).compareTo(testTabou) == 0){
					estTaboue=true;
					temp.getProcessingSchedule()[indiceSheldule].setBatchIndice(batch) ;
					min=temp.getNbrBatch();
					temp=optimiserSol(temp,min);
					temp.evaluate();
					//Critere d'aspiration
					// Si le voisin donné par le mouvement taboue a une meilleur evaluation que la BestSolution alors on l'accepte
					if(temp.getEvaluation() > 0 && temp.getEvaluation() < bestSolution.getEvaluation()){
						estTaboue=false;
					}
				}
			}
			min=temp.getNbrBatch();
			//Recherche de l'indice du batch mini

			if(!estTaboue){
				temp.getProcessingSchedule()[indiceSheldule].setBatchIndice(batch) ;
				temp=optimiserSol(temp,min);
				eval=temp.evaluate();
				if(eval != -1 && eval <= meilleurVal){
					meilleurVal=eval;
					best=temp.clone();
					mvtTabou=new Mouvement(indiceSheldule,batch);
				}	
			}
		}
		if(mvtTabou==null){
			mvtTabou=testTabou;
		}
		this.meilleurCandidats=best;
		return mvtTabou;
	}
	//Corrige la solution si le l'indice du batch minimum n'est pas 1
	public GSupplyLinkSolution optimiserSol(GSupplyLinkSolution temp,int min){
		//récupere l'indice minimum
		for(int k=0;k<temp.getProcessingSchedule().length;k++){
			if (temp.getProcessingSchedule()[k].getBatchIndice() < min){
				min=temp.getProcessingSchedule()[k].getBatchIndice();
			}
		}
		//si min est différent de 1 , on corrige
		if (min > 1){
			for(int k=0;k<temp.getProcessingSchedule().length;k++){
				temp.getProcessingSchedule()[k].setBatchIndice(temp.getProcessingSchedule()[k].getBatchIndice()-(min-1));
			}
			temp.setNbrBatch(temp.getNbrBatch()-(min-1));
		}
		return temp;
	}
	public GSupplyLinkSolution getMeilleurCandidats() {
		return meilleurCandidats;
	}

}
