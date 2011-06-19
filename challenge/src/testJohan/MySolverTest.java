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
 * Fonction init qui donne une valeur valide pour un nombre de batch
 * @param nbrbatch
 * @return
 */
	@Deprecated
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
	/**
	 * Fonction init qui est sencé trouvé une valeur initial tres "bonne"
	 * Mais qui est beaucoup trop gourmande .... donc inutile
	 * @return
	 */
	@Deprecated
	public GSupplyLinkSolution init2(){
		GSupplyLinkSolution sol = new GSupplyLinkSolution(problem) ;
		boolean fin=false;
		int nombreJob=problem.getN()-1;
		GJobData[] tempo= new GJobData[nombreJob+1];
		GJobData lastDate=null;
		double sommeVolume=0;
		boolean debutBatch=true;
		double cout = problem.getTransporter(0).getDeliveryTime();
		int nbrbatch = 1;
		int indice[]=new int[problem.getN()];
		while(!fin){
			//System.out.println(nombreJob);
			if(debutBatch){			
				tempo[nombreJob]=problem.getJobData()[nombreJob];
				lastDate = problem.getJobData()[nombreJob];
				sommeVolume=problem.getJobData()[nombreJob].getSize();
				debutBatch=false;
			}else{
				//Si la difference entre les deux date est inférieure au temps aller retour
				if (lastDate.getDueDate() - problem.getJobData()[nombreJob].getDueDate() < cout) {
					tempo[nombreJob] = problem.getJobData()[nombreJob];
					sommeVolume+=problem.getJobData()[nombreJob].getSize();
					//System.out.println("volume"+sommeVolume);
					System.out.println("ICI");
				}else{
					//Sinon
					// Si la somme des volume est inferieure a la capacité , création d'un batch

					if(sommeVolume <= problem.getTransporter(0).getCapacity()){
						// creation d'un batch
						System.out.println("capacity: "+problem.getTransporter(0).getCapacity());
						for(int k=0; k < problem.getN();k++){
							if(tempo[k]!=null){
								System.out.println("ajout");
								indice[k]=nbrbatch;
								tempo[k]=null;
								sommeVolume=0;
								System.out.println("nb job"+nombreJob);
							}
						}
						nbrbatch++;
						debutBatch=true;
						nombreJob++;
					}else{

					}
				}

			}

			if(nombreJob == 0){
				for(int k= problem.getN()-1; k >0;k--){
					if(tempo[k]!=null){
						nombreJob=k;
						break;
					}
				}
				sommeVolume=0;
				//parcour de la liste des job 
				boolean matchFinded=false;
				ArrayList<Integer> test = new ArrayList<Integer>();
				while(!matchFinded && nombreJob >0){
					sommeVolume+=problem.getJobData()[nombreJob].getSize();
					test.add(nombreJob);
					for (int j = nombreJob-1; j >= 0; --j) {
						//if(tempo[j]!= null ){
						if( sommeVolume + problem.getJobData()[j].getSize()==problem.getTransporter(0).getCapacity() ){
							test.add(j);
							System.out.println("MATCH");
							while(!test.isEmpty()){

								indice[test.get(test.size()-1)]=nbrbatch;
								tempo[test.get(test.size()-1)]=null;
								System.out.println("tempo["+test.get(test.size()-1)+"]=null");
								sommeVolume=0;
								matchFinded=true;
								test.remove(test.size()-1);
							}
							nbrbatch++;
						}
					}
					nombreJob--;

				}
				nombreJob=0;
			}
			if(nombreJob == 0){
				fin=true;
			}
			nombreJob--;

		}
		for (int i = 0; i < problem.getN(); i++) {
			System.out.println("tempo["+i+"]"+tempo[i]+"\n");
		}
		System.out.println(nbrbatch);
		sol.setNbrBatch(nbrbatch);
		for (int i = 0; i < problem.getN(); i++) {
			if(indice[i]==0){
				indice[i]=nbrbatch;
			}
			System.out.println("indice :"+indice[i]+"\n");
			indice[i]=Math.abs(indice[i]-nbrbatch)+1;	
			sol.getProcessingSchedule()[i].setBatchIndice(indice[i]);
			//System.out.println("indice :"+indice[i]+"\n");
		}

		sol.evaluate();
		log.println(sol.toString());
		return sol;
	}
	/**
	 * Fonction qui essaye de trouver une solution  initial qui minimize le nombre de batch
	 * Ce qui permet par la suite d'avoir a chercher juste en augmentant le nombre de batch
	 * @return
	 */
	@Deprecated
	public GSupplyLinkSolution initMinimizeBatch(){
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
			int index=indexMinimumSize(indice);
			
			if(index != -1){
				//log.println("Index minimum"+index);
				//log.println("Volume mini:"+problem.getJobData()[index].getSize());
				sommeVolume+=problem.getJobData()[index].getSize();
				if(sommeVolume > problem.getTransporter(0).getCapacity()){
					sommeVolume-=problem.getJobData()[index].getSize();
					added=true;
				}else{
					//log.println("Somme : "+sommeVolume);
					indiceBatch.add(index);
					//log.println("indicebach.add "+index);
					indice[index]=-1;
				}

				if(sommeVolume < problem.getTransporter(0).getCapacity()){
					for(int i=0;i<indice.length;i++){
					//	log.println("Somme : "+sommeVolume);
						if(indice[i]!=-1){
							if( sommeVolume + problem.getJobData()[i].getSize()==problem.getTransporter(0).getCapacity() ){
							//	log.println("Somme :" +i+ " "+(problem.getJobData()[i].getSize()));
							//	log.println("indicebach.add "+i);
								indiceBatch.add(i);
								added=true;
								break;
							}
						}
					}
				}else{
				//	log.println("autre cas");
					//indiceBatch.remove(indiceBatch.size()-1);
					added=true;
				}
			}else{
				added=true;
			}
			int test=0;
			if(added){
			//	log.println("AJOUT");
				while(!indiceBatch.isEmpty()){
					//log.println("AJOUT "+indiceBatch.get(indiceBatch.size()-1));
					finaltab[indiceBatch.get(indiceBatch.size()-1)]=nbrbatch;
					sommeVolume=0;
					test+=problem.getJobData()[indiceBatch.get(indiceBatch.size()-1)].getSize();
					indice[indiceBatch.get(indiceBatch.size()-1)]=-1;
					indiceBatch.remove(indiceBatch.size()-1);
					nbBatchAjouté++;
				}
				log.println(""+Math.abs(nbrbatch)+" "+test);
				test=0;
				for (int i = 0; i < problem.getN(); i++) {
					//log.println("fintab["+i+"]"+finaltab[i]+"\n");
				}
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
			//log.println("fintab["+i+"]"+finaltab[i]+"\n");
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
	//Méthode qui retourne l'index de la valeur minimum du tableau différente de -1
	// si il n'y en a pas , retourne -1
	@Deprecated
	public int indexMinimumSize(double indice[]){
		int index=-1;
		double minimum=2000000000;
		for(int i=0;i<problem.getN();++i){
			if(indice[i] != -1){
				if( (problem.getJobData()[i].getSize()) <= minimum){
					index=i;
					minimum=indice[i];
				}
			}
		}

		return index;
	}
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
		/*int nbrBatch=1;
		int compte=0;
		do {
			compte++;
			sol = init(nbrBatch);
			// si on a essayé de crée 100 sol initial qui ne marche pas , on increment le nb de batch
			if(compte > 1000){
				compte=0;
				nbrBatch++;
			}
		}while(sol.evaluate() <0);*/
		// Creation d'une bonne solution initial qui minimize le nombre de batch
		sol=initMinimizeBatch2();
		log.println(this.getElapsedTimeString());
		int nbrBatch=sol.getNbrBatch();
		log.println("BATCH DE DEPART"+nbrBatch);
		//durée des mouvements tabous
		duréeTaboue=5;
		boolean estDejaTaboue=false;
		int nbMvt=1;
		//Temps qu'il reste du temps
		long tempsCalculVoisin=0;
		long avant;
		long moyenne;
		while (true) {
			//Creation de la liste de voisin de la derniere solution 
			
			//log.println("avant "+this.getElapsedTimeString());
			avant=this.getElapsedTime();
			creerListeCandidats(sol,nbMvt);
			//log.println("apres "+this.getElapsedTimeString());
			tempsCalculVoisin+=this.getElapsedTime()-avant;
			moyenne=tempsCalculVoisin/(iteration+1);
			log.println("Temps cumulé du calcul des voisins"+this.getTimeString(tempsCalculVoisin));
			log.println("Moyenne par recherche"+this.getTimeString(moyenne));
			//*********************************MAJ liste tabou
			// Si la meilleur solution trouvé est deja dans la liste tabou
			for(int i =0;i <listeTabou.size();++i){
				if(listeTabou.get(i).getSol().toString().compareTo(meilleurCandidats.toString()) == 0){
					estDejaTaboue=true;
				}
			}
			//on ajoute dans la liste que les mvt qui n'y sont pas deja
			if(!estDejaTaboue){
				listeTabou.add(new Tabou(meilleurCandidats,duréeTaboue));
				nbMvt=1;
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
					irerationSansAmelio=0;
				}
			}
			iteration ++  ;
			irerationSansAmelio++;

			//Si on dépasse un certain nombre d'iteration sans amelioration, on change de nombre de batch
			log.println(""+irerationSansAmelio);
			
			if (irerationSansAmelio > 30){
				/*if(nombreBatchnegatif > 5){
				nbrBatch--;
				}else{*/
					nbrBatch++;
			//	}
				//log.println("nb batch:"+nbrBatch);
				sol.setNbrBatch(nbrBatch);
				irerationSansAmelio=0;
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
		
		//Correction du Bug du nombre de batch qui ne commence pas à 1
		int min=best.getNbrBatch();
		//Recherche de l'indice du batch mini
		for(int i=0;i<best.getProcessingSchedule().length;i++){
			if (best.getProcessingSchedule()[i].getBatchIndice() < min){
				min=best.getProcessingSchedule()[i].getBatchIndice();
			}
		}
		//si min est différent de 1 , on corrige
		if (min > 1){
			log.println("Correction : -"+(min-1)+" en nb batch");
			for(int i=0;i<best.getProcessingSchedule().length;i++){
		
					best.getProcessingSchedule()[i].setBatchIndice(best.getProcessingSchedule()[i].getBatchIndice()-(min-1));
			
			}
			best.setNbrBatch(best.getNbrBatch()-(min-1));
		}
		
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
