/* HIFF.java
 * Hierarchical if and only if fitness function
 */

package p3.fitness;

import java.util.BitSet;

import p3.Solution;

public class HIFF extends FitnessFn {

	private int solutionSize, levels;
	private int fitnessEvalCount = 0;
	
	public HIFF(int sSize) throws IllegalArgumentException {
		// Check if solutionSize is a power of 2
		Double logsolutionSize = Math.log(sSize)/Math.log(2);
		// Check log of solution Size is a whole number
		if (logsolutionSize%1==0) {
			solutionSize = sSize;
			levels = logsolutionSize.intValue();
		}
		else {
			throw new IllegalArgumentException("Solution size must be a power of 2");
		}
	}
	
	public double getFitness(Solution solution) {
		BitSet genotype = solution.genotype();
		double fitness = solutionSize;
		
		int groupSize = 2;
		int groupCount = solutionSize/groupSize;
		for(int i=1; i<=levels; i++){
			 for (int k=0; k<solutionSize; k+=groupSize) {
				 BitSet group = genotype.get(k, (k+groupSize));
				 // Check if all bits are the same value
				 if (group.isEmpty()||(group.cardinality()==groupSize)) {
					 fitness+=groupSize;
				 }
			 }	
			 
			 groupSize=groupSize*2;
			 groupCount = groupCount/2;
		}
		
		fitnessEvalCount++;
		
		return fitness;
	}

	public double getOptimalFitness() {
		return solutionSize*(levels+1);
	}

	@Override
	public double getMinimumFitness() {
		return solutionSize;
	}

	public int getEvalCount() {
		return fitnessEvalCount;
	}
	
	public String toString() {
		return ("HIFF\nSolution Size: "+solutionSize);
	}
	
}