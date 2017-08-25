/* JumpK.java
 * Jump K fitness function
 */

package p3.fitness;

import p3.Solution;

public class JumpK extends FitnessFn {
	private int solutionSize, k;
	private int fitnessEvalCount = 0;
	
	public JumpK(int solutionSize, int k) {
		this.solutionSize = solutionSize;
		this.k = k;
	}
	
	@Override
	public double getFitness(Solution s) {
		int cardinality = s.genotype().cardinality();
		double fitness;
		
		if(cardinality<=(solutionSize-k)||cardinality==solutionSize) {
			fitness = (k+cardinality);
		}
		else {
			fitness = solutionSize-cardinality;
		}
		fitnessEvalCount++;
		return fitness;
	}

	@Override
	public double getOptimalFitness() {
		return k+solutionSize;
	}

	@Override
	public double getMinimumFitness() {
		return 0;
	}
	
	@Override
	public int getEvalCount() {
		return fitnessEvalCount;
	}
	
	@Override
	public String toString() {
		return ("JumpK\nSolution Size: "+solutionSize+"\nK value: "+k);
	}
	
	
}