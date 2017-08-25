/* ConcatenatedTraps.java
 * Fitness function where solution scores based on a number of consecutive trap functions
 */

package p3.fitness;

import java.util.BitSet;

import p3.Solution;

public class ConcatenatedTraps extends FitnessFn {

	private int solSize, trapCount, trapSize;
	private int fitnessEvalCount = 0;

	public ConcatenatedTraps(int sSize, int nTraps) throws IllegalArgumentException {
		if (sSize % nTraps != 0) {
			String errorString = "Solution size must be divisible by the trap size";
			throw new IllegalArgumentException(errorString);
		} else {
			solSize = sSize;
			trapCount = nTraps;
			trapSize = solSize / trapCount;
		}
	}

	public double getFitness(Solution solution) {
		BitSet genotype = solution.genotype();
		double fitness = 0.0;
		for (int i = 0; i < solSize; i += trapSize) {
			BitSet partial = genotype.get(i, i + trapSize);
			double pCardinality = partial.cardinality();

			if (pCardinality == trapSize) {
				fitness += pCardinality;
			} else {
				fitness += (trapSize - 1 - pCardinality);
			}

		}
		fitnessEvalCount++;
		return fitness;
	}

	public double getOptimalFitness() {
		return solSize;
	}

	public double getMinimumFitness() {
		return 0.0;
	}

	public int getEvalCount() {
		return fitnessEvalCount;
	}
	
	public String toString() {
		return ("Concatenated Traps\nSolution Size: "+solSize+"\nNumber of Traps: "+trapCount);
	}

}