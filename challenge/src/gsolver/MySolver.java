
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
	 * 
	 */
	public void init(){
		boolean fin=false;
		int nombreJob=problem.getN()-1;
		int nbrBatch=1;
		GJobData[] tempo= new GJobData[nombreJob+1];
		GJobData lastDate=null;
		double sommeVolume=0;
		boolean debutBatch=true;
		double cout = problem.getTransporter(0).getDeliveryTime();
		int nbrbatch = 1;
		int indice[]=new int[problem.getN()];
		while(!fin){
			
			if(debutBatch){			
			tempo[nombreJob]=problem.getJobData()[nombreJob];
			lastDate = problem.getJobData()[nombreJob];
			sommeVolume=problem.getJobData()[nombreJob].getSize();
			debutBatch=false;
			}else{
				//Si la difference entre les deux date est inférieure au temps aller retour
				if (lastDate.getDueDate() - problem.getJobData()[nombreJob].getDueDate() < cout) {
					System.out.println("ajout");
					tempo[nombreJob] = problem.getJobData()[nombreJob];
					sommeVolume+=problem.getJobData()[nombreJob].getSize();
					
				}else{
					//Sinon
					// Si la somme des volume est inferieure a la capacité , création d'un batch
					if(sommeVolume <= problem.getTransporter(0).getCapacity()){
						// creation d'un batch
						System.out.println("capacity: "+problem.getTransporter(0).getCapacity());
						for(int k=0; k < problem.getN();k++){
							if(tempo[k]!=null){
								indice[k]=nbrbatch;
								tempo[k]=null;
							}
						}
						debutBatch=true;
						nombreJob++;
					}else{
						
					}
				}
			}
			
			if(nombreJob == 0){
				fin=true;
			}
			nombreJob--;
			
		}
		for (int i = 0; i < problem.getN(); i++) {
			System.out.println(tempo[i]+"\n");
		}

		for (int i = 0; i < problem.getN(); i++) {
			System.out.println("indice :"+indice[i]+"\n");
		}
	
		/*int nbrbatch = 1;
		GJobData lastDate = problem.getJobData()[problem.getN()-1];
		double cout = problem.getTransporter(0).getDeliveryTime();
		
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
						System.out.println("recherche vapacitÃ© optimal");
						// Parcours le tableau pour trouver une valeur qui optimise la capacitÃ©
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
										//crÃ©ation batch
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
					}
				}
			}
		}

		for (int i = 0; i < problem.getN(); i++) {
			System.out.println(tempo[i]+"\n");
		}
		for (int i = 0; i < problem.getN(); i++) {
			System.out.println("indice :"+indice[i]+"\n");
		}
	 */
}

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
	init();

	/*while (true) {

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
		}*/
}
}
