/**
 * 
 *     This file is part of ag41-print11-challenge.
 *     
 *     ag41-print11-challenge is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *     
 *     ag41-print11-challenge is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *     
 *     You should have received a copy of the GNU General Public License
 *     along with ag41-print11-challenge.  If not, see <http://www.gnu.org/licenses/>.
 *     
 */
package gproblem;

import java.util.Arrays;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import gio.FichierEntree;
import gio.FichierSortie;

/**
 * 
 * Optimisation problem for the challenge AG41 of spring 2011
 * 
 * @author Olivier Grunder
 * @version 0.03
 * @date 22 avril 2011
 *
 */
public class GSupplyLinkProblem {	

	public static final double MIN_INSTANCE_TRANSPORTER_CAPACITY = 50;
	public static final double MAX_INSTANCE_TRANSPORTER_CAPACITY = 200;
	public static final double MAX_INSTANCE_DELIVERY_COST = 2000;
	public static final double MIN_INSTANCE_DELIVERY_COST =  500;
	public static final double MAX_INSTANCE_DELIVERY_TIME =  200;
	public static final double MIN_INSTANCE_DELIVERY_TIME =   50;

	public static final double MAX_INSTANCE_JOB_SIZE = 50;
	public static final double MIN_INSTANCE_JOB_SIZE = 1;
	public static final double MAX_INSTANCE_DUE_DATE = 15000;
	public static final double MIN_INSTANCE_DUE_DATE = 10000;
	public static final double MAX_INSTANCE_HOLDING_COST = 2.0;
	public static final double MIN_INSTANCE_HOLDING_COST = 0.5;
	

	/**
	 * Number of jobs to deliver to the client
	 */
	protected int n ;

	/**
	 * data of the n jobs
	 */
	private GJobData jobData[] ;
	
	protected Vector<GSupplyLinkTransporter> transporters ;
	
	
	public GSupplyLinkProblem() {
		this.n = -1 ;
		this.jobData = null ;
		this.transporters = new Vector<GSupplyLinkTransporter> ();
	}

	public GSupplyLinkProblem(String string) {
		this.jobData = null ;
		this.transporters = new Vector<GSupplyLinkTransporter> ();
		load(string) ;
	}

	//-- Beginning of source automatic generation --
	/**
	 * @param n
	 * @param jobData
	 * @param transporter
	 */
	public GSupplyLinkProblem(int n, GJobData[] jobData,
			Vector<GSupplyLinkTransporter> transporter) {
		this.n = n;
		this.jobData = new GJobData[n] ; for (int i=0;i<n;i++) this.jobData[i] = new GJobData(jobData[i]) ;
		this.transporters = new Vector<GSupplyLinkTransporter> (transporter);
	}

	/**
	 * 
	 * @param problem
	 */
	public GSupplyLinkProblem(GSupplyLinkProblem problem) {
		n = problem.n ;
		jobData = new GJobData[n] ;
		for (int j=0;j<n;j++) {
			jobData[j] = new GJobData(problem.getJobData()[j]) ;
		}
		transporters = new Vector<GSupplyLinkTransporter>() ;
		for (int t=0;t<problem.transporters.size();t++) 
			transporters.add (problem.getTransporter(t)) ;
	}

	/**
	 * @return the n
	 */
	public int getN() {
		return n;
	}

	/**
	 * @param n the n to set
	 */
	public void setN(int n) {
		this.n = n;
		this.jobData = new GJobData[n] ; for (int i=0;i<n;i++) this.jobData[i] = new GJobData() ;
	}

	/**
	 * @return the jobData
	 */
	public GJobData[] getJobData() {
		return jobData;
	}

	/**
	 * @param jobData the jobData to set
	 */
	public void setJobData(GJobData[] jobData) {
		this.jobData = jobData;
	}

	/**
	 * @return the transporter
	 */
	public Vector<GSupplyLinkTransporter> getTransporters() {
		return transporters;
	}

