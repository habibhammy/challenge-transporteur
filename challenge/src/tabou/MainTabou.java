package tabou;

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

import java.util.ArrayList;
import java.util.List;

import gproblem.GSupplyLinkProblem;
import gsolution.GJobVariable;
import gsolution.GSupplyLinkSolution;
import gsolver.GSolver;
import gsolver.MySolver;

/**
 * 
 * @author Olivier Grunder
 * @version 0.03
 * @date 22 avril 2011
 *
 */
public class MainTabou {

	public static void main(String args[]) {

		GSupplyLinkProblem pb = new GSupplyLinkProblem("data/instance200f.txt") ;
		//System.out.println("PROBLEM="+pb.toString()+"\n") ;

		// New solver
		/*GSolver solv = new GSolver(pb, sol2) ;
		solv.start() ;	*/


		//MySolverTabou solv = new MySolverTabou(pb);
		//Temps pour un fichier taille 200
		//solv.setSolvingTime(10000);
		//solv.start();
		//solv.initSimple();
		
		 List<String> instances = new ArrayList<String>();
		 setUpInstances(instances);

		 for(int i=0;i<instances.size();i++){
			 
		        
		         MySolverTabou solv =new MySolverTabou(new GSupplyLinkProblem("data/" + instances.get(i)));
		         solv.start();
		         while(solv.isAlive()){
		        	 
		         }
		     
		         
		   
		 }
	}

	private static void setUpInstances(List<String> instances) {
		instances.add("instance005.txt");
		instances.add("instance007.txt");
		instances.add("instance050a.txt");
		instances.add("instance050b.txt");
		instances.add("instance050c.txt");
		instances.add("instance050d.txt");
		instances.add("instance050e.txt");
		instances.add("instance050f.txt");
		instances.add("instance050g.txt");
		instances.add("instance050h.txt");
		instances.add("instance050i.txt");
		instances.add("instance050j.txt");
		instances.add("instance050k.txt");
		instances.add("instance050l.txt");
		instances.add("instance050m.txt");
		instances.add("instance050n.txt");
		instances.add("instance050o.txt");
		instances.add("instance050p.txt");
		instances.add("instance050q.txt");
		instances.add("instance050r.txt");
		instances.add("instance050s.txt");
		instances.add("instance050t.txt");
		instances.add("instance100a.txt");
		instances.add("instance100b.txt");
		instances.add("instance100c.txt");
		instances.add("instance100d.txt");
		instances.add("instance100e.txt");
		instances.add("instance100f.txt");
		instances.add("instance100g.txt");
		instances.add("instance100h.txt");
		instances.add("instance100i.txt");
		instances.add("instance100j.txt");
		instances.add("instance100k.txt");
		instances.add("instance100l.txt");
		instances.add("instance100m.txt");
		instances.add("instance100n.txt");
		instances.add("instance100o.txt");
		instances.add("instance100p.txt");
		instances.add("instance100q.txt");
		instances.add("instance100r.txt");
		instances.add("instance100s.txt");
		instances.add("instance100t.txt");
		instances.add("instance150a.txt");
		instances.add("instance150b.txt");
		instances.add("instance150c.txt");
		instances.add("instance150d.txt");
		instances.add("instance150e.txt");
		instances.add("instance150f.txt");
		instances.add("instance150g.txt");
		instances.add("instance150h.txt");
		instances.add("instance150i.txt");
		instances.add("instance150j.txt");
		instances.add("instance150k.txt");
		instances.add("instance150l.txt");
		instances.add("instance150m.txt");
		instances.add("instance150n.txt");
		instances.add("instance150o.txt");
		instances.add("instance150p.txt");
		instances.add("instance150q.txt");
		instances.add("instance150r.txt");
		instances.add("instance150s.txt");
		instances.add("instance150t.txt");
		instances.add("instance200a.txt");
		instances.add("instance200b.txt");
		instances.add("instance200c.txt");
		instances.add("instance200d.txt");
		instances.add("instance200e.txt");
		instances.add("instance200f.txt");
		instances.add("instance200g.txt");
		instances.add("instance200h.txt");
		instances.add("instance200i.txt");
		instances.add("instance200j.txt");
		instances.add("instance200k.txt");
		instances.add("instance200l.txt");
		instances.add("instance200m.txt");
		instances.add("instance200n.txt");
		instances.add("instance200o.txt");
		instances.add("instance200p.txt");
		instances.add("instance200q.txt");
		instances.add("instance200r.txt");
		instances.add("instance200s.txt");
		instances.add("instance200t.txt");
	}
}
