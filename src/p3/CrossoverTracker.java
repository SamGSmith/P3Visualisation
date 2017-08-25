/* CrossoverTracker.java
 * P3 crossover does crossover for each cluster in the model's cluster list for each solution
 * in the population (randomised). In order to only perform one crossover at a time so the
 * user can see it, crossoverTracker is used to track which cluster and donor solution is 
 * currently being used.
 */

package p3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import p3.fitness.*;

public class CrossoverTracker {

	private Random shuffleRand;
	private ArrayList<int[]> clusterList;
	private ArrayList<Integer> shuffledPopIdx;
	private int currentClusterIndex, currentDonorIndex, lastClusterIndex;
	private int[] currentCluster;
	private Solution currentSolution, lastCurrentSolution, currentDonor, currentCross;
	private Population population;
	private boolean crossoverFinished;
	private FitnessFn ff;
	private double oldFitness, newFitness;

	// Constructor for a crossoverTracker for a specified current solution and population
	public CrossoverTracker(Solution currentSol, Population pop, long seed, FitnessFn ff) {
		this.population = pop;
		this.ff = ff;
		this.currentSolution = currentSol;
		this.lastCurrentSolution = currentSol;
		oldFitness = currentSolution.getFitness(ff);

		// Check the cluster model isn't empty before crossover
		clusterList = population.getClusterModel();
		crossoverFinished = (clusterList.size() == 0);
		currentClusterIndex = 0;

		shuffleRand = new Random(seed);
		shuffledPopIdx = shuffledOptions(population.size());
		currentDonorIndex = 0;
	}

	// Performs crossover once, for the current donor solution and cluster
	public void crossover(int creationStep) {
		// Used to find the cluster used after crossover has been called
		lastClusterIndex = currentClusterIndex;
		currentCluster = clusterList.get(currentClusterIndex);
		
		// Copy current solution here, so getCurrentSolution gets the currentSolution
		// before crossover (as it may have changed)
		lastCurrentSolution = currentSolution.getCopy();
		
		
		currentDonor = population.getSolution(shuffledPopIdx.get(currentDonorIndex));
		/* Unused as causes outofboundsexception when stepping backwards then forwards
		// Check if donor equals current solution, if so skip crossover
		if (currentSolution.equals(currentDonor)) {
			currentDonorIndex++;
			if (currentDonorIndex == population.size()) {
				nextCluster();
			} else {
				currentDonor = population.getSolution(shuffledPopIdx.get(currentDonorIndex));				
			}
		}
		*/
		
		// Copy current solution to currentCross before performing crossover
		currentCross = currentSolution.getCopy();
		currentCross.setCreationStep(creationStep);

		// Flip bits in currentCross based on cluster indices
		for (int bitIndex : currentCluster) {
			currentCross.set(bitIndex, currentDonor.get(bitIndex));
		}

		// Increment currentDonorIndex to get next donor solution on next crossover call
		currentDonorIndex++;
		
		// Boolean to track if next cluster should be called (as it should only be called once)
		boolean callNextCluster = false;
		
		// If solutions in pop have been checked for current cluster, get next cluster
		if (currentDonorIndex == population.size()) {
			callNextCluster = true;
		}

		if (!currentSolution.equals(currentCross)) {
			newFitness = currentCross.getFitness(ff);
			
			//if (newFitness > oldFitness) {
			//	crossoverFinished = true;
			//} else if (newFitness==oldFitness) {
			if (newFitness >= oldFitness) {
				// If equal fitness solution found continue from next cluster with currentCross
				currentSolution = currentCross.getCopy();
				oldFitness = newFitness;
				callNextCluster = true;
			} else if (currentClusterIndex > clusterList.size()-1) {
				// If checked for all clusters and no improvement, return currentSolution
				currentCross = currentSolution.getCopy();
				currentCross.setCreationStep(creationStep);
				crossoverFinished = true;
			}
		}
		
		if (callNextCluster) {
			nextCluster();
		}
	}
	
	private void nextCluster() {
		currentClusterIndex++;
		// Get a new shuffled donor index
		currentDonorIndex = 0;
		shuffledPopIdx = shuffledOptions(population.size());

		// If all clusters have been checked, crossover finished
		if (currentClusterIndex > clusterList.size()-1) {
			crossoverFinished = true;
		}
	}

	public boolean crossoverFinished() {
		return crossoverFinished;
	}

	public int[] getCurrentCluster() {
		return currentCluster;
	}

	public Solution getCurrentDonor() {
		return currentDonor;
	}

	public Solution getCurrentCross() {
		return currentCross;
	}

	public Solution getCurrentSolution() {
		return lastCurrentSolution;
	}

	public ArrayList<int[]> getClusterList() {
		return clusterList;
	}

	public int getClusterIndex() {
		return lastClusterIndex;
	}

	private ArrayList<Integer> shuffledOptions(int size) {
		ArrayList<Integer> options = new ArrayList<Integer>(size);
		for (int i = 0; i < size; i++) {
			options.add(i);
		}

		Collections.shuffle(options, shuffleRand);
		return options;
	}
}
