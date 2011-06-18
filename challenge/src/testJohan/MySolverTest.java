package testJohan;
import java.util.ArrayList;
import java.util.List;

import gproblem.GJobData;
import gproblem.GSupplyLinkProblem;
import gsolution.GSupplyLinkSolution;
import gsolver.GSolver;

public class MySolverTest extends GSolver {
	/** Variable à créer **/
	//Liste de candidats ( liste de voisins pour une itération )
	private ArrayList<GSupplyLinkSolution> listeCandidats = new ArrayList<GSupplyLinkSolution>();
	private GSupplyLinkSolution meilleurCandidats;
	//Liste Tabou	
	private ArrayList<Tabou> lisetTabou = new ArrayList<Tabou>();
	//Nombre d'iteration
	private int iteration = 0 ;
	//Nombre d'iteration sans amélioration de la fonction objectives
	//Meilleur solution trouvé	

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
	public GSupplyLinkSolution init(){
		GSupplyLinkSolution sol = new GSupplyLinkSolution(problem) ;

		int nbrbatch = (int) (rand.nextDouble()*problem.getN())+1 ;
		sol.setNbrBatch(nbrbatch);
		for (int i=0;i<problem.getN();++i) {
			int batch = 1;
			//int batch = (int) (rand.nextDouble()*nbrbatch)+1 ;
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
		// Configuration initiale
		GSupplyLinkSolution sol = init();
		
		while (true) {
			//Creation de la liste de voisin de la derniere solution
			creerListeCandidats(sol);
			//
			
			//MeilleurVoisins parmit les candidats
			sol=getMeilleurCandidats();
			
			// Evaluation of the newly built solution 
			double eval = sol.evaluate() ;

			if (sol.getEvaluation()>=0) {
				if (bestSolution==null || sol.getEvaluation()<bestSolution.getEvaluation()) { 
					bestSolution = sol ;

					// Nouveau message Ã  destination du log (ecran+fichier)
					log.println ("Iteration="+iteration) ;
					log.println ("New Best Solution = "+sol.getEvaluation()+"\n") ;
				}
			}
			iteration ++  ;
		}
	}
	/**
	 * Creation de la liste des voisins de sol
	 * @param sol
	 */
	public void creerListeCandidats(GSupplyLinkSolution sol){
		//Creation de la liste
		
		//fin creation liste
		//Evaluation du meilleur voisins de cette liste
		this.meilleurCandidats=evalListeCandidats();
	}

		public GSupplyLinkSolution evalListeCandidats(){
			GSupplyLinkSolution mSol=null;
			return mSol;
		}
	public GSupplyLinkSolution getMeilleurCandidats() {
		return meilleurCandidats;
	}
	
}
