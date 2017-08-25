/* p3.java
 * The main body of the p3 algorithm, with methods for running, stepping forwards and
 * backwards through in a stepwise and generation-wise manner. 
 */

package p3;

import java.util.*;

import p3.fitness.*;

public class P3 {

	private int solutionSize, popIndex;
	private FitnessFn ff;
	private Pyramid pyramid;
	private Solution currentSolution;
	private Population currentPop;
	private String nextStep, currentStepString;
	private StepTracker stepTracker;
	private long thisStepSeed;
	private HillClimber hillClimber;
	private CrossoverTracker crossoverTracker, nextCrossoverTracker;

	//Set up variables needed to start a run of P3
	public void setupP3(int solutionSize, FitnessFn ff) {
		this.solutionSize = solutionSize;
		this.ff = ff;
		pyramid = new Pyramid(solutionSize, ff);
		setCurrentSolution(null);
		nextStep = "New Random Solution";
		popIndex = 0;
		stepTracker = new StepTracker(pyramid);
	}

	public int getStepCount() {
		return stepTracker.getCurrentStep();
	}
	
	public int getGenCount() {
		return stepTracker.getCurrentGen();
	}
	
	public int getSolCount() {
		return pyramid.getSolCount();
	}

	public FitnessFn getFF() {
		return ff;
	}

	public int getSolutionSize() {
		return solutionSize;
	}

	public int getCurrentPopIndex() {
		return popIndex;
	}

	public String getNextStep() {
		return nextStep;
	}
	
	public String getCurrentStep() {
		return currentStepString;
	}

	public Pyramid getPyramid() {
		return pyramid;
	}

	public Solution getCurrentSolution() {
		return currentSolution;
	}

	public Solution getFittestSolution() {
		return pyramid.getFittestSolution();
	}

	public double getPyramidMaxFitness() {
		return pyramid.getMaxFitness();
	}
	
	public ArrayList<Double> getHillClimbProgression() {
		return hillClimber.getFitnessProgression();
	}
	
	public int getEvalCount() {
		return pyramid.getEvalCount();
	}
	
	public CrossoverTracker getCrossoverTracker() {
		return crossoverTracker;
	}

	// Run P3 algorithm until optimal fitness is found, returning that optimal solution
	public Solution runP3() {
		do {
			oneStep();
		} while (getPyramidMaxFitness() < ff.getOptimalFitness());
		return currentSolution;
	}

	// Do the next step in the p3 algorithm
	public void oneStep() {
		currentStepString = nextStep;
		// Get new random seed for this step
		thisStepSeed = stepTracker.getSeed();
		
		// Depending on stage of the algorithm, call different private methods
		switch (nextStep) {
		case "New Random Solution":
			setCurrentSolution(new Solution(solutionSize, thisStepSeed));
			nextStep = "Local Search";
			pyramid.increaseEvalCount(1);
			stepTracker.nextStep();
			break;
		case "Local Search":
			hillClimb();
			break;
		case "Clustering":
			clustering();
			break;
		case "Crossover":
			doCrossover();
			break;
		}

	}

	// Perform Local Search on the current solution
	private void hillClimb() {
		hillClimber = new HillClimber(currentSolution, ff, thisStepSeed);
		setCurrentSolution(hillClimber.getLocalOptimum());

		if (!pyramid.contains(currentSolution)) {
			// Add the current solution to the first population
			pyramid.addSolution(currentSolution, 0, thisStepSeed);
		}
		currentPop = pyramid.getPopulation(0);
		nextStep = "Clustering";
		//Increase eval count for each new tried solution
		pyramid.increaseEvalCount(hillClimber.getFitnessProgression().size()-1);
		stepTracker.nextStep();
	}

	private void clustering() {
		currentPop = pyramid.getPopulation(popIndex);
		
		nextStep = "Crossover";	
		//Setup crossoverTracker to track crossover for this population
		nextCrossoverTracker = new CrossoverTracker(currentSolution, currentPop, thisStepSeed, ff);
		stepTracker.nextStep();			
		
	}
	
