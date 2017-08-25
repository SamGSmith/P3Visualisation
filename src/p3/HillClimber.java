/* HillClimber.java
 * A random first ascent Hill Climber. Also has method for returning the fitness
 * for each attempted solution.
 */

package p3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import p3.fitness.FitnessFn;

public class HillClimber {
	
	private Solution initSol, finalSol;
	private FitnessFn ff;
	private ArrayList<Double> fitnessList = new ArrayList<Double>();
	private Random rand;
	
	public HillClimber(Solution initSol, FitnessFn ff, long seed) {
		this.initSol = initSol;
		this.ff = ff;
		rand = new Random(seed);
		fitnessList.add(initSol.getFitness(ff));
		climb();
	}
	
	// Get the result of the hill climb
	public Solution getLocalOptimum() {
		return finalSol;		
	}
	
	public Solution getInitialSol() {
		return initSol;
	}
	
	// Returns an array of the fitness value after each bit flip
	public ArrayList<Double> getFitnessProgression() {
		return fitnessList;
	}
	
	private void climb() {
		int solutionSize = initSol.size();
		ArrayList<Integer> shuffledOpts = shuffledOptions(solutionSize);
		
		finalSol = initSol.getCopy();

		// Keep trying bit flips until all bits have been tried.
		while(!shuffledOpts.isEmpty()) {
			double oldFitness = finalSol.getFitness(ff);
			finalSol.flip(shuffledOpts.get(0));
			fitnessList.add(finalSol.getFitness(ff));
			
			if (finalSol.getFitness(ff)>oldFitness) {
				// If improvement is found, refill shuffledOpts.
				shuffledOpts = shuffledOptions(solutionSize);	
			}
			else {
				finalSol.flip(shuffledOpts.get(0));
				shuffledOpts.remove(0);
			}
		}
	}
	
	// Return a shuffled list of consecutive integers from 0 to size-1.
	private ArrayList<Integer> shuffledOptions(int size){
		ArrayList<Integer> options = new ArrayList<Integer>(size);
		for(int i=0; i<size; i++) {
			options.add(i);
		}
		
		Collections.shuffle(options, rand);
		return options;
	}
	
}
