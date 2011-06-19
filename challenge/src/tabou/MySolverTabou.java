package tabou;
import java.util.ArrayList;
import java.util.List;

import gproblem.GJobData;
import gproblem.GSupplyLinkProblem;
import gsolution.GJobVariable;
import gsolution.GSupplyLinkSolution;
import gsolver.GSolver;

public class MySolverTabou extends GSolver {
	/** Variable à créer **/
	//Meilleur voisins
	private GSupplyLinkSolution meilleurCandidats;
	//Liste Tabou	
	private ArrayList<MouvementTabou> listeTabou = new ArrayList<MouvementTabou>();
	//liste de voisins
	private ArrayList<GSupplyLinkSolution> listeVoisin = new ArrayList<GSupplyLinkSolution>();
	//Nombre d'iteration
	private int iteration = 0 ;
	//Nombre d'iteration sans amélioration de la fonction objectives
	private int iterationSansAmelio=0;
	//Meilleur solution trouvé	

	//Durée taboue
	private int duréeTaboue;
	//Critere d'aspiration :
	/**Un mécanisme d’aspiration détermine un critère selon lequel un mouvement,
bien que tabou, peut quand même être accepté. Il faut faire attention,
cependant, au risque d’introduire à nouveau des cycles dans la recherche.
• Par exemple, un critère d’aspiration rudimentaire peut consister à accepter un
mouvement s’il conduit à une configuration meilleure que la meilleure
configuration déjà trouvée. Des mécanismes plus sophistiqués peuvent être
introduits.
	 **/
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




	/**
	 * Fonction qui essaye de trouver une solution  initial qui minimize le nombre de batch
	 * Ce qui permet par la suite d'avoir a chercher juste en augmentant le nombre de batch
	 * @return
	 */

