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
package gsolution;


/**
 * Variables for each job
 * 
 * @author Olivier Grunder
 * @version 0.03
 * @date 11 mai 2011
 *
 */
public class GJobVariable {
	/**
	 * Batch indice of the job : first batch is indice 1
	 */
	private int batchIndice ; 
	
	/**
	 * Completion (or Delivery) time of the job
	 */
	private double deliveryCompletionTime ;
	
	/**
	 * @param batchNumber
	 * @param deliveryCompletionTime
	 */
	public GJobVariable(int batchIndice,
			double deliveryCompletionTime) {
		this.batchIndice = batchIndice;
		this.deliveryCompletionTime = deliveryCompletionTime;
	}

	public GJobVariable() {	}

	public GJobVariable(GJobVariable gJobVariable) {
		this.batchIndice = gJobVariable.batchIndice;
		this.deliveryCompletionTime = gJobVariable.deliveryCompletionTime;
	}

	/**
	 * @return the batchNumber
	 */
	public int getBatchIndice() {
		return batchIndice;
	}

	/**
	 * @param batchNumber the batchNumber to set
	 */
	public void setBatchIndice(int batchNumber) {
		this.batchIndice = batchNumber;
	}

	/**
	 * @return the deliveryCompletionTime
	 */
	public double getDeliveryCompletionTime() {
		return deliveryCompletionTime;
	}

	/**
	 * @param deliveryCompletionTime the deliveryCompletionTime to set
	 */
	public void setDeliveryCompletionTime(double deliveryCompletionTime) {
		this.deliveryCompletionTime = deliveryCompletionTime;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GJobVariables [batchNumber=" + batchIndice
				+ ", deliveryCompletionTime=" + deliveryCompletionTime + "]";
	}
	
	
}
