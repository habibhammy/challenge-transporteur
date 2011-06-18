package testJohan;
import gproblem.GJobData;
import gproblem.GSupplyLinkProblem;
import gsolution.GSupplyLinkSolution;
import gsolver.GSolver;

public class MySolverTest extends GSolver {
	/** Variable à créer **/
	//Liste de candidats ( liste de voisins pour une itération )
	//Liste Tabou	
	//Nombre d'iteration
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
	public void init(){

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
		int iteration = 0 ;

		GSupplyLinkSolution sol = new GSupplyLinkSolution(problem) ;


		init();


	}
}