	public GSupplyLinkSolution initMinimizeBatch2(){
		GSupplyLinkSolution sol = new GSupplyLinkSolution(problem);
		boolean fin=false;
		double sommeVolume=0;
		int nbrbatch = -1;
		boolean added=false;
		//Tableau  des valeur qu'il reste à ajouter
		double indice[]=new double[problem.getN()];
		// Tableau de la solution final xD
		double finaltab[]=new double[problem.getN()];
		//initialisation du tableau indice
		for(int i=0;i<problem.getN();++i){
			indice[i]=problem.getJobData()[i].getSize();

		}
		int nbBatchAjouté=0;

		// indice des job qui vont constitué le prochain batch
		ArrayList<Integer> indiceBatch = new ArrayList<Integer>();
		while(!fin){
			//Retourne l'index qui correspond au plus petit volume, si il n'y a plus de job a ajouté , retourn -1
			int index=indexMaxiSize(indice);

			if(index != -1){
				//	log.println("Index maxi"+index);
				//	log.println("Volume maxi:"+problem.getJobData()[index].getSize());
				sommeVolume+=problem.getJobData()[index].getSize();
				if(sommeVolume > problem.getTransporter(0).getCapacity()){
					sommeVolume-=problem.getJobData()[index].getSize();
					added=true;
				}else{
					//log.println("Somme : "+sommeVolume);
					indiceBatch.add(index);
					//	log.println("indicebach.add "+index);
					indice[index]=-1;
				}

				if(sommeVolume < problem.getTransporter(0).getCapacity()){
					for(int i=0;i<indice.length;i++){
						//log.println("Somme : "+sommeVolume);
						if(indice[i]!=-1){
							if( sommeVolume + problem.getJobData()[i].getSize()<=problem.getTransporter(0).getCapacity() ){
								sommeVolume+=problem.getJobData()[i].getSize();
								//log.println("Somme :" +i+ " "+(problem.getJobData()[i].getSize()));
								//log.println("indicebach.add "+i);
								indiceBatch.add(i);
								added=true;
								//break;
							}
						}
					}
				}else{
					//log.println("autre cas");
					//indiceBatch.remove(indiceBatch.size()-1);
					added=true;
				}
			}else{
				added=true;
			}
			int test=0;
			if(added){
				//log.println("AJOUT");
				while(!indiceBatch.isEmpty()){
					//	log.println("AJOUT "+indiceBatch.get(indiceBatch.size()-1));
					finaltab[indiceBatch.get(indiceBatch.size()-1)]=nbrbatch;
					sommeVolume=0;
					test+=problem.getJobData()[indiceBatch.get(indiceBatch.size()-1)].getSize();
					indice[indiceBatch.get(indiceBatch.size()-1)]=-1;
					indiceBatch.remove(indiceBatch.size()-1);
					nbBatchAjouté++;
				}
				//log.println("xD"+Math.abs(nbrbatch)+" "+test);
				test=0;
				//for (int i = 0; i < problem.getN(); i++) {
				//	log.println("fintab["+i+"]"+finaltab[i]+"\n");
				//}
				nbrbatch--;
				added=false;
			}
			if(nbBatchAjouté>=problem.getN()){
				fin=true;
			}
		}
		for (int i = 0; i < problem.getN(); i++) {

			finaltab[i]=finaltab[i];
			finaltab[i]=Math.abs(finaltab[i]);
			//	log.println("fintab["+i+"]"+finaltab[i]+"\n");
		}

		for (int i = 0; i < problem.getN(); i++) {

			sol.getProcessingSchedule()[i].setBatchIndice((int)finaltab[i]);
			//System.out.println("indice :"+indice[i]+"\n");
		}
		sol.setNbrBatch(Math.abs(nbrbatch)-1);
		sol.evaluate();
		log.println(sol.toString());
		return sol;
	}
	//Méthode qui retourne l'index de la valeur maximum du tableau différente de -1
	// si il n'y en a pas , retourne -1

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
	protected void solve() {

		// Création d'une solution initiale aléatoire valide
		GSupplyLinkSolution sol;
		// Creation d'une bonne solution initial qui minimize le nombre de batch
		sol=initMinimizeBatch2();

		int nbrBatch=sol.getNbrBatch();
		log.println("BATCH DE DEPART"+nbrBatch);
		//durée des mouvements tabous
		duréeTaboue=12;
		boolean estDejaTaboue=false;
		//Temps qu'il reste du temps
		long tempsSansAmelio=0;
		long tempsIteration=0;
		Mouvement mvtTabou;
		while (true) {
			//Creation de la liste de voisin de la derniere solution 

			mvtTabou=creerListeCandidats(sol);
			//*********************************MAJ liste tabou
			// Si la meilleur solution trouvé est deja dans la liste tabou
			for(int i =0;i <listeTabou.size();++i){
				if(listeTabou.get(i).compareTo(mvtTabou,duréeTaboue) == 0){
					estDejaTaboue=true;
				}
			}
			//on ajoute dans la liste que les mvt qui n'y sont pas deja
			if(!estDejaTaboue){
				listeTabou.add(new MouvementTabou(mvtTabou,duréeTaboue));
			}
			estDejaTaboue=false;
			//la solution initiale devient le clone du meilleur voisin
			sol=meilleurCandidats.clone();
			//log.println(sol.toString());


			// Evaluation of the newly built solution 
			//double eval = sol.evaluate() ;

			if (sol.getEvaluation()>=0) {
				if (bestSolution==null || sol.getEvaluation()<bestSolution.getEvaluation()) { 
					bestSolution = sol ;
					// Nouveau message Ã  destination du log (ecran+fichier)
					log.println ("Iteration="+iteration+"trouvé en "+this.getElapsedTimeString()) ;
					log.println ("New Best Solution = "+sol.getEvaluation()+"\n") ;
					log.println("nb batch:"+nbrBatch);
					iterationSansAmelio=0;
					tempsIteration+=tempsSansAmelio;
					tempsSansAmelio=0;
				}
			}
			iteration ++  ;
			iterationSansAmelio++;
			tempsSansAmelio=this.getElapsedTime()-tempsIteration;
			//log.println("Nombre iteration: "+iteration);
			//Si on dépasse un certain nombre d'iteration sans amelioration, on change de nombre de batch
			// ou un temps sans amelioration
			if (iterationSansAmelio > 250 || tempsSansAmelio > 500 ){
				nbrBatch++;
				log.println("nb batch ++ :"+nbrBatch+" ISA "+iterationSansAmelio+" tsa"+this.getTimeString(tempsSansAmelio));
				sol.setNbrBatch(nbrBatch);
				iterationSansAmelio=0;
				tempsIteration+=tempsSansAmelio;
				tempsSansAmelio=0;
			}
			// Reduction de la durée tabou des mvt tabou de 1
			//	log.println("\n LISTE TABOU :");
			for(int i =0;i <listeTabou.size();++i){

				//log.println(listeTabou.get(i).getSol().toString());
				if(listeTabou.get(i).reductionDuréeTabou()){
					listeTabou.remove(i);
				}
			}
			//log.println ("Iteration="+iteration) ;
			//log.println ("Solution = "+sol.getEvaluation()+"\n") ;
			//log.println (sol.toString()) ;
		}
	}
	/**
	 * Creation de la liste des voisins de sol
	 * @param sol
	 */
	public Mouvement creerListeCandidats(GSupplyLinkSolution sol){
		double meilleurVal=2000000000;
		double eval=0;
		boolean tabou=false;
		listeVoisin.clear();
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
		for(int j=0;j<400;j++){
			temp=sol.clone();
			batch= (int) (rand.nextDouble()*nbrBatch)+1 ;
			indiceSheldule= (int) (rand.nextDouble()*sol.getProcessingSchedule().length) ;
			testTabou=new Mouvement(indiceSheldule, batch);
			for(int i =0;i <listeTabou.size();++i){
				if(listeTabou.get(i).compareTo(testTabou) == 0){
					estTaboue=true;
					//log.println("EST TABOU !!!!!!!!!!!");
					temp.getProcessingSchedule()[indiceSheldule].setBatchIndice(batch) ;
					min=temp.getNbrBatch();
					for(int k=0;k<temp.getProcessingSchedule().length;k++){
						if (temp.getProcessingSchedule()[k].getBatchIndice() < min){
							min=temp.getProcessingSchedule()[k].getBatchIndice();
						}
					}
					//si min est différent de 1 , on corrige
					if (min > 1){
						//log.println("Correction : -"+(min-1)+" en nb batch");
						for(int k=0;k<temp.getProcessingSchedule().length;k++){

							temp.getProcessingSchedule()[k].setBatchIndice(temp.getProcessingSchedule()[k].getBatchIndice()-(min-1));

						}
						temp.setNbrBatch(temp.getNbrBatch()-(min-1));
					}
					temp.evaluate();
					//Critere d'aspiration
					if(temp.getEvaluation() > 0 && temp.getEvaluation() < bestSolution.getEvaluation()){
						log.println("ASPIRATIONNNNNNNNNNNNNNNNNNNNNNNNN");
						estTaboue=false;
					}

				}
			}
			min=temp.getNbrBatch();
			//Recherche de l'indice du batch mini

			if(!estTaboue){
				temp.getProcessingSchedule()[indiceSheldule].setBatchIndice(batch) ;
				for(int k=0;k<temp.getProcessingSchedule().length;k++){
					if (temp.getProcessingSchedule()[k].getBatchIndice() < min){
						min=temp.getProcessingSchedule()[k].getBatchIndice();
					}
				}
				//si min est différent de 1 , on corrige
				if (min > 1){
					//log.println("Correction : -"+(min-1)+" en nb batch");
					for(int k=0;k<temp.getProcessingSchedule().length;k++){

						temp.getProcessingSchedule()[k].setBatchIndice(temp.getProcessingSchedule()[k].getBatchIndice()-(min-1));

					}
					temp.setNbrBatch(temp.getNbrBatch()-(min-1));
				}
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

	public GSupplyLinkSolution evalListeCandidats(){
		GSupplyLinkSolution mSol=null;
		return mSol;
	}
	public GSupplyLinkSolution getMeilleurCandidats() {
		return meilleurCandidats;
	}

}
