package testJohan;

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
public class MainTest {
	
	public static void main(String args[]) {
		
		GSupplyLinkProblem pb = new GSupplyLinkProblem("data/instance050b.txt") ;
		System.out.println("PROBLEM="+pb.toString()+"\n") ;

		// New solver
		/*GSolver solv = new GSolver(pb, sol2) ;
		solv.start() ;	*/
		

		MySolverTest solv = new MySolverTest(pb);
		//Temps pour un fichier taille 200
		solv.setSolvingTime(10000);
		solv.start();
		//solv.initMinimizeBatch2();
	}
}
/**
99;
99;
96;;3
96;
92;;7
92;;7
91;
88;;11
87;;13
86;;14
85;;10
76;;24
74;;25
72;;28
69;;30
67;;33
67;;33
62;;29
61;;30
56;44;
52;46;
51;49;
42;40;;17
40;28;27
;23;22;20;21;*/