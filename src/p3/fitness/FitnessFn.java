/* FitnessFn.java
 * A abstract class for fitness functions
 */

package p3.fitness;

import p3.Solution;

public abstract class FitnessFn {
		
	// Return fitnessEvalCount variable, incremented each time getFitness is called
	public abstract int getEvalCount();
	
	// Return the fitness as a double for a given solution
	public abstract double getFitness(Solution s);
	
	// Return the highest possible fitness for this Fitness Function
	public abstract double getOptimalFitness();
	
	// Return the minimum possible fitness fot this Fitness Function
	public abstract double getMinimumFitness();
	
	// Return a stating the name and parameters of the Fitness Function
	public abstract String toString();
	
}