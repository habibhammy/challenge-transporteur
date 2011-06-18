package testJohan;
import java.util.ArrayList;
import java.util.List;

import gproblem.GJobData;
import gproblem.GSupplyLinkProblem;
import gsolution.GJobVariable;
import gsolution.GSupplyLinkSolution;
import gsolver.GSolver;

public class MySolverTest extends GSolver {
	/** Variable à créer **/
	//Meilleur voisins
	private GSupplyLinkSolution meilleurCandidats;
	//Liste Tabou	
	private ArrayList<Tabou> listeTabou = new ArrayList<Tabou>();
	//liste de voisins
	private ArrayList<GSupplyLinkSolution> listeVoisin = new ArrayList<GSupplyLinkSolution>();
	//Nombre d'iteration
	private int iteration = 0 ;
	//Nombre d'iteration sans amélioration de la fonction objectives
	private int irerationSansAmelio=0;
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
	public MySolverTest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MySolverTest(GSupplyLinkProblem problem, GSupplyLinkSolution bestSolution) {
		super(problem, bestSolution);
		// TODO Auto-generated constructor stub
	}

	public MySolverTest(GSupplyLinkProblem problem) {
		super(problem);
		// TODO Auto-generated constructor stub
	}

	/**
	 * solves the problem
	 */
	public GSupplyLinkSolution init(int nbrbatch){
		GSupplyLinkSolution sol = new GSupplyLinkSolution(problem) ;

		//int nbrbatch = (int) (rand.nextDouble()*problem.getN())+1 ;
		;
		sol.setNbrBatch(nbrbatch);
		for (int i=0;i<problem.getN();++i) {
			//	int batch = 1;
			int batch = (int) (rand.nextDouble()* nbrbatch)+1 ;
			sol.getProcessingSchedule()[i].setBatchIndice(batch) ;
			sol.getProcessingSchedule()[i].setDeliveryCompletionTime((double)batch) ;
		}
		return sol;
	}

