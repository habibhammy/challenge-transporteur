package gsolver;

import gproblem.GJobData;
import gproblem.GSupplyLinkProblem;
import gsolution.GSupplyLinkSolution;

public class MySolver extends GSolver {

	public MySolver() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MySolver(GSupplyLinkProblem problem, GSupplyLinkSolution bestSolution) {
		super(problem, bestSolution);
		// TODO Auto-generated constructor stub
	}

	public MySolver(GSupplyLinkProblem problem) {
		super(problem);
		// TODO Auto-generated constructor stub
	}

	/**
	 * solves the problem
	 */
	protected void solve() {
		int iteration = 0 ;

		// Generate a new random solution
		GSupplyLinkSolution sol = new GSupplyLinkSolution(problem) ;

		// To generate a random number, use the "rand" attribute which is inherited from GSolver
		/*int nbrbatch = (int) (rand.nextDouble()*problem.getN())+1 ;
		sol.setNbrBatch(nbrbatch);
		for (int i=0;i<problem.getN();++i) {
			int batch = (int) (rand.nextDouble()*nbrbatch)+1 ;
			sol.getProcessingSchedule()[i].setBatchIndice(batch) ;
			sol.getProcessingSchedule()[i].setDeliveryCompletionTime((double)batch) ;
		}*/

		int nbrbatch = 1;
		GJobData lastDate = problem.getJobData()[problem.getN()-1];
		double cout = problem.getTransporter(0).getDeliveryTime();
		GJobData[] tempo = new GJobData[problem.getN()];
		double sommeVolume=0;
		int indice[]=new int[problem.getN()];
		boolean debutBatch=true;
		for (int i = problem.getN()-1; i >= 0; --i) {
			System.out.println("tour: "+i);
			if (debutBatch) {
				tempo[i] = problem.getJobData()[i];
				lastDate = problem.getJobData()[i];
				System.out.println("lastdate "+lastDate);
				System.out.println("test "+tempo[i]);
				sommeVolume=problem.getJobData()[i].getSize();
				debutBatch=false;
			} else {
				if (lastDate.getDueDate() - problem.getJobData()[i].getDueDate() < cout) {
					tempo[i] = problem.getJobData()[i];
					sommeVolume+=problem.getJobData()[i].getSize();
					
				} else {
					System.out.println("i: "+i+" sommeV "+sommeVolume);
					if(sommeVolume <= problem.getTransporter(0).getCapacity()){
						// creation d'un batch
						System.out.println("capacity: "+problem.getTransporter(0).getCapacity());
						for(int k=0; k < problem.getN();k++){
							if(tempo[k]!=null){

								indice[k]=nbrbatch;
								tempo[k]=null;
							}
						}
						nbrbatch++;
						i++;
						debutBatch=true;
					}else{
						System.out.println("recherche vapacité optimal");
						// Parcours le tableau pour trouver une valeur qui optimise la capacité
						boolean debut=true;
						for (int j = problem.getN()-1; j > 0; --j) {
							if(tempo[j]!= null && debut){
								sommeVolume=problem.getJobData()[j].getSize();
								debut=false;
								System.out.println("SommeV"+sommeVolume);
							}else{
								//if(problem.getJobData()[j] != null){
								System.out.println("Ajotu 2eme batch");
									if( sommeVolume + problem.getJobData()[j].getSize()==problem.getTransporter(0).getCapacity() ){
										//création batch
										indice[j]=nbrbatch;
										tempo[j]=null;
										nbrbatch++;
										i++;
										
									}
								//}
							}
						}
					}
					/*for (int j = problem.getN()-1; j > 0; --j) {

						//for ()
					}*/
				}
			}
		}

		for (int i = 0; i < problem.getN(); i++) {
			System.out.println(tempo[i]+"\n");
		}
		for (int i = 0; i < problem.getN(); i++) {
			System.out.println("indice :"+indice[i]+"\n");
		}
		/*while (true) {

			// Evaluation of the newly built solution 
			double eval = sol.evaluate() ;

			if (sol.getEvaluation()>=0) {
				if (bestSolution==null || sol.getEvaluation()<bestSolution.getEvaluation()) { 
					bestSolution = sol ;

					// Nouveau message à destination du log (ecran+fichier)
					log.println ("Iteration="+iteration) ;
					log.println ("New Best Solution = "+sol.getEvaluation()+"\n") ;
				}
			}
			iteration ++  ;
		}*/
	}
}
