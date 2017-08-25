/* OneMax.java
 * An implementation of the basic fitness function One Max, where fitness = number of 1s
 * in solution
 */

package p3.fitness;

import p3.Solution;

public class OneMax extends FitnessFn {

	private int solutionSize;
	private int fitnessEvalCount = 0;
	
	public OneMax(int sSize) {
		solutionSize = sSize;
	}
	
	public double getFitness(Solution solution) {
		fitnessEvalCount++;
		return solution.genotype().cardinality();
	}
	
	public double getOptimalFitness() {
		return solutionSize;
	}

	@Override
	public double getMinimumFitness() {
		return 0;
	}

	public int getEvalCount() {
		return fitnessEvalCount;
	}
	
	@Override
	public String toString() {
		return ("OneMax\nSolution Size: "+solutionSize);
	}
	
}