	// Perform a step of crossover
	private void doCrossover() {
		crossoverTracker = nextCrossoverTracker;
		if (popIndex < pyramid.getPopCount()) {
			currentPop = pyramid.getPopulation(popIndex);
			double currentSolutionFitness = currentSolution.getFitness(ff);

			Solution crossoverSolution = currentSolution.getCopy();
			double crossoverSolutionFitness = currentSolutionFitness;
			if(!crossoverTracker.crossoverFinished()){
				crossoverTracker.crossover(stepTracker.getCurrentStep());
				
				// Taken from current solution as current solution is set when crossover finds an improvement				
				crossoverSolution = crossoverTracker.getCurrentSolution();
				crossoverSolutionFitness = crossoverSolution.getFitness(ff);
				
				if (!pyramid.contains(crossoverSolution)) {
					pyramid.increaseEvalCount(1);
				}
			}
			
			//If crossover finished for current population, prepare for clustering/crossover on next population
			if (crossoverTracker.crossoverFinished()) {
				setCurrentSolution(crossoverSolution);

				popIndex++;
				
				if (crossoverSolutionFitness > currentSolutionFitness) {
					if (!pyramid.contains(crossoverSolution)) {
						if (popIndex == pyramid.getPopCount()) {
							pyramid.addSolutionToNewPopulation(crossoverSolution, thisStepSeed);
						} else {
							pyramid.addSolution(crossoverSolution, popIndex, thisStepSeed);
						}
						nextStep = "Clustering";
					} 
				}
				
				if (popIndex < pyramid.getPopCount()) {
					//Setup crossoverTracker to track crossover for next population
					currentPop = pyramid.getPopulation(popIndex);
					nextCrossoverTracker = new CrossoverTracker(currentSolution, currentPop, thisStepSeed, ff);
				}
				
			} else {
				nextStep = "Crossover";
			}
			
		}
		
		if (popIndex >= pyramid.getPopCount()) {
			nextGenSetup();
		} else {
			stepTracker.nextStep();
		}
		
	}
	
	// Called when at the start of a new generation to set variables to appropriate values
	private void nextGenSetup() {
		popIndex = 0;
		currentPop = pyramid.getPopulation(0);
		setCurrentSolution(null);
		nextStep = "New Random Solution";
		stepTracker.nextGen(pyramid);
	}
	
	// Continuously call oneStep for one iteration of iterate-P3
	public void oneIteration() {
		int currentIndex = stepTracker.getCurrentGen();
		while (stepTracker.getCurrentGen() == currentIndex) {
			oneStep();
		}
	}

	// Use StepTracker to move to previous step
	public void previousStep() {
		int numberOfStepsIntoGen = stepTracker.getCurrentGenStep();
		if (numberOfStepsIntoGen==0) {
			// add 1 to number of steps into generation so that when doing one less
			// brings to final step of that generation
			numberOfStepsIntoGen = stepTracker.getPreviousGenStep()+1;
		}
		//System.out.println("stepGenCount = "+numberOfStepsIntoGen);
		stepTracker.previousGen();
		pyramid = stepTracker.getPyramid();
		currentStepString = "Crossover";
		nextStep = "New Random Solution";
		popIndex = 0;
		setCurrentSolution(null);
		
		// Call oneStep until at previous step
		for (int i=0; i<numberOfStepsIntoGen-1; i++) {
			oneStep();
		}
	}

	// Use StepTracker to step to previous generation or start of current generation
	public void previousGen() {
		stepTracker.previousGen();
		pyramid = stepTracker.getPyramid();
		currentStepString = "Crossover";
		nextStep = "New Random Solution";
		popIndex = 0;
		currentPop = pyramid.getPopulation(0);
		setCurrentSolution(null);
	}
	
	// Move playback back to the first step of the current run
	public void rerun() {
		stepTracker.restart();
		pyramid = stepTracker.getPyramid();
		currentStepString = "New Random Solution";
		nextStep = "New Random Solution";
		popIndex = 0;
		currentPop = pyramid.getPopulation(0);
		setCurrentSolution(null);
	}
	
	private void setCurrentSolution(Solution sol) {
		if (sol != null) {
			currentSolution = sol.getCopy();
			currentSolution.setCreationStep(stepTracker.getCurrentStep());
		} else {
			currentSolution = null;
		}
	}

	// Continuously call onestep until crossover is finished
	public void stepToCrossoverEnd() {
		int initPop = popIndex;
		while (nextStep=="Crossover" && popIndex==initPop) {
			oneStep();
		}
	}

}