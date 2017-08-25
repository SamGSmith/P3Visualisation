/* StepTracker.java
 * This class keeps track of steps, generations and the number of steps in a 
 * generation for a given run of p3. It also provides a unique seed for each step,
 * and can recall a previous instance of the pyramid when called, alongside next step
 * instruction, current population index, and current solution.
 */

package p3;

import java.util.*;

public class StepTracker {
	
	private int currentStep, currentGen, currentGenStep;
	private Random rand;
	private long currentStepSeed, initSeed;
	
	private ArrayList<Pyramid> pyramidHistory = new ArrayList<Pyramid>();
	private ArrayList<Integer> genList = new ArrayList<Integer>();
	private ArrayList<Integer> genStepList = new ArrayList<Integer>();
	
	public StepTracker(Pyramid pyramid) {
		initSeed = new Random().nextLong();
		rand = new Random(initSeed);
		currentStep = 0;
		currentGen = 0;
		currentGenStep = 0;
		currentStepSeed = rand.nextLong();

		pyramidHistory.add(pyramid.getDeepCopy());
		updateLists();
	}
	
	// Move StepTracker to next step
	public void nextStep() {
		currentStep++;
		currentGenStep++;
		currentStepSeed = rand.nextLong();
		
		updateLists();
	}
	
	// Move StepTracker to next generation
	public void nextGen(Pyramid pyramid) {
		currentStep++;
		currentGen++;
		currentGenStep = 0;
		currentStepSeed = rand.nextLong();
		
		// If pyramid hasn't been saved for this generation, add to pyramidHistory
		if (currentGen+1>pyramidHistory.size()) {
			pyramidHistory.add(pyramid.getDeepCopy());			
		}
		
		updateLists();
	}

	// Update the list tracking generation for each step and step within generation
	private void updateLists() {
		// If step hasn't been found yet, add to arraylists, else set at position
		if (currentStep+1>genStepList.size()) {
			genList.add(currentGen);
			genStepList.add(currentGenStep);
		} 
	}
	
	// Move StepTracker to previous generation
	public void previousGen() {
		if (currentStep>0) {
			do {
				currentStep--;
				currentGen = genList.get(currentStep);
				currentGenStep = genStepList.get(currentStep);
			} while (currentGenStep>0);
			
			rand = new Random(initSeed);
			for (int i=0; i<currentStep; i++) {
				rand.nextLong();
			}
			currentStepSeed = rand.nextLong();
		}
	}
	
	// Move to first step in StepTracker
	public void restart() {
		currentStep = 0;
		currentGen = 0;
		currentGenStep = 0;
		rand = new Random(initSeed);
		currentStepSeed = rand.nextLong();
	}

	public int getCurrentStep() {
		return currentStep;
	}
	
	public int getCurrentGenStep() {
		return currentGenStep;
	}
	
	public int getPreviousGenStep() {
		return genStepList.get(currentStep-1);
	}
	
	public int getCurrentGen() {
		return currentGen;
	}
	
	public long getSeed() {
		return currentStepSeed;
	}
	
	public Pyramid getPyramid() {
		return pyramidHistory.get(currentGen).getDeepCopy();
	}
	
}