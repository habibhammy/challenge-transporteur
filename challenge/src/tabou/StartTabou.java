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


import gproblem.GSupplyLinkProblem;

public class StartTabou {
 public StartTabou(String file){
	 GSupplyLinkProblem pb = new GSupplyLinkProblem("data/"+file) ;
	 MySolverTabou solv =new MySolverTabou(pb);
	 solv.start();
 }
 public StartTabou(String file,int temps){
	 GSupplyLinkProblem pb = new GSupplyLinkProblem("data/"+file) ;
	 MySolverTabou solv =new MySolverTabou(pb,temps);
	 solv.start();
 }
}