	/**
	 * @param transporter the transporter to set
	 */
	public void setTransporters(Vector<GSupplyLinkTransporter> transporter) {
		this.transporters = transporter;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GSupplyLinkProblem [n=" + n + ", transp=" + transporters + "\n" + 
		"jobData=" + Arrays.toString(jobData) + "]";
	}

	//-- End of source automatic generation --
	
	public void setCustomerHoldingCost(int j, double hc) {
		jobData[j].setHoldingCost(hc) ;
	}
	
	public void addTransporter(GSupplyLinkTransporter t) {
		transporters.add(t) ;		
	}

	public void setDueDate(int i, double dd) {
		jobData[i].setDueDate(dd) ;
	}

	public void setVolume(int i, double vol) {
		jobData[i].setSize(vol) ;
	}

	/**
	 * @return the transporter
	 */
	public GSupplyLinkTransporter getTransporter(int i) {
		return transporters.get(i);
	}


	/**
	 * Open a problem description file
	 * 
	 * @param problemFilename name of the problem description file
	 * 
	 */
	public int load(String problemFilename) {
		if (problemFilename==null) return 1 ;

		FichierEntree fe = new FichierEntree (problemFilename) ;

		GSupplyLinkTransporter transporter=null ;
		int encore = 1 ;
		while (encore == 1) {
			
			String s = fe.lireLigne() ;
			if (s==null) {
				break ;
			}

			StringTokenizer st = new StringTokenizer(s, new String(":")) ;
			String sg = st.nextToken().trim() ;

			if (sg.compareToIgnoreCase("NBR_PRODUCT")==0) setN( Integer.parseInt(st.nextToken().trim()) ) ;
			if ( (sg.compareToIgnoreCase("CUSTOMER_HOLDING_COST")==0) ||
					             (sg.compareToIgnoreCase("CUSTOMER_HOLDING_COSTS")==0) ) {
				StringTokenizer st2 = new StringTokenizer(st.nextToken().trim(), new String(";")) ;
				for (int j=0;j<getN();j++) {
					setCustomerHoldingCost (j, (new Double(st2.nextToken())).doubleValue() ) ;
				}
			}

			if (sg.compareToIgnoreCase("TRANSPORTER")==0) {
				// Either the transporter ref is null, so we have to create it
				// or the rel is not null, and thus it is a precedent transporter
				// we have to store.
				transporter = new GSupplyLinkTransporter() ;
				addTransporter(transporter) ;
			}

			if (sg.compareToIgnoreCase("TRANSPORTER_CAPACITY")==0 ) { 
				if (transporter==null) { transporter = new GSupplyLinkTransporter() ; addTransporter(transporter) ; }
				transporter.setCapacity(Double.parseDouble(st.nextToken().trim()) )  ;
			}

			if (sg.compareToIgnoreCase("TRANSPORTER_DELIVERY_TIMES")==0 ) { 
				if (transporter==null) { transporter = new GSupplyLinkTransporter() ; addTransporter(transporter) ; }
				transporter.setDeliveryTime(Double.parseDouble(st.nextToken().trim()) )  ;
			}

			if (sg.compareToIgnoreCase("TRANSPORTER_DELIVERY_COSTS")==0 ) { 
				if (transporter==null) { transporter = new GSupplyLinkTransporter() ; addTransporter(transporter) ; }
				transporter.setDeliveryCost(Double.parseDouble(st.nextToken().trim()) )  ;
			}

			if (sg.compareToIgnoreCase("DUE_DATES")==0 ) {
				StringTokenizer st2 = new StringTokenizer(st.nextToken().trim(), new String(";")) ;
				for (int i=0;i<getN();i++)
					setDueDate (i, (new Double(st2.nextToken())).doubleValue()) ;
			}

			if (sg.compareToIgnoreCase("VOLUMES")==0 ) {
				StringTokenizer st2 = new StringTokenizer(st.nextToken().trim(), new String(";")) ;
				for (int i=0;i<getN();i++)
					setVolume (i, (new Double(st2.nextToken())).doubleValue()) ;
			}

			if (sg.compareToIgnoreCase("EOF")==0 ) {
				encore=0 ;
			}

		}

		fe.fermer() ;

		return 0 ;

	}

	/**
	 * Save a problem description file
	 * 
	 * @param problemFilename name of the problem description file
	 * 
	 */
	public int save(String problemFilename) {
	
//		NBR_PRODUCT:7
//		CUSTOMER_HOLDING_COSTS:1;0.5;1;0.5;1;0.5;1
//		TRANSPORTER:
//		TRANSPORTER_CAPACITY:6.0
//		TRANSPORTER_DELIVERY_TIMES:50.0
//		TRANSPORTER_DELIVERY_COSTS:50.0
//		DUE_DATES:1000.0;1010.0;1030.0;1030.0;1045.0;1100.0;1120.0;
//		VOLUMES:3.0;1.0;5.0;2.0;1.0;3.0;2.0;
//		EOF
		
		FichierSortie fs = new FichierSortie(problemFilename) ;
		fs.ecrire("NBR_PRODUCT:"+getN()+"\n") ;
		
		fs.ecrire("CUSTOMER_HOLDING_COSTS:") ;
		for (int i=0;i<getN();i++) fs.ecrire(getJobData()[i].getHoldingCost()+";") ;
		fs.ecrire("\n") ;
		
		fs.ecrire("TRANSPORTER:\n") ;
		
		fs.ecrire("TRANSPORTER_CAPACITY:"+getTransporter(0).getCapacity()+"\n") ;
		
		fs.ecrire("TRANSPORTER_DELIVERY_TIMES:"+getTransporter(0).getDeliveryTime()+"\n") ;
		
		fs.ecrire("TRANSPORTER_DELIVERY_COSTS:"+getTransporter(0).getDeliveryCost()+"\n") ;
		
		fs.ecrire("DUE_DATES:") ;
		for (int i=0;i<getN();i++) fs.ecrire(getJobData()[i].getDueDate()+";") ;
		fs.ecrire("\n") ;

		fs.ecrire("VOLUMES:") ;
		for (int i=0;i<getN();i++) fs.ecrire(getJobData()[i].getSize()+";") ;
		fs.ecrire("\n") ;

		fs.ecrire("EOF\n") ;
		fs.fermer() ;
		
		return 0 ;
	}

	/**
	 * Generate a random problem
	 * 
	 * @param n size of the problem
	 * 
	 */
	static public GSupplyLinkProblem generateRandom (int n, Random rand) {
		GJobData[] jobdata_ = new GJobData[n] ;
		
		Vector<GSupplyLinkTransporter> transporters_ = new Vector<GSupplyLinkTransporter>() ;
		transporters_.add(GSupplyLinkTransporter.generateRandom(rand)) ;
		
		
		for (int i=0;i<n;i++) {
			jobdata_[i] = GJobData.generateRandom(rand) ;
		}

		return new GSupplyLinkProblem(n, jobdata_, transporters_) ;
	}

	/**
	 * Generate a random problem
	 * 
	 * @param n size of the problem
	 * 
	 */
	static public GSupplyLinkProblem generateRandom (int n) {
		return generateRandom(n, new Random()) ;
	}

}