	protected void solve() {
		/**
		 * Schéma de l’algorithme tabou de base
			• Engendrer une configuration initiale S0 ; S := S0
			• S* := S ; f* := f(S)
			• T := {} // liste taboue
			• Répéter
				– m := le meilleur mouvement parmi les mouvements non tabous et les
				mouvements tabous exceptionnels (critère d’aspiration)
				– S := S (+) m
				– Si f(S) < f(S*) faire S* := S ; f* := f(S)
				– Mettre T à jour ;
			• Jusqu’à <condition fin>
			• Retourner S*
		 */
		// Création d'une solution initiale aléatoire valide

		GSupplyLinkSolution sol;
		int nbrBatch=1;
		int compte=0;
		do {
			compte++;
			sol = init(nbrBatch);
			// si on a essayé de crée 100 sol initial qui ne marche pas , on increment le nb de batch
			if(compte > 100){
				compte=0;
				nbrBatch++;
			}
		}while(sol.evaluate() <0);
		
		duréeTaboue=15;
		boolean estDejaTaboue=false;
		int nbMvt=1;
		while (true) {
			//Creation de la liste de voisin de la derniere solution
			creerListeCandidats(sol,nbMvt);
			//MAJ liste tabou
			
			
			// Si la meilleur solution trouvé dans les voisins est TABOU , on augmente le nombre de mouvement
			for(int i =0;i <listeTabou.size();++i){
				if(listeTabou.get(i).getSol().toString().compareTo(meilleurCandidats.toString()) == 0){
					estDejaTaboue=true;
					//nbMvt=3;
				}
			}
			//on ajoute dans la liste que les mvt qui n'y sont pas deja
			if(!estDejaTaboue){
				listeTabou.add(new Tabou(meilleurCandidats,duréeTaboue));
				nbMvt=1;
			}
			estDejaTaboue=false;
			sol=meilleurCandidats.clone();
			
			

			// Evaluation of the newly built solution 
			//double eval = sol.evaluate() ;

			if (sol.getEvaluation()>=0) {
				if (bestSolution==null || sol.getEvaluation()<bestSolution.getEvaluation()) { 
					bestSolution = sol ;

					// Nouveau message Ã  destination du log (ecran+fichier)
					log.println ("Iteration="+iteration) ;
					log.println ("New Best Solution = "+sol.getEvaluation()+"\n") ;
					irerationSansAmelio=0;
				}
			}
			iteration ++  ;
			irerationSansAmelio++;
			
			//Si on dépasse un certain nombre d'iteration sans amelioration, on change de nombre de batch
			if (irerationSansAmelio > 300){
				nbrBatch++;
				sol.setNbrBatch(nbrBatch);
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
	public void creerListeCandidats(GSupplyLinkSolution sol,int nbMvtAutorisé){
		double meilleurVal=2000000000;
		double eval=0;
		boolean tabou=false;
		listeVoisin.clear();
		GSupplyLinkSolution taboue;
		//Initialisation de temp au valeur de sol
		GSupplyLinkSolution temp=sol.clone();
		GSupplyLinkSolution best=sol.clone();
		
		//nb d'evaluation a -1
		int compte=0;
		//Creation de la liste
	//	System.out.println(sol.toString());
		// Boucle de génération  et test des voisins
		//log.println(sol.toString());
		for (int i=0;i<problem.getN();i++) {
			for (int j=1;j<sol.getNbrBatch();++j){
				temp.getProcessingSchedule()[i].setBatchIndice( ((sol.getProcessingSchedule()[i].getBatchIndice()+j)%(sol.getNbrBatch()))) ;
				if(temp.getProcessingSchedule()[i].getBatchIndice()==0){
					temp.getProcessingSchedule()[i].setBatchIndice(sol.getNbrBatch());
				}
				if(nbMvtAutorisé > 1){
					for(int k=1;k<nbMvtAutorisé;k++){
						//Si plusieur mvt sont autorisé , on choisi un batch aleatoirement et on l'incremente
						int batch = (int) (rand.nextDouble()*sol.getNbrBatch()+1);

						temp.getProcessingSchedule()[batch].setBatchIndice( (sol.getProcessingSchedule()[batch].getBatchIndice()+j)%(sol.getNbrBatch()) ) ;	
						if(temp.getProcessingSchedule()[batch].getBatchIndice()==0){
							temp.getProcessingSchedule()[batch].setBatchIndice(sol.getNbrBatch());
						}
					}
				}
				//Ajout d'un voisin a la liste
				
			//	log.println(temp.toString());
				listeVoisin.add(temp);
				eval=temp.evaluate();
			
					//log.println(temp.toString());
			
				//log.println("eval:"+eval);
				if(eval != -1 && eval <= meilleurVal){
					//test si la valeur est tabou
					for(int k=0;k<listeTabou.size();k++){
						if (temp.toString().compareTo((listeTabou.get(k)).getSol().toString()) == 0){
							tabou=true;
							compte++;
						}
					}
					if(!tabou){
						meilleurVal=eval;
						best=temp.clone();
						
					}
					tabou=false;
				}
				if(eval== -1){
					compte++;
					if (compte==problem.getN()*(sol.getNbrBatch()-1)){
						//Si il n'y a pas de meilleur solution trouvé , on affecte aleatoirement une solution voisine
						//Probleme : temps important si bcp de voisin ...
						//log.println("BEST:\n"+best.toString());
					
						int r = (int) (rand.nextDouble()*(listeVoisin.size()));
						
						best=listeVoisin.get(r);
					}
				}
				temp=sol.clone();
			}
			
		}
		//log.println("\n\n\nMeilleur voisin:"+best.toString());
		//fin creation liste
		//Evaluation du meilleur voisins de cette liste
		//log.println("COmpte"+compte);
		this.meilleurCandidats=best;
	}

	public GSupplyLinkSolution evalListeCandidats(){
		GSupplyLinkSolution mSol=null;
		return mSol;
	}
	public GSupplyLinkSolution getMeilleurCandidats() {
		return meilleurCandidats;
	}

}
